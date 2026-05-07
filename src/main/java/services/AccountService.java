import java.sql.*;
public class AccountService {
    public static double getBalance(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT balance FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            double balance = rs.next() ? rs.getDouble("balance") : 0.0;
            rs.close();
            ps.close();
            return balance;
        } catch (Exception e) { return 0.0; }
    }
    public static boolean updateBalance(String accountNumber, double newBalance) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET balance = ? WHERE account_number = ?");
            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) { return false; }
    }
    public static boolean accountExists(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT 1 FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            rs.close();
            ps.close();
            return exists;
        } catch (Exception e) { return false; }
    }
        public static String getAccountHolder(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT account_holder_name FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            String name = rs.next() ? rs.getString("account_holder_name") : "";
            rs.close();
            ps.close();
            return name;
        } catch (Exception e) { return ""; }
    }
    public static boolean blockAccount(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET status = 'Frozen' WHERE account_number = ?");
            ps.setString(1, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) { return false; }
    }
    public static boolean unblockAccount(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET status = 'Active' WHERE account_number = ?");
            ps.setString(1, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) { return false; }
    }
    public static boolean deleteAccount(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM accounts WHERE account_number = ? AND role != 'Admin'");
            ps.setString(1, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) { return false; }
    }
}