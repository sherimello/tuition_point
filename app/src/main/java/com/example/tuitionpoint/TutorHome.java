package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionpoint.adapter.TutorHomeAdapter;
import com.example.tuitionpoint.classes.TuitionRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class TutorHome extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private CardView card_popup, card_profile, card_options;
    private Spinner spinner_districts;
    private TextView text_logout;
    private String district = "";
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        recyclerView = findViewById(R.id.recycler);
        card_popup = findViewById(R.id.card_popup);
        card_profile = findViewById(R.id.card_profile);
        text_logout = findViewById(R.id.text_logout);
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
//        recyclerView.setAdapter(new TutorHomeAdapter(getApplicationContext(), this));

        getAllTuitions();

        card_profile.setOnClickListener(this);
        text_logout.setOnClickListener(this);

    }

    private void getAllTuitions() {

        ArrayList<TuitionRequest> tuitionRequests = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Tuition Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keys.add(dataSnapshot.getKey());
                    tuitionRequests.add(new TuitionRequest(
                            Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("district").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("student_class").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("subject").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("days").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("salary").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString(),
                            Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString()
                            ));
                    if (tuitionRequests.size() == snapshot.getChildrenCount()) {
                        recyclerView.setAdapter(new TutorHomeAdapter(getApplicationContext(), TutorHome.this, tuitionRequests, keys));
                        Toast.makeText(TutorHome.this, String.valueOf(tuitionRequests.size()), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == text_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
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
        if (district.equals("All Districts"))
            getAllTuitions();
        else
            getTuitionForSelectedDistrict();

    }

    int i = 0;

    private void getTuitionForSelectedDistrict() {

        ArrayList<TuitionRequest> tuitionRequests = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Tuition Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keys.add(dataSnapshot.getKey());
                    i++;
                    if (Objects.requireNonNull(dataSnapshot.child("district").getValue()).toString().equals(district))
                        tuitionRequests.add(new TuitionRequest(
                                Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("district").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("student_class").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("subject").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("days").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("salary").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("address").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                                Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString(),
                                Objects.requireNonNull(snapshot.child("userName").getValue()).toString()
                                ));
                    if (i == snapshot.getChildrenCount()) {
                        recyclerView.removeAllViews();
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(new TutorHomeAdapter(getApplicationContext(), TutorHome.this, tuitionRequests, keys));
                        Toast.makeText(TutorHome.this, String.valueOf(tuitionRequests.size()), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}