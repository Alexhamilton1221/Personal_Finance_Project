package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class GroupActivity extends AppCompatActivity {

    private ListView list_group;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        setupUIViews();

        // Retrieve user's email from Intent
        String userEmail = getIntent().getStringExtra("userEmail");


        DatabaseAccessListView databaseAccessListView = new DatabaseAccessListView(this, list_group, "group_tab");
        databaseAccessListView.execute(userEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();

            }
        });

    }
    private void return_menu(){
        startActivity(new Intent(GroupActivity.this, MenuActivity.class));
    }

    private void setupUIViews(){
        list_group = (ListView) findViewById(R.id.listViewgroupmembers);
        back = (Button) findViewById(R.id.buttonBack);
    }
}