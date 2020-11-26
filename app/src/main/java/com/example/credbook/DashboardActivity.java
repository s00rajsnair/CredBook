package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity {
    SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        userPref = getSharedPreferences("user",MODE_PRIVATE);
        setTitle("CredBook > " + userPref.getString("username",null));

    }
}