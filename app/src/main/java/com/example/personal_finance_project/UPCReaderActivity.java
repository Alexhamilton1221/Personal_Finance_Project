package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Inside UPCReaderActivity.java
public class UPCReaderActivity extends AppCompatActivity {

    private Button back, search;
    public EditText enter_upc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcreader);
        setupUIViews();




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_item();
            }
        });
    }

    private void setupUIViews() {
        back = findViewById(R.id.buttonBack);
        search = findViewById(R.id.buttonSearch);
        enter_upc = findViewById(R.id.enterUPC);
    }

    private void return_menu() {
        startActivity(new Intent(UPCReaderActivity.this, MenuActivity.class));
    }

    private void create_item() {
        String upcCode = enter_upc.getText().toString().trim();
        ApiCallTask apiCallTask = new ApiCallTask(new ApiCallTask.ApiCallback() {
            @Override
            public void onApiCallComplete(String result) {
                // Handle the API call result here
                if (result != null) {
                    Log.d("InsertShoppingItem Using UPC", "Product Title: " + result);
                    display_item(result);

                } else {
                    // Handle the error or notify the user
                    Log.e("InsertShoppingItem Using UPC", "API call failed");
                    return_menu();
                }
            }
        });
        apiCallTask.execute(upcCode);
    }

    private void display_item(String title){
        String userEmail = getIntent().getStringExtra("userEmail");

        Intent intent = (new Intent(UPCReaderActivity.this, DisplayUPCItemActivity.class));
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("title", title);
        startActivity(intent);
    }

}



