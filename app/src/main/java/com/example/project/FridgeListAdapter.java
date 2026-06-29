package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class FridgeListAdapter extends BaseAdapter {

    private List<FoodItem> items;
    private OnQuantityChangeListener listener;

    public interface OnQuantityChangeListener {
        void onPlus(String itemName);
        void onMinus(String itemName);
        void onDelete(String itemName);
    }

    public FridgeListAdapter(List<FoodItem> items, OnQuantityChangeListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fridge_item, parent, false);

        FoodItem item = items.get(position);

        MaterialCardView card = view.findViewById(R.id.fridgeItemCard);
        TextView name = view.findViewById(R.id.foodItemName);
        TextView category = view.findViewById(R.id.foodItemCategory);
        TextView quantityText = view.findViewById(R.id.quantityText);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton plusBtn = view.findViewById(R.id.plusBtn);
        ImageButton minusBtn = view.findViewById(R.id.minusBtn);

        TextView badgeSalt = view.findViewById(R.id.badgeSalt);
        TextView badgeSugar = view.findViewById(R.id.badgeSugar);
        TextView badgeFat = view.findViewById(R.id.badgeFat);
        TextView badgeGluten = view.findViewById(R.id.badgeGluten);
        TextView badgeLactose = view.findViewById(R.id.badgeLactose);

        name.setText(item.getName());
        category.setText(item.getCategory());
        quantityText.setText(String.valueOf(item.getQuantity()));
        card.setCardBackgroundColor(item.getColor());

        badgeSalt.setVisibility(item.isHighSalt() ? View.VISIBLE : View.GONE);
        badgeSugar.setVisibility(item.isHighSugar() ? View.VISIBLE : View.GONE);
        badgeFat.setVisibility(item.isHighFat() ? View.VISIBLE : View.GONE);
        badgeGluten.setVisibility(item.isHasGluten() ? View.VISIBLE : View.GONE);
        badgeLactose.setVisibility(item.isHasLactose() ? View.VISIBLE : View.GONE);

        plusBtn.setOnClickListener(v -> listener.onPlus(item.getName()));
        minusBtn.setOnClickListener(v -> listener.onMinus(item.getName()));
        deleteBtn.setOnClickListener(v -> listener.onDelete(item.getName()));

        return view;
    }
}
