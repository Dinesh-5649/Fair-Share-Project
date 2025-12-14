package com.example.fairshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteAccountActivity extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnDelete;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        db = new MyDatabase(this);

        etUser  = findViewById(R.id.etDeleteUser);
        etPass  = findViewById(R.id.etDeletePass);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(v -> deleteAccount());
    }

    private void deleteAccount() {
        String user = etUser.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean deleted = db.deleteAccount(user, pass);

        if (deleted) {
            Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show();

            // Clear shared preferences
            getSharedPreferences("login", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            startActivity(new Intent(this, LoginPage.class));
            finishAffinity();
        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
