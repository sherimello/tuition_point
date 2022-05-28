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

public class TutorHome extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private CardView card_popup, card_profile, card_options;
    private Spinner spinner_districts;
    private String district = "";
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        recyclerView = findViewById(R.id.recycler);
        card_popup = findViewById(R.id.card_popup);
        card_profile = findViewById(R.id.card_profile);
        card_options = findViewById(R.id.card_options);
        constraintLayout = findViewById(R.id.drawer_layout);


        spinner_districts = findViewById(R.id.spinner_districts);

        spinner_districts.setOnItemSelectedListener(this);
        ArrayAdapter<String> ad
                = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.bd_districts));

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinner_districts.setAdapter(ad);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new TutorHomeAdapter(getApplicationContext(), this));

        card_profile.setOnClickListener(this);

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
            new Handler().postDelayed(this::finish, 1000);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        district = getResources().getStringArray(R.array.bd_districts)[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}