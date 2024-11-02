package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ArticleDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label contentLabel;

    private Article article;

    // Set the article data to be displayed
    public void setArticle(Article article) {
        this.article = article;
        titleLabel.setText(article.getTitle());
        //categoryLabel.setText("Category: " + article.getCategory());
        contentLabel.setText(article.getContent());
    }

    // Back button to return to the main view
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("articles.fxml")); // Main articles view
            Parent root = loader.load();

            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
