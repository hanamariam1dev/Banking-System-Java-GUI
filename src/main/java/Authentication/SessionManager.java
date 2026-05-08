public class SessionManager {
    private static String currentAccountNumber;
    private static String currentAccountHolder;
    private static String currentUsername;
    private static String currentRole;
    private static boolean isLoggedIn = false;
    public static void login(String accountNumber, String accountHolder,
                              String username, String role) {
        currentAccountNumber = accountNumber;
        currentAccountHolder = accountHolder;
        currentUsername = username;
        currentRole = role;
        isLoggedIn = true;
    }
    public static void logout() {
        currentAccountNumber = null;
        currentAccountHolder = null;
        currentUsername = null;
        currentRole = null;
        isLoggedIn = false;
    }
    public static String getAccountNumber() { return currentAccountNumber; }
    public static String getAccountHolder() { return currentAccountHolder; }
    public static String getUsername() { return currentUsername; }
    public static String getRole() { return currentRole; }
    public static boolean isLoggedIn() { return isLoggedIn; }
    public static boolean isAdmin() { return "Admin".equals(currentRole); }
}