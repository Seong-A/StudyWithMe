package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        TextView userTextView = findViewById(R.id.user_welcome);

        // 로고
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, LoginSuccessActivity.class);
                startActivity(intent);
            }
        });

        // 사용자 정보 수정
        findViewById(R.id.user_modify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ModifyUserInfoActivity.class);
                startActivity(intent);
            }
        });

        // 이용 내역
        findViewById(R.id.reservation_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this, ReservationListActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
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
                                Intent homeIntent = new Intent(MypageActivity.this, LoginSuccessActivity.class);
                                startActivity(homeIntent);
                                break;
                            case R.id.find:
                                Intent findIntent = new Intent(MypageActivity.this, FindActivity.class);
                                startActivity(findIntent);
                                break;
                            case R.id.info:
                                Intent infoIntent = new Intent(MypageActivity.this, InfoActivity.class);
                                startActivity(infoIntent);
                                break;
                            case R.id.mypage:
                                break;
                        }
                        return true;
                    }
                }
        );
        bottomNavigationView.setSelectedItemId(R.id.mypage);
        checkAndUpdateUserName(userTextView);
    }

    // 사용자 이름 찾기
    private void checkAndUpdateUserName(final TextView userTextView) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        if (name != null) {
                            String welcomeMessage = name + " 님 환영합니다";

                            SpannableString spannableString = new SpannableString(welcomeMessage);
                            ForegroundColorSpan nameColor = new ForegroundColorSpan(getResources().getColor(R.color.blue));
                            ForegroundColorSpan timeColor = new ForegroundColorSpan(getResources().getColor(R.color.black));

                            spannableString.setSpan(nameColor, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(timeColor, name.length(), welcomeMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            userTextView.setText(spannableString);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MypageActivity.this, "사용자 데이터 찾기 실패 ㅠㅠ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 로그아웃
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}