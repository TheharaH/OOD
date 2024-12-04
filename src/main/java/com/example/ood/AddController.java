package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController {

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField contentTextField;


    @FXML
    private void saveArticle() {
        String title = titleTextField.getText().trim();
        String content = contentTextField.getText().trim();

        if (!title.isEmpty() && !content.isEmpty()) {
            try {
                String filePath = "articles.csv";

                System.out.println("Title: " + title);
                System.out.println("Content: " + content);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                    // Write the title and content to the CSV file in a proper format
                    writer.write("\"" + title + "\",\"" + content + "\"");
                    writer.newLine();  // Add a new line after each article
                    System.out.println("Article saved: " + title);
                }
            } catch (IOException e) { // Handle errors that occur during file writing
                System.out.println("An error occurred while saving the article.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Please fill in both title and content.");
        }
    }


    @FXML
    private void handleBackButton() {
        try {
            // Load the admin fxml when clicked back
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) titleTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
