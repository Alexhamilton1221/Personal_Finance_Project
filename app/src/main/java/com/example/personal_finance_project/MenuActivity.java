package com.example.personal_finance_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MenuActivity extends AppCompatActivity {

    private Button open;
    private DatabaseReference firebaseRef;
    private TextView title;

//    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://shopunityhub-default-rtdb.firebaseio.com/");

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        // Set IDs for widgets
        setupUIViews();
        //Set title
        set_title();




//        firebaseRef = FirebaseDatabase.getInstance().getReference("test");
//
//
//        firebaseRef.setValue("working?");

    }



            private void showToast(String message) {
                Toast.makeText(MenuActivity.this, message, Toast.LENGTH_SHORT).show();
            }

    private void setupUIViews(){
        title = (TextView) findViewById(R.id.textViewTitleWelcome);
        open = (Button) findViewById(R.id.buttonOpenList);
    }

    private void set_title(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            if (name != null) {
                String newText = "Welcome " + name;
                title.setText(newText);
            } else {
                // Handle the case where the display name is null
                title.setText("Welcome");
            }
        } else {
            // Handle the case where the user is null
            title.setText("Welcome");
        }

    }



        }










