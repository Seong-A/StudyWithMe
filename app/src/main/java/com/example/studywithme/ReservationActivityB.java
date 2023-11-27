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

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

}
