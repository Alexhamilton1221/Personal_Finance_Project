package com.example.personal_finance_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private ArrayList<ShoppingItem> shoppingItems;

    private Button back,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        setupUIViews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return_menu();
            }
        });

        // Create and set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize the data (shoppingItems) and the adapter
        shoppingItems = new ArrayList<>();
        adapter = new ShoppingListAdapter(shoppingItems);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Retrieve user's email from Intent
        String userEmail = getIntent().getStringExtra("userEmail");

        DatabaseAccessRecyclerView databaseAccessRecyclerView = new DatabaseAccessRecyclerView(this, recyclerView, adapter, "group_tab");
        databaseAccessRecyclerView.execute(userEmail);




        // Add items to the shoppingItems list as needed
        // shoppingItems.add(new ShoppingItem(...));
    }
    private void setupUIViews(){
        recyclerView = findViewById(R.id.recyclerViewItems);
        back = (Button) findViewById(R.id.buttonBack);
        add = (Button) findViewById(R.id.buttonAdd);

    }

    private void return_menu(){
        startActivity(new Intent(ViewListActivity.this, MenuActivity.class));
    }

}