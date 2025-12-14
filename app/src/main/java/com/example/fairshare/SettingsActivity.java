package com.example.fairshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    TextView tvChangePassword, tvEditProfile, tvAbout, tvDeleteAccount, tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Match the new XML name

        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvEditProfile    = findViewById(R.id.tvEditProfile);
        tvAbout          = findViewById(R.id.tvAbout);
        tvDeleteAccount  = findViewById(R.id.tvDeleteAccount);
        tvLogout         = findViewById(R.id.tvLogout);

        tvChangePassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgetPassword.class))
        );

        tvEditProfile.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class))
        );

        tvAbout.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)) // You can create AboutActivity later
        );

        tvDeleteAccount.setOnClickListener(v ->
                startActivity(new Intent(this, DeleteAccountActivity.class))
        );

        tvLogout.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit().clear().apply();
            startActivity(new Intent(this, LoginPage.class));
            finish();
        });
    }
}
