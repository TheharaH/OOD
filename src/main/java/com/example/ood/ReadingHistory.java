package com.example.ood;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadingHistory {
    private static List<HistoryEntry> readingHistory = new ArrayList<>();
    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\reading_history.csv"; // Path to CSV file



    public static void addArticle(String username, Article article) {
        HistoryEntry entry = new HistoryEntry(username, article.getTitle());
        readingHistory.add(entry);
        saveToCSV(entry);
    }


    public static List<HistoryEntry> getReadingHistory() {
        return readingHistory;
    }

    public static void loadReadingHistory() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String articleTitle = parts[1];
                    readingHistory.add(new HistoryEntry(username, articleTitle));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private static void saveToCSV(HistoryEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            bw.write(entry.getUsername() + "," + entry.getArticleTitle());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private static void saveToCSV(HistoryEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            String username = entry.getUsername();
            String articleTitle = entry.getArticleTitle();

            if (username != null) {
                bw.write(username + "," + articleTitle);
                bw.newLine();
            } else {
                System.out.println("Username is null. Cannot save to CSV.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Inner class to represent a history entry
    public static class HistoryEntry {
        private String username;
        private String articleTitle;

        public HistoryEntry(String username, String articleTitle) {
            this.username = username;
            this.articleTitle = articleTitle;
        }

        public String getUsername() {
            return username;
        }

        public String getArticleTitle() {
            return articleTitle;
        }
    }


}

//new one
/*package com.example.ood;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadingHistory {
    private static List<HistoryEntry> readingHistory = new ArrayList<>();
    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\reading_history.csv";

    public static void addArticle(String username, Article article) {
        HistoryEntry entry = new HistoryEntry(username, article.getTitle());
        readingHistory.add(entry);
        saveToCSV(entry);
    }

    public static List<HistoryEntry> getReadingHistoryForUser(String username) {
        return readingHistory.stream()
                .filter(entry -> entry.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public static void loadReadingHistory() {
        readingHistory.clear(); // Clear existing history to avoid duplicates on reload
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String articleTitle = parts[1];
                    readingHistory.add(new HistoryEntry(username, articleTitle));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveToCSV(HistoryEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            bw.write(entry.getUsername() + "," + entry.getArticleTitle());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class HistoryEntry {
        private String username;
        private String articleTitle;

        public HistoryEntry(String username, String articleTitle) {
            this.username = username;
            this.articleTitle = articleTitle;
        }

        public String getUsername() {
            return username;
        }

        public String getArticleTitle() {
            return articleTitle;
        }
    }
}*/
