package com.tanush.scminventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.tanush.scminventory.loginActivities.AdminLoginActivity;
import com.tanush.scminventory.loginActivities.OverViewLoginActivity;

public class MainActivity extends AppCompatActivity {

    MaterialButton overviewBtn, AdminBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overviewBtn = findViewById(R.id.btnOverview);
        AdminBtn = findViewById(R.id.btnAdmin);


        overviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), OverViewLoginActivity.class);
                startActivity(i);
            }
        });

        AdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AdminLoginActivity.class);
                startActivity(i);
            }
        });
    }
}
