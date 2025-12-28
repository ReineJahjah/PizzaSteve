package com.example.pizzasteve.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzasteve.R;
import com.example.pizzasteve.models.MenuItem;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private Context context;
    private List<MenuItem> items;

    public MenuAdapter(Context context, List<MenuItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText("$" + item.getPrice());
        holder.tvDescription.setText(item.getDescription());

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Update items
    public void updateItems(List<MenuItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // ViewHolder
    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvDescription;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
            tvDescription = itemView.findViewById(R.id.tvItemDescription);
        }
    }
}