package com.example.personal_finance_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class UserMenu extends AppCompatActivity {

    private Button manage;


    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://shopunityhub-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        // Set IDs for widgets
        setupUIViews();

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testdbms();
            }
        });


    }

    private void testdbms(){

        DatabaseReference playersRef = FirebaseDatabase.getInstance().getReference("product/");

        playersRef.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (dataSnapshot.exists()) {
                    String playerName = dataSnapshot.child("name").getValue(String.class);
                    showToast(playerName);
                } else {
                    showToast("DataSnapshot does not exist");
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            private void showToast(String message) {
                Toast.makeText(UserMenu.this, message, Toast.LENGTH_SHORT).show();
            }


        });

    }


    private void setupUIViews(){
        manage = (Button) findViewById(R.id.buttonManage);
    }



}

