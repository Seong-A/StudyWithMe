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
import androidx.appcompat.widget.AppCompatButton;

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

public class ReservationActivityD extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int selectedLockerNum;
    private ImageView lastSelectedLocker;
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
            layoutResourceId = R.layout.activity_reservation_a4;
            totalSeats = 40;
        } else if ("cafe2".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_b4;
            totalSeats = 40;
        } else if ("cafe3".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_c4;
            totalSeats = 40;
        } else if ("cafe4".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_d4;
            totalSeats = 40;
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
            setLockerClickListener(i);
        }

        // 예약 버튼 클릭 리스너 설정
        View reservationButton = findViewById(R.id.reservation_btn);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLockerStatusAndImage(selectedLockerNum, "reserved", "locker_r");
                Intent reservationIntent = new Intent(ReservationActivityD.this, ReservationActivity_locker.class);
                reservationIntent.putExtra("selected_locker_num", selectedLockerNum);
                reservationIntent.putExtra("cafeId", cafeId);
                startActivity(reservationIntent);
            }
        });

        updateLockerStatusBasedOnTime();
    }

    private void updateLockerStatusBasedOnTime() {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference lockersRef = cafeRef.child("lockers");

        lockersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot lockerSnapshot : dataSnapshot.getChildren()) {
                        String endUsingTime = lockerSnapshot.child("endUsingTime").getValue(String.class);
                        String status = lockerSnapshot.child("status").getValue(String.class);

                        Log.d("FirebaseData", "Status: " + status + ", EndUsingTime: " + endUsingTime);

                        if ("reserved".equals(status) && isEndUsingTimePassed(endUsingTime)) {
                            lockerSnapshot.getRef().child("status").setValue("available");

                            ImageView lockerImageView = getLockerImageView(lockerSnapshot.child("LockerNumber").getValue(Integer.class));
                                if (lockerImageView != null) {
                                    lockerImageView.setBackgroundResource(R.drawable.locker_a);
                                }
                            }
                        }
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityD.this, "자동 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLockerClickListener(int lockerNumber) {
        String lockerIdName = "locker" + lockerNumber;
        int lockerId = getResources().getIdentifier(lockerIdName, "id", getPackageName());

        ImageView lockerImageView = findViewById(lockerId);

        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference lockersRef = cafeRef.child("lockers");

        lockersRef.orderByChild("LockerNumber").equalTo(lockerNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot lockerSnapshot : dataSnapshot.getChildren()) {
                        String status = lockerSnapshot.child("status").getValue(String.class);

                        if ("reserved".equals(status)) {
                            lockerImageView.setImageResource(R.drawable.locker_r);
                        } else if ("available".equals(status)) {
                            lockerImageView.setImageResource(R.drawable.locker_a);
                        }
                    }
                } else {
                    lockerImageView.setImageResource(R.drawable.locker_a);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityD.this, "상태 조회 실패", Toast.LENGTH_SHORT).show();
            }
        });

        lockerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedLocker != null) {
                    lastSelectedLocker.setImageResource(R.drawable.locker_a);
                }
                lockerImageView.setImageResource(R.drawable.locker_r);
                lastSelectedLocker = lockerImageView;

                View reservationButton = findViewById(R.id.reservation_btn);
                reservationButton.setVisibility(View.VISIBLE);
                Toast.makeText(ReservationActivityD.this, "사물함 " + lockerNumber + "을 선택하였습니다", Toast.LENGTH_SHORT).show();

                selectedLockerNum = lockerNumber;

                updateLockerStatusAndImage(selectedLockerNum, "reserved", "locker_r");
            }
        });

        startLockerStatusUpdateHandler();
    }


    private Handler lockerStatusUpdateHandler;
    private static final int UPDATE_INTERVAL = 60000;

    private void startLockerStatusUpdateHandler() {
        lockerStatusUpdateHandler = new Handler();
        lockerStatusUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLockerStatusForCafe(cafeId);
                lockerStatusUpdateHandler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, UPDATE_INTERVAL);
    }

    private void stopLockerStatusUpdateHandler() {
        if (lockerStatusUpdateHandler != null) {
            lockerStatusUpdateHandler.removeCallbacksAndMessages(null);
            lockerStatusUpdateHandler = null;
        }
    }

    private void updateLockerStatusAndImage(int lockerNumber, String status, String imageName) {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference lockersRef = cafeRef.child("lockers");

        lockersRef.orderByChild("LockerNumber").equalTo(lockerNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot lockerSnapshot : dataSnapshot.getChildren()) {
                        lockerSnapshot.getRef().child("status").setValue(status);

                        ImageView lockerImageView = getLockerImageView(lockerNumber);
                        if (lockerImageView != null) {
                            if ("reserved".equals(status)) {
                                lockerImageView.setImageResource(R.drawable.locker_r);
                            } else if ("available".equals(status)) {
                                lockerImageView.setImageResource(R.drawable.locker_a);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityD.this, "상태 및 이미지 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLockerStatusForCafe(String cafeId) {
        DatabaseReference cafeRef = databaseReference.child("cafes").child(cafeId);
        DatabaseReference lockersRef = cafeRef.child("lockers");

        lockersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot lockerSnapshot : dataSnapshot.getChildren()) {
                        String status = lockerSnapshot.child("status").getValue(String.class);
                        String endUsingTime = lockerSnapshot.child("endUsingTime").getValue(String.class);

                        if ("reserved".equals(status) && isEndUsingTimePassed(endUsingTime)) {
                            lockerSnapshot.getRef().child("status").setValue("available");
                            ImageView lockerImageView = getLockerImageView(lockerSnapshot.child("LockerNumber").getValue(Integer.class));
                            if (lockerImageView != null) {
                                lockerImageView.setImageResource(R.drawable.locker_a);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationActivityD.this, "자동 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isEndUsingTimePassed(String endUsingTime) {
        try {
            if (endUsingTime == null || endUsingTime.isEmpty()) {
                return true;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            Date endDate = dateFormat.parse(endUsingTime);

            return System.currentTimeMillis() > endDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ImageView getLockerImageView(int lockerNumber) {
        String lockerIdName = "locker" + lockerNumber;
        int lockerId = getResources().getIdentifier(lockerIdName, "id", getPackageName());
        ImageView lockerImageView = findViewById(lockerId);
        Log.d("LockerImageView", "Locker Number: " + lockerNumber + ", ImageView: " + lockerImageView);
        return lockerImageView;
    }
}
