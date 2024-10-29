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
    private void handleSignup() {
        // Handle sign up action here (you can load another FXML for sign-up)
    }
}

