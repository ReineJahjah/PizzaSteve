package com.example.pizzasteve.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzasteve.R;
import com.example.pizzasteve.firebase.AuthHelper;

public class SignUpActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etPassword;
    private Button btnSignUp;
    private TextView tvLogin;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        // Initialize AuthHelper
        authHelper = new AuthHelper();

        // Sign up button click
        btnSignUp.setOnClickListener(v -> signUpUser());

        // Login text click
        tvLogin.setOnClickListener(v -> {
            finish(); // Go back to login
        });
    }

    private void signUpUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic validation
        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            return;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Disable button
        btnSignUp.setEnabled(false);

        // Sign up
        authHelper.signUp(email, password, name, phone, new AuthHelper.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                finish(); // Go back to login
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(SignUpActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                btnSignUp.setEnabled(true);
            }
        });
    }
}