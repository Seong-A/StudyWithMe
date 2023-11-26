package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String cafeId = intent.getStringExtra("cafeId");

        int layoutResourceId;
        int totalSeats;

        // 각 카페에 따라 레이아웃 및 총 좌석 수 설정
        if ("cafe1".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_a2;
            totalSeats = 6;
        } else if ("cafe2".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_b2;
            totalSeats = 6;
        } else if ("cafe3".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_c2;
            totalSeats = 6;
        } else if ("cafe4".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_d2;
            totalSeats = 6;
        } else {
            return;
        }

        setContentView(layoutResourceId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 좌석 개수에 맞게 반복문을 통해 클릭 리스너 등록
        for (int i = 1; i <= totalSeats; i++) {
            setSeatClickListener(i);
        }
    }

    private void setSeatClickListener(int seatNum) {

    }
}