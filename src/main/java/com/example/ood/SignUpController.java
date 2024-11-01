package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.BufferedReader;
import java.io.FileReader;
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

    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv";

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage.setText("Please fill in all fields.");
            successMessage.setText("");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setText("Passwords do not match.");
            successMessage.setText("");
            return;
        }

        if (isUsernameTaken(username)) {
            errorMessage.setText("Username already taken. Please choose another one.");
            successMessage.setText("");
            return;
        }

        List<String> selectedCategories = new ArrayList<>();
        if (category1.isSelected()) selectedCategories.add("Technology");
        if (category2.isSelected()) selectedCategories.add("Health");
        if (category3.isSelected()) selectedCategories.add("AI");
        if (category4.isSelected()) selectedCategories.add("Sports");
        if (category5.isSelected()) selectedCategories.add("Education");

        if (selectedCategories.isEmpty()) {
            errorMessage.setText("Please select at least one category.");
            successMessage.setText("");
            return;
        }

        String categories = String.join(";", selectedCategories);

        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            writer.append(username)
                    .append(",")
                    .append(password)
                    .append(",")
                    .append(categories)
                    .append("\n");

            successMessage.setText("Successfully signed up!");
            errorMessage.setText("");

            // Clear fields after successful sign-up
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
            category1.setSelected(false);
            category2.setSelected(false);
            category3.setSelected(false);
            category4.setSelected(false);
            category5.setSelected(false);

            loadHomePage();

        } catch (IOException e) {
            errorMessage.setText("Failed to save data. Try again.");
            e.printStackTrace();
        }
    }

    private boolean isUsernameTaken(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent homeRoot = loader.load();

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
