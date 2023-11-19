package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                if(validate()){
                    //Check database for a user.


                }
            }
        });
    }

    private void setupUIViews(){
        userName = (EditText) findViewById(R.id.username_entry);
        userPassword = (EditText) findViewById(R.id.password_entry);
        login = (Button) findViewById(R.id.buttonLogin);
        create_account = (Button) findViewById(R.id.buttonCreateAccount);
    }

    private Boolean validate(){
        Boolean result = false;
        String name =  userName.getText().toString();
        String password = userPassword.getText().toString();

        if (name.isEmpty() && password.isEmpty()){
            Toast.makeText(this,"You missed a field.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}