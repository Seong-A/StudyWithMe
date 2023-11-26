package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 메인홀
        findViewById(R.id.cafe1_mainroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity1.this, ReservationActivityA.class);
                intent.putExtra("cafeId", "cafe1");
                startActivity(intent);
            }
        });

    }
}
