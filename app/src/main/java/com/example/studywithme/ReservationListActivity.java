package com.example.studywithme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReservationListActivity extends AppCompatActivity {
    private DatabaseReference paymentsRef;
    private LinearLayout reservationListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        paymentsRef = FirebaseDatabase.getInstance().getReference().child("payments");
        reservationListLayout = findViewById(R.id.reservationListLayout);

        // Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.reservation_list);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.mainroom:
                                fetchAndDisplayData("mainroom");
                                return true;
                            case R.id.studyroom:
                                fetchAndDisplayData("studyroom");
                                return true;
                            case R.id.locker:
                                fetchAndDisplayData("locker");
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        // Logo
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationListActivity.this, LoginSuccessActivity.class);
                startActivity(intent);
            }
        });

        fetchAndDisplayData("mainroom");
    }


    private void fetchAndDisplayData(String itemType) {
        reservationListLayout.removeAllViews();

        paymentsRef.orderByChild("userEmail").equalTo(getCurrentUserEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Handle data based on the selected item type
                            switch (itemType) {
                                case "mainroom":
                                    displayMainRoomData(snapshot);
                                    break;
                                case "studyroom":
                                    displayStudyRoomData(snapshot);
                                    break;
                                case "locker":
                                    displayLockerData(snapshot);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void displayMainRoomData(DataSnapshot snapshot) {
        if (snapshot.exists() && snapshot.child("cafeId").getValue() != null
                && snapshot.child("seatNum").getValue() != null
                && snapshot.child("paymentDate").getValue() != null
                && snapshot.child("cardName").getValue() != null
                && snapshot.child("fee").getValue() != null) {

            String cafeId = snapshot.child("cafeId").getValue(String.class);
            Integer seatNum = snapshot.child("seatNum").getValue(Integer.class);
            String paymentDate = snapshot.child("paymentDate").getValue(String.class);
            String cardName = snapshot.child("cardName").getValue(String.class);
            Integer fee = snapshot.child("fee").getValue(Integer.class);

            if (cafeId != null && seatNum != null && paymentDate != null && cardName != null && fee != null) {
                LinearLayout newReservationLayout = createTextView(cafeId, seatNum, paymentDate, cardName, fee);
                reservationListLayout.addView(newReservationLayout);
            } else {

            }
        } else {

        }
    }



    private void displayStudyRoomData(DataSnapshot snapshot) {
        if (snapshot.exists() && snapshot.child("cafeId").getValue() != null
                && snapshot.child("id").getValue() != null
                && snapshot.child("paymentDate").getValue() != null
                && snapshot.child("cardName").getValue() != null
                && snapshot.child("fee").getValue() != null) {

            String cafeId = snapshot.child("cafeId").getValue(String.class);
            Integer id = snapshot.child("id").getValue(Integer.class);
            String paymentDate = snapshot.child("paymentDate").getValue(String.class);
            String cardName = snapshot.child("cardName").getValue(String.class);
            Integer fee = snapshot.child("fee").getValue(Integer.class);

            if (cafeId != null && id != null && paymentDate != null && cardName != null && fee != null) {
                LinearLayout newReservationLayout = createTextView(cafeId, id, paymentDate, cardName, fee);
                reservationListLayout.addView(newReservationLayout);
            } else {
            }
        } else {
        }
    }

    private void displayLockerData(DataSnapshot snapshot) {
        if (snapshot.exists() && snapshot.child("cafeId").getValue() != null
                && snapshot.child("lockerNumber").getValue() != null
                && snapshot.child("paymentDate").getValue() != null
                && snapshot.child("cardName").getValue() != null
                && snapshot.child("fee").getValue() != null) {

            String cafeId = snapshot.child("cafeId").getValue(String.class);
            Integer lockerNumber = snapshot.child("lockerNumber").getValue(Integer.class);
            String paymentDate = snapshot.child("paymentDate").getValue(String.class);
            String cardName = snapshot.child("cardName").getValue(String.class);
            Integer fee = snapshot.child("fee").getValue(Integer.class);

            if (cafeId != null && lockerNumber != null && paymentDate != null && cardName != null && fee != null) {
                LinearLayout newReservationLayout = createTextView(cafeId, lockerNumber, paymentDate, cardName, fee);
                reservationListLayout.addView(newReservationLayout);
            } else {
            }
        } else {
        }
    }


    private LinearLayout createTextView(String cafeId, int detail, String paymentDate, String cardName, int fee) {
        // 각 대여 기록을 위한 새 LinearLayout 생성
        LinearLayout newReservationLayout = new LinearLayout(this);
        newReservationLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newReservationLayout.setOrientation(LinearLayout.VERTICAL);
        newReservationLayout.setBackgroundResource(R.drawable.edit_background_bluewhite);
        newReservationLayout.setGravity(Gravity.CENTER);

        // CafeId에 따라 원하는 문자열 생성
        String cafeName;
        switch (cafeId) {
            case "cafe1":
                cafeName = "바른스터디카페";
                break;
            case "cafe2":
                cafeName = "비허밍 스터디카페";
                break;
            case "cafe3":
                cafeName = "어반트리스터디카페";
                break;
            case "cafe4":
                cafeName = "작심스터디카페";
                break;
            default:
                cafeName = "...";
                break;
        }

        TextView textView = new TextView(this);
        textView.setText(String.format("%s: %d 번\n결제날짜: %s\n카드이름: %s\n요금: %d 원", cafeName, detail, paymentDate, cardName, fee));
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView.setTextSize(16);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(50, 20, 0 , 20);

        newReservationLayout.addView(textView);

        // 대여 기록 간의 여백 추가
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(30, 30, 30, 20);
        newReservationLayout.setLayoutParams(layoutParams);

        return newReservationLayout;
    }


    private String getCurrentUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getEmail() : "";
    }
}