package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    SharedPreferences userCreated;
    Intent goToLoginOrHomeDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userCreated = getSharedPreferences("user",MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userCreated.contains("username"))
                    goToLoginOrHomeDashboard = new Intent(MainActivity.this, DashboardActivity.class);
                else
                    goToLoginOrHomeDashboard = new Intent(MainActivity.this,CreateUserActivity.class);
                startActivity(goToLoginOrHomeDashboard);
                finish();
            }
        }, 2000);
    }
}
