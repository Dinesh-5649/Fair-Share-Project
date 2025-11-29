package com.example.fairshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterPage extends AppCompatActivity {

    Button btn;
    EditText etName, etNum, etPass, etConfirmPass, etEmail, etAge;
    RadioButton rbMale, rbFemale;

    // Hold form values after validation
    String finalName, finalNumber, finalPassword, finalGender, finalEmail;
    int finalAge;

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
        etAge = findViewById(R.id.etAge);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btn = findViewById(R.id.btn);

        applyRealtimeValidation();

        btn.setOnClickListener(v -> registerUser());
    }

    // -------------------------------
    // REAL-TIME VALIDATION
    // -------------------------------
    private void applyRealtimeValidation() {

        // Validate email when user leaves the field
        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = etEmail.getText().toString().trim();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter a valid email");
                }
            }
        });

        // Validate phone when user leaves the field
        etNum.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String num = etNum.getText().toString().trim();
                if (num.length() != 10) {
                    etNum.setError("Phone number must be 10 digits");
                }
            }
        });

        // Validate password match in real-time
        etConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String pass = etPass.getText().toString().trim();
                String confirm = etConfirmPass.getText().toString().trim();

                if (!confirm.equals(pass)) {
                    etConfirmPass.setError("Passwords do not match");
                }
            }
        });
    }


    // -------------------------------
    // VALIDATE → THEN RUN CAPTCHA
    // -------------------------------
    private void registerUser() {

        String name = etName.getText().toString().trim();
        String number = etNum.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String confirmPass = etConfirmPass.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        // Basic validations
        if (name.isEmpty()) {
            etName.setError("Please enter your name");
            return;
        }
        if (number.isEmpty() || number.length() != 10) {
            etNum.setError("Phone number must be 10 digits");
            return;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }
        if (password.isEmpty()) {
            etPass.setError("Enter password");
            return;
        }
        if (!password.equals(confirmPass)) {
            etConfirmPass.setError("Passwords do not match");
            return;
        }
        if (ageStr.isEmpty()) {
            etAge.setError("Enter your age");
            return;
        }

        int age = Integer.parseInt(ageStr);

        // Gender
        String gender;
        if (rbMale.isChecked()) {
            gender = "Male";
        } else if (rbFemale.isChecked()) {
            gender = "Female";
        } else {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save validated values
        finalName = name;
        finalNumber = number;
        finalPassword = password;
        finalEmail = email;
        finalGender = gender;
        finalAge = age;

        // Now run CAPTCHA
        Intent intent = new Intent(RegisterPage.this, CaptchaActivity.class);
        startActivityForResult(intent, 1001);
    }


    // -------------------------------
    // CAPTCHA → REGISTER
    // -------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == RESULT_OK) {

            String token = data.getStringExtra("captcha_token");

            if (token != null && !token.isEmpty()) {

                if (db.registerUser(finalName, finalNumber, finalPassword, finalAge, finalGender, finalEmail)) {
                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Captcha failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
