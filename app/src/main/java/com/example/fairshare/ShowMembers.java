package com.example.fairshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowMembers extends AppCompatActivity {

    FloatingActionButton button;
    Button delete;
    TextView tv;
    EditText et;
    ListView lv;
    String groupName, userName;
    int  groupId;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.show_members);

        MyDatabase db1= new MyDatabase(this);
        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        groupId = i.getIntExtra("groupId",0);
        userName = i.getStringExtra("userName");
        String groupCreator = db1.getGroupCreator(groupId);

        button = findViewById(R.id.button);
        delete = findViewById(R.id.delete);
        tv = findViewById(R.id.tv);
        lv = findViewById(R.id.lv);
        tv.setText(""+groupName);

        if(groupCreator.equals(userName)){
            delete.setText("Delete");
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupCreator.equals(userName)){
                    db1.deleteGroup(groupId);
                    Intent i = new Intent(ShowMembers.this, ShowGroups.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();


                }
                else{
                    db1.deleteMemberFromGroup(userName,groupId);
                    Intent k = new Intent(ShowMembers.this, ShowGroups.class);
                    k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(k);
                    finish();



                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowMembers.this, AddMembers.class);
                i.putExtra("groupName", groupName);
                i.putExtra("groupId", groupId);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume(){

        super.onResume();
        updateMembers();
    }
    public void updateMembers(){
        ArrayList<String> members = new ArrayList<>();
        db= new MyDatabase(this);
        members = db.getMembersByGroup(groupId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,members);
        lv.setAdapter(adapter);

    }
}