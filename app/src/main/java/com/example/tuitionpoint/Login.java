package com.example.tuitionpoint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.example.tuitionpoint.classes.StudentData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button button_signin, button_signup;
    private CardView card_signin, card_signup;
    private TextView text_signin, text_signup;
    private FirebaseAuth mAuth;
    private LinearLayout layout_loading;
    private ImageView image_loading;
    private ProgressDialog progressDialog;
    private EditText edit_fullname, edit_study_level, edit_student_id, edit_student_institution, edit_student_phone, edit_student_address,
            edit_signup_mail, edit_signup_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("checking log state...");
        progressDialog.setIcon(R.drawable.loading);
//        progressDialog.show();

// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        button_signin = findViewById(R.id.button);
        button_signup = findViewById(R.id.button_signup);

        card_signin = findViewById(R.id.card_signin);
        card_signup = findViewById(R.id.card_signup);

        text_signin = findViewById(R.id.text_signin);
        text_signup = findViewById(R.id.text_signup);

        edit_fullname = findViewById(R.id.edit_fullname);
        edit_signup_mail = findViewById(R.id.edit_signup_mail);
        edit_signup_password = findViewById(R.id.edit_signup_password);
        edit_student_address = findViewById(R.id.edit_student_address);
        edit_student_id = findViewById(R.id.edit_student_id);
        edit_student_institution = findViewById(R.id.edit_student_institution);
        edit_student_phone = findViewById(R.id.edit_student_phone);
        edit_study_level = findViewById(R.id.edit_study_level);

        image_loading = findViewById(R.id.image_loading);

        layout_loading = findViewById(R.id.layout_loading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(image_loading);


        button_signin.setOnClickListener(this);
        button_signup.setOnClickListener(this);
        text_signup.setOnClickListener(this);
        text_signin.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            layout_loading.setVisibility(View.GONE);
            return;
        }
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            return;
        }
        startActivity(new Intent(getApplicationContext(), StudentHome.class));
        finish();
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException weakPassword) {

                            // TODO: take your actions!
                        } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthUserCollisionException malformedEmail) {
                            Snackbar.make(edit_fullname, Objects.requireNonNull(malformedEmail.getLocalizedMessage()), Snackbar.LENGTH_LONG).setAction(
                                    "login", view -> text_signup.callOnClick()
                            ).show();
                            // TODO: Take your action
                        } catch (Exception e) {

                            Snackbar.make(edit_fullname, Objects.requireNonNull(e.getLocalizedMessage()), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "in", Toast.LENGTH_SHORT).show();
                        uploadData();
                    } else {
                        Toast.makeText(Login.this, "Authentication failed. \nerror message: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void uploadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://tuition-point-57df4-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("student data")
                .child(edit_signup_mail.getText().toString().trim().substring(0, edit_signup_mail.getText().toString().trim()
                .indexOf('@')));
        databaseReference.setValue(new StudentData(edit_signup_mail.getText().toString().trim(), edit_signup_password.getText().toString().trim(), edit_fullname.getText().toString().trim(), edit_student_address.getText().toString().trim()
                , edit_student_phone.getText().toString().trim(), edit_study_level.getText().toString().trim(), edit_student_institution.getText().toString().trim(),
                edit_student_id.getText().toString().trim())).addOnCompleteListener(task1 -> updateUI(mAuth.getCurrentUser())).addOnCanceledListener(() -> Snackbar.make(edit_fullname, "oops! try again later!", Snackbar.LENGTH_SHORT).show());


    }

    @Override
    public void onClick(View view) {
        if (view == button_signup) {
            register(edit_signup_mail.getText().toString().trim(), edit_signup_password.getText().toString().trim());
        }
        if (view == button_signin) {
            Intent intent = new Intent(getApplicationContext(), StudentHome.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, card_signin, "card");
            startActivity(intent, options.toBundle());
        }
        if (view == text_signin) {
            card_signin.setVisibility(View.GONE);
            card_signup.setVisibility(View.VISIBLE);
        }
        if (view == text_signup) {
            card_signin.setVisibility(View.VISIBLE);
            card_signup.setVisibility(View.GONE);
        }
    }
}