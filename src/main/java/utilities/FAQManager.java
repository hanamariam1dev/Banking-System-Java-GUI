import java.util.*;
public class FAQManager {
    private static final List<FAQItem> faqItems = new ArrayList<>();
    static {
        faqItems.add(new FAQItem("Transactions", "How do I deposit money?",
            "Click 'Deposit' in the sidebar or Quick Actions. Enter the amount and confirm with your password."));
        faqItems.add(new FAQItem("Transactions", "How do I withdraw cash?",
            "Click 'Withdraw' in the sidebar. Enter the amount (must not exceed your balance) and confirm with your password."));
        faqItems.add(new FAQItem("Transactions", "How do I transfer money?",
            "Click 'Transfer', enter the recipient's CBE account number and the amount. Confirm with your password to complete."));
        faqItems.add(new FAQItem("Account", "How do I view my transaction history?",
            "Click 'History' in the sidebar or Quick Actions to see all past transactions."));
        faqItems.add(new FAQItem("Security", "How do I change my password?",
            "Go to TOOLS > Change PIN in the sidebar. Enter your current password, then your new password twice."));
        faqItems.add(new FAQItem("Security", "Is my account secure?",
            "Yes. Every transaction requires password confirmation. Passwords are stored using PBKDF2 hashing."));
        faqItems.add(new FAQItem("QR & Payments", "What is the QR Code for?",
            "Your QR code encodes your account number. Others can scan it using the CBE app to send money directly to you."));
        faqItems.add(new FAQItem("Account", "How do I update my profile picture?",
            "Go to Profile in the sidebar, then click 'Update Profile'. Select an image file from your computer."));
    }
    public static List<FAQItem> getAllFAQs() {
        return new ArrayList<>(faqItems);
    }
    public static List<FAQItem> searchFAQs(String query) {
        List<FAQItem> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (FAQItem item : faqItems) {
            if (item.question.toLowerCase().contains(lowerQuery) ||
                item.answer.toLowerCase().contains(lowerQuery)) {
                results.add(item);
            }
        }
        return results;
    }
    public static List<FAQItem> getByCategory(String category) {
        List<FAQItem> results = new ArrayList<>();
        for (FAQItem item : faqItems) {
            if (item.category.equals(category)) {
                results.add(item);
            }
        }
        return results;
    }
    public static List<String> getCategories() {
        Set<String> categories = new LinkedHashSet<>();
        for (FAQItem item : faqItems) {
            categories.add(item.category);
        }
        return new ArrayList<>(categories);
    }
    public static class FAQItem {
        public final String category;
        public final String question;
        public final String answer;
        public FAQItem(String category, String question, String answer) {
            this.category = category;
            this.question = question;
            this.answer = answer;
        }
    }
}