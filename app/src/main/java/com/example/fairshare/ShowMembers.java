package com.example.fairshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
    TextView tv;
    EditText et;
    ListView lv;
    String groupName;
    int  groupId;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.show_members);

        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        groupId = i.getIntExtra("groupId",0);

        button = findViewById(R.id.button);
        tv = findViewById(R.id.tv);
        lv = findViewById(R.id.lv);
        tv.setText(""+groupName);

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