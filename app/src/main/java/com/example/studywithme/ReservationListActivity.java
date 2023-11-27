package com.example.studywithme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReservationListActivity extends AppCompatActivity {
    private DatabaseReference reservationsRef;
    private LinearLayout reservationListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        reservationsRef = FirebaseDatabase.getInstance().getReference().child("rents");
        reservationListLayout = findViewById(R.id.reservationListLayout);

        // 로고
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationListActivity.this, LoginSuccessActivity.class);
                startActivity(intent);
            }
        });

        getDataFromFirebase();
    }

    // 데이터베이스에서 데이터 가져오기
    private void getDataFromFirebase() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // 해당 사용자의 주행내역 가져오기
            String userEmail = user.getEmail();

            reservationsRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int reservationCount = (int) snapshot.getChildrenCount();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Reservation reservation = dataSnapshot.getValue(Reservation.class);
                        if (reservation != null) {
                            String date = reservation.getDate();
                            String time = reservation.getTime();
                            String fee = reservation.getFee();

                            addReservationTextView(date, time, fee);
                        }
                    }

                    // 동적으로 계산된 높이를 reservationListLayout에 설정
                    setReservationListLayoutHeight(reservationCount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 데이터 가져오기에 실패한 경우 처리
                }
            });
        }
    }

    // 대여기록
    private void addReservationTextView(String date, String time, String fee) {
        // 각 대여 기록을 위한 새 LinearLayout 생성
        LinearLayout newReservationLayout = new LinearLayout(this);
        newReservationLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newReservationLayout.setOrientation(LinearLayout.VERTICAL);
        newReservationLayout.setBackgroundResource(R.drawable.edit_background_bluewhite);
        newReservationLayout.setGravity(Gravity.CENTER);

        // 날짜, 시간 및 요금에 대한 TextView 생성
        TextView dateTextView = new TextView(this);
        dateTextView.setText("이용날짜 : " + date);
        dateTextView.setTextSize(18);
        dateTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
        dateTextView.setTypeface(null, Typeface.BOLD);
        dateTextView.setPadding(50, 20, 10, 20);

        TextView timeTextView = new TextView(this);
        timeTextView.setText("이용시간 : " + time);
        timeTextView.setTextSize(15);
        timeTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
        timeTextView.setPadding(50, 20, 10, 20);

        TextView feeTextView = new TextView(this);
        feeTextView.setText("이용요금 : " + fee);
        feeTextView.setTextSize(15);
        feeTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
        feeTextView.setPadding(50, 20, 10, 20);

        newReservationLayout.addView(dateTextView);
        newReservationLayout.addView(timeTextView);
        newReservationLayout.addView(feeTextView);

        // 새 LinearLayout을 rentListLayout에 추가
        reservationListLayout.addView(newReservationLayout);

        // 대여 기록 간의 여백 추가
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) newReservationLayout.getLayoutParams();
        layoutParams.setMargins(30, 30, 30, 20);
        newReservationLayout.setLayoutParams(layoutParams);
    }


    private void setReservationListLayoutHeight(int reservationCount) {
        // 최소한의 높이 설정
        int minHeight = 400;
        int calculatedHeight = minHeight + (reservationCount - 1) * 150;
        reservationListLayout.setMinimumHeight(calculatedHeight);
    }

}
