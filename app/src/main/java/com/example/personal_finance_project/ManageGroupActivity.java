package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ManageGroupActivity extends AppCompatActivity {
    private Button create,join,back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
        setupUIViews();



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_group();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join_group();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });

    }
    private void create_group() {
        String userEmail = getIntent().getStringExtra("userEmail");

        Intent intent = (new Intent(ManageGroupActivity.this, CreateGroupActivity.class));
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);

    }

    private void join_group() {
        String userEmail = getIntent().getStringExtra("userEmail");

        Intent intent = (new Intent(ManageGroupActivity.this, JoinGroupActivity.class));
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);

    }

    private void return_menu() {
        startActivity(new Intent(ManageGroupActivity.this, MenuActivity.class));

    }

    private void setupUIViews(){
        create = (Button) findViewById(R.id.buttonCreate);
        join = (Button) findViewById(R.id.buttonJoinGroup);
        back = (Button) findViewById(R.id.buttonBack);
    }

}