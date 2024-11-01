package com.example.ood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Ensure this import is present
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private Label welcomeLabel;  // This should match the fx:id in FXML

    @FXML
    private Button articlesButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button manageProfileButton;

    // Method to set the user and update the welcome message
    public void setUser(User user) {
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!"); // Set the text for the label
        } else {
            welcomeLabel.setText("Welcome!"); // Default welcome message
        }
    }

    // Handle button click to load the articles page
    @FXML
    public void handleArticlesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("articles.fxml")); // Path to your articles FXML
            Parent root = loader.load();

            // Switch to the articles scene
            Stage stage = (Stage) articlesButton.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root)); // Switch to the articles scene
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    // Handle button click to load the reading history page
    @FXML
    public void handleHistoryButton() {
        System.out.println("Loading Reading History Page...");
        // Add your logic here to switch to the History page.
    }

    // Handle button click to load the manage profile page
    @FXML
    public void handleManageProfileButton() {
        System.out.println("Loading Manage Profile Page...");
        // Add your logic here to switch to the Manage Profile page.
    }
}
