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
    private static final String CSV_FILE_PATH = "reading_history.csv";




    public static void addArticle(String username, Article article) {
        // Default likeStatus  "didn't like" and skip status "didn't skip"
        HistoryEntry entry = new HistoryEntry(username, article.getTitle(), "didn't like", "didn't skip");
        readingHistory.add(entry);
        saveToCSV(entry);
    }

    public static List<HistoryEntry> getReadingHistory() {
        return readingHistory;
    }


    public static void loadReadingHistory() {
        readingHistory.clear();// Clear previous history to avoid duplicates on reload

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) { // Check if the current user is null
                System.out.println("No user is currently logged in.");
                return;
            }

            // Loop through each line in the file
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String articleTitle = parts[1];

                    String likeStatus = "didn't like";
                    String skipStatus = "didn't skip";

                    if (username.equals(currentUser.getUsername())) {
                        readingHistory.add(new HistoryEntry(username, articleTitle, likeStatus, skipStatus));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // method to save a reading history entry to the CSV file
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



    public static class HistoryEntry {
        private String username;
        private String articleTitle;
        private String likeStatus;
        private String skipStatus;



        public HistoryEntry(String username, String articleTitle, String likeStatus, String skipStatus) {
            this.username = username;
            this.articleTitle = articleTitle;
            this.likeStatus = likeStatus;
            this.skipStatus = skipStatus;
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

        public String getSkipStatus() {
            return skipStatus;
        }
    }
}






