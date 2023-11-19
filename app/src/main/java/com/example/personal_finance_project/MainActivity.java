package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText userName, userPassword;
    private Button login, create_account;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set IDs for widgets
        setupUIViews();
        // Instance of Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=userName.getText().toString(); String password=userPassword.getText().toString();
                if (AuthenticationFunctions.validate(username,password)) {
                    //Check database for a user.
                }
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_account();
            }
        });

    }

    private void register_account(){
        // Create an Intent to start the RegisterActivity
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    private void setupUIViews(){
        userName = (EditText) findViewById(R.id.email_entry_login);
        userPassword = (EditText) findViewById(R.id.password_entry_login);
        login = (Button) findViewById(R.id.buttonLogin);
        create_account = (Button) findViewById(R.id.buttonCreateAccount);
    }



}