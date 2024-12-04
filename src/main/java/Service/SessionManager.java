package Service;

import Model.User;

public class SessionManager {
    private static User currentUser;

    // Set the current user in the session
    public static void setCurrentUser(User user) {
        if (user != null) {
            currentUser = user;
            System.out.println("SessionManager: currentUser set to: " + user.getUsername());
        }
    }

    // Get the current user from the session
    public static User getCurrentUser() {
        System.out.println("SessionManager: getCurrentUser called. currentUser is: " +
                (currentUser != null ? currentUser.getUsername() : "null"));
        return currentUser;
    }
}
