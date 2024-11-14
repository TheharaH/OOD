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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView; // ListView to display article titles
    @FXML
    private ComboBox<String> categoryComboBox; // ComboBox for selecting categories

    private final String API_KEY = "7b15371b5730491896a63377122237bb";
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&pageSize=10&apiKey=" + API_KEY;
    private final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\articles.csv";

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

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @FXML
    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categories)); // Populate ComboBox
        categoryComboBox.setOnAction(event -> filterArticlesByCategory()); // Set event handler

        // Schedule article fetching every 6 hours
        scheduler.scheduleAtFixedRate(this::fetchArticles, 0, 6, TimeUnit.HOURS);

        // Set event listener to load full article content when an item is selected
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = articles.get(newVal.intValue());
                openArticleDetailView(selectedArticle);
                saveReadingHistory(selectedArticle); // Save reading history when article is clicked
            }
        });
    }

    private String categorizeArticle(String title, String content) {
        String textToCheck = (title + " " + content).toLowerCase();

        for (int i = 0; i < categories.length; i++) {
            String[] keywordArray = keywords[i].split(", ");
            for (String keyword : keywordArray) {
                if (textToCheck.contains(keyword)) {
                    return categories[i];
                }
            }
        }
        return "Uncategorized";
    }

    public void filterArticlesByCategory() {
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(selectedCategory)) {
                    filterArticlesByKeywords(keywords[i]);
                    break;
                }
            }
        }
    }

    private void filterArticlesByKeywords(String keywords) {
        List<Article> filteredArticles = new ArrayList<>();

        for (Article article : articles) {
            String title = article.getTitle().toLowerCase();
            String description = article.getContent().toLowerCase();
            String[] keywordArray = keywords.split(", ");

            for (String keyword : keywordArray) {
                if (title.contains(keyword) || description.contains(keyword)) {
                    filteredArticles.add(article);
                    break;
                }
            }
        }
        updateArticlesListView(filteredArticles);
    }

    private void updateArticlesListView(List<Article> filteredArticles) {
        List<String> articleTitles = new ArrayList<>();

        for (Article article : filteredArticles) {
            articleTitles.add(article.getTitle());
        }

        articlesListView.setItems(FXCollections.observableArrayList(articleTitles));
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    private void fetchArticles() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                parseAndSaveArticles(response.toString());
                loadArticlesFromCSV();

            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch articles. Please try again later."));
                e.printStackTrace();
            }
        }).start();
    }

    private void parseAndSaveArticles(String jsonResponse) {
        articles.clear();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray jsonArticles = jsonObject.getAsJsonArray("articles");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (int i = 0; i < jsonArticles.size(); i++) {
                JsonObject jsonArticle = jsonArticles.get(i).getAsJsonObject();
                String title = jsonArticle.has("title") && !jsonArticle.get("title").isJsonNull()
                        ? jsonArticle.get("title").getAsString()
                        : "Untitled";
                String content = jsonArticle.has("content") && !jsonArticle.get("content").isJsonNull()
                        ? jsonArticle.get("content").getAsString()
                        : "Content not available.";

                String category = categorizeArticle(title, content);

                writer.write(String.join(",", title, content, category));
                writer.newLine();

                articles.add(new Article(title, content, category));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArticlesFromCSV() {
        articles.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0];
                    String content = parts[1];
                    String category = parts[2];

                    articles.add(new Article(title, content, category));
                }
            }

            Platform.runLater(() -> {
                List<String> articleTitles = new ArrayList<>();
                for (Article article : articles) {
                    articleTitles.add(article.getTitle());
                }
                articlesListView.setItems(FXCollections.observableArrayList(articleTitles));
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveReadingHistory(Article article) {
        if (currentUsername != null) {
            ReadingHistory.addArticle(currentUsername, article);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    private void openArticleDetailView(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetailView.fxml"));
            Parent root = loader.load();

            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) articlesListView.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
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

