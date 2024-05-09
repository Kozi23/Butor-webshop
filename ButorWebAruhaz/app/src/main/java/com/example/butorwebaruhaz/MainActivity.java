package com.example.butorwebaruhaz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private Button signIn, signUp;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_pass);

        signIn = findViewById(R.id.log_btn);
        signUp = findViewById(R.id.signup_btn);

        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        signIn.setOnClickListener(v -> {
            String loginEmail = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (TextUtils.isEmpty(loginEmail)) {
                email.setError("Az email nem lehet üres!");
            } else if (TextUtils.isEmpty(pass)) {
                password.setError("A jelszó nem lehet üres!");
            } else {
                LoginUser(loginEmail, pass);
            }
        });
    }

    private void LoginUser(String email, String pass) {

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Sikeres bejelentkezés", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else {
                Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Hoppá, error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    }
}