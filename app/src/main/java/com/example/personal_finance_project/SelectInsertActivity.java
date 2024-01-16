package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectInsertActivity extends AppCompatActivity {

    private Button manual,upc_code,back;

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

        upc_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upc_insert();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });

    }
    private void setupUIViews(){
        manual = (Button) findViewById(R.id.buttonManual);
        upc_code = (Button) findViewById(R.id.buttonUPC);
        back = (Button) findViewById(R.id.buttonBack);
    }

    private void return_menu(){
        startActivity(new Intent(SelectInsertActivity.this, MenuActivity.class));
    }
    private void manual_insert(){
        String userEmail = getIntent().getStringExtra("userEmail");
        Intent intent = new Intent(SelectInsertActivity.this, ManualInsertActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);

    }

    private void upc_insert(){
        String userEmail = getIntent().getStringExtra("userEmail");
        Intent intent = new Intent(SelectInsertActivity.this, UPCReaderActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);

    }

}