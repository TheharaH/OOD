/*package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

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
    private TextField preferencesField;

    @FXML
    private Label errorMessage;

    @FXML
    private Label successMessage;

    private final String filePath = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv";

    // Handle updating the user profile
    public void handleUpdate() {
        errorMessage.setText("");
        successMessage.setText("");

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();
        String newPreferences = preferencesField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            errorMessage.setText("All password fields are required.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            errorMessage.setText("New passwords do not match.");
            return;
        }

        String username = SessionManager.getCurrentUser().getUsername(); // Get the username from the session
        if (username == null) {
            errorMessage.setText("User not logged in.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<String> updatedLines = new ArrayList<>();

            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String fileUsername = parts[0];
                String filePassword = parts[1];
                String filePreferences = parts[2];

                if (fileUsername.equals(username)) {
                    found = true;
                    if (!filePassword.equals(currentPassword)) {
                        errorMessage.setText("Current password is incorrect.");
                        return;
                    }
                    updatedLines.add(fileUsername + "," + newPassword + "," + (newPreferences.isEmpty() ? filePreferences : newPreferences));
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                errorMessage.setText("User not found in the file.");
                return;
            }

            Files.write(Paths.get(filePath), updatedLines);
            successMessage.setText("Profile updated successfully!");

        } catch (IOException e) {
            errorMessage.setText("Error updating the file.");
            e.printStackTrace();
        }
    }

    // Handle the Back button press
    public void handleBack() {
        try {
            // Load the Home.fxml scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the scene to Home.fxml
            Stage stage = (Stage) currentPasswordField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
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

    private final String filePath = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv";

    // Handle updating the user profile
    public void handleUpdate() {
        errorMessage.setText("");
        successMessage.setText("");

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

        String newPreferences = String.join(";", selectedPreferences);

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            errorMessage.setText("All password fields are required.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            errorMessage.setText("New passwords do not match.");
            return;
        }

        String username = SessionManager.getCurrentUser().getUsername(); // Get the username from the session
        if (username == null) {
            errorMessage.setText("User not logged in.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            List<String> updatedLines = new ArrayList<>();

            boolean found = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                String fileUsername = parts[0];
                String filePassword = parts[1];
                String filePreferences = parts[2];

                if (fileUsername.equals(username)) {
                    found = true;
                    if (!filePassword.equals(currentPassword)) {
                        errorMessage.setText("Current password is incorrect.");
                        return;
                    }
                    updatedLines.add(fileUsername + "," + newPassword + "," + (newPreferences.isEmpty() ? filePreferences : newPreferences));
                } else {
                    updatedLines.add(line);
                }
            }

            if (!found) {
                errorMessage.setText("User not found in the file.");
                return;
            }

            Files.write(Paths.get(filePath), updatedLines);
            successMessage.setText("Profile updated successfully!");

        } catch (IOException e) {
            errorMessage.setText("Error updating the file.");
            e.printStackTrace();
        }
    }

    // Handle the Back button press
    public void handleBack() {
        try {
            // Load the Home.fxml scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the scene to Home.fxml
            Stage stage = (Stage) currentPasswordField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

