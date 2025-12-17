package com.example.fairshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginPage extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnRegister, btnForgot;
    SupabaseRepository db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        etUsername = findViewById(R.id.et1);
        etPassword = findViewById(R.id.et2);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgot = findViewById(R.id.btnForgotPassword);
        db = new SupabaseRepository(this);



        btnRegister.setOnClickListener(v -> {

            Intent intent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intent);
            Toast.makeText(LoginPage.this, "Register", Toast.LENGTH_SHORT).show();
        });

        btnLogin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty()) {
                etUsername.setError("Enter username");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                return;
            }

            db.loginUser(username, password, new Callbacks.LoginCallback() {
                @Override
                public void onSuccess(UserModel user) {

                    Toast.makeText(LoginPage.this,
                            "Login successful",
                            Toast.LENGTH_SHORT).show();

                    // Save username
                    SharedPreferences prefs =
                            getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putString("username", username)
                            .apply();

                    Intent intent = new Intent(LoginPage.this, ShowGroups.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(LoginPage.this,
                            error,
                            Toast.LENGTH_SHORT).show();
                }
            });

        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(LoginPage.this,ForgetPassword.class);
                startActivity(k);
            }
        });
    }
}
