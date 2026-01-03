package com.example.pizzasteve.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzasteve.R;
import com.example.pizzasteve.adapters.CartAdapter;
import com.example.pizzasteve.firebase.CartHelper;
import com.example.pizzasteve.firebase.OrderHelper;
import com.example.pizzasteve.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {
    private RecyclerView rvCartItems;
    private TextView tvEmptyCart, tvTotal;
    private Button btnPlaceOrder;
    private View layoutTotal;
    private CartAdapter adapter;
    private CartHelper cartHelper;
    private OrderHelper orderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        rvCartItems = findViewById(R.id.rvCartItems);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        layoutTotal = findViewById(R.id.layoutTotal);

        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, new ArrayList<>(),
                new CartAdapter.CartUpdateListener() {
                    @Override
                    public void onQuantityChanged() {
                        loadCart();
                    }

                    @Override
                    public void onItemRemoved() {
                        loadCart();
                    }
                });
        rvCartItems.setAdapter(adapter);

        // Setup navigation
        setupBottomNavigation();
        setupActionBar();
        setSelectedNavItem(R.id.nav_cart);

        // Initialize helpers
        cartHelper = new CartHelper();
        orderHelper = new OrderHelper();

        // Load cart
        loadCart();

        // Place order button
        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void loadCart() {
        cartHelper.getCartItems(new CartHelper.CartItemsCallback() {
            @Override
            public void onSuccess(List<CartItem> items) {
                if (items.isEmpty()) {
                    showEmptyCart();
                } else {
                    showCart(items);
                    calculateTotal();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(CartActivity.this,
                        "Error loading cart: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyCart() {
        rvCartItems.setVisibility(View.GONE);
        layoutTotal.setVisibility(View.GONE);
        tvEmptyCart.setVisibility(View.VISIBLE);
    }

    private void showCart(List<CartItem> items) {
        rvCartItems.setVisibility(View.VISIBLE);
        layoutTotal.setVisibility(View.VISIBLE);
        tvEmptyCart.setVisibility(View.GONE);
        adapter.updateItems(items);
    }

    private void calculateTotal() {
        cartHelper.calculateTotal(new CartHelper.TotalCallback() {
            @Override
            public void onTotal(double total) {
                tvTotal.setText(String.format("$%.2f", total));
            }
        });
    }

    private void placeOrder() {
        btnPlaceOrder.setEnabled(false);

        orderHelper.placeOrder(new OrderHelper.OrderCallback() {
            @Override
            public void onSuccess(String message, String orderId) {
                Toast.makeText(CartActivity.this,
                        "Order placed successfully!",
                        Toast.LENGTH_LONG).show();
                loadCart(); // Refresh cart (should be empty now)
                btnPlaceOrder.setEnabled(true);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(CartActivity.this,
                        "Failed to place order: " + error,
                        Toast.LENGTH_LONG).show();
                btnPlaceOrder.setEnabled(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }
}