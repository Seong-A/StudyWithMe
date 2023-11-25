package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // navi bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                Intent findIntent = new Intent(InfoActivity.this, LoginSuccessActivity.class);
                                startActivity(findIntent);
                                break;
                            case R.id.find:
                                Intent infoIntent = new Intent(InfoActivity.this, FindActivity.class);
                                startActivity(infoIntent);
                                break;
                            case R.id.info:
                                break;
                            case R.id.mypage:
                                break;
                        }
                        return true;
                    }
                }
        );

        bottomNavigationView.setSelectedItemId(R.id.info);

    }
}