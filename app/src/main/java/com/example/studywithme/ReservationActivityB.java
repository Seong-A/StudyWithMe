package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationActivityB extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String cafeId;
    private ImageView lastSelectedSeat;
    private int studyroom_id = 1;
    private int selectedSeatNum;

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

        // 각 카페에 따라 레이아웃 및 총 좌석 수 설정
        if ("cafe1".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_a2;
        } else if ("cafe2".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_b2;
        } else if ("cafe3".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_c2;
        } else if ("cafe4".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_d2;
        } else {
            return;
        }

        setContentView(layoutResourceId);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 예약 버튼 클릭 리스너 설정
        // 예약 버튼 클릭 리스너 설정
        View reservationButton = findViewById(R.id.reservation_btn);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studyroom_id = 1;
                updateSeatStatusAndImage(studyroom_id, "reserved", "studyroom6_r");
                Intent reservationIntent = new Intent(ReservationActivityB.this, ReservationActivity_room.class);
                reservationIntent.putExtra("cafeId", cafeId);
                reservationIntent.putExtra("id", studyroom_id);
                startActivity(reservationIntent);
            }
        });
        setSeatClickListener(studyroom_id);
        updateSeatStatusBasedOnTime();
    }

    private void updateSeatStatusBasedOnTime() {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference studyRoomRef = cafeRef.child("studyrooms");

        studyRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                        String endUsingTime = seatSnapshot.child("endUsingTime").getValue(String.class);
                        String status = seatSnapshot.child("status").getValue(String.class);

                        if ("reserved".equals(status) && isEndUsingTimePassed(endUsingTime)) {
                            seatSnapshot.getRef().child("status").setValue("available");

                            ImageView seatImageView = getSeatImageView(seatSnapshot.child("id").getValue(Integer.class));
                            if (seatImageView != null) {
                                seatImageView.setImageResource(R.drawable.studyroom6_a);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityB.this, "자동 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSeatClickListener(int id) {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference studyRoomRef = cafeRef.child("studyrooms");

        studyRoomRef.orderByChild("id").equalTo(studyroom_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseData", "DataSnapshot: " + dataSnapshot);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                        Log.d("FirebaseData", "Seat Snapshot: " + seatSnapshot);

                        String status = seatSnapshot.child("status").getValue(String.class);

                        ImageView studyroomImageView = findViewById(R.id.studyroom6);
                        if ("reserved".equals(status)) {
                            studyroomImageView.setImageResource(R.drawable.studyroom6_r);
                        } else {
                            studyroomImageView.setImageResource(R.drawable.studyroom6_a);
                        }

                        studyroomImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lastSelectedSeat != null) {
                                    lastSelectedSeat.setImageResource(R.drawable.studyroom6_a);
                                }
                                studyroomImageView.setImageResource(R.drawable.studyroom6_r);
                                lastSelectedSeat = studyroomImageView;

                                View reservationButton = findViewById(R.id.reservation_btn);
                                reservationButton.setVisibility(View.VISIBLE);
                                Toast.makeText(ReservationActivityB.this, "스터디룸 " + studyroom_id + "을 선택하였습니다", Toast.LENGTH_SHORT).show();

                                selectedSeatNum = studyroom_id;
                            }
                        });
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityB.this, "상태 조회 실패", Toast.LENGTH_SHORT).show();
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
        DatabaseReference studyRoomRef = cafeRef.child("studyrooms");
        studyRoomRef.orderByChild("studyroom_id").equalTo(seatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
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
                Toast.makeText(ReservationActivityB.this, "상태 및 이미지 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSeatStatusForCafe(String cafeId) {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference studyRoomRef = cafeRef.child("studyrooms");

        studyRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                        String status = seatSnapshot.child("status").getValue(String.class);
                        String endUsingTime = seatSnapshot.child("endUsingTime").getValue(String.class);

                        if ("reserved".equals(status) && isEndUsingTimePassed(endUsingTime)) {
                            seatSnapshot.getRef().child("status").setValue("available");
                            ImageView seatImageView = getSeatImageView(seatSnapshot.child("id").getValue(Integer.class));
                            if (seatImageView != null) {
                                seatImageView.setImageResource(R.drawable.studyroom6_a);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityB.this, "자동 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEndUsingTimePassed(String endUsingTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            Date endDate = dateFormat.parse(endUsingTime);

            // 시간 비교
            return System.currentTimeMillis() > endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    private ImageView getSeatImageView(int id) {
        String seatIdName = "studyroom" + id;
        int seatId = getResources().getIdentifier(seatIdName, "id", getPackageName());
        return findViewById(seatId);
    }

}





