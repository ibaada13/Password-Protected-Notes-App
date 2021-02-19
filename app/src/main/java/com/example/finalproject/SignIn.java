package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity {

    EditText text;
    Button btn;
    ListView list;
    ArrayList <String> arrayList;
    ArrayAdapter<String> adapter;

    DatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabaseHelper = new DatabaseHelper(this);

        text = (EditText) findViewById(R.id.list_item);
        btn = (Button) findViewById(R.id.button4);
        list = (ListView) findViewById(R.id.list_view);

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<>(SignIn.this, android.R.layout.simple_list_item_1, arrayList);

        list.setAdapter(adapter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = text.getText().toString();

                if (text.length() != 0) {
                    arrayList.add(note);
                    adapter.notifyDataSetChanged();
                    UserNote userNote = new UserNote(text.getText().toString());
                    boolean status = mDatabaseHelper.addNote(userNote);
                    if (status) {
                        toastMessage("Successfully Added Note");
                        text.setText("");
                    } else {
                        toastMessage("Unable to add Note");
                    }
                }
                else{
                    toastMessage("Enter note.");
                }
            }
        });

        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();

        int i = 1;
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList

            listData.add(data.getString(1));
            i++;
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        list.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String note = adapterView.getItemAtPosition(i).toString();
                Log.d("ListDataActivity", "onItemClick: You Clicked on " + note);

                Cursor data = mDatabaseHelper.getItemID(note); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }

                if(itemID > -1){
                    Log.d("ListDataActivity", "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent( SignIn.this, EditList.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",note);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });
        
    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}