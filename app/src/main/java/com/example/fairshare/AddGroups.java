package com.example.fairshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddGroups extends AppCompatActivity {
    EditText group_name;
    TextView tv;

    Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_groups);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("name");

        group_name = findViewById(R.id.et1);
        bt1 = findViewById(R.id.bt1);
        tv = findViewById(R.id.tv);
        tv.setText(userName);



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabase my = new MyDatabase(AddGroups.this);
                final String name = group_name.getText().toString().trim();
                if(my.addGroup(name,userName)){
                    finish();
                }
            }
        });



    }
}