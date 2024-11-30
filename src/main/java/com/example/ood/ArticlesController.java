
package com.example.ood;

import Model.Article;
import Service.ReadingHistory;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView; // ListView to display article titles
    @FXML
    private ComboBox<String> categoryComboBox;
    private String currentUsername; // ComboBox for selecting categories

    private final String API_KEY = "72f3769687af4decb26ba81130a9af2a";
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&pageSize=10&apiKey=" + API_KEY;
    private final String CSV_FILE_PATH = "articles.csv";

    private List<Article> articles = new ArrayList<>();  // List to store full article details
    private Set<String> existingArticleTitles = new HashSet<>(); // Set to store titles of articles already in CSV

    // Define categories and their associated keywords
    private final String[] categories = {"Health", "Artificial Intelligence", "Technology", "Education", "Sports"};
    private final String[] keywords = {
            "health|wellness|fitness|medicine",
            "technology|tech|gadgets|innovation",
            "artificial intelligence|machine learning|deep learning",
            "education|learning|schools|universities",
            "sports|athletics|games|competitions"
    };

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @FXML
    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categories)); // Populate ComboBox
        categoryComboBox.setOnAction(event -> filterArticlesByCategory()); // Set event handler

        // Schedule article fetching every 10 hours
        scheduler.scheduleAtFixedRate(this::fetchArticles, 0, 10, TimeUnit.HOURS);

        // Set event listener to load full article content when an item is selected
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = articles.get(newVal.intValue());
                openArticleDetailView(selectedArticle);
                saveReadingHistory(selectedArticle); // Save reading history when article is clicked
            }
        });

        loadArticlesFromCSV();  // Load articles from CSV initially
    }

    private boolean containsKeyword(String text, String keyword) {
        return text.matches(".*\\b" + keyword + "\\b.*");
    }

    private String categorizeArticle(String title, String content) {
        String textToCheck = (title + " " + content).toLowerCase();
        int[] scores = new int[categories.length];

        for (int i = 0; i < categories.length; i++) {
            String[] keywordArray = keywords[i].split("\\|");
            for (String keyword : keywordArray) {
                if (containsKeyword(textToCheck, keyword)) {
                    scores[i]++;
                }
            }
        }

        int maxScore = 0;
        int bestCategoryIndex = -1;

        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                bestCategoryIndex = i;
            }
        }

        return bestCategoryIndex != -1 ? categories[bestCategoryIndex] : "Uncategorized";
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
            String[] keywordArray = keywords.split("\\|");

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

            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch articles. Please try again later."));
                e.printStackTrace();
            }
        }).start();
    }

    private void parseAndSaveArticles(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonArray jsonArticles = jsonObject.getAsJsonArray("articles");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {  // Open in append mode
            for (int i = 0; i < jsonArticles.size(); i++) {
                JsonObject jsonArticle = jsonArticles.get(i).getAsJsonObject();
                String title = jsonArticle.has("title") && !jsonArticle.get("title").isJsonNull()
                        ? jsonArticle.get("title").getAsString()
                        : "Untitled";
                String content = jsonArticle.has("content") && !jsonArticle.get("content").isJsonNull()
                        ? jsonArticle.get("content").getAsString()
                        : "Content not available.";

                // Only add articles that are not already in the CSV
                if (!existingArticleTitles.contains(title)) {
                    String category = categorizeArticle(title, content);

                    // Save article to CSV
                    writer.write(String.join(",", title, content, category));
                    writer.newLine();

                    // Add to articles list
                    articles.add(new Article(title, content, category));
                    existingArticleTitles.add(title);  // Add the article title to the set
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update ListView
        Platform.runLater(() -> updateArticlesListView(articles));
    }

    private void loadArticlesFromCSV() {
        List<String> articleTitles = new ArrayList<>();
        articles.clear();  // Clear the articles list before loading new articles

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1); // Split by comma, and allow empty entries for missing columns

                if (parts.length >= 2) {
                    String title = parts[0].trim();  // Get the title (first column)
                    String content = parts.length > 1 ? parts[1].trim() : "Content not available";  // Get content (second column)
                    String category = parts.length > 2 ? parts[2].trim() : "Uncategorized";  // Get category (third column, optional)

                    // Add the article to the list with its real content
                    articles.add(new Article(title, content, category));
                    articleTitles.add(title);  // Add the title to the ListView
                    existingArticleTitles.add(title);  // Add the title to the set of existing articles
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update ListView with article titles
        articlesListView.setItems(FXCollections.observableArrayList(articleTitles));
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


            //controller.setArticle(new Article(article.getTitle(), article.getContent(), null)); // Only set title and content


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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Stage stage = (Stage) articlesListView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
