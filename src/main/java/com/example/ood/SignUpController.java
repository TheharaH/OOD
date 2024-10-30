package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private CheckBox category1;

    @FXML
    private CheckBox category2;

    @FXML
    private CheckBox category3;

    @FXML
    private CheckBox category4;

    @FXML
    private CheckBox category5;

    @FXML
    private Label errorMessage;

    @FXML
    private Label successMessage;

    // Path to the CSV file
    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv";

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage.setText("Please fill in all fields.");
            successMessage.setText(""); // Clear success message
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setText("Passwords do not match.");
            successMessage.setText(""); // Clear success message
            return;
        }

        // Collect selected categories
        List<String> selectedCategories = new ArrayList<>();
        if (category1.isSelected()) selectedCategories.add("Category 1");
        if (category2.isSelected()) selectedCategories.add("Category 2");
        if (category3.isSelected()) selectedCategories.add("Category 3");
        if (category4.isSelected()) selectedCategories.add("Category 4");
        if (category5.isSelected()) selectedCategories.add("Category 5");

        if (selectedCategories.isEmpty()) {
            errorMessage.setText("Please select at least one category.");
            successMessage.setText(""); // Clear success message
            return;
        }

        // Create a comma-separated string for categories
        String categories = String.join(";", selectedCategories);

        // Save data to CSV
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) { // true to append data
            writer.append(username)
                    .append(",")
                    .append(password)
                    .append(",")
                    .append(categories)
                    .append("\n");

            successMessage.setText("Successfully signed up!");
            errorMessage.setText(""); // Clear error message

            // Clear input fields
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            category1.setSelected(false);
            category2.setSelected(false);
            category3.setSelected(false);
            category4.setSelected(false);
            category5.setSelected(false);

            // Load the home page after successful signup
            loadHomePage();

        } catch (IOException e) {
            errorMessage.setText("Failed to save data. Try again.");
            e.printStackTrace();
        }
    }

    private void loadHomePage() {
        // Load the home page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent homeRoot = loader.load();

            // Get the current stage from the usernameField's scene
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene homeScene = new Scene(homeRoot);
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage.setText("Failed to load home page.");
        }
    }
}
