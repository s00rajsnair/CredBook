package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserActivity extends AppCompatActivity {
    SharedPreferences userPreference;
    SharedPreferences.Editor userPrefEditor;
    EditText enteredUserName;
    Intent goToDashboardIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        userPreference = getSharedPreferences("user",MODE_PRIVATE);
        enteredUserName = findViewById(R.id.username);
        goToDashboardIntent = new Intent(CreateUserActivity.this,DashboardActivity.class);
    }

    public void createUser(View view){
        if(enteredUserName.getText().equals("")){
            Toast.makeText(this,"Please enter a name to create account",Toast.LENGTH_SHORT).show();
        }else{
            userPrefEditor = userPreference.edit();
            userPrefEditor.putString("username", enteredUserName.getText().toString());
            userPrefEditor.apply();
            startActivity(goToDashboardIntent);
        }
    }


}