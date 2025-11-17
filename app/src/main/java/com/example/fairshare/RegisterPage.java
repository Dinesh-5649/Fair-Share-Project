package com.example.fairshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterPage extends AppCompatActivity {

    Button btn;
    EditText etName, etNum, etPass, etConfirmPass, etEmail, etAge;
    RadioButton rbMale, rbFemale;
    String gender;
    MyDatabase db = new MyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_page);

        etName = findViewById(R.id.etName);
        etNum = findViewById(R.id.etNum);
        etPass = findViewById(R.id.etPass);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        etEmail = findViewById(R.id.etEmail);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        etAge = findViewById(R.id.etAge);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = etName.getText().toString().trim();
                final String number = etNum.getText().toString().trim();
                final String password = etPass.getText().toString().trim();
                final String confirmPass = etConfirmPass.getText().toString().trim();
                final String ageStr = etAge.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();

                if (ageStr.isEmpty()) {
                    Toast.makeText(RegisterPage.this, "Please enter age", Toast.LENGTH_SHORT).show();
                    return;
                }
                int age = Integer.parseInt(ageStr);

                if(rbMale.isChecked()){
                    gender = rbMale.getText().toString();
                }else gender= rbFemale.getText().toString();

                if(!password.equals(confirmPass)){
                    Toast.makeText(RegisterPage.this,"Password doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(email.isEmpty()){
                    Toast.makeText(RegisterPage.this,"Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(db.registerUser(name,number,password,age,gender,email)){
                  finish();
                }
            }
        });
    }
}