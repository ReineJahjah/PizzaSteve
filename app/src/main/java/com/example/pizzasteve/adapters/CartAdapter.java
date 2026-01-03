package com.example.pizzasteve.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzasteve.R;
import com.example.pizzasteve.firebase.CartHelper;
import com.example.pizzasteve.models.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> items;
    private CartHelper cartHelper;
    private CartUpdateListener listener;

    public interface CartUpdateListener {
        void onQuantityChanged();
        void onItemRemoved();
    }

    public CartAdapter(Context context, List<CartItem> items, CartUpdateListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.cartHelper = new CartHelper();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText("$" + String.format("%.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvSubtotal.setText("$" + String.format("%.2f", item.getTotalPrice()));

        // Increase quantity
        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            updateCartItem(item);
        });

        // Decrease quantity
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                updateCartItem(item);
            }
        });

        // Remove item
        holder.btnRemove.setOnClickListener(v -> {
            cartHelper.removeFromCart(item.getItemId(), new CartHelper.OperationCallback() {
                @Override
                public void onSuccess(String message) {
                    listener.onItemRemoved();
                }

                @Override
                public void onFailure(String error) {
                    // Handle error
                }
            });
        });
    }

    private void updateCartItem(CartItem item) {
        cartHelper.updateCartItem(item, new CartHelper.OperationCallback() {
            @Override
            public void onSuccess(String message) {
                listener.onQuantityChanged();
            }

            @Override
            public void onFailure(String error) {
                // Handle error
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<CartItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity, tvSubtotal;
        Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartItemName);
            tvPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}