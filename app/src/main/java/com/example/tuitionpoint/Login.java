package com.example.tuitionpoint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button button_signin, button_signup;
    private CardView card_signin, card_signup;
    private TextView text_signin, text_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_signin = findViewById(R.id.button);
        button_signup = findViewById(R.id.button_signup);

        card_signin = findViewById(R.id.card_signin);
        card_signup = findViewById(R.id.card_signup);

        text_signin = findViewById(R.id.text_signin);
        text_signup = findViewById(R.id.text_signup);

        button_signin.setOnClickListener(this);
        button_signup.setOnClickListener(this);
        text_signup.setOnClickListener(this);
        text_signin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == text_signin) {
            card_signin.setVisibility(View.GONE);
            card_signup.setVisibility(View.VISIBLE);
        }
        if (view == text_signup) {
            card_signin.setVisibility(View.VISIBLE);
            card_signup.setVisibility(View.GONE);
        }
    }
}