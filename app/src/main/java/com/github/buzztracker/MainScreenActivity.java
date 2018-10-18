package com.github.buzztracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        final Button button = findViewById(R.id.button_logOut_main);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(MainScreenActivity.this, Login.class);
                MainScreenActivity.this.startActivity(myIntent);


            }
        });

        final Button locButton = findViewById(R.id.location);
        locButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = new Intent(MainScreenActivity.this, LocationListActivity.class);
                MainScreenActivity.this.startActivity(myIntent);
            }
        });
    }

}
