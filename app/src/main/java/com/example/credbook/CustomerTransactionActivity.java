package com.example.credbook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class CustomerTransactionActivity extends AppCompatActivity {
    TextView creditorDebit;
    TextView creditorDebitMessage;
    DatabaseHelper myDb;
    EditText transactionAmount;
    boolean transactionIsCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_transaction);
        creditorDebit = findViewById(R.id.credit_or_debit);
        creditorDebitMessage = findViewById(R.id.credit_or_debit_status);
        transactionAmount = findViewById(R.id.transaction_amount);
        myDb = new DatabaseHelper(this);
        updateCustomerTransactionActivity();
        HashMap<String, String> selectedCustomer = myDb.getTransactedCustomer(getIntent().getStringExtra(DatabaseHelper.ID));
        System.out.println(selectedCustomer);
        setTitle("Transaction with " + selectedCustomer.get(DatabaseHelper.NAME));
        creditorDebit.setText("₹ " + selectedCustomer.get("AMOUNT"));
        creditorDebitMessage.setText(selectedCustomer.get("STATUS"));
    }

    public void getFunction(View view) {
        transactionIsCredit = true;
        if (transactionAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
        } else {
            myDb.updateTransactionDetails(getIntent().getStringExtra(DatabaseHelper.ID), transactionIsCredit, Double.parseDouble(transactionAmount.getText().toString()));
            updateCustomerTransactionActivity();


        }
    }

    public void giveFunction(View view) {
        transactionIsCredit = false;
        if (transactionAmount.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
        } else {
            myDb.updateTransactionDetails(getIntent().getStringExtra(DatabaseHelper.ID), transactionIsCredit, Double.parseDouble(transactionAmount.getText().toString()));
            updateCustomerTransactionActivity();

        }
    }

    void updateCustomerTransactionActivity() {
        HashMap<String, String> selectedCustomer = myDb.getTransactedCustomer(getIntent().getStringExtra(DatabaseHelper.ID));
        System.out.println(selectedCustomer);
        creditorDebit.setText("₹ " + selectedCustomer.get("AMOUNT"));
        creditorDebitMessage.setText(selectedCustomer.get("STATUS"));
        if (selectedCustomer.get("STATE").equals("CREDIT")) {
            creditorDebit.setTextColor(getResources().getColor(R.color.creditColor));
        } else if (selectedCustomer.get("STATE").equals("DEBIT")) {
            creditorDebit.setTextColor(getResources().getColor(R.color.debitColor));
        }else{
            creditorDebit.setTextColor(getResources().getColor(R.color.settledColor));
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardActivity.class));
    }

}