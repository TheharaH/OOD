package Model;

public class Admin extends User {

    // Constructor
    public Admin(String username, String password) {
        super(username, password, null); // Admin doesn't need preferences
    }

    // Method to validate admin credentials
    public static boolean isAdmin(String username, String password) {
        return "thehara".equals(username) && "musaeus".equals(password);
    }
}
