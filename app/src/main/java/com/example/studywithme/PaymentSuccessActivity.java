package com.example.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pg);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView moneyTextView = findViewById(R.id.money);
        int fee = getIntent().getIntExtra("fee", 0);
        String paymentDate = getIntent().getStringExtra("PAYMENT_DATE");
        String cardName = getIntent().getStringExtra("CARD_NAME");

        TextView paymentDateTextView = findViewById(R.id.pay_date);
        TextView cardNameTextView = findViewById(R.id.card_name);

        updateMoneyTextView(moneyTextView, fee);
        paymentDateTextView.setText(paymentDate);
        cardNameTextView.setText(cardName);

        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentSuccessActivity.this, LoginSuccessActivity.class);
                startActivity(intent);
            }
        });
    }


    private void updateMoneyTextView(TextView moneyTextView, int fee) {
        String moneyMessage = "요금: " + fee + "원";
        moneyTextView.setText(moneyMessage);
    }
}
