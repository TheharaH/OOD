package com.example.ood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Ensure this import is present
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.stage.Stage;
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
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!"); // Set the text for the label
        } else {
            welcomeLabel.setText("Welcome!"); // Default welcome message
        }
    }

    // Handle button click to load the articles page
    /*@FXML
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
    }*/
    @FXML
    public void handleArticlesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("articles.fxml")); // Path to your articles FXML
            Parent root = loader.load();

            // Get the controller associated with the FXML
            ArticlesController articlesController = loader.getController();

            // Assuming the current user is stored in SessionManager
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser != null) {
                articlesController.setCurrentUsername(currentUser.getUsername());
            }

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReadingHistory.fxml")); // Path to the reading history FXML
            Parent root = loader.load();

            // Switch to the reading history scene
            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }


    // Handle button click to load the manage profile page
    @FXML
    public void handleManageProfileButton() {
        System.out.println("Loading Manage Profile Page...");
        // Add your logic here to switch to the Manage Profile page.
    }

    @FXML
    public void logout() {
        try {
            // Load the Hello.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage using the logoutButton
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set the new scene
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

            // Switch to the reading history scene
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
            // Load the ManageProfile.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageProfile.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source (the button)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
