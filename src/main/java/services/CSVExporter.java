
import java.io.*;
import java.sql.*;

public class CSVExporter {

    public static boolean exportTransactions(String filePath, String accountNumber) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            FileWriter writer = new FileWriter(filePath);
            writer.write("Transaction ID,Account Number,Type,Amount,Balance After,Recipient,Description,Date\n");
            while (rs.next()) {
                writer.write(rs.getInt("transaction_id") + ",");
                writer.write(escapeCSV(rs.getString("account_number")) + ",");
                writer.write(rs.getString("transaction_type") + ",");
                writer.write(rs.getDouble("amount") + ",");
                writer.write(rs.getDouble("balance_after") + ",");
                writer.write(escapeCSV(rs.getString("recipient_account")) + ",");
                writer.write(escapeCSV(rs.getString("description")) + ",");
                writer.write(rs.getTimestamp("transaction_date") + "\n");
            }
            writer.close();
            rs.close();
            ps.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportAllTransactions(String filePath) {
        try {
            Connection con = ConnectionManager.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM transactions ORDER BY transaction_date DESC");
            FileWriter writer = new FileWriter(filePath);
            writer.write("Transaction ID,Account Number,Type,Amount,Balance After,Recipient,Description,Date\n");
            while (rs.next()) {
                writer.write(rs.getInt("transaction_id") + ",");
                writer.write(escapeCSV(rs.getString("account_number")) + ",");
                writer.write(rs.getString("transaction_type") + ",");
                writer.write(rs.getDouble("amount") + ",");
                writer.write(rs.getDouble("balance_after") + ",");
                writer.write(escapeCSV(rs.getString("recipient_account")) + ",");
                writer.write(escapeCSV(rs.getString("description")) + ",");
                writer.write(rs.getTimestamp("transaction_date") + "\n");
            }
            writer.close();
            rs.close();
            st.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
