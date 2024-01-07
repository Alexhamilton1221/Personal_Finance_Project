package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupActivity extends AppCompatActivity implements DatabaseAccessOperation.OnTaskCompleteListener{

    private Button create,back;
    public EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setupUIViews();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_group();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });

    }
    private void return_menu() {
        startActivity(new Intent(CreateGroupActivity.this, MenuActivity.class));

    }
    private void create_group() {
        String userEmail = getIntent().getStringExtra("userEmail");

        DatabaseAccessOperation fetchGroupIdOperation = new DatabaseAccessOperation(this, "create_group", nameEditText, null, null);
        fetchGroupIdOperation.setOnTaskCompleteListener(this);
        fetchGroupIdOperation.execute(userEmail);
        return_menu();


    }

    @Override
    public void onTaskComplete(int result) {
        if (result == 0) {
            Toast.makeText(this, "Record inserted successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Failed to insert record", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUIViews(){
        create = (Button) findViewById(R.id.buttonCreate);
        back = (Button) findViewById(R.id.buttonBack);
        nameEditText=(EditText) findViewById(R.id.editTextName);

    }



}