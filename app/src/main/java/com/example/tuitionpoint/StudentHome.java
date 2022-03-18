package com.example.tuitionpoint;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentHome extends AppCompatActivity {


    private TextView text_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        text_welcome = findViewById(R.id.text_welcome);

        text_welcome.setAlpha(0);
        text_welcome.setTranslationX(-100);

        new Handler().postDelayed(() -> {
            text_welcome.animate().alpha(1).translationX(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000).start();

        }, 700);

    }
}