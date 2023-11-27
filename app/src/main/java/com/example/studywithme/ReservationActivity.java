package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ReservationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int selectedSeatNum;
    private int selectedTime;
    private TextView selectedTimeTextView;
    private HashMap<Integer, Integer> timeRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 사용자 이름
        TextView userTextView = findViewById(R.id.user_name);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button payButton = findViewById(R.id.pay_btn);

        Intent intent = getIntent();
        if (intent.hasExtra("selected_seat_num")) {
            selectedSeatNum = intent.getIntExtra("selected_seat_num", 0);
        }

        checkAndUpdateUserName(userTextView);

        setClickListener(R.id.time_2h, 2);
        setClickListener(R.id.time_3h, 3);
        setClickListener(R.id.time_4h, 4);
        setClickListener(R.id.time_6h, 6);
        setClickListener(R.id.time_9h, 9);
        setClickListener(R.id.time_12h, 12);

        // 결제버튼
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fee = calculateFee(selectedTime); // 요금
                String cafeId = getCafeIdForSelectedSeat(); // 카페아이디

                Intent paymentIntent = new Intent(ReservationActivity.this, PaymentActivity.class);
                paymentIntent.putExtra("selected_seat_num", selectedSeatNum);
                paymentIntent.putExtra("selected_time", selectedTime);
                paymentIntent.putExtra("fee", fee);
                paymentIntent.putExtra("cafeId", cafeId);
                startActivity(paymentIntent);
            }
        });

        initTimeRates();
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
                            String welcomeMessage = name + " 님 - 좌석 " + selectedSeatNum;

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
                    Toast.makeText(ReservationActivity.this, "사용자 불러오기 실패ㅠㅠ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 시간대별 요금 초기화 메소드
    private void initTimeRates() {
        timeRates = new HashMap<>();
        timeRates.put(2, 3000);
        timeRates.put(3, 4000);
        timeRates.put(4, 5000);
        timeRates.put(6, 6000);
        timeRates.put(9, 8000);
        timeRates.put(12, 10000);
    }

    private void setClickListener(int textViewId, int time) {
        TextView timeTextView = findViewById(textViewId);
        Button payButton = findViewById(R.id.pay_btn);

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTimeTextView != null) {
                    selectedTimeTextView.setBackgroundResource(R.drawable.btn_background2);
                }
                selectedTime = time;
                selectedTimeTextView = timeTextView;
                selectedTimeTextView.setBackgroundResource(R.drawable.edit_background_blue);

                payButton.setVisibility(View.VISIBLE);

                TextView moneyTextView = findViewById(R.id.money);

                if (moneyTextView != null) {
                    updateMoneyTextView(moneyTextView, time);
                } else {
                }
            }
        });
    }


    private void updateMoneyTextView(TextView moneyTextView, int selectedTime) {
    }

    private int calculateFee(int selectedTime) {
        if (timeRates == null || timeRates.isEmpty()) {
            initTimeRates();
        }

        if (timeRates.containsKey(selectedTime)) {
            return timeRates.get(selectedTime);
        } else {
            Toast.makeText(this, "Invalid selected time", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private String getCafeIdForSelectedSeat() {
        Intent intent = getIntent();
        if (intent.hasExtra("cafeId")) {
            return intent.getStringExtra("cafeId");
        } else {
            return "";
        }
    }
}
