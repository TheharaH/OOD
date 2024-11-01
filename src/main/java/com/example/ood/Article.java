package com.example.ood;

public class Article {
    private String title;        // Title of the article
    private String content;      // Content of the article
    private String category;     // Category of the article

    // Constructor
    public Article(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    // Override equals() to compare articles based on title, content, and category if needed
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Article article = (Article) obj;
        return title.equals(article.title);
    }

    // Override hashCode() based on title (or all fields if preferred)
    @Override
    public int hashCode() {
        return title.hashCode();
    }

    // Optional: A toString() method to help with debugging or displaying in a ListView
    @Override
    public String toString() {
        return title + " (" + category + ")";
    }
}
