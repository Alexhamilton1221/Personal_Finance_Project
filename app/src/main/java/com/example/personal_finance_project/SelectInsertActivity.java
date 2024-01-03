package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectInsertActivity extends AppCompatActivity {

    private Button manual,upc_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_insert);
        setupUIViews();


        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manual_insert();
            }
        });
    }
    private void setupUIViews(){
        manual = (Button) findViewById(R.id.buttonManual);
        upc_code = (Button) findViewById(R.id.buttonUPC);
    }

    private void manual_insert(){
        String userEmail = getIntent().getStringExtra("userEmail");
        Intent intent = new Intent(SelectInsertActivity.this, ManualInsertActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);


    }



}