package com.example.fairshare;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddMembers extends AppCompatActivity {

    TextView tv;
    ListView lv;
    String groupName;
    String userName;
    int groupId;
    MyDatabase db = new MyDatabase(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_members);

        lv = findViewById(R.id.lv);
        Intent j = getIntent();
        groupName = j.getStringExtra("groupName");
        groupId = j.getIntExtra("groupId",0);
        ArrayList<String> userNames = db.getAllUserNames();



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.show_users_layout,
                R.id.text1,
                userNames);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                userName = userNames.get(position);
                String phoneNumber = db.getPhoneNumber(userName);
                String member = userNames.get(position);

                if(db.addMembers(member,groupId, phoneNumber)){
                    Toast.makeText(AddMembers.this,"User added to the group successfully",Toast.LENGTH_SHORT).show();
                }
                else  Toast.makeText(AddMembers.this,"Failed to add the user",Toast.LENGTH_SHORT).show();

            }
        });
    }
}