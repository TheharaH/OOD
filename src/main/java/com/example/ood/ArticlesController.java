package com.example.ood;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView; // ListView to display article titles
    @FXML
    private ComboBox<String> categoryComboBox; // ComboBox for selecting categories

    private final String API_KEY = "7b15371b5730491896a63377122237bb";
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&pageSize=100&apiKey=" + API_KEY;


    private List<Article> articles = new ArrayList<>();  // List to store full article details
    private String currentUsername; // Store the current user's username

    // Define categories and their associated keywords
    private final String[] categories = {"Health", "Artificial Intelligence", "Technology", "Education", "Sports"};
    private final String[] keywords = {
            "health, wellness, fitness, medicine",
            "technology, tech, gadgets, innovation",
            "AI, artificial intelligence, machine learning, deep learning",
            "education, learning, schools, universities",
            "sports, athletics, games, competitions"
    };

    @FXML
    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categories)); // Populate ComboBox
        categoryComboBox.setOnAction(event -> filterArticlesByCategory()); // Set event handler

        fetchArticles();

        // Set event listener to load full article content when an item is selected
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = articles.get(newVal.intValue());
                openArticleDetailView(selectedArticle);
            }
        });
    }

    // Set the current username for reading history
    /*public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }*/

    /*public void setCurrentUsername(String username) {
        this.currentUsername = username;
        System.out.println("Current username set to: " + username); // Print statement for confirmation
    }*/


    /*public void setCurrentUsername(String username) {
        User user = SessionManager.getCurrentUser();
        this.currentUsername = user != null ? user.getUsername() : null;
        System.out.println("ArticlesController: Current username set to: " + this.currentUsername);
    }*/

    /*public void setCurrentUsername(String username) {
        // Ensure currentUser is set in the session before trying to access it
        User user = SessionManager.getCurrentUser();

        // If user is not null, set currentUsername from the username passed
        if (user != null && username != null) {
            this.currentUsername = username;
            System.out.println("ArticlesController: Current username set to: " + this.currentUsername);
        } else {
            // If user is null, print an error message or handle accordingly
            this.currentUsername = null;
            System.out.println("ArticlesController: No user is logged in, cannot set username.");
        }
    }*/

    public void setCurrentUsername(String username) {
        // Fetch the current user from the session
        User user = SessionManager.getCurrentUser();

        if (user != null) {
            // Set the current username from the user object
            this.currentUsername = user.getUsername();
            System.out.println("ArticlesController: Current username set to: " + this.currentUsername);
        } else {
            // If no user is logged in, print an error message
            this.currentUsername = null;
            System.out.println("ArticlesController: No user is logged in, cannot set username.");
        }
    }



    // Fetch articles from the API
    private void fetchArticles() {
        new Thread(() -> {
            try {
                // Establish connection
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                // Read API response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse and update UI with article titles
                parseArticles(response.toString());

                // Update ListView with all articles by default
                updateArticleListView(articles);

            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch articles. Please try again later."));
                e.printStackTrace();
            }
        }).start();
    }

    // Parse JSON response and extract article titles and content
    private void parseArticles(String jsonResponse) {
        articles.clear(); // Clear previous articles
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray jsonArticles = jsonObject.getAsJsonArray("articles");

        for (int i = 0; i < jsonArticles.size(); i++) {
            JsonObject jsonArticle = jsonArticles.get(i).getAsJsonObject();
            String title = jsonArticle.has("title") && !jsonArticle.get("title").isJsonNull()
                    ? jsonArticle.get("title").getAsString()
                    : "Untitled";
            String content = jsonArticle.has("content") && !jsonArticle.get("content").isJsonNull()
                    ? jsonArticle.get("content").getAsString()
                    : "Content not available.";

            String category = categorizeArticle(title, content); // Categorize the article

            articles.add(new Article(title, content, category)); // Include category in Article object
        }
    }

    // Categorize the article based on keywords
    private String categorizeArticle(String title, String content) {
        for (int i = 0; i < categories.length; i++) {
            String[] categoryKeywords = keywords[i].split(", ");
            for (String keyword : categoryKeywords) {
                if (title.toLowerCase().contains(keyword.toLowerCase()) ||
                        content.toLowerCase().contains(keyword.toLowerCase())) {
                    return categories[i];
                }
            }
        }
        return "General"; // Default category
    }

    // Update ListView with articles
    private void updateArticleListView(List<Article> articlesToShow) {
        List<String> titles = new ArrayList<>();
        for (Article article : articlesToShow) {
            titles.add(article.getTitle());
        }
        articlesListView.setItems(FXCollections.observableArrayList(titles));
    }

    // Filter articles by selected category
    private void filterArticlesByCategory() {
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            List<Article> filteredArticles = new ArrayList<>();
            for (Article article : articles) {
                if (article.getCategory().equals(selectedCategory)) {
                    filteredArticles.add(article);
                }
            }
            updateArticleListView(filteredArticles);
        } else {
            updateArticleListView(articles); // Show all articles if no category is selected
        }
    }

    // Show article details in a new view
    private void openArticleDetailView(Article article) {
        try {
            // Save the article to the reading history
            ReadingHistory.addArticle(currentUsername, article); // Pass the current username to save to history

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetailView.fxml"));
            Parent root = loader.load();

            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);

            Stage stage = (Stage) articlesListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Save the article to the reading history with the current username
    /*private void openArticleDetailView(Article article) {
        try {
            // Save article to reading history
            ReadingHistory.addArticle(currentUsername, article);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetailView.fxml"));
            Parent root = loader.load();

            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);

            Stage stage = (Stage) articlesListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    // Display an alert in case of API failure
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) articlesListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


