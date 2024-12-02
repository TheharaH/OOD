package Model;

public class Article {
    private String title;        // Title of the article
    private String content;      // Content of the article
    private String category;


    // Constructor
    public Article(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;

    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;}



}
