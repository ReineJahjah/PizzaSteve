package com.example.pizzasteve.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.pizzasteve.models.User;

public class AuthHelper {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // Sign up new user
    public void signUp(String email, String password, String name, String phone,
                       AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Create user document in Firestore
                            User user = new User(firebaseUser.getUid(), name, email, phone);
                            db.collection("users").document(firebaseUser.getUid())
                                    .set(user)
                                    .addOnSuccessListener(aVoid ->
                                            callback.onSuccess("User registered successfully"))
                                    .addOnFailureListener(e ->
                                            callback.onFailure(e.getMessage()));
                        }
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    // Sign in existing user
    public void signIn(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Login successful");
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    // Sign out
    public void signOut() {
        mAuth.signOut();
    }

    // Get current user
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    // Check if user is logged in
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    // Callback interface
    public interface AuthCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }
}