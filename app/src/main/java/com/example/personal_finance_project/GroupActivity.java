package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GroupActivity extends AppCompatActivity {

    private ListView list_group;
    private Button back;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        setupUIViews();

        // Retrieve user's email from Intent
        userEmail = getIntent().getStringExtra("userEmail");




        DatabaseAccessListView databaseAccessListView = new DatabaseAccessListView(this, list_group, "group_tab");
        databaseAccessListView.execute(userEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recall the code when the activity is brought back to the foreground
        DatabaseAccessListView databaseAccessListView = new DatabaseAccessListView(this, list_group, "group_tab");
        databaseAccessListView.execute(userEmail);
    }



    private void return_menu() {
        startActivity(new Intent(GroupActivity.this, MenuActivity.class));
    }

    private void setupUIViews() {
        list_group = findViewById(R.id.listViewgroupmembers);
        back = findViewById(R.id.buttonBack);
    }
}
