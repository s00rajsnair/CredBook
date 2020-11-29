package com.example.credbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TransactionActivity extends AppCompatActivity {
    Switch creditDebitSwitch;
    boolean transactionIsCredit = true;
    TextView displayMessage;
    Button enterButton;
    EditText amount;
    DatabaseHelper dbHelper = new DatabaseHelper(TransactionActivity.this);
    String customerName;
    String customerId ;
    String customerPhno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        customerName = getIntent().getStringExtra(DatabaseHelper.NAME);
        customerId = getIntent().getStringExtra(DatabaseHelper.ID);
        customerPhno = getIntent().getStringExtra(DatabaseHelper.PHONE_NUMBER);
        setTitle("Transaction with " + customerName);
        creditDebitSwitch = findViewById(R.id.creditdebit_switch);
        displayMessage = findViewById(R.id.display_message);
        enterButton = findViewById(R.id.enter_btn);
        amount = findViewById(R.id.amount);
        if(transactionIsCredit){
            displayMessage.setText("You will get");
            displayMessage.setTextColor(getResources().getColor(R.color.creditColor));
        }
    }

    public void changeTransactionType(View view){
        transactionIsCredit = !transactionIsCredit;
        if(transactionIsCredit){
            displayMessage.setText("You will get");
            displayMessage.setTextColor(getResources().getColor(R.color.creditColor));

        }
        else{
            displayMessage.setText("You will give");
            displayMessage.setTextColor(getResources().getColor(R.color.debitColor));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveTransactionDetails (View view){
        dbHelper.insertCustomerData(customerName,customerPhno, Integer.parseInt(customerId));
        dbHelper.insertTransactionData(Integer.parseInt(customerId),Double.parseDouble(amount.getText().toString()),transactionIsCredit);
        goToDashboard();
    }

    private void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}