package com.example.pizzasteve.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzasteve.R;
import com.example.pizzasteve.firebase.CartHelper;
import com.example.pizzasteve.firebase.FavoritesHelper;
import com.example.pizzasteve.models.CartItem;
import com.example.pizzasteve.models.MenuItem;

public class ItemDetailsActivity extends AppCompatActivity {
    private ImageView ivItemImage;
    private TextView tvItemName, tvItemPrice, tvItemDescription, tvQuantity;
    private Button btnIncrease, btnDecrease, btnAddToCart;
    private ImageButton btnFavorite;

    private MenuItem item;
    private int quantity = 1;
    private boolean isFavorite = false;

    private CartHelper cartHelper;
    private FavoritesHelper favoritesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Initialize views
        ivItemImage = findViewById(R.id.ivItemImage);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemPrice = findViewById(R.id.tvItemPrice);
        tvItemDescription = findViewById(R.id.tvItemDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnFavorite = findViewById(R.id.btnFavorite);

        // Initialize helpers
        cartHelper = new CartHelper();
        favoritesHelper = new FavoritesHelper();

        // Get item data from intent
        getItemData();

        // Setup buttons
        setupButtons();

        // Check if item is favorite
        checkIfFavorite();
    }

    private void getItemData() {
        // Get data from intent
        String itemId = getIntent().getStringExtra("itemId");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);
        String category = getIntent().getStringExtra("category");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Create MenuItem object
        item = new MenuItem(itemId, name, description, price, category, imageUrl);

        // Display data
        tvItemName.setText(item.getName());
        tvItemPrice.setText(String.format("$%.2f", item.getPrice()));
        tvItemDescription.setText(item.getDescription());
        tvQuantity.setText(String.valueOf(quantity));
    }

    private void setupButtons() {
        // Increase quantity
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Decrease quantity
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Add to cart
        btnAddToCart.setOnClickListener(v -> addToCart());

        // Toggle favorite
        btnFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void checkIfFavorite() {
        favoritesHelper.isFavorite(item.getItemId(), new FavoritesHelper.FavoriteCheckCallback() {
            @Override
            public void onResult(boolean favorite) {
                isFavorite = favorite;
                updateFavoriteButton();
            }
        });
    }

    private void updateFavoriteButton() {
        if (isFavorite) {
            btnFavorite.setImageResource(android.R.drawable.star_big_on);
        } else {
            btnFavorite.setImageResource(android.R.drawable.star_big_off);
        }
    }

    private void toggleFavorite() {
        if (isFavorite) {
            // Remove from favorites
            favoritesHelper.removeFromFavorites(item.getItemId(),
                    new FavoritesHelper.OperationCallback() {
                        @Override
                        public void onSuccess(String message) {
                            isFavorite = false;
                            updateFavoriteButton();
                            Toast.makeText(ItemDetailsActivity.this,
                                    "Removed from favorites", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(ItemDetailsActivity.this,
                                    "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Add to favorites
            favoritesHelper.addToFavorites(item.getItemId(),
                    new FavoritesHelper.OperationCallback() {
                        @Override
                        public void onSuccess(String message) {
                            isFavorite = true;
                            updateFavoriteButton();
                            Toast.makeText(ItemDetailsActivity.this,
                                    "Added to favorites", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(ItemDetailsActivity.this,
                                    "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addToCart() {
        CartItem cartItem = new CartItem(
                item.getItemId(),
                item.getName(),
                item.getPrice(),
                quantity,
                item.getImageUrl()
        );

        btnAddToCart.setEnabled(false);

        cartHelper.addToCart(cartItem, new CartHelper.OperationCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ItemDetailsActivity.this,
                        "Added to cart!", Toast.LENGTH_SHORT).show();
                btnAddToCart.setEnabled(true);
                finish(); // Return to previous page
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ItemDetailsActivity.this,
                        "Error: " + error, Toast.LENGTH_SHORT).show();
                btnAddToCart.setEnabled(true);
            }
        });
    }
}
