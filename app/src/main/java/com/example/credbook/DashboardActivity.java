package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {
    SharedPreferences userPref;
    ListView transactionsLv;
    ArrayList<HashMap<String, String>> transactionContacts;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        myDb = new DatabaseHelper(this);
        transactionContacts = myDb.getTransactedUsers();
        System.out.println(transactionContacts);
        userPref = getSharedPreferences("user",MODE_PRIVATE);
        setTitle("CredBook > " + userPref.getString("username",null));
        transactionsLv = findViewById(R.id.transactions_lv);
        ListAdapter adapter = new SimpleAdapter(this, transactionContacts, R.layout.transacted_customer, new String[]{DatabaseHelper.NAME,DatabaseHelper.TRANSACTION_DATE,DatabaseHelper.PHONE_NUMBER}, new int[]{R.id.customer_name,R.id.transaction_date,R.id.customer_phone});
        transactionsLv.setAdapter(adapter);
    }

    public void goToAddCustomerActivity(View view){
        System.out.println("Clicked");
        Intent goToAddCustomerActivity = new Intent(DashboardActivity.this,AddCustomerActivity.class);
        startActivity(goToAddCustomerActivity);
    }
}