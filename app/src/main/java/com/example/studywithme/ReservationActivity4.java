package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_4);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 메인홀
        findViewById(R.id.cafe4_mainroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity4.this, ReservationActivityA.class);
                intent.putExtra("cafeId", "cafe4");
                startActivity(intent);
            }
        });

        // 1번 스터디룸
        findViewById(R.id.cafe4_studyroom1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity4.this, ReservationActivityB.class);
                intent.putExtra("cafeId", "cafe4");
                startActivity(intent);
            }
        });

        // 2번 스터디룸
        findViewById(R.id.cafe4_studyroom2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity4.this, ReservationActivityC.class);
                intent.putExtra("cafeId", "cafe4");
                startActivity(intent);
            }
        });
    }
}
