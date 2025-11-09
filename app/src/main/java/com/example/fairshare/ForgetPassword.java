package com.example.fairshare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPassword extends AppCompatActivity {

    EditText et1,et2;
    Button btn;
    MyDatabase db = new MyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forget_password);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et1.getText().toString().trim();
                String password = et2.getText().toString().trim();
                if(db.updatePassword(name,password)){
                    Toast.makeText(ForgetPassword.this,"Password updated successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}