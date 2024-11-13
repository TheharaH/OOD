package com.example.ood;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteController {

    @FXML
    private ListView<String> articleListView;

    // File path of the CSV where the articles are stored
    private static final String FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\articles.csv";

    @FXML
    public void initialize() {
        // Load articles from the CSV into the ListView
        loadArticles();
    }

    // Load articles from CSV file into ListView

    /*private void loadArticles() {
        List<String> articles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String title = parts[0];  // Get the title (first column)
                    articles.add(title);  // Add the title to the list
                } else {
                    System.out.println("Skipping invalid line: " + line);  // Optionally log invalid lines
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        articleListView.getItems().setAll(articles);  // Update ListView with loaded titles
    }*/

    /*private void loadArticles() {
        List<String> articles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String title = parts[0];  // Get the title (first column)
                    title = title.replace("\"", "");  // Remove any quotes around the title
                    articles.add(title);  // Add the title to the list
                } else {
                    System.out.println("Skipping invalid line: " + line);  // Optionally log invalid lines
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        articleListView.getItems().setAll(articles);  // Update ListView with loaded titles
    }*/



    // Delete the selected article
    @FXML
    private void deleteArticle() {
        String selectedArticle = articleListView.getSelectionModel().getSelectedItem();

        if (selectedArticle != null) {
            // Perform deletion
            System.out.println("Deleting article: " + selectedArticle);

            // Remove from the ListView
            articleListView.getItems().remove(selectedArticle);

            // Remove from the CSV file
            deleteArticleFromCSV(selectedArticle);
        } else {
            System.out.println("Please select an article to delete.");
        }
    }

    // Delete the article from the CSV file

    /*private void deleteArticleFromCSV(String articleToDelete) {
        List<String> updatedArticles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;  // Skip empty lines
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String title = parts[0];  // Get the title (first column)
                    if (!title.equals(articleToDelete)) {
                        updatedArticles.add(line);  // Add line to updated list if title doesn't match
                    }
                } else {
                    System.out.println("Skipping invalid line: " + line);  // Log invalid lines
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the updated list back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String article : updatedArticles) {
                writer.write(article);
                writer.newLine();  // Write each article as a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



    // Load articles from CSV and remove quotes around the titles
    private void loadArticles() {
        List<String> articles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String title = parts[0].trim();  // Get the title (first column)
                    title = title.replace("\"", "");  // Remove any quotes around the title
                    articles.add(title);  // Add the title to the list
                } else {
                    System.out.println("Skipping invalid line: " + line);  // Optionally log invalid lines
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        articleListView.getItems().setAll(articles);  // Update ListView with loaded titles
    }

    // Delete the article from the CSV file
    private void deleteArticleFromCSV(String articleToDelete) {
        List<String> updatedArticles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;  // Skip empty lines
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String title = parts[0].trim();  // Get the title and trim any leading/trailing spaces
                    articleToDelete = articleToDelete.trim();  // Trim the selected article title
                    title = title.replace("\"", "");  // Remove quotes from the title in the CSV
                    if (!title.equals(articleToDelete)) {
                        updatedArticles.add(line);  // Add line to updated list if title doesn't match
                    }
                } else {
                    System.out.println("Skipping invalid line: " + line);  // Log invalid lines
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write the updated list back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String article : updatedArticles) {
                writer.write(article);
                writer.newLine();  // Write each article as a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            // Load the adminfxml.fxml (go back to the admin screen)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the scene to admin screen
            Stage stage = (Stage) articleListView.getScene().getWindow(); // You can use any control's scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
