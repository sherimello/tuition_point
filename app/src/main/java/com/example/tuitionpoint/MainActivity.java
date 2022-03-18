package com.example.tuitionpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card_student, card_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card_student = findViewById(R.id.card_student);
        card_teacher = findViewById(R.id.card_teacher);

        card_student.setOnClickListener(this);
        card_teacher.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == card_student) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            // below method is used to make scene transition
            // and adding fade animation in it.
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    MainActivity.this, card_student, "card");
            // starting our activity with below method.
            startActivity(intent, options.toBundle());
        }
    }
}