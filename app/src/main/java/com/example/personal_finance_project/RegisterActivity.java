package com.example.personal_finance_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {

    private EditText userName, userPassword;
    private Button register,back;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set IDs for widgets
        setupUIViews();
        // Instance of Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=userName.getText().toString(); String password=userPassword.getText().toString();
                if (AuthenticationFunctions.validate(username,password)) {
                    register_user(username,password);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));

            }
        });

    }

    private void setupUIViews(){
        userName = (EditText) findViewById(R.id.email_entry_register);
        userPassword = (EditText) findViewById(R.id.password_entry_register);
        register = (Button) findViewById(R.id.buttonRegister);
        back = (Button) findViewById(R.id.buttonBack);
    }

   private void register_user(String username, String password) {
       if (!AuthenticationFunctions.isValidEmail(username)) {
           Toast.makeText(RegisterActivity.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
           return;
       }
        firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registered an account with email.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        } else {
                            AuthenticationFunctions.showErrorMessage(RegisterActivity.this, task.getException().getMessage());
                        }
                    }
                });
    }

}