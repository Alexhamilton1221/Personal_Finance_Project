package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinGroupActivity extends AppCompatActivity implements DatabaseAccessOperation.OnTaskCompleteListener{

    private Button join,back;
    public EditText editTextGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        setupUIViews();

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

    private void join_group() {
        String userEmail = getIntent().getStringExtra("userEmail");

        DatabaseAccessOperation fetchGroupIdOperation = new DatabaseAccessOperation(this, "join_group", editTextGroupName, null, null);
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


    private void return_menu() {
        startActivity(new Intent(JoinGroupActivity.this, MenuActivity.class));

    }



    private void setupUIViews(){
        join = (Button) findViewById(R.id.buttonJoin);
        back = (Button) findViewById(R.id.buttonReturn);
        editTextGroupName=(EditText) findViewById(R.id.editTextGroupName);

    }

}