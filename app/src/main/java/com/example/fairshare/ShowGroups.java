package com.example.fairshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ShowGroups extends AppCompatActivity {

    FloatingActionButton button;
    TextView tv;
    EditText et;
    ListView lv;
    String name;
    GroupAdapter ga;

    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.show_groups);

        Intent i = getIntent();
        name = i.getStringExtra("username");

        button = findViewById(R.id.button);
        tv = findViewById(R.id.tv);
        lv = findViewById(R.id.lv);
        tv.setText(""+name);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowGroups.this,AddGroups.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        updateGroup();
    }

    public void updateGroup(){

        db= new MyDatabase(this);

        ArrayList<String> groups = db.getGroupsForUser(name);
        ga = new GroupAdapter((Context) this, groups);
        lv.setAdapter(ga);

    }

}