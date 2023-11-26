package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_3);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 메인홀
        findViewById(R.id.cafe3_mainroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity3.this, ReservationActivityA.class);
                intent.putExtra("cafeId", "cafe3");
                startActivity(intent);
            }
        });

        // 1번 스터디룸
        findViewById(R.id.cafe3_studyroom1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity3.this, ReservationActivityB.class);
                intent.putExtra("cafeId", "cafe3");
                startActivity(intent);
            }
        });

        // 2번 스터디룸
        findViewById(R.id.cafe3_studyroom2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity3.this, ReservationActivityC.class);
                intent.putExtra("cafeId", "cafe3");
                startActivity(intent);
            }
        });

        // 사물함
        findViewById(R.id.cafe3_locker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity3.this, ReservationActivityD.class);
                intent.putExtra("cafeId", "cafe3");
                startActivity(intent);
            }
        });
    }
}
