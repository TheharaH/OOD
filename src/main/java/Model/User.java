package Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<String> preferences; // Preferences for recommendations
    private List<Article> readArticles; // Articles the user has read

    // Constructor
    public User(String username, String password,List<String> preferences) {
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




    // Method to view an article
    public void viewArticle(Article article) {
        System.out.println(username + " viewed article: ");
        System.out.println("Title: " + article.getTitle());


        // Optionally, you can add the article to the list of read articles
        readArticles.add(article);
    }

    public void likeArticle() {
        System.out.println(username + " liked an article ");
    }

    public void viewHistory() {
        System.out.println(username + " viewed history");
    }

    public void skipArticle() {
        System.out.println(username + " skipped an article ");
    }

    public void manageProfile() {
        System.out.println(username + " updated the profile ");
    }


}