package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tuitionpoint.classes.Constants;
import com.example.tuitionpoint.classes.TuitionRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class StudentHome extends AppCompatActivity implements View.OnClickListener {

    private TextView text_welcome, text_logout;
    private CardView cardSend;
    private EditText edit_name, edit_class, edit_subject, edit_days, edit_salary, edit_student_address, edit_description;
    private LinearLayout layout_loading;
    private FirebaseUser currentUser;
    private Constants constants;

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

        text_welcome.setAlpha(0);
        text_welcome.setTranslationX(-100);

        new Handler().postDelayed(() -> {
            text_welcome.animate().alpha(1).translationX(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000).start();

        }, 700);

        text_logout.setOnClickListener(this);
        cardSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == cardSend) {
            if (!check_if_editText_is_empty()) {
                layout_loading.setVisibility(View.VISIBLE);
                send_tuition_request();
            }
            show_snackBar("make sure all fields are filled!");
        }
        if (view == text_logout) {
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
                || edit_description.getText().toString().trim().isEmpty()) {
            return true;
        }

        return false;
    }

    private void send_tuition_request() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(constants.databaseAddress).getReference();
        String key = databaseReference.push().getKey();
        assert key != null;
        databaseReference.child("Tuition Requests").child(key).setValue(new TuitionRequest(edit_name.getText().toString().trim(),
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
}