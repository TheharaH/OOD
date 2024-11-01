package com.example.ood;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView;  // ListView to display article titles

    @FXML
    private Label articleContentLabel;  // Label to display full article content

    private final String API_KEY = "7b15371b5730491896a63377122237bb"; // Replace with your actual API key
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    private List<Article> articles = new ArrayList<>();  // List to store full article details

    @FXML
    public void initialize() {
        fetchArticles();

        // Set an event listener to load full article content when an item is selected
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = articles.get(newVal.intValue());
                articleContentLabel.setText(selectedArticle.getContent());
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

        // Extract titles, content, and category
        for (int i = 0; i < jsonArticles.size(); i++) {
            JsonObject jsonArticle = jsonArticles.get(i).getAsJsonObject();

            // Check for null values in each field and provide a default value if necessary
            String title = jsonArticle.has("title") && !jsonArticle.get("title").isJsonNull()
                    ? jsonArticle.get("title").getAsString()
                    : "Untitled";

            String content = jsonArticle.has("content") && !jsonArticle.get("content").isJsonNull()
                    ? jsonArticle.get("content").getAsString()
                    : "Content not available.";

            String category = jsonArticle.has("category") && !jsonArticle.get("category").isJsonNull()
                    ? jsonArticle.get("category").getAsString()
                    : "General";

            // Add to titles list and articles list
            titles.add(title);
            articles.add(new Article(title, content, category));
        }
        return titles;
    }

    // Display an alert in case of API failure
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Back button to return to the main view
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
