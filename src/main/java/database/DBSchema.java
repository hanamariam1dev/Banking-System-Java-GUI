import java.sql.*;
public class DBSchema {
    public static void createTables(Connection con) throws SQLException {
        Statement st = con.createStatement();
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS accounts (" +
            "  account_id          INT AUTO_INCREMENT PRIMARY KEY," +
            "  account_number      VARCHAR(20)  UNIQUE NOT NULL," +
            "  account_holder_name VARCHAR(100) NOT NULL," +
            "  email               VARCHAR(100)," +
            "  phone               VARCHAR(15)," +
            "  username            VARCHAR(50)  UNIQUE NOT NULL," +
            "  password            VARCHAR(100) NOT NULL," +
            "  account_type        ENUM('Savings','Current','Fixed Deposit') DEFAULT 'Savings'," +
            "  balance             DECIMAL(15,2) DEFAULT 0.00," +
            "  status              ENUM('Active','Inactive','Frozen') DEFAULT 'Active'," +
            "  role                ENUM('Customer','Admin') DEFAULT 'Customer'," +
            "  profile_image       VARCHAR(255)," +
            "  created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
            ")");
        st.executeUpdate("ALTER TABLE accounts MODIFY password VARCHAR(255) NOT NULL");
        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS transactions (" +
            "  transaction_id    INT AUTO_INCREMENT PRIMARY KEY," +
            "  account_number  VARCHAR(20) NOT NULL," +
            "  transaction_type  ENUM('Deposit','Withdrawal','Transfer Out','Transfer In') NOT NULL," +
            "  amount            DECIMAL(15,2) NOT NULL," +
            "  balance_after     DECIMAL(15,2) NOT NULL," +
            "  recipient_account VARCHAR(20)," +
            "  description       VARCHAR(255)," +
            "  transaction_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")");
        st.close();
    }
}