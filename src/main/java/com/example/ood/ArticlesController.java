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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView; // ListView to display article titles

    private final String API_KEY = "7b15371b5730491896a63377122237bb";
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    private List<Article> articles = new ArrayList<>();  // List to store full article details

    // Define categories and their associated keywords
    private final String[] categories = {"Technology", "AI", "Health", "Education", "Sports"};
    private final String[] keywords = {
            "technology, tech, gadgets, innovation",
            "AI, artificial intelligence, machine learning, deep learning",
            "health, wellness, fitness, medicine",
            "education, learning, schools, universities",
            "sports, athletics, games, competitions"
    };

    @FXML
    public void initialize() {
        fetchArticles();

        // Set event listener to load full article content when an item is selected
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = articles.get(newVal.intValue());
                openArticleDetailView(selectedArticle);
            }
        });
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
                List<String> articleTitles = parseArticles(response.toString());

                // Update ListView on the JavaFX Application Thread
                Platform.runLater(() -> articlesListView.setItems(FXCollections.observableArrayList(articleTitles)));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch articles. Please try again later."));
                e.printStackTrace();
            }
        }).start();
    }

    // Parse JSON response and extract article titles and content
    private List<String> parseArticles(String jsonResponse) {
        List<String> titles = new ArrayList<>();
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

            titles.add(title);
            articles.add(new Article(title, content, category)); // Include category in Article object
        }
        return titles;
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

    // Show article details in a new view
    private void openArticleDetailView(Article article) {
        try {
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