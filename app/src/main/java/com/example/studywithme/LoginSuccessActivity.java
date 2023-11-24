package com.example.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.studywithme.InfoActivity;
import com.example.studywithme.JoinActivity;
import com.example.studywithme.LoginActivity;
import com.example.studywithme.R;
import com.google.firebase.FirebaseApp;

public class LoginSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        FirebaseApp.initializeApp(this);

        // 좌석 예약 버튼
        findViewById(R.id.reservation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSuccessActivity.this, StudyRoomActivity.class);
                startActivity(intent);
            }
        });

        // 이용 안내
        findViewById(R.id.info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSuccessActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });
    }


}