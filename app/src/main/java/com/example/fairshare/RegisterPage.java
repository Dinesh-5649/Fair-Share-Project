package com.example.fairshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterPage extends AppCompatActivity {

    Button btn;
    EditText etName, etNum, etPass;
    MyDatabase db = new MyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_page);

        etName = findViewById(R.id.etName);
        etNum = findViewById(R.id.etNum);
        etPass = findViewById(R.id.etPass);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = etName.getText().toString().trim();
                final String number = etNum.getText().toString().trim();
                final String password = etPass.getText().toString().trim();
                if(db.registerUser(name,number,password)){
                  finish();
                }
            }
        });
    }
}