package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class StudentHome extends AppCompatActivity implements View.OnClickListener {

    private TextView text_welcome, text_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        text_welcome = findViewById(R.id.text_welcome);
        text_logout = findViewById(R.id.text_logout);

        text_welcome.setAlpha(0);
        text_welcome.setTranslationX(-100);

        new Handler().postDelayed(() -> {
            text_welcome.animate().alpha(1).translationX(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000).start();

        }, 700);

        text_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == text_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    }
}