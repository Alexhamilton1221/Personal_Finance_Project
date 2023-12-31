package com.example.personal_finance_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private ArrayList<ShoppingItem> shoppingItems;

    // Constructor
    public ShoppingListAdapter(ArrayList<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }

    // Define ViewHolder and other required methods...
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind data to the item views
        ShoppingItem item = shoppingItems.get(position);

        String itemName = item.getName();
        String priceText = item.getPrice() > 0 ? "$"+String.valueOf(item.getPrice()) : "N/A";
        String quantityText = item.getQuantity() > 0 ? String.valueOf(item.getQuantity()) : "N/A";

        holder.textViewName.setText(itemName + "      Cost: " + priceText + "      Quantity: " + quantityText);
        // Bind other data as needed...
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views from the item layout
        public TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            textViewName = itemView.findViewById(R.id.textViewName);
            // Initialize other views...
        }
    }
    public void setItems(List<ShoppingItem> items) {
        this.shoppingItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }


}
