public class ValidationService {
    public static boolean validEmail(String email) {
        if (email == null || email.trim().isEmpty()) return true;
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
    public static boolean validPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return true;
        return phone.matches("^[0-9+\\-\\s]{7,15}$");
    }
    public static boolean validEthiopianPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.matches("^09[0-9]{8}$");
    }
    public static boolean validAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) return false;
        return accountNumber.matches("^CBE[0-9]{10}$");
    }
    public static boolean validAmount(double amount) {
        return amount > 0;
    }
}