package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {
    SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        userPref = getSharedPreferences("user",MODE_PRIVATE);
        setTitle("CredBook > " + userPref.getString("username",null));
    }

    public void goToAddCustomerActivity(View view){
        System.out.println("Clicked");
        Intent goToAddCustomerActivity = new Intent(DashboardActivity.this,AddCustomerActivity.class);
        startActivity(goToAddCustomerActivity);
    }
}