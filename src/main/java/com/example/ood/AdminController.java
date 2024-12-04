

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

    private Admin admin;


    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AddButton.getScene().getWindow();
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
            Stage stage = (Stage) DeleteButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
