package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MealSuggestionActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail;
    private LinearLayout suggestionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_suggestion);

        dbHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("USER_EMAIL", "");

        suggestionsContainer = findViewById(R.id.suggestionsContainer);
        findViewById(R.id.declineAllBtn).setOnClickListener(v -> finish());

        generateSuggestions();
    }

    private void generateSuggestions() {
        Map<String, Integer> fridgeItemsMap = dbHelper.getFridgeItemsWithQuantity(userEmail);
        String complications = dbHelper.getComplications(userEmail);

        List<FoodItem> safeFridgeItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fridgeItemsMap.entrySet()) {
            FoodItem item = FoodRepository.findByName(entry.getKey());
            if (item != null && isSafe(item, complications)) {
                // Add the item as many times as it's in the fridge to the pool
                for (int i = 0; i < entry.getValue(); i++) {
                    safeFridgeItems.add(item);
                }
            }
        }

        if (safeFridgeItems.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No safe food items found in your fridge.");
            emptyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            suggestionsContainer.addView(emptyText);
            return;
        }

        List<List<FoodItem>> suggestions = new ArrayList<>();
        Set<String> seenSuggestions = new HashSet<>();
        
        // Try to generate up to 3 different unique suggestions
        // We iterate more than 3 times to account for skipped duplicates
        for (int i = 0; i < 30 && suggestions.size() < 3; i++) {
            List<FoodItem> suggestion = createOneSuggestion(new ArrayList<>(safeFridgeItems));
            if (!suggestion.isEmpty()) {
                // Create a "fingerprint" of the meal to check for uniqueness
                String fingerPrint = suggestion.stream()
                        .map(FoodItem::getName)
                        .sorted()
                        .collect(Collectors.joining("|"));
                
                if (!seenSuggestions.contains(fingerPrint)) {
                    suggestions.add(suggestion);
                    seenSuggestions.add(fingerPrint);
                    // Remove used items from pool for the next suggestion
                    for (FoodItem used : suggestion) {
                        safeFridgeItems.remove(used);
                    }
                }
            } else {
                // If we couldn't make a structured meal and have no suggestions yet, fallback to random item
                if (suggestions.isEmpty() && !safeFridgeItems.isEmpty()) {
                    Collections.shuffle(safeFridgeItems);
                    suggestions.add(Collections.singletonList(safeFridgeItems.get(0)));
                }
                break;
            }
        }

        for (List<FoodItem> suggestion : suggestions) {
            addSuggestionView(suggestion);
        }
    }

    private List<FoodItem> createOneSuggestion(List<FoodItem> pool) {
        List<FoodItem> veggies = pool.stream().filter(i -> i.getCategory().equals("Veggie")).collect(Collectors.toList());
        List<FoodItem> proteins = pool.stream().filter(i -> i.getCategory().equals("Protein")).collect(Collectors.toList());
        List<FoodItem> carbs = pool.stream().filter(i -> i.getCategory().equals("Carb")).collect(Collectors.toList());
        List<FoodItem> fruits = pool.stream().filter(i -> i.getCategory().equals("Fruit")).collect(Collectors.toList());

        // Shuffle within categories to provide variety in suggestions
        Collections.shuffle(veggies);
        Collections.shuffle(proteins);
        Collections.shuffle(carbs);
        Collections.shuffle(fruits);

        List<FoodItem> result = new ArrayList<>();

        if (veggies.size() >= 1 && proteins.size() >= 1) {
            result.add(veggies.get(0));
            if (veggies.size() >= 2) {
                // Try to find a different vegetable for the second slot if available
                FoodItem v1 = veggies.get(0);
                FoodItem v2 = veggies.get(1);
                for (int i = 1; i < veggies.size(); i++) {
                    if (!veggies.get(i).getName().equals(v1.getName())) {
                        v2 = veggies.get(i);
                        break;
                    }
                }
                result.add(v2);
            }
            result.add(proteins.get(0));
            if (!carbs.isEmpty()) result.add(carbs.get(0));
        } else if (!fruits.isEmpty()) {
            result.add(fruits.get(0));
        } else if (!veggies.isEmpty()) {
            result.add(veggies.get(0));
        }

        return result;
    }

    private void addSuggestionView(List<FoodItem> items) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_suggestion, suggestionsContainer, false);
        
        TextView title = view.findViewById(R.id.suggestionTitle);
        TextView itemsText = view.findViewById(R.id.suggestionItems);
        MaterialButton acceptBtn = view.findViewById(R.id.acceptBtn);

        String names = items.stream().map(FoodItem::getName).collect(Collectors.joining(", "));
        itemsText.setText(names);

        if (items.size() >= 3) {
            title.setText("Balanced Meal");
        } else if (items.size() == 1 && items.get(0).getCategory().equals("Fruit")) {
            title.setText("Healthy Snack");
        } else {
            title.setText("Quick Bite");
        }

        acceptBtn.setOnClickListener(v -> {
            for (FoodItem item : items) {
                dbHelper.removeFridgeItem(userEmail, item.getName());
            }
            Toast.makeText(this, "Enjoy your meal! Fridge updated.", Toast.LENGTH_SHORT).show();
            finish();
        });

        suggestionsContainer.addView(view);
    }

    private boolean isSafe(FoodItem item, String complications) {
        if (complications == null || complications.isEmpty()) return true;
        if (complications.contains("High Blood Pressure") && item.isHighSalt()) return false;
        if (complications.contains("Diabetes") && item.isHighSugar()) return false;
        if (complications.contains("High Cholesterol") && item.isHighFat()) return false;
        if (complications.contains("Gluten Intolerance") && item.isHasGluten()) return false;
        if (complications.contains("Lactose Intolerance") && item.isHasLactose()) return false;
        if (complications.contains("Obesity") && (item.isHighFat() || item.isHighSugar())) return false;
        return true;
    }
}
