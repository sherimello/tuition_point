package com.example.tuitionpoint;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    private ImageView image_logo, image_bg;
    private ConstraintLayout logo_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image_logo = findViewById(R.id.image_logo);
        image_bg = findViewById(R.id.image_bg);

        logo_layout = findViewById(R.id.logo_layout);

        logo_layout.setScaleX(1.5f);
        logo_layout.setScaleY(1.5f);
        logo_layout.setAlpha(0);
        image_bg.setScaleX(1.7f);
        image_bg.setScaleY(1.7f);
        image_bg.setAlpha(0f);

        logo_layout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        image_bg.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        logo_layout.animate().alpha(1).scaleX(1).scaleY(1).setDuration(700).setInterpolator(new OvershootInterpolator()).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        image_bg.animate().alpha(0.35f).scaleX(1).scaleY(1).setDuration(1100).setInterpolator(new DecelerateInterpolator());
                    }
                }
        );

        new Handler().postDelayed(() -> {
            logo_layout.setLayerType(View.LAYER_TYPE_NONE, null);
            image_bg.setLayerType(View.LAYER_TYPE_NONE, null);

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return;
            }
            startActivity(new Intent(getApplicationContext(), StudentHome.class));
            finish();
        }, 3300);

    }
}