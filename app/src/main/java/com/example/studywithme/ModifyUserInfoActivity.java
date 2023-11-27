package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyUserInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText editTextName;
    private TextView userEmailTextView;
    private EditText editTextPhone;
    private EditText editTextNewPassword;
    private Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextName = findViewById(R.id.user_name);
        userEmailTextView = findViewById(R.id.user_email);
        editTextPhone = findViewById(R.id.user_phone);
        editTextNewPassword = findViewById(R.id.new_password);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // 로고
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyUserInfoActivity.this, LoginSuccessActivity.class);
                startActivity(intent);
            }
        });

        // 수정 내용 저장 버튼
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        checkAndUpdateUserInfo();
    }

    // 사용자 정보 불러오기
    private void checkAndUpdateUserInfo() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        try {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);
                            String phone = dataSnapshot.child("phone").getValue(String.class);

                            if (name != null) {
                                editTextName.setText(name);
                            }

                            if (email != null) {
                                userEmailTextView.setText(email);
                            }

                            if (phone != null) {
                                editTextPhone.setText(phone);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ModifyUserInfoActivity.this, "데이터를 읽는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ModifyUserInfoActivity.this, "사용자 데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 사용자 정보 수정내용 저장
    private void saveChanges() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            String newName = editTextName.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();
            String newPhone = editTextPhone.getText().toString().trim();

            try {
                if (!newName.isEmpty()) {
                    databaseReference.child("users").child(uid).child("name").setValue(newName);
                }

                if (!newPassword.isEmpty()) {
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ModifyUserInfoActivity.this, "비밀번호가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ModifyUserInfoActivity.this, "비밀번호 업데이트 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                if (!newPhone.isEmpty()) {
                    databaseReference.child("users").child(uid).child("phone").setValue(newPhone);
                }

                Toast.makeText(ModifyUserInfoActivity.this, "변경 사항이 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ModifyUserInfoActivity.this, "변경 사항을 저장하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
