package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // 메인홀
        findViewById(R.id.cafe2_mainroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity2.this, ReservationActivityA.class);
                intent.putExtra("cafeId", "cafe2");
                startActivity(intent);
            }
        });
    }
}
