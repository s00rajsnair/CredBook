package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {
    SharedPreferences userPref;
    ListView transactionsLv;
    ArrayList<HashMap<String, String>> transactionContacts;
    DatabaseHelper myDb;
    double creditSum;
    double debitSum;
    TextView creditSumView;
    TextView debitSumView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        creditSumView = findViewById(R.id.credit);
        debitSumView = findViewById(R.id.debit);
        myDb = new DatabaseHelper(this);
        transactionContacts = myDb.getTransactedCustomers();
        System.out.println(transactionContacts);
        creditSum = myDb.getSumOf(DatabaseHelper.CREDIT);
        debitSum = myDb.getSumOf(DatabaseHelper.DEBIT);
        creditSumView.setText("₹ " + creditSum);
        debitSumView.setText("₹ " + debitSum);
        userPref = getSharedPreferences("user",MODE_PRIVATE);
        setTitle("CredBook > " + userPref.getString("username",null));
        transactionsLv = findViewById(R.id.transactions_lv);
        ListAdapter adapter = new SimpleAdapter(this, transactionContacts, R.layout.transacted_customer, new String[]{DatabaseHelper.NAME,DatabaseHelper.PHONE_NUMBER,"AMOUNT","STATUS"}, new int[]{R.id.customer_name,R.id.customer_phone,R.id.pending_amount,R.id.status});
        transactionsLv.setAdapter(adapter);
        transactionsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),CustomerTransactionActivity.class);
                intent.putExtra(DatabaseHelper.ID, transactionContacts.get(position).get(DatabaseHelper.ID));
                startActivity(intent);
            }
        });
    }

    public void goToAddCustomerActivity(View view){
        System.out.println("Clicked");
        Intent goToAddCustomerActivity = new Intent(DashboardActivity.this,AddCustomerActivity.class);
        startActivity(goToAddCustomerActivity);
    }
}