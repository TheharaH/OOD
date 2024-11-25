/*package com.example.ood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {

    @FXML
    private TextField usernameField;  // For admin username input

    @FXML
    private PasswordField passwordField;  // For admin password input

    @FXML
    private Button loginButton;  // For login button

    // Predefined admin credentials
    private static final String ADMIN_USERNAME = "thehara";
    private static final String ADMIN_PASSWORD = "musaeus";

    @FXML
    private void handleAdminLogin() {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Check if the entered credentials match the predefined ones
        if (enteredUsername.equals(ADMIN_USERNAME) && enteredPassword.equals(ADMIN_PASSWORD)) {
            // If login is successful, navigate to the admin page (adminfxml.fxml)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // If credentials are incorrect, show an alert message
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        // Display an alert with the given title and message
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/
