package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class SignupActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView loginRedirect = findViewById(R.id.loginTextRedirect);
        loginRedirect.setOnClickListener(v -> {
            finish();
        });
        dbHelper = new DatabaseHelper(this);
        EditText nameInput = findViewById(R.id.name);
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);
        Button signupBtn = findViewById(R.id.signupButton);

        signupBtn.setOnClickListener(v-> {
           String name = nameInput.getText().toString();
           String email = emailInput.getText().toString();
           String password = passwordInput.getText().toString();
           if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
               Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
           } else if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            }else if(!password.matches("^(?=.*[A-Z])(?=.*\\d).{7,}$") ){
               Toast.makeText(this, "Password must be at least 7 characters long and contain at least one uppercase letter and one number", Toast.LENGTH_SHORT).show();
           }else {
               boolean success = dbHelper.insertUser(name, email, password);
               if (success) {
                   Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                   startActivity(intent);
               } else {
                   Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
               }
           }


        });




    }
}