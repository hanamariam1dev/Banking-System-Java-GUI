import java.util.ArrayList;
import java.util.List;
public class NotificationManager {
    private static final List<Notification> notifications = new ArrayList<>();
    public static void addNotification(String title, String message, String type) {
        notifications.add(new Notification(title, message, type, System.currentTimeMillis()));
    }
    public static List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }
    public static void clearNotifications() {
        notifications.clear();
    }
    public static String[] getLoginNotifications() {
        return new String[] {
            "\uD83D\uDD12 CBE will NEVER ask for your PIN or password. Stay safe.",
            "\uD83C\uDF81 Zero transfer fees on all CBE-to-CBE transfers. Bank smarter!",
            "\uD83D\uDCF1 Use CBE mobile banking for faster, easier transactions 24/7.",
            "\u26A0\uFE0F Never share your OTP or account details with anyone.",
            "\u2705 Your deposits are insured. Bank with confidence at CBE."
        };
    }
    public static class Notification {
        public final String title;
        public final String message;
        public final String type;
        public final long timestamp;
        public Notification(String title, String message, String type, long timestamp) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.timestamp = timestamp;
        }
    }
}