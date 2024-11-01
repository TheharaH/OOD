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

    public List<String> getPreferences() {
        return preferences;
    }

    public List<Article> getReadArticles() {
        return readArticles; // Getter for read articles
    }

    // Method to update user preferences
    public void updatePreferences(List<String> newPreferences) {
        this.preferences = newPreferences;
    }

    // Method to add an article to the read history
    public void addReadArticle(Article article) {
        if (article != null && !readArticles.contains(article)) {
            this.readArticles.add(article);
        }
    }

    // Method to remove an article from the read history
    public void removeReadArticle(Article article) {
        this.readArticles.remove(article);
    }

    // Method to check if a user has read a specific article
    public boolean hasReadArticle(Article article) {
        return readArticles.contains(article);
    }
}
