package com.example.butorwebaruhaz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail, pass1, pass2;
    Button registerButton;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.reg_email);
        pass1 = findViewById(R.id.reg_pass1);
        pass2 = findViewById(R.id.reg_pass2);

        registerButton = findViewById(R.id.reg_btn);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Regisztráció");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(v -> {

            String email = registerEmail.getText().toString().trim();
            String password = pass1.getText().toString().trim();
            String password2 = pass2.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                registerEmail.setError("Az email cím kötelező!");
            } else if (TextUtils.isEmpty(password)) {
                pass1.setError("A jelszó kötelező!");
            } else if (TextUtils.isEmpty(password2)) {
                pass2.setError("A jelszó kötelező!");
            } else if (password2.length() != password.length()) {
                Toast.makeText(RegisterActivity.this, "A két jelszó nem egyezik!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password) {
        progressDialog.setTitle("Kérem várjon!");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Hoppá, error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}