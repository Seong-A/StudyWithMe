package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReservationActivityA extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int selectedSeatNum;
    private ImageView lastSelectedSeat;
    private String cafeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        cafeId = intent.getStringExtra("cafeId");

        if (cafeId == null || cafeId.isEmpty()) {
            Toast.makeText(this, "존재하지 않는 스터디카페입니다", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int layoutResourceId;
        int totalSeats;

        // 각 카페에 따라 레이아웃 및 총 좌석 수 설정
        if ("cafe1".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_a1;
            totalSeats = 36;
        } else if ("cafe2".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_b1;
            totalSeats = 40;
        } else if ("cafe3".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_c1;
            totalSeats = 46;
        } else if ("cafe4".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_d1;
            totalSeats = 44;
        } else {
            return;
        }

        setContentView(layoutResourceId);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 좌석 개수에 맞게 반복문을 통해 클릭 리스너 등록
        for (int i = 1; i <= totalSeats; i++) {
            setSeatClickListener(i);
        }

        // 예약 버튼 클릭 리스너 설정
        View reservationButton = findViewById(R.id.reservation_btn);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSeatStatusAndImage(selectedSeatNum, "reserved", "seat_r");
                Intent reservationIntent = new Intent(ReservationActivityA.this, ReservationActivity.class);
                reservationIntent.putExtra("selected_seat_num", selectedSeatNum);
                reservationIntent.putExtra("cafeId", cafeId);
                startActivity(reservationIntent);
            }
        });
    }

    private void setSeatClickListener(int seatNum) {
        String seatIdName = "seat" + seatNum;
        int seatId = getResources().getIdentifier(seatIdName, "id", getPackageName());

        ImageView seatImageView = findViewById(seatId);

        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference seatsRef = cafeRef.child("seats");

        seatsRef.orderByChild("seatNumber").equalTo(seatNum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                        String status = seatSnapshot.child("status").getValue(String.class);

                        if ("reserved".equals(status)) {
                            seatImageView.setImageResource(R.drawable.seat_r);
                        } else {
                            seatImageView.setImageResource(R.drawable.seat_a);
                        }
                    }
                } else {
                    seatImageView.setImageResource(R.drawable.seat_a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityA.this, "상태 조회 실패", Toast.LENGTH_SHORT).show();
            }
        });

        seatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedSeat != null) {
                    lastSelectedSeat.setImageResource(R.drawable.seat_a);
                }
                seatImageView.setImageResource(R.drawable.seat_r);
                lastSelectedSeat = seatImageView;

                View reservationButton = findViewById(R.id.reservation_btn);
                reservationButton.setVisibility(View.VISIBLE);
                Toast.makeText(ReservationActivityA.this, "좌석 " + seatNum + "을 선택하였습니다", Toast.LENGTH_SHORT).show();

                selectedSeatNum = seatNum;
            }
        });

        startSeatStatusUpdateHandler();
    }

    private Handler seatStatusUpdateHandler;
    private static final int UPDATE_INTERVAL = 60000;

    private void startSeatStatusUpdateHandler() {
        seatStatusUpdateHandler = new Handler();
        seatStatusUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateSeatStatusForCafe(cafeId);
                seatStatusUpdateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
    }

    private void stopSeatStatusUpdateHandler() {
        if (seatStatusUpdateHandler != null) {
            seatStatusUpdateHandler.removeCallbacksAndMessages(null);
            seatStatusUpdateHandler = null;
        }
    }

    private void updateSeatStatusAndImage(int seatNumber, String status, String imageName) {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference seatsRef = cafeRef.child("seats");
        seatsRef.orderByChild("seatNumber").equalTo(seatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                        seatSnapshot.getRef().child("status").setValue(status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityA.this, "상태 및 이미지 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSeatStatusForCafe(String cafeId) {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference seatsRef = cafeRef.child("seats");

        seatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                        String status = seatSnapshot.child("status").getValue(String.class);
                        String endUsingTime = seatSnapshot.child("endUsingTime").getValue(String.class);

                        if ("reserved".equals(status) && isEndUsingTimePassed(endUsingTime)) {
                            seatSnapshot.getRef().child("status").setValue("available");
                            ImageView seatImageView = getSeatImageView(seatSnapshot.child("seatNumber").getValue(Integer.class));
                            if (seatImageView != null) {
                                seatImageView.setImageResource(R.drawable.seat_a);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityA.this, "자동 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEndUsingTimePassed(String endUsingTime) {
        return System.currentTimeMillis() > Long.parseLong(endUsingTime);
    }

    private ImageView getSeatImageView(int seatNumber) {
        String seatIdName = "seat" + seatNumber;
        int seatId = getResources().getIdentifier(seatIdName, "id", getPackageName());
        return findViewById(seatId);
    }
}
