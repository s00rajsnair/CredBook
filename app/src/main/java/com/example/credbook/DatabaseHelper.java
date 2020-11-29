package com.example.credbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.RequiresApi;

import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "CredBook.db";
    public static String CUSTOMER_TABLE = "customer";
    public static String TRANSACTION_TABLE = "cutomer_transaction";
    public static String NAME = "name";
    public static String ID = "id";
    public static String PHONE_NUMBER = "phno";
    public static String DEBIT = "debit";
    public static String CREDIT = "credit";
    public static String TRANSACTION_DATE = "transactiondate";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String customerTableCreateQuery = "create table " + CUSTOMER_TABLE + " ( " + ID + " integer primary key , " + NAME + " text, " + PHONE_NUMBER + " text)";
        String transactionTableCreateQuery = "create table " + TRANSACTION_TABLE + " ( " + ID + " integer primary key , " + DEBIT + " real, " + CREDIT + " real, " + TRANSACTION_DATE + " text, foreign key(" + ID + ") references " + CUSTOMER_TABLE + "(" + ID + "))";
        System.out.println(customerTableCreateQuery);
        System.out.println(transactionTableCreateQuery);
        db.execSQL(customerTableCreateQuery);
        db.execSQL(transactionTableCreateQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CUSTOMER_TABLE);
        db.execSQL("drop table if exists " + TRANSACTION_TABLE);
        onCreate(db);
    }

    public long insertCustomerData(String name, String phno, int id) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PHONE_NUMBER, phno);
        contentValues.put(ID, id);
        return myDB.insert(CUSTOMER_TABLE, null, contentValues);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long insertTransactionData(int id, double amount, boolean transactionIsCredit) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy ");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = dtf.format(now);
        contentValues.put(ID, id);
        if (transactionIsCredit)
            contentValues.put(CREDIT, amount);
        else
            contentValues.put(DEBIT, amount);
        contentValues.put(TRANSACTION_DATE, currentDate);
        return myDB.insert(TRANSACTION_TABLE, null, contentValues);
    }

    public ArrayList<HashMap<String, String>> getAllTransactionsData() {
        ArrayList<HashMap<String, String>> transactions = new ArrayList<>();
        try {
            SQLiteDatabase myDb = this.getWritableDatabase();
            Cursor csr = myDb.rawQuery("select * from " + TRANSACTION_TABLE, null);
            while (csr.moveToNext()) {
                HashMap<String, String> transaction = new HashMap<>();
                transaction.put(ID, csr.getString(csr.getColumnIndex(ID)));
                transaction.put(DEBIT, csr.getString(csr.getColumnIndex(DEBIT)));
                transaction.put(CREDIT, csr.getString(csr.getColumnIndex(CREDIT)));
                transaction.put(TRANSACTION_DATE, csr.getString(csr.getColumnIndex(TRANSACTION_DATE)));
                transactions.add(transaction);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return transactions;
    }

    public ArrayList<HashMap<String, String>> getTransactedUsers(){
        SQLiteDatabase myDb = this.getWritableDatabase();
        ArrayList<HashMap<String,String>> transactions = getAllTransactionsData();
        ArrayList<HashMap<String,String>> transactedUserDetails = new ArrayList<>();
        ArrayList<String> transactedUserIds = new ArrayList<>();
        for (HashMap<String,String> transaction: transactions) {
            transactedUserIds.add(transaction.get("id"));
        }
        for (String id: transactedUserIds) {
            Cursor csr = myDb.rawQuery("select * from " + CUSTOMER_TABLE , new String[]{id});
            HashMap<String, String> transactedUser = new HashMap<>();
            transactedUser.put(ID, csr.getString(csr.getColumnIndex(ID)));
            transactedUser.put(NAME, csr.getString(csr.getColumnIndex(NAME)));
            transactedUser.put(PHONE_NUMBER, csr.getString(csr.getColumnIndex(PHONE_NUMBER)));
            transactedUserDetails.add(transactedUser);
            System.out.println(transactedUser);
        }
        return  transactedUserDetails;
    }
    public int deleteContact(int contactID) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        return myDb.delete(CUSTOMER_TABLE, ID + "= ?", new String[]{String.valueOf(contactID)});
    }

    public HashMap<String, String> getContactById(int id) {
        HashMap<String, String> contact = new HashMap<>();
        try {
            SQLiteDatabase myDb = this.getWritableDatabase();
            String query = "select * from " + TRANSACTION_TABLE;
            System.out.println(query);
            Cursor csr = myDb.rawQuery(query, new String[]{String.valueOf(id)});
            System.out.println(csr.getCount());
            contact.put(ID, csr.getString(csr.getColumnIndex(ID)));
            System.out.println(csr.getString(csr.getColumnIndex(ID)));
            contact.put(DEBIT, csr.getString(csr.getColumnIndex(DEBIT)));
            contact.put(CREDIT, csr.getString(csr.getColumnIndex(CREDIT)));
            contact.put(TRANSACTION_DATE, csr.getString(csr.getColumnIndex(TRANSACTION_DATE)));
        } catch (Exception e) {
            System.out.println(e);
        }
        return contact;
    }

    public int updateContactDetails(int id, String name, String phno, String email) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PHONE_NUMBER, phno);
        return myDb.update(CUSTOMER_TABLE, contentValues, ID + " = ?", new String[]{String.valueOf(id)});

    }

    public void deleteContacts() {
        SQLiteDatabase myDb = this.getWritableDatabase();
        myDb.delete(CUSTOMER_TABLE, null, null);
    }

}
