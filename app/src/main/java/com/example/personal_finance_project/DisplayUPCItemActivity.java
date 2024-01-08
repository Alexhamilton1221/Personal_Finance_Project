package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayUPCItemActivity extends AppCompatActivity implements DatabaseAccessOperation.OnTaskCompleteListener {

    private Button add, cancel;
    public TextView display_item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_upcitem);

        setupUIViews();

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        String title = intent.getStringExtra("title");

        display_item.setText(title);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_item(userEmail,title);
            }
            }
        );
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });
    }
    private void setupUIViews() {
        add = findViewById(R.id.buttonAdd);
        cancel = findViewById(R.id.buttonCancel);
        display_item = findViewById(R.id.textViewDisplayItem);
    }

    private void return_menu() {
        startActivity(new Intent(DisplayUPCItemActivity.this, MenuActivity.class));
    }

    private void add_item(String userEmail, String item_name) {

        // Now, you have the group ID, and you can proceed to insert the shopping item
        DatabaseAccessOperation fetchGroupIdOperation = new DatabaseAccessOperation(this, "UPC_insert_shopping_item", null, null,null);
        fetchGroupIdOperation.setOnTaskCompleteListener(this);
        fetchGroupIdOperation.execute(userEmail,item_name);
        return_menu();



    }

    @Override
    public void onTaskComplete(int result) {

    }
}
