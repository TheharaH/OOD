

package com.example.ood;

import Model.Article;
import Model.User;
import Service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private Button likeButton;

    @FXML
    private Button skipButton;

    private boolean liked = false;

    private boolean skiped = false;// Track if the article is liked

    private Article article;

    // Called when the "Like" button is clicked
    @FXML
    private void handleLikeButton() {
        // Toggle like status
        liked = !liked;
        likeButton.setText(liked ? "Liked" : "Like");  // Update button text

        // Disable the "Like" button and enable the "Skip" button
        likeButton.setDisable(false);
        skipButton.setDisable(true);

        // Save the like status to the CSV
        String title = titleLabel.getText();
        String likeStatus = liked ? "like" : "didn't like";
        updateLikeStatusInCSV(title, likeStatus);
    }

    // Called when the "Skip" button is clicked
    @FXML
    private void handleSkipButton() {

        skiped = !skiped;
        skipButton.setText(skiped ? "Skiped" : "skip");  // Update button text


        // Disable the "Skip" button and enable the "Like" button
        skipButton.setDisable(false);
        likeButton.setDisable(true);

        // Save the skip status to the CSV
        String title = titleLabel.getText();
        String skipStatus = skiped ? "skip" : "didn't skip";
        //String likeStatus = skiped ? "like" : "didn't like";
        updateSkipStatusInCSV(title, skipStatus);
    }

    // Update like or skip status in the CSV
    private void updateLikeStatusInCSV(String articleTitle, String likeStatus) {
        String CSV_FILE_PATH = "reading_history.csv";
        List<String> lines = new ArrayList<>();
        boolean isUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            String username = SessionManager.getCurrentUser().getUsername(); // Get the current logged-in user

            // Read each line and update the matching entry's like status
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(articleTitle)) {
                    // If a matching entry is found, update the like status
                    line = username + "," + articleTitle + "," + likeStatus;
                    isUpdated = true;  // Mark that we've updated the status
                }
                lines.add(line);  // Add each line (updated or not) to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If an entry was updated, write the updated content back to the CSV
        if (isUpdated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        User currentUser = SessionManager.getCurrentUser(); // Assuming you have a SessionManager that provides the current user
        if (currentUser != null) {
            currentUser.likeArticle(); // Pass the article to the viewArticle method
        }
    }



    private void updateSkipStatusInCSV(String articleTitle, String skipStatus) {
        String CSV_FILE_PATH = "reading_history.csv";
        List<String> lines = new ArrayList<>();
        boolean isUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            String username = SessionManager.getCurrentUser().getUsername(); // Get the current logged-in user

            // Read each line and update the matching entry's skip status
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 4 && parts[0].equals(username) && parts[1].equals(articleTitle)) {
                    // Update the skip status while keeping the current like status
                    String likeStatus = parts[2];  // Keep the existing like status
                    line = username + "," + articleTitle + "," + likeStatus + "," + skipStatus;
                    isUpdated = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated content back to the CSV if an entry was modified
        if (isUpdated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        User currentUser = SessionManager.getCurrentUser(); // Assuming you have a SessionManager that provides the current user
        if (currentUser != null) {
            currentUser.skipArticle(); // Pass the article to the viewArticle method
        }
    }



    // Set the article data to be displayed
    public void setArticle(Article article) {
        this.article = article;
        titleLabel.setText(article.getTitle());
        //categoryLabel.setText("Category: " + article.getCategory());
        contentLabel.setText(article.getContent());
        User currentUser = SessionManager.getCurrentUser(); // Assuming you have a SessionManager that provides the current user
        if (currentUser != null) {
            currentUser.viewArticle(article); // Pass the article to the viewArticle method
        }
    }

    // Back button to return to the main view
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml")); // Main articles view
            Parent root = loader.load();

            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
