package com.example.tuitionpoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.tuitionpoint.adapter.TutorHomeAdapter;

public class TutorHome extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private CardView card_popup, card_profile, card_options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        recyclerView = findViewById(R.id.recycler);
        card_popup = findViewById(R.id.card_popup);
        card_profile = findViewById(R.id.card_profile);
        card_options = findViewById(R.id.card_options);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new TutorHomeAdapter(card_popup));

        card_profile.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        card_popup.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == card_profile) {
            Intent intent = new Intent(getApplicationContext(), TutorProfile.class);
            // below method is used to make scene transition
            // and adding fade animation in it.
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    TutorHome.this, card_options, "card");
            // starting our activity with below method.
            startActivity(intent, options.toBundle());
            new Handler().postDelayed(this::finish,1000);
        }
    }
}