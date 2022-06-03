package com.example.tuitionpoint;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.tuitionpoint.classes.Constants;
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

    private Button button_signup;
    private CardView card_signin, card_signup, card_sign_in;
    private TextView text_signin, text_signup, text_progress_dialog;
    private FirebaseAuth mAuth;
    private LinearLayout layout_loading;
    private ImageView image_loading;
    private EditText edit_fullname, edit_study_level, edit_student_id, edit_student_institution, edit_student_phone, edit_student_address,
            edit_signup_mail, edit_signup_password, edit_login_mail, edit_login_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        button_signup = findViewById(R.id.button_signup);

        card_signin = findViewById(R.id.card_signin);
        card_signup = findViewById(R.id.card_signup);
        card_sign_in = findViewById(R.id.card_sign_in);

        text_signin = findViewById(R.id.text_signin);
        text_signup = findViewById(R.id.text_signup);
        text_progress_dialog = findViewById(R.id.text_progress_dialog);

        edit_fullname = findViewById(R.id.edit_fullname);
        edit_signup_mail = findViewById(R.id.edit_signup_mail);
        edit_signup_password = findViewById(R.id.edit_signup_password);
        edit_student_address = findViewById(R.id.edit_student_address);
        edit_student_id = findViewById(R.id.edit_student_id);
        edit_student_institution = findViewById(R.id.edit_student_institution);
        edit_student_phone = findViewById(R.id.edit_student_phone);
        edit_study_level = findViewById(R.id.edit_study_level);
        edit_login_mail = findViewById(R.id.edit_login_mail);
        edit_login_password = findViewById(R.id.edit_login_password);

        image_loading = findViewById(R.id.image_loading);

        layout_loading = findViewById(R.id.layout_loading);

        Glide.with(getApplicationContext())
                .load(R.drawable.loading)
                .into(image_loading);


        card_sign_in.setOnClickListener(this);
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

    private void show_snackBar(String msg) {
        Snackbar.make(edit_fullname, msg, Snackbar.LENGTH_SHORT).show();
    }

    private String button_name = "";

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            //if user creation fails, will terminate here...
            return;
        }

        layout_loading.setVisibility(View.GONE);
        if (button_name.equals("reg")) {

            //executes only when registration button is clicked...

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).substring(0, edit_signup_mail.getText().toString().trim()
                    .indexOf('@')));
            myEdit.apply(); //sharedpreference saves the username for future user data query...

            Intent intent = new Intent(getApplicationContext(), StudentHome.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    Login.this, card_signup, "card");
            startActivity(intent, options.toBundle());
        }
        if (button_name.equals("login")) {

            //executes only when login button is clicked...

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("username", edit_login_mail.getText().toString().trim().substring(0, edit_login_mail.getText().toString().trim()
                    .indexOf('@')));
            myEdit.apply(); //sharedpreference saves the username for future user data query...

            Intent intent = new Intent(getApplicationContext(), StudentHome.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    Login.this, card_signin, "card");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(new Intent(getApplicationContext(), StudentHome.class));
            finish();
        }
    }


    private void sign_in() {
        //signs in user to firebase Auth...
        mAuth.signInWithEmailAndPassword(edit_login_mail.getText().toString().trim(), edit_login_password.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        //decides where to send next...
                        updateUI(mAuth.getCurrentUser());
                        return;
                    }
                    layout_loading.setVisibility(View.GONE);
                    show_snackBar(Objects.requireNonNull(task.getException()).getMessage());
                }).addOnCanceledListener(() -> show_snackBar("connection cancelled. try again later")).addOnFailureListener(e -> {
            show_snackBar(e.getLocalizedMessage());
            layout_loading.setVisibility(View.GONE);
        });
    }

    private void register(String email, String password) {

        //registers user to firebase auth...
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

                        //uploads the data of newly signed in user...
                        uploadData();
                    } else {
                        text_progress_dialog.setText(Objects.requireNonNull(task.getException()).toString());
                        Toast.makeText(Login.this, "Authentication failed. \nerror message: " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void uploadData() {

        //uploading data to realtime database with child node fetched from user mail [e.g; c183018@gmail.com -> username: c183018]

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("student data")
                .child(edit_signup_mail.getText().toString().trim().substring(0, edit_signup_mail.getText().toString().trim()
                        .indexOf('@')));


        //after data is successfully uploaded, calls updateUI...
        databaseReference.setValue(new StudentData(edit_signup_mail.getText().toString().trim(), edit_signup_password.getText().toString().trim(), edit_fullname.getText().toString().trim(), edit_student_address.getText().toString().trim()
                , edit_student_phone.getText().toString().trim(), edit_study_level.getText().toString().trim(), edit_student_institution.getText().toString().trim(),
                edit_student_id.getText().toString().trim())).addOnCompleteListener(task1 -> updateUI(mAuth.getCurrentUser())).addOnCanceledListener(() -> Snackbar.make(edit_fullname, "oops! try again later!", Snackbar.LENGTH_SHORT).show());


    }

    @Override
    public void onClick(View view) {
        if (view == button_signup) {
            if (!edit_fullname.getText().toString().trim().isEmpty() && !edit_study_level.getText().toString().trim().isEmpty() &&
                    !edit_student_phone.getText().toString().trim().isEmpty() && !edit_student_id.getText().toString().trim().isEmpty() &&
                    !edit_signup_password.getText().toString().trim().isEmpty() && !edit_signup_mail.getText().toString().trim().isEmpty() &&
                    !edit_student_institution.getText().toString().trim().isEmpty() && !edit_student_address.getText().toString().trim().isEmpty()) {
                text_progress_dialog.setText("setting you up...");
                layout_loading.setVisibility(View.VISIBLE);
                button_name = "reg";
                register(edit_signup_mail.getText().toString().trim(), edit_signup_password.getText().toString().trim());
                return;
            }

            show_snackBar("make sure no field is empty!");

        }
        if (view == card_sign_in) {
            if (!edit_login_mail.getText().toString().trim().isEmpty() && !edit_login_password.getText().toString().trim().isEmpty()) {
                text_progress_dialog.setText("setting you up...");
                layout_loading.setVisibility(View.VISIBLE);
                button_name = "login";
                sign_in();
            } else
                show_snackBar("make sure no field is empty!");
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