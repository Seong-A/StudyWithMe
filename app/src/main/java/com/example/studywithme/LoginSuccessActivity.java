package com.example.studywithme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginSuccessActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        FirebaseApp.initializeApp(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // 사용자 이름
        TextView userTextView = findViewById(R.id.user);

        // 배너
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(Fragment1.newInstance(0));
        fragments.add(Fragment2.newInstance(1));

        viewPager2 = findViewById(R.id.viewPager2_container);

        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this, fragments);
        viewPager2.setAdapter(viewPager2Adapter);


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

        checkAndUpdateUserName(userTextView);

        // navi bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                break;
                            case R.id.find:
                                Intent findIntent = new Intent(LoginSuccessActivity.this, FindActivity.class);
                                startActivity(findIntent);
                                break;
                            case R.id.info:
                                Intent infoIntent = new Intent(LoginSuccessActivity.this, InfoActivity.class);
                                startActivity(infoIntent);
                                break;
                            case R.id.mypage:
                                Intent mypageIntent = new Intent(LoginSuccessActivity.this, MypageActivity.class);
                                startActivity(mypageIntent);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    // 사용자 이름 불러오기
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
                            String welcomeMessage = name + " 님" + "\n자리이동 " + " | " + " 자리연장" ;

                            SpannableString spannableString = new SpannableString(welcomeMessage);
                            ForegroundColorSpan nameColor = new ForegroundColorSpan(getResources().getColor(R.color.white));
                            ForegroundColorSpan timeColor = new ForegroundColorSpan(getResources().getColor(R.color.black));

                            spannableString.setSpan(nameColor, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(timeColor, name.length(), welcomeMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            userTextView.setText(spannableString);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginSuccessActivity.this, "사용자 불러오기 실패ㅠㅠ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}