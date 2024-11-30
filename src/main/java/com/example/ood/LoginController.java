
/*package com.example.ood;

import Model.Admin;
import Model.User;
import Service.SessionManager;
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

        private static final String CSV_FILE_PATH = "login_details_50_rows.csv";

        private User validateLogin(String username, String password) {
            // Check if the user is the admin
            if (Admin.isAdmin(username, password)) {
                return new Admin(username, password); // Successful admin login
            }
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

        private void displayLoginError(String message) {
            errorMessage.setText(message);
            successMessage.setText("");
        }

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

                // Redirect based on user type
                if (loggedInUser instanceof Admin) {
                    loadAdminPage((Admin) loggedInUser);
                } else {
                    loadHomePage(loggedInUser);
                }
            } else {
                displayLoginError("Invalid. Please try again");
            }
        }

        private void loadHomePage(User user) {
            try {
                // Load the home.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
                Parent homeRoot = loader.load();

                // Pass user information to the home page controller
                HomeController homeController = loader.getController();
                homeController.setUser(user);

                // Create a new stage for the home page
                Stage homeStage = new Stage();
                Scene homeScene = new Scene(homeRoot);
                homeStage.setScene(homeScene);

                // Set properties for the new stage (e.g., title, modality if needed)
                homeStage.setTitle("Home Page");
                homeStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                displayLoginError("Failed to load home page.");
            }
        }

        private void loadAdminPage(Admin admin) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                Parent adminRoot = loader.load();

                // Pass admin information to the admin page controller if needed
                AdminController adminController = loader.getController();

                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene adminScene = new Scene(adminRoot);
                stage.setScene(adminScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                displayLoginError("Failed to load admin page.");
            }
        }
    }
*/

package com.example.ood;

import Model.Admin;
import Model.User;
import Service.SessionManager;
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
import java.util.ArrayList;
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

    private static final String CSV_FILE_PATH = "login_details_50_rows.csv";

    // New method to read the CSV file and retrieve user details
    private List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length >= 3) {
                    String csvUsername = credentials[0].trim();
                    String csvPassword = credentials[1].trim();
                    String[] categories = credentials[2].split(";");

                    List<String> userPreferences = Arrays.asList(categories);
                    users.add(new User(csvUsername, csvPassword, userPreferences));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Validate login credentials
    private User validateLogin(String username, String password) {
        // Check if the user is the admin
        if (Admin.isAdmin(username, password)) {
            return new Admin(username, password); // Successful admin login
        }

        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Successful login
            }
        }

        return null; // Login failed
    }

    private void displayLoginError(String message) {
        errorMessage.setText(message);
        successMessage.setText("");
    }

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

            // Redirect based on user type
            if (loggedInUser instanceof Admin) {
                loadAdminPage((Admin) loggedInUser);
            } else {
                loadHomePage(loggedInUser);
            }
        } else {
            displayLoginError("Invalid. Please try again");
        }
    }

    private void loadHomePage(User user) {
        try {
            // Load the home.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent homeRoot = loader.load();

            // Pass user information to the home page controller
            HomeController homeController = loader.getController();
            homeController.setUser(user);

            // Create a new stage for the home page
            Stage homeStage = new Stage();
            Scene homeScene = new Scene(homeRoot);
            homeStage.setScene(homeScene);

            // Set properties for the new stage (e.g., title, modality if needed)
            homeStage.setTitle("Home Page");
            homeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            displayLoginError("Failed to load home page.");
        }
    }

    private void loadAdminPage(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent adminRoot = loader.load();

            // Pass admin information to the admin page controller if needed
            AdminController adminController = loader.getController();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene adminScene = new Scene(adminRoot);
            stage.setScene(adminScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            displayLoginError("Failed to load admin page.");
        }
    }
}
