package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class ReadingHistoryController {

    @FXML
    private ListView<String> historyListView;

    @FXML
    public void initialize() {
        ReadingHistory.loadReadingHistory(); // Load reading history from CSV
        loadReadingHistory();
    }

    private void loadReadingHistory() {
        List<ReadingHistory.HistoryEntry> entries = ReadingHistory.getReadingHistory();
        List<String> displayEntries = new ArrayList<>();

        for (ReadingHistory.HistoryEntry entry : entries) {
            displayEntries.add(entry.getUsername() + ": " + entry.getArticleTitle()); // Display username and title
        }

        historyListView.setItems(FXCollections.observableArrayList(displayEntries));
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) historyListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


/*package com.example.ood;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class ReadingHistoryController {

    @FXML
    private ListView<String> historyListView;

    private String currentUsername;

    @FXML
    public void initialize() {
        ReadingHistory.loadReadingHistory();
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        loadReadingHistory();
    }

    private void loadReadingHistory() {
        List<ReadingHistory.HistoryEntry> entries = ReadingHistory.getReadingHistoryForUser(currentUsername);
        List<String> displayEntries = new ArrayList<>();

        for (ReadingHistory.HistoryEntry entry : entries) {
            displayEntries.add(entry.getUsername() + ": " + entry.getArticleTitle());
        }

        historyListView.setItems(FXCollections.observableArrayList(displayEntries));
    }*/

    /*private void loadReadingHistory() {
        List<ReadingHistory.HistoryEntry> entries = ReadingHistory.getReadingHistoryForUser(currentUsername);
        System.out.println("Loaded entries for user: " + currentUsername);
        for (ReadingHistory.HistoryEntry entry : entries) {
            System.out.println(entry.getUsername() + ": " + entry.getArticleTitle());  // Check if entries are correctly loaded
        }

        List<String> displayEntries = new ArrayList<>();
        for (ReadingHistory.HistoryEntry entry : entries) {
            displayEntries.add(entry.getUsername() + ": " + entry.getArticleTitle());
        }
        historyListView.setItems(FXCollections.observableArrayList(displayEntries));
    }*/


    /*@FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) historyListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/