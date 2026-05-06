
import java.sql.*;

public class TransactionService {

    public static int recordTransaction(String accNo, String type, double amount,
            double balAfter, String recipient, String desc) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO transactions(account_number,transaction_type,amount,balance_after,"
                    + "recipient_account,description) VALUES(?,?,?,?,?,?)",
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

    public static int getTransactionCount(String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM transactions WHERE account_number = ?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            rs.close();
            ps.close();
            return count;
        } catch (Exception e) {
            return 0;
        }
    }
}
