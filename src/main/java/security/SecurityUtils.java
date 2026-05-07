import java.sql.*;
public class SecurityUtils {
    public static boolean verifyAccountPassword(String accountNumber, String password) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT password FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String stored = rs.getString("password");
                rs.close();
                ps.close();
                return PasswordService.verifyPassword(password, stored);
            }
            rs.close();
            ps.close();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    public static boolean changePassword(String accountNumber, String currentPassword, String newPassword) {
        if (!verifyAccountPassword(accountNumber, currentPassword)) {
            return false;
        }
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET password = ? WHERE account_number = ?");
            ps.setString(1, PasswordService.hashPassword(newPassword));
            ps.setString(2, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) { return false; }
    }
    public static boolean isAccountBlocked(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT status FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            boolean blocked = rs.next() && "Frozen".equals(rs.getString("status"));
            rs.close();
            ps.close();
            return blocked;
        } catch (Exception e) { return true; }
    }
    public static boolean isAdmin(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT role FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            boolean admin = rs.next() && "Admin".equals(rs.getString("role"));
            rs.close();
            ps.close();
            return admin;
        } catch (Exception e) { return false; }
    }
}