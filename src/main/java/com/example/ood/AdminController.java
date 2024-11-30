/*package com.example.ood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class AdminController {

    @FXML
    private Button AddButton;

    @FXML
    private Button DeleteButton;


    @FXML
    private void handleBackButton() {
        try {
            // Load the hello-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) AddButton.getScene().getWindow(); // You can use any button, here AddButton is used
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAddArticle() {
        try {
            // Load the Add Article FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Add.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) AddButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDeleteArticle() {
        try {
            // Load the Delete Article FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Delete.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) DeleteButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

package com.example.ood;

import Model.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class AdminController {

    @FXML
    private Button AddButton;

    @FXML
    private Button DeleteButton;

    private Admin admin; // Field to store the Admin object

    // Setter to initialize the Admin object
    public void setAdmin(Admin admin) {
        this.admin = admin;

        // Optionally, initialize UI elements based on admin details
        initializeAdminDetails();
    }

    private void initializeAdminDetails() {
        // For example, customize the admin page based on admin data
        System.out.println("Admin logged in: " + admin.getUsername());
    }

    @FXML
    private void handleBackButton() {
        try {
            // Load the hello-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) AddButton.getScene().getWindow(); // You can use any button, here AddButton is used
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAddArticle() {
        try {
            // Load the Add Article FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Add.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) AddButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDeleteArticle() {
        try {
            // Load the Delete Article FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Delete.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) DeleteButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
