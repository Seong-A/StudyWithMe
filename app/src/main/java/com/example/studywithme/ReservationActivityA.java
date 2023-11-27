package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivityA extends AppCompatActivity {

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Log.d("DEBUG", "cafeId: " + cafeId);

        // 좌석 개수에 맞게 반복문을 통해 클릭 리스너 등록
        for (int i = 1; i <= totalSeats; i++) {
            setSeatClickListener(i);
        }

        // 예약 버튼 클릭 리스너 설정
        View reservationButton = findViewById(R.id.reservation_btn);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reservationIntent = new Intent(ReservationActivityA.this, ReservationActivity.class);
                reservationIntent.putExtra("selected_seat_num", selectedSeatNum);
                reservationIntent.putExtra("cafeId", cafeId);
                startActivity(reservationIntent);
            }
        });
    }

    private void setSeatClickListener(int seatNum) {
        // 리소스 ID를 가져오기 위해 좌석 ID 설정
        String seatIdName = "seat" + seatNum;
        int seatId = getResources().getIdentifier(seatIdName, "id", getPackageName());

        ImageView seatImageView = findViewById(seatId);

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
    }
}
