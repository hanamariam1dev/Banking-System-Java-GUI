import java.sql.*;
public class ProfileService {
    public static boolean updateProfileName(String accountNumber, String newName) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET account_holder_name = ? WHERE account_number = ?");
            ps.setString(1, newName);
            ps.setString(2, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean updateEmail(String accountNumber, String newEmail) {
        if (!ValidationService.validEmail(newEmail)) {
            return false;
        }
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET email = ? WHERE account_number = ?");
            ps.setString(1, newEmail);
            ps.setString(2, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean updatePhone(String accountNumber, String newPhone) {
        if (!ValidationService.validPhone(newPhone)) {
            return false;
        }
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE accounts SET phone = ? WHERE account_number = ?");
            ps.setString(1, newPhone);
            ps.setString(2, accountNumber);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (Exception e) {
            return false;
        }
    }
    public static class ProfileData {
        public String name;
        public String email;
        public String phone;
        public String accountType;
        public String status;
        public double balance;
        public String createdAt;
    }
    public static ProfileData getProfile(String accountNumber) {
        ProfileData data = new ProfileData();
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT account_holder_name, email, phone, account_type, status, balance, created_at " +
                "FROM accounts WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data.name = rs.getString("account_holder_name");
                data.email = rs.getString("email");
                data.phone = rs.getString("phone");
                data.accountType = rs.getString("account_type");
                data.status = rs.getString("status");
                data.balance = rs.getDouble("balance");
                data.createdAt = rs.getTimestamp("created_at").toString();
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}