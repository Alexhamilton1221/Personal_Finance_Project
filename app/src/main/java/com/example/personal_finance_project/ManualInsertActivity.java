package com.example.personal_finance_project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.personal_finance_project.DatabaseAccessOperation;

public class ManualInsertActivity extends AppCompatActivity implements DatabaseAccessOperation.OnTaskCompleteListener {

    private Button back, create;
    public EditText nameEditText;
    public EditText priceEditText;
    public EditText quantityEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_insert);
        setupUIViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_item();
            }
        });

    }

    private void setupUIViews() {
        back = (Button) findViewById(R.id.buttonBack);
        create = (Button) findViewById(R.id.buttonCreate);
        nameEditText = findViewById(R.id.editTextName);
        priceEditText = findViewById(R.id.editTextPrice);
        quantityEditText = findViewById(R.id.editTextQuantity);
    }

    private void return_menu() {
        Intent refreshIntent = new Intent("refresh_list_action");
        sendBroadcast(refreshIntent);

        startActivity(new Intent(ManualInsertActivity.this, ViewListActivity.class));

    }

    private void create_item() {
        //String userEmail = "test@gmail.com";
        String userEmail = getIntent().getStringExtra("userEmail");

        // Now, you have the group ID, and you can proceed to insert the shopping item
        DatabaseAccessOperation fetchGroupIdOperation = new DatabaseAccessOperation(this, "insert_shopping_item", nameEditText, priceEditText, quantityEditText);
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
}


