package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE Table users(email TEXT PRIMARY KEY, password TEXT, name TEXT, complications TEXT)");
        db.execSQL("CREATE Table fridge_items(id INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT, food_name TEXT, category TEXT, quantity INTEGER DEFAULT 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE users ADD COLUMN complications TEXT");
        }
        if (oldVersion < 4) {
            db.execSQL("CREATE Table fridge_items(id INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT, food_name TEXT, category TEXT)");
        }
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE fridge_items ADD COLUMN quantity INTEGER DEFAULT 1");
        }
    }

    public String hashPassword(String password){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b: hash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length()==1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }catch(NoSuchAlgorithmException e){
            return null;
        }
    }

    public boolean insertUser(String name, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", hashPassword(password));
        values.put("complications", "");

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, hashedPassword});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return "";
    }

    public boolean updateComplications(String email, String complications) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("complications", complications);
        int result = db.update("users", values, "email = ?", new String[]{email});
        return result > 0;
    }

    public String getComplications(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT complications FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String complications = cursor.getString(0);
            cursor.close();
            return complications;
        }
        cursor.close();
        return "";
    }

    public boolean addFridgeItem(String email, String foodName, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Check if item already exists
        Cursor cursor = db.rawQuery("SELECT quantity FROM fridge_items WHERE user_email = ? AND food_name = ?", new String[]{email, foodName});
        
        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("quantity", currentQuantity + 1);
            db.update("fridge_items", values, "user_email = ? AND food_name = ?", new String[]{email, foodName});
            cursor.close();
            return true;
        } else {
            ContentValues values = new ContentValues();
            values.put("user_email", email);
            values.put("food_name", foodName);
            values.put("category", category);
            values.put("quantity", 1);
            long result = db.insert("fridge_items", null, values);
            cursor.close();
            return result != -1;
        }
    }

    public Map<String, Integer> getFridgeItemsWithQuantity(String email) {
        Map<String, Integer> items = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT food_name, quantity FROM fridge_items WHERE user_email = ? ORDER BY id ASC", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                items.put(cursor.getString(0), cursor.getInt(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void removeFridgeItem(String email, String foodName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM fridge_items WHERE user_email = ? AND food_name = ?", new String[]{email, foodName});
        
        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            if (currentQuantity > 1) {
                ContentValues values = new ContentValues();
                values.put("quantity", currentQuantity - 1);
                db.update("fridge_items", values, "user_email = ? AND food_name = ?", new String[]{email, foodName});
            } else {
                db.delete("fridge_items", "user_email = ? AND food_name = ?", new String[]{email, foodName});
            }
        }
        cursor.close();
    }
}
