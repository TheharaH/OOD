package com.example.ood;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    private Label successMessage;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Clear previous messages
        errorMessage.setText("");
        successMessage.setText("");

        // Validate login credentials
        if (validateLogin(username, password)) {
            successMessage.setText("Successfully logged in!");

            // Load the home page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                Parent homeRoot = loader.load();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene homeScene = new Scene(homeRoot);
                stage.setScene(homeScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMessage.setText("Invalid username or password. Please try again.");
        }
    }

    private boolean validateLogin(String username, String password) {
        String csvFile = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                // Check if the line has at least 2 elements for username and password
                if (credentials.length >= 2) {
                    String csvUsername = credentials[0].trim();
                    String csvPassword = credentials[1].trim();

                    // Validate username and password
                    if (csvUsername.equals(username) && csvPassword.equals(password)) {
                        return true; // Successful login
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Login failed
    }
}
