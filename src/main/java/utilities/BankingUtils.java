import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
public class BankingUtils {
    public static String hashPassword(String plainPassword) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, 65536, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
    public static boolean verifyPassword(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null)
            return false;
        if (!storedPassword.contains(":"))
            return plainPassword.equals(storedPassword);
        try {
            String[] parts = storedPassword.split(":", 2);
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expected = Base64.getDecoder().decode(parts[1]);
            PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, 65536, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] actual = skf.generateSecret(spec).getEncoded();
            if (actual.length != expected.length)
                return false;
            int diff = 0;
            for (int i = 0; i < actual.length; i++)
                diff |= actual[i] ^ expected[i];
            return diff == 0;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 6)
            return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch))
                hasUpper = true;
            else if (Character.isLowerCase(ch))
                hasLower = true;
            else if (Character.isDigit(ch))
                hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }
    public static boolean validEmail(String email) {
        if (email == null || email.trim().isEmpty())
            return true;
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
    public static boolean validPhone(String phone) {
        if (phone == null || phone.trim().isEmpty())
            return true;
        return phone.matches("^[0-9+\\-\\s]{7,15}$");
    }
    public static String format(double value) {
        return String.format("ETB %,.2f", value);
    }
    public static String generateAccountNumber() {
        return "CBE" + (1000000000L + (long) (Math.random() * 9000000000L));
    }
    public static int recordTransaction(String accNo, String type, double amount,
            double balAfter, String recipient, String desc) {
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "INSERT INTO transactions(account_number,transaction_type,amount,balance_after," +
                            "recipient_account,description) VALUES(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, accNo);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setDouble(4, balAfter);
            ps.setString(5, recipient);
            ps.setString(6, desc);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.next() ? rs.getInt(1) : -1;
            rs.close();
            ps.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void generateReceiptPDF(int transactionId, String filePath) {
        PDFService.generateReceiptPDF(transactionId, filePath);
    }
    public static void generateCustomPDF(String filePath, String customContent) {
        PDFService.generateCustomPDF(filePath, customContent);
    }
    static String passwordRuleMessage() {
        return "Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character.";
    }
}