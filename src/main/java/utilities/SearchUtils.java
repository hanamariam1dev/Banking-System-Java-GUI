import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SearchUtils {
    public static List<SearchResult> searchTransactions(String query, String accountNumber) {
        List<SearchResult> results = new ArrayList<>();
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM transactions WHERE account_number = ? " +
                "AND (description LIKE ? OR transaction_type LIKE ? OR CAST(transaction_id AS CHAR) LIKE ?)" +
                "ORDER BY transaction_date DESC LIMIT 50");
            ps.setString(1, accountNumber);
            String pattern = "%" + query + "%";
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new SearchResult(
                    "Transaction",
                    rs.getString("transaction_type") + " - " + rs.getString("description"),
                    rs.getTimestamp("transaction_date").toString()
                ));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
    public static List<SearchResult> searchUsers(String query) {
        List<SearchResult> results = new ArrayList<>();
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT account_number, account_holder_name FROM accounts " +
                "WHERE (account_holder_name LIKE ? OR account_number LIKE ? OR username LIKE ?) " +
                "AND role = 'Customer' LIMIT 50");
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new SearchResult(
                    "User",
                    rs.getString("account_holder_name") + " (" + rs.getString("account_number") + ")",
                    ""
                ));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
    public static class SearchResult {
        public final String type;
        public final String title;
        public final String subtitle;
        public SearchResult(String type, String title, String subtitle) {
            this.type = type;
            this.title = title;
            this.subtitle = subtitle;
        }
    }
}