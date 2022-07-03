package com.example.tuitionpoint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tuitionpoint.classes.TuitionRequestModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SendTuitionRequest extends AppCompatActivity {
//    private ImageView image_header;

    private CardView cardSend;
    private EditText edit_message, edit_phone;
    private String userNode, studentUseName, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tuition_request);

        studentUseName = getIntent().getExtras().getString("studentUserName");
        key = getIntent().getExtras().getString("key");

        cardSend = findViewById(R.id.cardSend);
        edit_message = findViewById(R.id.edit_message);
        edit_phone = findViewById(R.id.edit_phone);
        userNode = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).substring(0, Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail())
                .indexOf('@'));
        clearUserName();

        cardSend.setOnClickListener(view -> {

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("student data").child(studentUseName);
            databaseReference1.child("pending").push().setValue(new TuitionRequestModel(edit_message.getText().toString(), edit_phone.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teacher data")
                                    .child(userNode).child("pending");
                            databaseReference.push().setValue(key).addOnCompleteListener(
                                    task1 -> {
                                        startActivity(new Intent(getApplicationContext(), TutorHome.class));
                                        finish();
                                    }
                            );
                        }
                    });


        });

    }

    private void clearUserName() {
        userNode = userNode.replaceAll("[^a-zA-Z0-9]", "");
    }
}