/*package com.example.ood;

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


}*/

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

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView; // ListView to display article titles
    @FXML
    private ComboBox<String> categoryComboBox; // ComboBox for selecting categories

    private final String API_KEY = "7b15371b5730491896a63377122237bb";
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&pageSize=100&apiKey=" + API_KEY;
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

    @FXML
    public void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(categories)); // Populate ComboBox
        categoryComboBox.setOnAction(event -> filterArticlesByCategory()); // Set event handler

        fetchArticles(); // Fetch and store articles in CSV

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
        // Define categories and their associated keywords
        String[] categories = {"Health", "Artificial Intelligence", "Technology", "Education", "Sports"};
        String[] keywords = {
                "health, wellness, fitness, medicine",
                "technology, tech, gadgets, innovation",
                "AI, artificial intelligence, machine learning, deep learning",
                "education, learning, schools, universities",
                "sports, athletics, games, competitions"
        };

        // Combine title and content to check for keywords
        String textToCheck = (title + " " + content).toLowerCase();

        // Loop through the categories and check if any keyword is in the article's text
        for (int i = 0; i < categories.length; i++) {
            String[] keywordArray = keywords[i].split(", ");
            for (String keyword : keywordArray) {
                if (textToCheck.contains(keyword)) {
                    return categories[i]; // Return the category if a keyword is found
                }
            }
        }

        // Return "Uncategorized" if no match is found
        return "Uncategorized";
    }


    public void filterArticlesByCategory() {
        String selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Find the index of the selected category
            int categoryIndex = -1;
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(selectedCategory)) {
                    categoryIndex = i;
                    break;
                }
            }

            // If the selected category is found, filter articles based on its associated keywords
            if (categoryIndex != -1) {
                String selectedKeywords = keywords[categoryIndex];
                filterArticlesByKeywords(selectedKeywords);
            }
        }
    }

    private void filterArticlesByKeywords(String keywords) {
        List<Article> filteredArticles = new ArrayList<>();

        // Loop through the articles and filter based on keywords
        for (Article article : articles) {
            String title = article.getTitle().toLowerCase();
            String description = article.getContent().toLowerCase();
            String[] keywordArray = keywords.split(", ");

            // Check if any keyword is found in the article's title or description
            for (String keyword : keywordArray) {
                if (title.contains(keyword) || description.contains(keyword)) {
                    filteredArticles.add(article);
                    break;
                }
            }
        }

        // Update the ListView with the filtered articles
        updateArticlesListView(filteredArticles);
    }

    private void updateArticlesListView(List<Article> filteredArticles) {
        List<String> articleTitles = new ArrayList<>();

        // Extract article titles and add them to the ListView
        for (Article article : filteredArticles) {
            articleTitles.add(article.getTitle());
        }

        articlesListView.setItems(FXCollections.observableArrayList(articleTitles));
    }



    // Set the current username for reading history
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
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

                // Parse articles from the API response and save to CSV
                parseAndSaveArticles(response.toString());

                // Load articles from CSV to display
                loadArticlesFromCSV();

            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch articles. Please try again later."));
                e.printStackTrace();
            }
        }).start();
    }

    // Parse JSON response, extract article details, and save to CSV
    private void parseAndSaveArticles(String jsonResponse) {
        articles.clear(); // Clear previous articles
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

                String category = categorizeArticle(title, content); // Categorize the article

                // Write article details to CSV
                writer.write(String.join(",", title, content, category));
                writer.newLine();

                articles.add(new Article(title, content, category));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load articles from CSV to display
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

            // Update ListView with article titles
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

    // Save article reading history
    private void saveReadingHistory(Article article) {
        if (currentUsername != null) {
            ReadingHistory.addArticle(currentUsername, article);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    // Open the full article view
    /*private void openArticleDetailView(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetailView.fxml"));
            Parent root = loader.load();

            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void openArticleDetailView(Article article) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetailView.fxml"));
            Parent root = loader.load();

            ArticleDetailController controller = loader.getController();
            controller.setArticle(article);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (Article List view)
            Stage currentStage = (Stage) articlesListView.getScene().getWindow();
            currentStage.close();  // Close the current stage

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Display an alert
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



/*package com.example.ood;

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

public class ArticlesController {

    @FXML
    private ListView<String> articlesListView; // ListView to display article titles
    @FXML
    private ComboBox<String> categoryComboBox; // ComboBox for selecting categories

    private final String API_KEY = "7b15371b5730491896a63377122237bb";
    private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&pageSize=100&apiKey=" + API_KEY;
    private final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\articles.csv";
    ; // Path to store articles

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

        fetchArticles(); // Fetch and store articles in CSV

        // Set event listener to load full article content when an item is selected
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = articles.get(newVal.intValue());
                openArticleDetailView(selectedArticle);
            }
        });
    }

    // Set the current username for reading history
    public void setCurrentUsername(String username) {
        // Fetch the current user from the session
        User user = SessionManager.getCurrentUser();

        if (user != null) {
            this.currentUsername = user.getUsername();
        } else {
            this.currentUsername = null;
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

                // Parse articles from the API response and save to CSV
                parseAndSaveArticles(response.toString());

                // Load articles from CSV to display
                loadArticlesFromCSV();

            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Error", "Failed to fetch articles. Please try again later."));
                e.printStackTrace();
            }
        }).start();
    }

    // Parse JSON response, extract article details, and save to CSV
    private void parseAndSaveArticles(String jsonResponse) {
        articles.clear(); // Clear previous articles
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

                String category = categorizeArticle(title, content); // Categorize the article

                // Write article details to CSV
                writer.write(String.join(",", title, content, category));
                writer.newLine();

                articles.add(new Article(title, content, category));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load articles from CSV to display
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
            // Update ListView with articles loaded from CSV
            Platform.runLater(() -> updateArticleListView(articles));
        } catch (IOException e) {
            e.printStackTrace();
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
*/

