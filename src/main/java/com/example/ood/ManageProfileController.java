package com.example.ood;

import Model.User;
import Service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ManageProfileController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private CheckBox category1; // Technology
    @FXML
    private CheckBox category2; // Health
    @FXML
    private CheckBox category3; // AI
    @FXML
    private CheckBox category4; // Sports
    @FXML
    private CheckBox category5; // Education

    @FXML
    private Label errorMessage;

    @FXML
    private Label successMessage;

    private final String filePath = "login_details_50_rows.csv";


    private void displayMessage(String message, boolean isSuccess) {
        if (isSuccess) {
            successMessage.setText(message);
            errorMessage.setText("");
        } else {
            errorMessage.setText(message);
            successMessage.setText("");
        }
    }

    // Handle updating the user profile
    public void handleUpdate() {
        displayMessage("", true);
        displayMessage("", false);

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        // Collect selected preferences
        List<String> selectedPreferences = new ArrayList<>();
        if (category1.isSelected()) selectedPreferences.add("Technology");
        if (category2.isSelected()) selectedPreferences.add("Health");
        if (category3.isSelected()) selectedPreferences.add("AI");
        if (category4.isSelected()) selectedPreferences.add("Sports");
        if (category5.isSelected()) selectedPreferences.add("Education");

        // Join preferences into a single string
        String newPreferences = String.join(";", selectedPreferences);

        // Validation checks for input fields
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            displayMessage("All password fields are required.", false);
            return;
        }

        // Check if the new passwords match
        if (!newPassword.equals(confirmNewPassword)) {
            displayMessage("New passwords do not match.", false);
            return;
        }

        String username = SessionManager.getCurrentUser().getUsername(); // Get the username
        if (username == null) {
            displayMessage("User not logged in.", false);
            return;
        }

        updateUserData(username, currentPassword, newPassword, newPreferences);
    }

    // Method to update the user data in the CSV file
    private void updateUserData(String username, String currentPassword, String newPassword, String newPreferences) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath)); // Read the current data from the CSV file
            List<String> updatedLines = new ArrayList<>();

            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String fileUsername = parts[0];
                String filePassword = parts[1];
                String filePreferences = parts[2];

                if (fileUsername.equals(username)) { // Check if the username matches
                    found = true;
                    if (!filePassword.equals(currentPassword)) { // Validate current password
                        displayMessage("Current password is incorrect.", false);
                        return;
                    }
                    updatedLines.add(fileUsername + "," + newPassword + "," + (newPreferences.isEmpty() ? filePreferences : newPreferences));
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                displayMessage("User not found in the file.", false);
                return;
            }

            // Write the updated data back to the CSV file
            Files.write(Paths.get(filePath), updatedLines);
            displayMessage("Profile updated successfully!", true);
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser != null) {
                currentUser.manageProfile();
            }
        } catch (IOException e) {
            displayMessage("Error updating the file.", false);
            e.printStackTrace();
        }
    }


    public void handleBack() {
        try {
            // Load the Home.fxml scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) currentPasswordField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            displayMessage("Error loading the Home page.", false);
        }
    }
}
