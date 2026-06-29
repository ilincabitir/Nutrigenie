package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FridgeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail;
    private List<FoodItem> predefinedFoodItems;
    private Map<String, Integer> currentFridgeItemsWithQuantity;
    private List<FoodItem> displayList;
    private FridgeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("USER_EMAIL", "");

        if (userEmail.isEmpty()) {
            finish();
            return;
        }

        predefinedFoodItems = FoodRepository.getAllPredefinedItems();

        AutoCompleteTextView autoComplete = findViewById(R.id.foodAutoComplete);
        Button addBtn = findViewById(R.id.addItemBtn);
        ListView listView = findViewById(R.id.fridgeListView);

        List<String> allPredefinedNames = new ArrayList<>();
        for (FoodItem item : predefinedFoodItems) allPredefinedNames.add(item.getName());
        ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allPredefinedNames);
        autoComplete.setAdapter(autoAdapter);

        displayList = new ArrayList<>();
        adapter = new FridgeListAdapter(displayList, new FridgeListAdapter.OnQuantityChangeListener() {
            @Override
            public void onPlus(String itemName) {
                dbHelper.addFridgeItem(userEmail, itemName, "");
                refreshData();
            }

            @Override
            public void onMinus(String itemName) {
                dbHelper.removeFridgeItem(userEmail, itemName);
                refreshData();
            }

            @Override
            public void onDelete(String itemName) {
                dbHelper.removeFridgeItem(userEmail, itemName);
                refreshData();
            }
        });
        listView.setAdapter(adapter);

        refreshData();

        addBtn.setOnClickListener(v -> {
            String selection = autoComplete.getText().toString();
            if (allPredefinedNames.contains(selection)) {
                FoodItem fi = FoodRepository.findByName(selection);
                String category = (fi != null) ? fi.getCategory() : "";
                
                dbHelper.addFridgeItem(userEmail, selection, category);
                refreshData();
                autoComplete.setText("");
            } else {
                Toast.makeText(this, "Please select an item from the list", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());
    }

    private void refreshData() {
        currentFridgeItemsWithQuantity = dbHelper.getFridgeItemsWithQuantity(userEmail);
        displayList.clear();

        for (Map.Entry<String, Integer> entry : currentFridgeItemsWithQuantity.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            
            FoodItem foodDetails = FoodRepository.findByName(itemName);
            if (foodDetails != null) {
                // Use the new copy constructor in FoodItem to include quantity
                displayList.add(new FoodItem(foodDetails, quantity));
            }
        }
        
        adapter.notifyDataSetChanged();
    }
}
