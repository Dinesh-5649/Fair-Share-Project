package com.example.fairshare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    EditText etUsername, etPhone, etEmail, etAge;
    Button btnSave;

    MyDatabase db;
    SharedPreferences prefs;

    String oldUsername;   // logged-in username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Init DB & SharedPreferences
        db = new MyDatabase(this);
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Get logged-in username
        oldUsername = prefs.getString("username", null);

        if (oldUsername == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Bind views
        etUsername = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        btnSave = findViewById(R.id.btnSave);

        // -------------------------------
        // AUTO-POPULATE EXISTING DATA
        // -------------------------------
        etUsername.setText(oldUsername);
        etPhone.setText(db.getPhoneNumber(oldUsername));
        etEmail.setText(db.getEmailByUsername(oldUsername));
        etAge.setText(String.valueOf(db.getAgeByUsername(oldUsername)));

        // -------------------------------
        // SAVE UPDATED PROFILE
        // -------------------------------
        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {

        String newUsername = etUsername.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        // Validation
        if (newUsername.isEmpty()) {
            etUsername.setError("Username required");
            return;
        }

        if (phone.length() != 10) {
            etPhone.setError("Enter valid 10-digit phone");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            return;
        }

        if (ageStr.isEmpty()) {
            etAge.setError("Age required");
            return;
        }

        int age = Integer.parseInt(ageStr);

        // Update DB
        boolean success = db.updateUser(
                oldUsername,
                newUsername,
                phone,
                email,
                age
        );

        if (success) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

            // Update SharedPreferences if username changed
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", newUsername);
            editor.apply();

            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
