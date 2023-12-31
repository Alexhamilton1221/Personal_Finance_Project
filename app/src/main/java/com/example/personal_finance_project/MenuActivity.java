package com.example.personal_finance_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private Button view_list,view_group,logout;
    private TextView title;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        // Set IDs for widgets
        setupUIViews();
        //Set title
        set_title();

        view_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_group();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        view_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_list();
            }
        });

    }

    private void logout(){
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }
    private void view_group(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            Intent intent = new Intent(MenuActivity.this, GroupActivity.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        }
    }
    private void view_list(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            Intent intent = new Intent(MenuActivity.this, ViewListActivity.class);
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        }
    }

    private void setupUIViews(){
        title = (TextView) findViewById(R.id.textViewTitleWelcome);
        view_list = (Button) findViewById(R.id.buttonViewList);
        view_group = (Button) findViewById(R.id.buttonViewGroup);
        logout = (Button) findViewById(R.id.buttonLogout);
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










