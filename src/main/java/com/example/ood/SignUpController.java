/*package com.example.ood;

import Model.User;
import Service.SessionManager;
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

    private static final String CSV_FILE_PATH = "login_details_50_rows.csv";

    private void displayError(String message) {
        errorMessage.setText(message);
        successMessage.setText("");
    }

    private void displaySuccess(String message) {
        successMessage.setText(message);
        errorMessage.setText("");
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            displayError("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            displayError("Passwords do not match.");
            return;
        }

        if (isUsernameTaken(username)) {
            displayError("Username already taken. Please choose another one.");
            return;
        }

        List<String> selectedCategories = new ArrayList<>();
        if (category1.isSelected()) selectedCategories.add("Technology");
        if (category2.isSelected()) selectedCategories.add("Health");
        if (category3.isSelected()) selectedCategories.add("AI");
        if (category4.isSelected()) selectedCategories.add("Sports");
        if (category5.isSelected()) selectedCategories.add("Education");

        if (selectedCategories.isEmpty()) {
            displayError("Please select at least one category.");
            return;
        }

        // Write new user to CSV
        if (writeNewUserToCSV(username, password, selectedCategories)) {
            displaySuccess("Successfully signed up!");

            // Set the new user in SessionManager
            User newUser = new User(username, password, selectedCategories);
            SessionManager.setCurrentUser(newUser);

            // Load the home page
            loadHomePage();
        } else {
            displayError("Failed to save data. Try again.");
        }
    }

    private boolean writeNewUserToCSV(String username, String password, List<String> selectedCategories) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            String categories = String.join(";", selectedCategories);
            writer.append(username)
                    .append(",")
                    .append(password)
                    .append(",")
                    .append(categories)
                    .append("\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
            displayError("Failed to load home page.");
            e.printStackTrace();
        }
    }
}*/

package com.example.ood;

import Model.User;
import Service.SessionManager;
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

    private static final String CSV_FILE_PATH = "login_details_50_rows.csv";

    private void displayError(String message) {
        errorMessage.setText(message);
        successMessage.setText("");
    }

    private void displaySuccess(String message) {
        successMessage.setText(message);
        errorMessage.setText("");
    }

    @FXML
    private void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            displayError("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            displayError("Passwords do not match.");
            return;
        }

        if (isUsernameTaken(username)) {
            displayError("Username already taken. Please choose another one.");
            return;
        }

        List<String> selectedCategories = getSelectedCategories();

        if (selectedCategories.isEmpty()) {
            displayError("Please select at least one category.");
            return;
        }

        // Write new user to CSV
        if (writeNewUserToCSV(username, password, selectedCategories)) {
            displaySuccess("Successfully signed up!");

            // Set the new user in SessionManager
            User newUser = new User(username, password, selectedCategories);
            SessionManager.setCurrentUser(newUser);

            // Load the home page
            loadHomePage();
        } else {
            displayError("Failed to save data. Try again.");
        }
    }

    private List<String> getSelectedCategories() {
        List<String> selectedCategories = new ArrayList<>();
        if (category1.isSelected()) selectedCategories.add("Technology");
        if (category2.isSelected()) selectedCategories.add("Health");
        if (category3.isSelected()) selectedCategories.add("AI");
        if (category4.isSelected()) selectedCategories.add("Sports");
        if (category5.isSelected()) selectedCategories.add("Education");
        return selectedCategories;
    }

    private boolean writeNewUserToCSV(String username, String password, List<String> selectedCategories) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
            String categories = String.join(";", selectedCategories);
            writer.append(username)
                    .append(",")
                    .append(password)
                    .append(",")
                    .append(categories)
                    .append("\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
            displayError("Failed to load home page.");
            e.printStackTrace();
        }
    }
}


