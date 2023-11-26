package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivityA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String cafeId = intent.getStringExtra("cafeId");

        int layoutResourceId;
        if ("cafe1".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_a1;
        } else if ("cafe2".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_b1;
        } else if ("cafe3".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_c1;
        } else if ("cafe4".equals(cafeId)) {
            layoutResourceId = R.layout.activity_reservation_d1;
        } else {
            return;
        }

        setContentView(layoutResourceId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
