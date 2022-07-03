package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionpoint.adapter.PendingRequestsAdapter;
import com.example.tuitionpoint.classes.TuitionRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentProfile extends AppCompatActivity {

    private RecyclerView recycler;
    private TextView username;
    private String userNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        recycler = findViewById(R.id.recycler);

        username = findViewById(R.id.text_username);

        userNode = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).substring(0, Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())
                .indexOf('@'));
        clearUserName();

        username.setText(userNode);

        getAllRequests();

        recycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        Toast.makeText(this, userNode, Toast.LENGTH_SHORT).show();

    }

    private void getAllRequests() {
        List<String> keys = new ArrayList<>();
        ArrayList<TuitionRequest> tuitionRequests = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("student data")
                .child(userNode).child("requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keys.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
//                    Toast.makeText(StudentProfile.this, Objects.requireNonNull(dataSnapshot.getValue()).toString(), Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < keys.size(); i++) {
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Tuition Requests").child(keys.get(i));
                    int finalI = i;
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            tuitionRequests.add(new TuitionRequest(Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("district").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("student_class").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("subject").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("days").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("salary").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("address").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("status").getValue()).toString(),
                                    Objects.requireNonNull(snapshot.child("userName").getValue()).toString()
                                    ));
                            if (finalI == keys.size() - 1) {
                                recycler.setAdapter(new PendingRequestsAdapter(getApplicationContext(), StudentProfile.this, tuitionRequests));
                                Toast.makeText(StudentProfile.this, String.valueOf(tuitionRequests.size()), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(StudentProfile.this, String.valueOf(tuitionRequests.get(0).name), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(StudentProfile.this, Objects.requireNonNull(snapshot.child("address").getValue()).toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            show_snackBar(error.getMessage());

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_snackBar(String msg) {
        Snackbar.make(recycler, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void clearUserName() {
        userNode = userNode.replaceAll("[^a-zA-Z0-9]", "");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), StudentHome.class));
        finish();
    }
}