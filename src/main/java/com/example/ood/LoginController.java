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
import java.util.Arrays;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessage;

    @FXML
    private Label successMessage;

    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\login_details_50_rows.csv";

    private User validateLogin(String username, String password) {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 3) {
                    String csvUsername = credentials[0].trim();
                    String csvPassword = credentials[1].trim();
                    String[] categories = credentials[2].split(";");

                    if (csvUsername.equals(username) && csvPassword.equals(password)) {
                        List<String> userPreferences = Arrays.asList(categories);
                        return new User(csvUsername, csvPassword, userPreferences); // Successful login
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Login failed
    }

    /*@FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Clear previous messages
        errorMessage.setText("");
        successMessage.setText("");

        // Validate login credentials
        User loggedInUser = validateLogin(username, password);
        if (loggedInUser != null) {
            successMessage.setText("Successfully logged in!");
            loadHomePage(loggedInUser);
        } else {
            errorMessage.setText("Invalid username or password. Please try again.");
        }
    }*/

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Clear previous messages
        errorMessage.setText("");
        successMessage.setText("");

        // Validate login credentials
        User loggedInUser = validateLogin(username, password);
        if (loggedInUser != null) {
            successMessage.setText("Successfully logged in!");

            // Set the logged-in user in the session
            SessionManager.setCurrentUser(loggedInUser);

            loadHomePage(loggedInUser);
        } else {
            errorMessage.setText("Invalid username or password. Please try again.");
        }
    }


    private void loadHomePage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent homeRoot = loader.load();

            // Pass user information to the home page controller (e.g., for personalized content)
            HomeController homeController = loader.getController();
            homeController.setUser(user);

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
