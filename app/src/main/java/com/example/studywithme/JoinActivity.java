package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {

    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText phoneEdit;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // FirebaseApp 초기화 추가
        FirebaseApp.initializeApp(this);

        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_edit);
        passwordEdit = findViewById(R.id.password_edit);
        phoneEdit = findViewById(R.id.phone_edit);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // 이름
        nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameEdit.setBackgroundResource(R.drawable.edit_background_blue);
                } else {
                    nameEdit.setBackgroundResource(R.drawable.edit_background_gray);
                }
            }
        });

        // 이메일
        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailEdit.setBackgroundResource(R.drawable.edit_background_blue);
                } else {
                    emailEdit.setBackgroundResource(R.drawable.edit_background_gray);
                }
            }
        });

        // 비밀번호
        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Change the input type when the passwordEdit gains focus
                    passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    passwordEdit.setBackgroundResource(R.drawable.edit_background_blue);
                } else {
                    // Change it back to the password input type when it loses focus
                    passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordEdit.setBackgroundResource(R.drawable.edit_background_gray);
                }
            }
        });

        // 전화번호
        phoneEdit.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phoneEdit.setBackgroundResource(R.drawable.edit_background_blue);
                } else {
                    phoneEdit.setBackgroundResource(R.drawable.edit_background_gray);
                }
            }
        });

        // 회원가입버튼
        findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String phone = phoneEdit.getText().toString().trim();

                // 비밀번호 형식 검증
                if (!isValidPassword(password)) {
                    Toast.makeText(JoinActivity.this, "비밀번호는 영문과 숫자의 조합으로 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 전화번호 형식 검증
                if (!isValidPhoneNumber(phone)) {
                    Toast.makeText(JoinActivity.this, "올바른 전화번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(JoinActivity.this, task -> {
                            if (task.isSuccessful()) {
                                User user = new User(name, email, password, phone);
                                String uid = task.getResult().getUser().getUid();
                                databaseReference.child(uid).setValue(user);

                                Toast.makeText(JoinActivity.this, "회원가입을 축하합니다!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    // 이메일이 이미 사용 중인 경우 처리
                                    // 사용자에게 오류 메시지를 표시하는 것이 좋습니다.
                                    Toast.makeText(JoinActivity.this, "이미 사용 중인 이메일 주소입니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // 기타 등록 실패 처리
                                    Toast.makeText(JoinActivity.this, "등록 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    private class PhoneNumberFormattingTextWatcher implements TextWatcher {
        private boolean isFormatting;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isFormatting) {
                return;
            }

            isFormatting = true;

            // Remove old dashes
            String digits = s.toString().replaceAll("-", "");

            // Insert new dashes in the appropriate places
            if (digits.length() >= 4 && digits.length() <= 7) {
                digits = digits.substring(0, 3) + "-" + digits.substring(3);
            } else if (digits.length() >= 8) {
                digits = digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
            }

            phoneEdit.setText(digits);
            phoneEdit.setSelection(phoneEdit.getText().length());

            isFormatting = false;
        }
    }

    private boolean isValidPassword(String password) {
        // 비밀번호는 영문과 숫자의 조합으로 8자 이상이어야 함
        return password.matches("^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$");
    }

    // 전화번호는 010-0000-0000 형식이어야 함
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{3}-\\d{4}-\\d{4}");
    }

}
