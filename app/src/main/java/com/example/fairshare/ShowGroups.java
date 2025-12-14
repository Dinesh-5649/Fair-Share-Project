package com.example.fairshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    ArrayList<String> groups;

    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.show_groups);

        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvHello = findViewById(R.id.tvHello);
        tvHello.setText("Hello " + name);

        Intent i = getIntent();
        name = i.getStringExtra("username");

        if (name == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        button = findViewById(R.id.button);
        tv = findViewById(R.id.tvHello);
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupName = groups.get(position);
                int groupId = db.getGroupIdByGroupName(groupName);
                Intent i = new Intent(ShowGroups.this,GroupChat.class);
                i.putExtra("groupId",groupId);
                i.putExtra("groupName",groupName);
                i.putExtra("userName",name);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateGroup();
    }

    public void updateGroup() {

        if (name == null || name.trim().isEmpty()) {
            Toast.makeText(this, "Invalid session. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = new MyDatabase(this);
        groups = db.getGroupsForUser(name);

        if (groups == null) {
            groups = new ArrayList<>();
        }

        ga = new GroupAdapter(this, groups);
        lv.setAdapter(ga);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

