import java.sql.*;
public class AuthController {
    public static class LoginResult {
        public final boolean success;
        public final String accountNumber;
        public final String accountHolder;
        public final String role;
        public final String errorMessage;
        public LoginResult(boolean success, String accountNumber, String accountHolder,
                           String role, String errorMessage) {
            this.success = success;
            this.accountNumber = accountNumber;
            this.accountHolder = accountHolder;
            this.role = role;
            this.errorMessage = errorMessage;
        }
    }
    public static LoginResult login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            return new LoginResult(false, null, null, null, "Please enter username and password");
        }
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT account_number, account_holder_name, password, role, status " +
                "FROM accounts WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPass = rs.getString("password");
                if (PasswordService.verifyPassword(password, storedPass)) {
                    String accountNumber = rs.getString("account_number");
                    String accountHolder = rs.getString("account_holder_name");
                    String role = rs.getString("role");
                    String status = rs.getString("status");
                    if ("Frozen".equals(status)) {
                        rs.close();
                        ps.close();
                        return new LoginResult(false, null, null, null, "Account is blocked");
                    }
                    rs.close();
                    ps.close();
                    SessionManager.login(accountNumber, accountHolder, username, role);
                    return new LoginResult(true, accountNumber, accountHolder, role, null);
                }
            }
            rs.close();
            ps.close();
            return new LoginResult(false, null, null, null, "Invalid username or password");
        } catch (SQLException e) {
            return new LoginResult(false, null, null, null, "Database error: " + e.getMessage());
        }
    }
    public static void logout() {
        NavigationController.logout();
        LoginRegister.showLogin();
    }
}