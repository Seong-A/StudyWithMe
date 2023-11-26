package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CafeDetailActivity extends AppCompatActivity {

    private TextView cafeNameTextView;
    private TextView cafeLocationTextView;
    private TextView cafeTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        cafeNameTextView = findViewById(R.id.cafe_name);
        cafeLocationTextView = findViewById(R.id.cafe_location);
        cafeTimeTextView = findViewById(R.id.cafe_time);

        if (getIntent().hasExtra("cafe")) {
            Cafe cafe = (Cafe) getIntent().getSerializableExtra("cafe");
            showCafeDetails(cafe);
        }

        // 좌석 예약 버튼
        findViewById(R.id.reservation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("cafe")) {
                    Cafe cafe = (Cafe) getIntent().getSerializableExtra("cafe");

                    // Start the appropriate reservation activity based on the cafe ID
                    if (cafe != null) {
                        int cafeId = cafe.getId();
                        startReservationActivity("ReservationActivity" + cafeId);
                    } else {
                        Toast.makeText(CafeDetailActivity.this, "유효하지 않은 카페 데이터입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        // navi bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                Intent homeIntent = new Intent(CafeDetailActivity.this, LoginSuccessActivity.class);
                                startActivity(homeIntent);
                                break;
                            case R.id.find:
                                Intent findIntent = new Intent(CafeDetailActivity.this, FindActivity.class);
                                startActivity(findIntent);
                                break;
                            case R.id.info:
                                Intent infoIntent = new Intent(CafeDetailActivity.this, InfoActivity.class);
                                startActivity(infoIntent);
                                break;
                            case R.id.mypage:
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void startReservationActivity(String activityName) {
        try {
            // Construct the class name dynamically
            Class<?> reservationActivityClass = Class.forName("com.example.studywithme." + activityName);
            Intent reservationIntent = new Intent(CafeDetailActivity.this, reservationActivityClass);
            startActivity(reservationIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(CafeDetailActivity.this, "좌석 예약을 진행할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showCafeDetails(Cafe cafe) {
        if (cafeNameTextView != null && cafeLocationTextView != null && cafeTimeTextView != null) {
            cafeNameTextView.setText(cafe.getName());
            cafeLocationTextView.setText(cafe.getLocation());
            cafeTimeTextView.setText(cafe.getTime());
        } else {
            // 초기화되지 않은 경우에 대한 로그 또는 토스트 메시지를 추가할 수 있습니다.
            Log.e("CafeDetailActivity", "TextViews are not properly initialized.");
            Toast.makeText(this, "화면 초기화 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
