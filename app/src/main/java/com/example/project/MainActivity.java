package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tipText;
    private String[] healthTips = {
        "Drink at least 8 glasses of water a day to stay hydrated.",
        "Include more fiber in your diet with fruits and vegetables.",
        "Don't skip breakfast; it kickstarts your metabolism.",
        "Limit sugary drinks and opt for water or herbal tea.",
        "Eat slowly and mindfully to help with digestion.",
        "Ensure you get 7-9 hours of quality sleep each night.",
        "Reduce salt intake to maintain healthy blood pressure."
    };

    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("USER_EMAIL", "");
        String name = dbHelper.getUserName(userEmail);

        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText(name.isEmpty() ? "Welcome!" : "Welcome, " + name + "!");

        tipText = findViewById(R.id.tipText);
        displayRandomTip();

        // Navbar buttons
        ImageButton accountBtn = findViewById(R.id.accountBtn);
        accountBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        ImageButton fridgeBtn = findViewById(R.id.fridgeBtn);
        fridgeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FridgeActivity.class);
            startActivity(intent);
        });

        MaterialButton mealBtn = findViewById(R.id.mealBtn);
        mealBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MealSuggestionActivity.class);
            startActivity(intent);
        });
    }

    private void displayRandomTip() {
        Random random = new Random();
        int index = random.nextInt(healthTips.length);
        tipText.setText(healthTips[index]);
    }
}
