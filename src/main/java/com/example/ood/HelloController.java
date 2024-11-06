/*package com.example.ood;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    public void AHD(ActionEvent event) {
        try {
            // Load the AHD.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AHD.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded root and set it to the stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Set the controller for the loaded FXML to be AHDController
            LoginController ahdController = fxmlLoader.getController();

            // Optionally, you can pass any data to the AHDController using its public methods

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
}}*/
/*package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class HelloController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    @FXML
    void initialize() {
        // The login button action will now be handled by the handleLogin method
    }

    @FXML
    private void handleLogin() {
        try {
            // Load the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            AnchorPane loginPage = loader.load();

            // Set the scene with the login page
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(loginPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
package com.example.ood;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;

public class HelloController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    /*@FXML
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @FXML
    private ListView<String> articlesListView;

    private String currentUsername;

    // You can set the username via a setter method, constructor, or directly
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
    @FXML
    public void handleLogin(ActionEvent event) {
        try {
            // Load the AHD.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded root and set it to the stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Set the controller for the loaded FXML to be AHDController
            LoginController ahdController = fxmlLoader.getController();

            // Optionally, you can pass any data to the AHDController using its public methods

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSignup(ActionEvent event) {
        try {
            // Load the AHD.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded root and set it to the stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Set the controller for the loaded FXML to be AHDController
            SignUpController ahdController = fxmlLoader.getController();

            // Optionally, you can pass any data to the AHDController using its public methods

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public void goToReadingHistory() {
        try {
            // Load the FXML file for the reading history view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReadingHistoryView.fxml"));
            Parent root = loader.load();

            // Get the controller for the reading history view
            ReadingHistoryController controller = loader.getController();

            // Set the current username in the controller to load user-specific history
            controller.setCurrentUsername(currentUsername);

            // Get the current stage (the window) of the Articles view
            Stage stage = (Stage) articlesListView.getScene().getWindow();

            // Set the new scene with the loaded FXML
            stage.setScene(new Scene(root));

            // Show the stage (window)
            stage.show();
        } catch (IOException e) {
            // Handle any exceptions that may occur during the scene loading
            e.printStackTrace();
        }
    }*/
}

