package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        FirebaseApp.initializeApp(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // cafe1
        findViewById(R.id.cafe1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCafeInfoFromFirebase("cafe1");
            }
        });

        // cafe2
        findViewById(R.id.cafe2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCafeInfoFromFirebase("cafe2");
            }
        });

        // cafe3
        findViewById(R.id.cafe3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCafeInfoFromFirebase("cafe3");
            }
        });

        // cafe4
        findViewById(R.id.cafe4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCafeInfoFromFirebase("cafe4");
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
                                Intent homeIntent = new Intent(FindActivity.this, LoginSuccessActivity.class);
                                startActivity(homeIntent);
                                break;
                            case R.id.find:
                                break;
                            case R.id.info:
                                Intent infoIntent = new Intent(FindActivity.this, InfoActivity.class);
                                startActivity(infoIntent);
                                break;
                            case R.id.mypage:
                                break;
                        }
                        return true;
                    }
                }
        );

        bottomNavigationView.setSelectedItemId(R.id.find);
    }

    private void getCafeInfoFromFirebase(String cafeId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cafes").child(cafeId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cafe cafe = dataSnapshot.getValue(Cafe.class);
                    openCafeDetailActivity(cafe);
                } else {
                    Toast.makeText(FindActivity.this, "해당 카페의 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindActivity.this, "카페 정보를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openCafeDetailActivity(Cafe cafe) {
        Intent intent = new Intent(FindActivity.this, CafeDetailActivity.class);
        intent.putExtra("cafe", cafe);
        startActivity(intent);
    }
}
