
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

    @FXML
    public void handleAdmin(ActionEvent event) {
        try {
            // Load the AHD.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded root and set it to the stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Set the controller for the loaded FXML to be AHDController
            AdminLoginController ahdController = fxmlLoader.getController();

            // Optionally, you can pass any data to the AHDController using its public methods

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

