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
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity implements DatabaseAccessOperation.OnTaskCompleteListener{

    private EditText userName, userPassword, userDispName;
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
                String username = userName.getText().toString();
                String password = userPassword.getText().toString();
                String displayName = userDispName.getText().toString();

            if (AuthenticationFunctions.validate(username, password)) {
                    register_user(username, password, displayName);
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
        userDispName = (EditText) findViewById(R.id.displayname_entry_register);
        register = (Button) findViewById(R.id.buttonRegister);
        back = (Button) findViewById(R.id.buttonBack);
    }

    private void register_user(String username, String password, String displayName) {
        if (!AuthenticationFunctions.isValidEmail(username)) {
            Toast.makeText(RegisterActivity.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            // Set the display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> displayNameTask) {
                                            if (displayNameTask.isSuccessful()) {
                                                // Display name updated successfully
                                                Toast.makeText(RegisterActivity.this, "Registered an account with email.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            } else {
                                                AuthenticationFunctions.showErrorMessage(RegisterActivity.this, displayNameTask.getException().getMessage());
                                            }
                                        }
                                    });

                            // Register into database
                            register_user(username);

                        } else {
                            AuthenticationFunctions.showErrorMessage(RegisterActivity.this, task.getException().getMessage());
                        }
                    }
                });
    }
    public void register_user(String username){
        DatabaseAccessOperation fetchGroupIdOperation = new DatabaseAccessOperation(this, "register", null, null, null);
        fetchGroupIdOperation.setOnTaskCompleteListener(this);
        fetchGroupIdOperation.execute(username);
    }

    @Override
    public void onTaskComplete(int result) {
        if (result == 0) {
            Toast.makeText(this, "Record inserted successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Failed to insert record", Toast.LENGTH_SHORT).show();
        }
    }

}