package com.example.studywithme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class StudyRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // 1번 스터디룸
        AppCompatButton studyroom1Button = findViewById(R.id.studyroom1);
        studyroom1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRoomActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        // 2번 스터디룸
        AppCompatButton studyroom2Button = findViewById(R.id.studyroom2);
        studyroom2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRoomActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        // 1번 스터디룸
        AppCompatButton studyroom3Button = findViewById(R.id.studyroom3);
        studyroom3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRoomActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

        // 1번 스터디룸
        AppCompatButton studyroom4Button = findViewById(R.id.studyroom4);
        studyroom4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRoomActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });

    }
}