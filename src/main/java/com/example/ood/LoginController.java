package com.example.ood;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage; // Label to display error messages
    @FXML
    private Label successMessage; // Label to display success messages

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Clear previous messages
        errorMessage.setText("");
        successMessage.setText("");

        // Validate login credentials
        if (validateLogin(username, password)) {
            // Show success message
            successMessage.setText("Successfully logged in!");
            // Proceed to the next scene or main application
            System.out.println("Login successful!");
            // Load the next FXML scene here
        } else {
            // Show error message
            errorMessage.setText("Invalid username or password.");
        }
    }

    private boolean validateLogin(String username, String password) {
        // Load credentials from CSV file
        String csvFile = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv"; // Updated with your CSV file path
        String line;
        boolean isValid = false;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(","); // Assuming CSV format is username,password
                if (credentials.length == 2) {
                    if (credentials[0].equals(username) && credentials[1].equals(password)) {
                        isValid = true;
                        break; // Stop reading if valid credentials found
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
