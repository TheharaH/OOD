package Service;

import Model.Article;
import Model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadingHistory {
    private static List<HistoryEntry> readingHistory = new ArrayList<>();
    private static final String CSV_FILE_PATH = "reading_history.csv"; // Path to CSV file




    public static void addArticle(String username, Article article) {
        // Default the likeStatus to "didn't like"
        HistoryEntry entry = new HistoryEntry(username, article.getTitle(), "didn't like", "didn't skip");
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
                    String likeStatus = "didn't like";
                    String skipStatus = "didn't skip";  // Default value
// Default value

                    // Only add history entries for the current user
                    if (username.equals(currentUser.getUsername())) {
                        readingHistory.add(new HistoryEntry(username, articleTitle, likeStatus, skipStatus));
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
            String skipStatus = entry.getSkipStatus();


            if (username != null) {
                bw.write(username + "," + articleTitle + "," + likeStatus + "," + skipStatus);
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
        private String likeStatus;
        private String skipStatus;  // Field to store the like status
        // Field to store the like status

        // Constructor
        public HistoryEntry(String username, String articleTitle, String likeStatus, String skipStatus) {
            this.username = username;
            this.articleTitle = articleTitle;
            this.likeStatus = likeStatus;
            this.skipStatus = skipStatus;
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

        public String getSkipStatus() {
            return skipStatus;
        }


        public void setSkipStatus(String skipStatus) {
            this.skipStatus = skipStatus;
        }
    }
}






