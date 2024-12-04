package com.example.ood;

import Model.User;
import Service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Ensure this import is present
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;


public class HomeController {

    @FXML
    private Label welcomeLabel;  // This should match the fx:id in FXML

    @FXML
    private Button articlesButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button logoutButton;

    // Method to set the user and update the welcome message
    public void setUser(User user) {
        welcomeLabel.setText("Welcome!");
    }


    @FXML
    public void handleArticlesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("articles.fxml")); // Path to your articles FXML
            Parent root = loader.load();
            ArticlesController articlesController = loader.getController();
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser != null) {
                articlesController.setCurrentUsername(currentUser.getUsername());
            }
            Stage stage = (Stage) articlesButton.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root)); // Switch to the articles scene
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Handle button click to load the reading history page
    @FXML
    public void handleHistoryButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReadingHistory.fxml")); // Path to the reading history FXML
            Parent root = loader.load();
            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }



    @FXML
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully logged out and navigated to Hello.fxml");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load Hello.fxml on logout");
        }
    }

    @FXML
    public void handleRecommendationButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecommendedArticle.fxml")); // Path to the reading history FXML
            Parent root = loader.load();
            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    @FXML
    public void loadManageProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageProfile.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
