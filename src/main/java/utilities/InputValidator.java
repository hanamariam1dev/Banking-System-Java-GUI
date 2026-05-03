public class InputValidator {
    public static ValidationResult validateRegistration(String name, String email,
                                                         String phone, String username,
                                                         String password, String confirmPass) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "Full name is required");
        }
        if (username == null || username.trim().isEmpty()) {
            return new ValidationResult(false, "Username is required");
        }
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password is required");
        }
        if (!password.equals(confirmPass)) {
            return new ValidationResult(false, "Passwords do not match");
        }
        if (!PasswordService.isStrongPassword(password)) {
            return new ValidationResult(false, "Password must be at least 6 characters with uppercase, lowercase, and number");
        }
        if (!ValidationService.validEmail(email)) {
            return new ValidationResult(false, "Invalid email address");
        }
        if (!ValidationService.validPhone(phone)) {
            return new ValidationResult(false, "Invalid phone number");
        }
        return new ValidationResult(true, "");
    }
    public static ValidationResult validateTransfer(String fromAccount, String toAccount,
                                                     double amount, double balance) {
        if (fromAccount.equals(toAccount)) {
            return new ValidationResult(false, "Cannot transfer to same account");
        }
        if (!ValidationService.validAccountNumber(toAccount)) {
            return new ValidationResult(false, "Invalid recipient account number");
        }
        if (amount <= 0) {
            return new ValidationResult(false, "Amount must be greater than 0");
        }
        if (amount > balance) {
            return new ValidationResult(false, "Insufficient balance");
        }
        return new ValidationResult(true, "");
    }
    public static ValidationResult validatePasswordChange(String current, String newPass,
                                                           String confirmPass, String accountNumber) {
        if (current == null || current.isEmpty()) {
            return new ValidationResult(false, "Current password is required");
        }
        if (!SecurityUtils.verifyAccountPassword(accountNumber, current)) {
            return new ValidationResult(false, "Current password is incorrect");
        }
        if (!newPass.equals(confirmPass)) {
            return new ValidationResult(false, "New passwords do not match");
        }
        if (!PasswordService.isStrongPassword(newPass)) {
            return new ValidationResult(false, "Password does not meet strength requirements");
        }
        return new ValidationResult(true, "");
    }
    public static class ValidationResult {
        public final boolean valid;
        public final String message;
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
    }
}