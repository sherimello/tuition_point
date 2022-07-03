package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionpoint.adapter.TutorHomeAdapter;

public class TutorProfile extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);

        recyclerView = findViewById(R.id.recycler);
        constraintLayout = findViewById(R.id.constraintLayout);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        recyclerView.setAdapter(new TutorHomeAdapter(getApplicationContext(), this));

    }

    @Override
    public void onBackPressed() {
//        if (card_popup.getVisibility() == View.VISIBLE) {
//            card_popup.setVisibility(View.GONE);
//            return;
//        }

        Intent intent = new Intent(getApplicationContext(), TutorHome.class);
        // below method is used to make scene transition
        // and adding fade animation in it.
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                TutorProfile.this, constraintLayout, "card");
        // starting our activity with below method.
        startActivity(intent, options.toBundle());
        new Handler().postDelayed(this::finish, 1000);
    }
}