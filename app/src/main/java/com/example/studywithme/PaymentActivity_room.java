package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity_room extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int selectedTime;
    private int selectedId;
    private String selectedCardName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView userTextView = findViewById(R.id.user_name);
        TextView timeTextView = findViewById(R.id.time);
        TextView moneyTextView = findViewById(R.id.money);
        EditText payDateEditText = findViewById(R.id.editCardNumber);
        EditText editExpDateEditText = findViewById(R.id.editExpDate);
        EditText editCVVEditText = findViewById(R.id.editCVV);
        AppCompatButton paymentButton = findViewById(R.id.payment_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // 결제버튼
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateAndTime = getCurrentDateAndTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR, selectedTime);
                String endUsingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.getTime());

                DatabaseReference paymentsRef = databaseReference.child("payments");
                String paymentKey = paymentsRef.push().getKey();
                Payment_room payment = new Payment_room(
                        selectedId,
                        selectedTime,
                        getIntent().getIntExtra("fee", 0),
                        currentDateAndTime,
                        selectedCardName,
                        getIntent().getStringExtra("cafeId"),
                        getUserEmail()
                );

                paymentsRef.child(paymentKey).setValue(payment);

                String cafeId = getIntent().getStringExtra("cafeId");
                DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);

                DatabaseReference studyRoomsRef = cafeRef.child("studyrooms");
                DatabaseReference studyRoomReservationRef = studyRoomsRef.push();
                studyRoomReservationRef.child("id").setValue(selectedId);
                studyRoomReservationRef.child("status").setValue("reserved");
                studyRoomReservationRef.child("reservationTime").setValue(currentDateAndTime);
                studyRoomReservationRef.child("endUsingTime").setValue(endUsingTime);
                studyRoomReservationRef.child("userEmail").setValue(userEmail);

                updateSeatStatusAndImage(cafeRef, selectedId, "reserved");

                Intent successIntent = new Intent(PaymentActivity_room.this, PaymentSuccessActivity.class);
                successIntent.putExtra("fee", getIntent().getIntExtra("fee", 0));
                successIntent.putExtra("PAYMENT_DATE", currentDateAndTime);
                successIntent.putExtra("CARD_NAME", selectedCardName);
                startActivity(successIntent);
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.hasExtra("selected_time") && intent.hasExtra("fee")) {
            selectedId = intent.getIntExtra("id", 1);
            selectedTime = intent.getIntExtra("selected_time", 0);
            selectedCardName = intent.getStringExtra("CARD_NAME");
            int fee = intent.getIntExtra("fee", 0);
            String cafeId = intent.getStringExtra("cafeId");

            String timeMessage = selectedTime + "시간권";
            timeTextView.setText(timeMessage);

            updateMoneyTextView(moneyTextView, fee);
        }

        payDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().replaceAll("-", "");

                // 하이픈을 추가
                if (input.length() > 0 && (input.length() % 4) == 0) {
                    StringBuilder formattedDate = new StringBuilder();
                    for (int i = 0; i < input.length(); i++) {
                        if (i > 0 && (i % 4) == 0) {
                            formattedDate.append("-");
                        }
                        formattedDate.append(input.charAt(i));
                    }

                    payDateEditText.removeTextChangedListener(this);
                    payDateEditText.setText(formattedDate.toString());
                    payDateEditText.setSelection(formattedDate.length());
                    payDateEditText.addTextChangedListener(this);
                }
            }
        });

        editExpDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString().replaceAll("/", "");

                // 슬래시를 추가
                if (input.length() > 0 && (input.length() % 2) == 0) {
                    StringBuilder formattedDate = new StringBuilder();
                    for (int i = 0; i < input.length(); i++) {
                        if (i > 0 && (i % 2) == 0) {
                            formattedDate.append("/");
                        }
                        formattedDate.append(input.charAt(i));
                    }

                    // 텍스트 변경 시에는 다시 TextWatcher를 제거하여 무한 루프를 방지
                    editExpDateEditText.removeTextChangedListener(this);
                    editExpDateEditText.setText(formattedDate.toString());
                    editExpDateEditText.setSelection(formattedDate.length());
                    editExpDateEditText.addTextChangedListener(this);
                }
            }
        });

        editCVVEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                // 3글자 제한
                if (editable.length() > 3) {
                    editable.delete(3, editable.length());
                }
            }
        });

        checkAndUpdateUserName(userTextView);
        userEmail = getUserEmail();
    }

    // 사용자 이름 불러오기
    private void checkAndUpdateUserName(final TextView userTextView) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        if (name != null) {
                            String welcomeMessage = name + " 님 - 스터디룸 " + selectedId;

                            SpannableString spannableString = new SpannableString(welcomeMessage);
                            ForegroundColorSpan nameColor = new ForegroundColorSpan(getResources().getColor(R.color.blue));
                            ForegroundColorSpan timeColor = new ForegroundColorSpan(getResources().getColor(R.color.black));

                            spannableString.setSpan(nameColor, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(timeColor, name.length(), welcomeMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            userTextView.setText(spannableString);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PaymentActivity_room.this, "사용자 불러오기 실패ㅠㅠ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 현재 날짜 / 시간
    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void updateMoneyTextView(TextView moneyTextView, int fee) {
        String moneyMessage = "요금: " + fee + "원";
        moneyTextView.setText(moneyMessage);
    }

    public void showCardMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 메뉴 아이템 클릭 시 처리할 로직 추가
                Toast.makeText(PaymentActivity_room.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                setCardName(item.getTitle().toString());
                selectedCardName = item.getTitle().toString();
                return true;
            }
        });

        popupMenu.show();
    }

    private void setCardName(String cardName) {
        EditText cardNameEditText = findViewById(R.id.CardName);
        cardNameEditText.setText(cardName);
    }

    private void updateSeatStatusAndImage(DatabaseReference cafeRef, int id, String status) {
        DatabaseReference studyRoomsRef = cafeRef.child("studyrooms");

        studyRoomsRef.orderByChild("id").equalTo(selectedId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studyRoomSnapshot : dataSnapshot.getChildren()) {
                        studyRoomSnapshot.getRef().child("status").setValue(status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PaymentActivity_room.this, "Study room status update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getUserEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return "";
    }

}
