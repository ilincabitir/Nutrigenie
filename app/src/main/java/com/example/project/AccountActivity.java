package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail;
    private CheckBox cbHighBloodPressure, cbDiabetes, cbCholesterol, cbGluten, cbLactose, cbObesity;
    private TextView complicationsListText;
    private LinearLayout editComplicationsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("USER_EMAIL", "");

        if (userEmail.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        TextView tvName = findViewById(R.id.userName);
        TextView tvEmail = findViewById(R.id.userEmail);
        complicationsListText = findViewById(R.id.complicationsListText);
        editComplicationsLayout = findViewById(R.id.editComplicationsLayout);

        tvName.setText(dbHelper.getUserName(userEmail));
        tvEmail.setText(userEmail);

        cbHighBloodPressure = findViewById(R.id.cbHighBloodPressure);
        cbDiabetes = findViewById(R.id.cbDiabetes);
        cbCholesterol = findViewById(R.id.cbCholesterol);
        cbGluten = findViewById(R.id.cbGluten);
        cbLactose = findViewById(R.id.cbLactose);
        cbObesity = findViewById(R.id.cbObesity);

        updateDisplayList();
        loadPreferences();

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        findViewById(R.id.editComplicationsBtn).setOnClickListener(v -> {
            if (editComplicationsLayout.getVisibility() == View.GONE) {
                editComplicationsLayout.setVisibility(View.VISIBLE);
                complicationsListText.setVisibility(View.GONE);
            } else {
                editComplicationsLayout.setVisibility(View.GONE);
                complicationsListText.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.saveBtn).setOnClickListener(v -> savePreferences());

        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void savePreferences() {
        StringBuilder sb = new StringBuilder();
        if (cbHighBloodPressure.isChecked()) sb.append("High Blood Pressure, ");
        if (cbDiabetes.isChecked()) sb.append("Diabetes, ");
        if (cbCholesterol.isChecked()) sb.append("High Cholesterol, ");
        if (cbGluten.isChecked()) sb.append("Gluten Intolerance, ");
        if (cbLactose.isChecked()) sb.append("Lactose Intolerance, ");
        if (cbObesity.isChecked()) sb.append("Obesity, ");

        String result = sb.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }

        if (dbHelper.updateComplications(userEmail, result)) {
            Toast.makeText(this, "Preferences Saved!", Toast.LENGTH_SHORT).show();
            updateDisplayList();
            editComplicationsLayout.setVisibility(View.GONE);
            complicationsListText.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplayList() {
        String complications = dbHelper.getComplications(userEmail);
        if (complications == null || complications.isEmpty()) {
            complicationsListText.setText("No complications added.");
        } else {
            complicationsListText.setText(complications);
        }
    }

    private void loadPreferences() {
        String complications = dbHelper.getComplications(userEmail);
        if (complications != null) {
            cbHighBloodPressure.setChecked(complications.contains("High Blood Pressure"));
            cbDiabetes.setChecked(complications.contains("Diabetes"));
            cbCholesterol.setChecked(complications.contains("High Cholesterol"));
            cbGluten.setChecked(complications.contains("Gluten Intolerance"));
            cbLactose.setChecked(complications.contains("Lactose Intolerance"));
            cbObesity.setChecked(complications.contains("Obesity"));
        }
    }
}