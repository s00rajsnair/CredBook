
package com.example.credbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;


import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCustomerActivity extends AppCompatActivity {
    ListView contactList;
    ArrayList<HashMap<String, String>> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_customer);
        setTitle("Add Customer from Contacts");
        if (ActivityCompat.checkSelfPermission(AddCustomerActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            contacts = getAllContacts();
        } else {
            requestPermission();

        }
        contactList = findViewById(R.id.contact_list);
        ListAdapter adapter = new SimpleAdapter(AddCustomerActivity.this, contacts, R.layout.contact, new String[]{"name", "phno"}, new int[]{R.id.contact_name, R.id.contact_number});
        contactList.setAdapter(adapter);
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AddCustomerActivity.this,TransactionActivity.class);
                intent.putExtra(DatabaseHelper.ID, contacts.get(position).get(DatabaseHelper.ID));
                intent.putExtra(DatabaseHelper.NAME, contacts.get(position).get(DatabaseHelper.NAME));
                intent.putExtra(DatabaseHelper.PHONE_NUMBER, contacts.get(position).get(DatabaseHelper.PHONE_NUMBER));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 79: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contacts = getAllContacts();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public  ArrayList<HashMap<String, String>> getAllContacts() {

        ArrayList<HashMap<String, String>> contactList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                HashMap<String, String> contact = new HashMap<>();
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                contact.put(DatabaseHelper.ID, id);
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contact.put(DatabaseHelper.NAME, name);
                Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    String phoneNo = pCur.getString(pCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.put(DatabaseHelper.PHONE_NUMBER, phoneNo);
                }
                contactList.add(contact);
            }
        }
        if (cur != null) {
            cur.close();
        }
        return contactList;
    }


    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 79);
        }

    }
}

