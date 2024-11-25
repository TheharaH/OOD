package com.example.ood;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<String> preferences; // Preferences for recommendations
    private List<Article> readArticles; // Articles the user has read

    // Constructor
    public User(String username, String password, List<String> preferences) {
        this.username = username;
        this.password = password;
        this.preferences = preferences;
        this.readArticles = new ArrayList<>(); // Initialize the read articles list
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
