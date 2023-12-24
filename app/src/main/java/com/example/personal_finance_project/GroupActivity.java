package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class GroupActivity extends AppCompatActivity {

    private ListView list_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        setupUIViews();

        // Retrieve user's email from Intent
        String userEmail = getIntent().getStringExtra("userEmail");

        // Use userEmail as needed in your DatabaseAccess or other parts of the activity
        // Example: Log.d("MyApp", "User Email: " + userEmail);

        DatabaseAccess databaseAccess = new DatabaseAccess(this, list_group);
        databaseAccess.execute();
    }

    private void setupUIViews(){
        list_group = (ListView) findViewById(R.id.listViewgroupmembers);
    }
}
