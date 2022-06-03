package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tuitionpoint.classes.Constants;
import com.example.tuitionpoint.classes.TuitionRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.Objects;

public class StudentHome extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TextView text_welcome, text_logout;
    private CardView cardSend;
    private EditText edit_name, edit_class, edit_subject, edit_days, edit_salary, edit_student_address, edit_description;
    private LinearLayout layout_loading;
    private FirebaseUser currentUser;
    private Constants constants;
    private Spinner spinner_districts;
    private String district="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        constants = new Constants();

        text_welcome = findViewById(R.id.text_welcome);
        text_logout = findViewById(R.id.text_logout);

        cardSend = findViewById(R.id.cardSend);

        edit_name = findViewById(R.id.edit_name);
        edit_class = findViewById(R.id.edit_class);
        edit_subject = findViewById(R.id.edit_subject);
        edit_days = findViewById(R.id.edit_days);
        edit_salary = findViewById(R.id.edit_salary);
        edit_student_address = findViewById(R.id.edit_student_address);
        edit_description = findViewById(R.id.edit_description);

        layout_loading = findViewById(R.id.layout_loading);

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

        text_welcome.setAlpha(0);
        text_welcome.setTranslationX(-100);

        getUserNameAndDisplayIt();

        text_logout.setOnClickListener(this);
        cardSend.setOnClickListener(this);

    }

    private void getUserNameAndDisplayIt() {

        //gets user's full name from realtime database and displays it...

        FirebaseDatabase.getInstance().getReference("student data")
                .child(constants.getUserName(Objects.requireNonNull(currentUser.getEmail()))).child("fullname")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(getApplicationContext(), String.valueOf(snapshot.getValue()), Toast.LENGTH_SHORT).show();
                        String tempName = String.valueOf(snapshot.getValue());
                        if (String.valueOf(snapshot.getValue()).length() > 16) {
                            tempName = String.valueOf(snapshot.getValue()).substring(0, 16) + "...";
                        }
                        text_welcome.setText(MessageFormat.format("welcome {0},\n", tempName));
                        new Handler().postDelayed(() -> {
                            text_welcome.animate().alpha(1).translationX(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000).start();

                        }, 700);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == cardSend) {
            if (!check_if_editText_is_empty()) {
                layout_loading.setVisibility(View.VISIBLE);
                send_tuition_request();
                return;
            }
            show_snackBar("make sure all fields are filled!");
        }
        if (view == text_logout) {

            //signs user out and redirects to login page...

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    }

    private void show_snackBar(String msg) {
        Snackbar.make(edit_class, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void clear_editTexts() {
        edit_name.setText("");
        edit_class.setText("");
        edit_description.setText("");
        edit_days.setText("");
        edit_salary.setText("");
        edit_student_address.setText("");
        edit_subject.setText("");
    }

    private boolean check_if_editText_is_empty() {

        if (edit_name.getText().toString().trim().isEmpty() || edit_class.getText().toString().trim().isEmpty()
                || edit_subject.getText().toString().trim().isEmpty() || edit_days.getText().toString().trim().isEmpty()
                || edit_salary.getText().toString().trim().isEmpty() || edit_student_address.getText().toString().trim().isEmpty()
                || edit_description.getText().toString().trim().isEmpty() || district.isEmpty()) {
            return true;
        }

        return false;
    }

    private void send_tuition_request() {

        //sends user request to realtime database and stores the key of each request to the corresponding user node...

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String key = databaseReference.push().getKey();
        assert key != null;
        databaseReference.child("Tuition Requests").child(key).setValue(new TuitionRequest(edit_name.getText().toString().trim(), district,
                        edit_class.getText().toString().trim(), edit_subject.getText().toString().trim(), edit_days.getText().toString().trim(),
                        edit_salary.getText().toString().trim(), edit_student_address.getText().toString().trim(), edit_description.getText().toString().trim(), "0"))
                .addOnCompleteListener(task -> databaseReference.child("student data").child(constants.getUserName(Objects.requireNonNull(currentUser.getEmail())))
                        .child("requests").push().setValue(key).addOnCompleteListener(task1 -> {
                            show_snackBar("Request sent!");
                            layout_loading.setVisibility(View.GONE);
                            clear_editTexts();
                        })).addOnFailureListener(e -> {
                    show_snackBar(e.getLocalizedMessage());
                    clear_editTexts();
                });

    }

    //gets the current district name and stores it for later use...
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        district = getResources().getStringArray(R.array.bd_districts)[i];
        Toast.makeText(this, "you're from: " + district, Toast.LENGTH_SHORT).show();

    }

    //if no district is chosen...
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Toast.makeText(this, "please choose a district to send a tuition request!", Toast.LENGTH_SHORT).show();

    }
}