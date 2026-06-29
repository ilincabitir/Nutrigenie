package com.example.project;

import android.graphics.Color;

public class FoodItem {
    private String name;
    private String category;
    private boolean highSalt;
    private boolean highSugar;
    private boolean highFat;
    private boolean hasGluten;
    private boolean hasLactose;
    private String hexColor;
    private int quantity = 1;

    public FoodItem(String name, String category, boolean highSalt, boolean highSugar, boolean highFat, boolean hasGluten, boolean hasLactose, String hexColor) {
        this.name = name;
        this.category = category;
        this.highSalt = highSalt;
        this.highSugar = highSugar;
        this.highFat = highFat;
        this.hasGluten = hasGluten;
        this.hasLactose = hasLactose;
        this.hexColor = hexColor;
    }

    // Constructor for creating "instances" with quantity
    public FoodItem(FoodItem other, int quantity) {
        this(other.name, other.category, other.highSalt, other.highSugar, other.highFat, other.hasGluten, other.hasLactose, other.hexColor);
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isHighSalt() { return highSalt; }
    public boolean isHighSugar() { return highSugar; }
    public boolean isHighFat() { return highFat; }
    public boolean isHasGluten() { return hasGluten; }
    public boolean isHasLactose() { return hasLactose; }
    public String getHexColor() { return hexColor; }
    public int getColor() { return Color.parseColor(hexColor); }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}