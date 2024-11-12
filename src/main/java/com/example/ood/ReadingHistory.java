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
        // Default the likeStatus to "didn't like"
        HistoryEntry entry = new HistoryEntry(username, article.getTitle(), "didn't like");
        readingHistory.add(entry);
        saveToCSV(entry);
    }

    public static List<HistoryEntry> getReadingHistory() {
        return readingHistory;
    }


    public static void loadReadingHistory() {
        // Clear previous history to avoid duplicates on reload
        readingHistory.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Get the current user from the session
            User currentUser = SessionManager.getCurrentUser();

            // Check if the current user is null before processing history
            if (currentUser == null) {
                System.out.println("No user is currently logged in.");
                return;  // Exit if no user is logged in
            }

            // Loop through each line in the file
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String articleTitle = parts[1];

                    // Set a default value for likeStatus (for example, "didn't like")
                    String likeStatus = "didn't like";  // Default value

                    // Only add history entries for the current user
                    if (username.equals(currentUser.getUsername())) {
                        readingHistory.add(new HistoryEntry(username, articleTitle, likeStatus));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private static void saveToCSV(HistoryEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            String username = entry.getUsername();
            String articleTitle = entry.getArticleTitle();
            String likeStatus = entry.getLikeStatus();

            if (username != null) {
                bw.write(username + "," + articleTitle + "," + likeStatus);
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
        private String likeStatus;  // Field to store the like status

        // Constructor
        public HistoryEntry(String username, String articleTitle, String likeStatus) {
            this.username = username;
            this.articleTitle = articleTitle;
            this.likeStatus = likeStatus;
        }

        // Getter for username
        public String getUsername() {
            return username;
        }

        // Getter for articleTitle
        public String getArticleTitle() {
            return articleTitle;
        }

        // Getter for likeStatus
        public String getLikeStatus() {
            return likeStatus;
        }

        // Setter for likeStatus
        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }
    }
}


/*package com.example.ood;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingHistory {
    private static List<HistoryEntry> readingHistory = new ArrayList<>();
    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\reading_history.csv"; // Path to CSV file

    // Add a new article to the reading history with initial likeStatus as "didn't like"
    public static void addArticle(String username, Article article) {
        HistoryEntry entry = new HistoryEntry(username, article.getTitle(), "didn't like");
        readingHistory.add(entry);
        saveToCSV(entry);
    }

    // Retrieve the reading history list
    public static List<HistoryEntry> getReadingHistory() {
        return readingHistory;
    }

    // Load reading history for the current user
    public static void loadReadingHistory() {
        readingHistory.clear();
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0];
                    String articleTitle = parts[1];
                    String likeStatus = parts[2];

                    if (username.equals(currentUser.getUsername())) {
                        readingHistory.add(new HistoryEntry(username, articleTitle, likeStatus));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save a new history entry to the CSV
    private static void saveToCSV(HistoryEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            String username = entry.getUsername();
            String articleTitle = entry.getArticleTitle();
            String likeStatus = entry.getLikeStatus();

            if (username != null) {
                bw.write(username + "," + articleTitle + "," + likeStatus);
                bw.newLine();
            } else {
                System.out.println("Username is null. Cannot save to CSV.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the like status of a specific article and save it to the CSV
    public static void updateLikeStatus(String username, String articleTitle, String newLikeStatus) {
        for (HistoryEntry entry : readingHistory) {
            if (entry.getUsername().equals(username) && entry.getArticleTitle().equals(articleTitle)) {
                entry.setLikeStatus(newLikeStatus);
                break;
            }
        }
        saveAllToCSV();
    }

    // Save all entries to the CSV, overwriting existing data
    private static void saveAllToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (HistoryEntry entry : readingHistory) {
                bw.write(entry.getUsername() + "," + entry.getArticleTitle() + "," + entry.getLikeStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to represent a history entry
    public static class HistoryEntry {
        private String username;
        private String articleTitle;
        private String likeStatus;

        public HistoryEntry(String username, String articleTitle, String likeStatus) {
            this.username = username;
            this.articleTitle = articleTitle;
            this.likeStatus = likeStatus;
        }

        public String getUsername() {
            return username;
        }

        public String getArticleTitle() {
            return articleTitle;
        }

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }
    }
}*/

/*package com.example.ood;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadingHistory {
    private static List<HistoryEntry> readingHistory = new ArrayList<>();
    private static final String CSV_FILE_PATH = "D:\\2nd Year - Copy\\1st sem\\OOD\\reading_history.csv"; // Path to CSV file

    // Add a new article to the reading history with initial likeStatus as "didn't like"
    public static void addArticle(String username, Article article) {
        HistoryEntry entry = new HistoryEntry(username, article.getTitle(), "didn't like");
        readingHistory.add(entry);
        saveEntryToCSV(entry);
    }

    // Retrieve the reading history list
    public static List<HistoryEntry> getReadingHistory() {
        return readingHistory;
    }

    // Load reading history for the current user
    public static void loadReadingHistory() {
        readingHistory.clear();
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0];
                    String articleTitle = parts[1];
                    String likeStatus = parts[2];

                    if (username.equals(currentUser.getUsername())) {
                        readingHistory.add(new HistoryEntry(username, articleTitle, likeStatus));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save a new history entry to the CSV
    private static void saveEntryToCSV(HistoryEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            String username = entry.getUsername();
            String articleTitle = entry.getArticleTitle();
            String likeStatus = entry.getLikeStatus();

            if (username != null) {
                bw.write(username + "," + articleTitle + "," + likeStatus);
                bw.newLine();
            } else {
                System.out.println("Username is null. Cannot save to CSV.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the like status of a specific article and save it to the CSV
    public static void updateLikeStatus(String username, String articleTitle, String newLikeStatus) {
        for (HistoryEntry entry : readingHistory) {
            if (entry.getUsername().equals(username) && entry.getArticleTitle().equals(articleTitle)) {
                entry.setLikeStatus(newLikeStatus);
                break;
            }
        }
        saveAllEntriesToCSV();
    }

    // Save all entries to the CSV, overwriting existing data (including headers)
    private static void saveAllEntriesToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write headers for the CSV file
            bw.write("Username,ArticleTitle,LikeStatus");
            bw.newLine();

            // Write each entry in the reading history
            for (HistoryEntry entry : readingHistory) {
                bw.write(entry.getUsername() + "," + entry.getArticleTitle() + "," + entry.getLikeStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to represent a history entry
    public static class HistoryEntry {
        private String username;
        private String articleTitle;
        private String likeStatus;

        public HistoryEntry(String username, String articleTitle, String likeStatus) {
            this.username = username;
            this.articleTitle = articleTitle;
            this.likeStatus = likeStatus;
        }

        public String getUsername() {
            return username;
        }

        public String getArticleTitle() {
            return articleTitle;
        }

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }
    }
}
*/



