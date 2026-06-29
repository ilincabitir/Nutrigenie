package com.example.project;

import java.util.ArrayList;
import java.util.List;

public class FoodRepository {
    private static List<FoodItem> cachedItems;

    public static List<FoodItem> getAllPredefinedItems() {
        if (cachedItems != null) return cachedItems;

        List<FoodItem> items = new ArrayList<>();
        // Veggies (#d0f4de)
        items.add(new FoodItem("Carrot", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Broccoli", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Spinach", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Tomato", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Cucumber", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Potato", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Onion", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Garlic", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Bell Pepper", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Zucchini", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Eggplant", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Cabbage", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Asparagus", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Cauliflower", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Mushroom", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Lettuce", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Corn", "Veggie", false, false, false, false, false, "#d0f4de"));
        items.add(new FoodItem("Peas", "Veggie", false, false, false, false, false, "#d0f4de"));

        // Fruits (#ff99c8)
        items.add(new FoodItem("Apple", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Banana", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Orange", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Grapes", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Strawberry", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Blueberry", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Mango", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Pineapple", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Watermelon", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Peach", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Kiwi", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Pear", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Cherry", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Raspberry", "Fruit", false, true, false, false, false, "#ff99c8"));
        items.add(new FoodItem("Avocado", "Fruit", false, false, true, false, false, "#ff99c8"));

        // Proteins (#e4c1f9)
        items.add(new FoodItem("Chicken Breast", "Protein", false, false, false, false, false, "#e4c1f9"));
        items.add(new FoodItem("Beef Steak", "Protein", false, false, true, false, false, "#e4c1f9"));
        items.add(new FoodItem("Salmon", "Protein", false, false, true, false, false, "#e4c1f9"));
        items.add(new FoodItem("Pork meat", "Protein", false, false, true, false, false, "#e4c1f9"));
        items.add(new FoodItem("Tuna", "Protein", true, false, false, false, false, "#e4c1f9"));
        items.add(new FoodItem("Eggs", "Protein", false, false, true, false, false, "#e4c1f9"));
        items.add(new FoodItem("Tofu", "Protein", false, false, false, false, false, "#e4c1f9"));
        items.add(new FoodItem("Turkey", "Protein", false, false, false, false, false, "#e4c1f9"));
        items.add(new FoodItem("Lamb", "Protein", false, false, true, false, false, "#e4c1f9"));
        items.add(new FoodItem("Shrimp", "Protein", true, false, false, false, false, "#e4c1f9"));

        // Carbs (#fcf6bd)
        items.add(new FoodItem("White Bread", "Carb", true, false, false, true, false, "#fcf6bd"));
        items.add(new FoodItem("Whole wheat bread", "Carb", false, false, false, true, false, "#fcf6bd"));
        items.add(new FoodItem("Rice", "Carb", false, false, false, false, false, "#fcf6bd"));
        items.add(new FoodItem("Pasta", "Carb", false, false, false, true, false, "#fcf6bd"));
        items.add(new FoodItem("Quinoa", "Carb", false, false, false, false, false, "#fcf6bd"));
        items.add(new FoodItem("Oats", "Carb", false, false, false, false, false, "#fcf6bd"));
        items.add(new FoodItem("Baguette", "Carb", true, false, false, true, false, "#fcf6bd"));
        items.add(new FoodItem("Bagel", "Carb", true, false, false, true, false, "#fcf6bd"));

        // Dairy (#a9def9)
        items.add(new FoodItem("Milk", "Dairy", false, true, true, false, true, "#a9def9"));
        items.add(new FoodItem("Cheddar Cheese", "Dairy", true, false, true, false, true, "#a9def9"));
        items.add(new FoodItem("Yoghurt", "Dairy", false, false, false, false, true, "#a9def9"));
        items.add(new FoodItem("Greek Yoghurt", "Dairy", false, false, false, false, true, "#a9def9"));
        items.add(new FoodItem("Cow cheese", "Dairy", true, false, true, false, true, "#a9def9"));
        items.add(new FoodItem("Mozzarella", "Dairy", true, false, true, false, true, "#a9def9"));
        items.add(new FoodItem("Parmesan", "Dairy", true, false, true, false, true, "#a9def9"));
        items.add(new FoodItem("Feta Cheese", "Dairy", true, false, true, false, true, "#a9def9"));
        items.add(new FoodItem("Butter", "Dairy", true, false, true, false, true, "#a9def9"));
        items.add(new FoodItem("Cottage Cheese", "Dairy", false, false, false, false, true, "#a9def9"));
        
        cachedItems = items;
        return items;
    }

    public static FoodItem findByName(String name) {
        for (FoodItem item : getAllPredefinedItems()) {
            if (item.getName().equalsIgnoreCase(name)) return item;
        }
        return null;
    }
}
