


package com.example.ood;

import Model.Article;
import Model.User;
import Service.ReadingHistory;
import Service.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecommendedArticlesController {

    @FXML
    private ListView<String> articlesListView;

    private List<Article> filteredArticles = new ArrayList<>();

    private static final String CSV_FILE_PATH = "recommended_articles (6).csv";

    private String currentUsername;

    public void initialize() {
        // Get the current user
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            currentUsername = currentUser.getUsername();
            loadRecommendedArticles();
            updateArticlesListView();
        } else {
            System.out.println("No user is currently logged in.");
        }

        // Add a listener for selection changes in the ListView
        articlesListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                Article selectedArticle = filteredArticles.get(newVal.intValue());
                openArticleDetailView(selectedArticle);
                saveReadingHistory(selectedArticle); // Save reading history when article is clicked
            }
        });
    }


    @FXML
    private void handleArticleClick(MouseEvent event) {
        String selectedTitle = articlesListView.getSelectionModel().getSelectedItem();
        if (selectedTitle != null) {
            Article article = getArticleByTitle(selectedTitle);
            if (article != null) {
                saveReadingHistory(article);
                openArticleDetailView(article);
            }
        }
    }

    private Article getArticleByTitle(String title) {
        for (Article article : filteredArticles) {
            if (article.getTitle().equals(title)) {
                return article;
            }
        }
        return null; // Return null if no matching article is found
    }


    public void loadRecommendedArticles() {
        filteredArticles.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;

            if (currentUsername == null) {
                System.out.println("No user is currently logged in.");
                return;
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0].trim();
                    String articleTitle = parts[1].trim();
                    String articleContent = parts[2].trim();

                    if (username.equals(currentUsername)) {
                        filteredArticles.add(new Article(articleTitle, articleContent, username));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateArticlesListView() {
        List<String> articleTitles = new ArrayList<>();
        for (Article article : filteredArticles) {
            articleTitles.add(article.getTitle());
        }
        ObservableList<String> observableTitles = FXCollections.observableArrayList(articleTitles);
        articlesListView.setItems(observableTitles);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecommendedArticleDetail.fxml"));
            Parent root = loader.load();

            RecommendedArticleDetailController controller = loader.getController();
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

    @FXML
    private void handleBackButton() {
        try {
            // Load the FXML file for the home view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            // Get the current stage from the back button's scene
            Stage currentStage = (Stage) articlesListView.getScene().getWindow();

            // Set the new scene
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
