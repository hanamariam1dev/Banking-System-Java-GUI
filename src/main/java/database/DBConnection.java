import java.sql.*;
import javax.swing.*;
public class DBConnection {
    private static Connection con;
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "banking_system1";
    private static final String USER     = "root";
    private static final String PASSWORD = "";
    private static final String OPTS =
        "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" +
        "&useUnicode=true&characterEncoding=UTF-8" +
        "&autoReconnect=true&connectTimeout=5000";
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded OK");
            Connection temp = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/?" + OPTS, USER, PASSWORD);
            System.out.println("Connected to MySQL/MariaDB server OK");
            Statement st = temp.createStatement();
            st.executeUpdate(
                "CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "`" +
                " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            st.close();
            temp.close();
            System.out.println("Database '" + DB_NAME + "' ready");
            con = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?" + OPTS,
                USER, PASSWORD);
            Statement st2 = con.createStatement();
            st2.executeUpdate(
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
            st2.executeUpdate("ALTER TABLE accounts MODIFY password VARCHAR(255) NOT NULL");
            System.out.println("Table 'accounts' ready");
            st2.executeUpdate(
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "  transaction_id    INT AUTO_INCREMENT PRIMARY KEY," +
                "  account_number    VARCHAR(20) NOT NULL," +
                "  transaction_type  ENUM('Deposit','Withdrawal','Transfer Out','Transfer In') NOT NULL," +
                "  amount            DECIMAL(15,2) NOT NULL," +
                "  balance_after     DECIMAL(15,2) NOT NULL," +
                "  recipient_account VARCHAR(20)," +
                "  description       VARCHAR(255)," +
                "  transaction_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
            System.out.println("Table 'transactions' ready");
            ResultSet seedCheck = st2.executeQuery("SELECT COUNT(*) FROM accounts");
            boolean isFirstRun = false;
            if (seedCheck.next()) isFirstRun = seedCheck.getInt(1) == 0;
            seedCheck.close();
            if (isFirstRun) {
                st2.executeUpdate(
                    "INSERT INTO accounts " +
                    "(account_number,account_holder_name,email,phone,username,password,account_type,balance,role)" +
                    " VALUES ('CBE1234567890','Admin User','admin@cbe.com','0911000000'," +
                    "'admin','" + BankingConfig.hashPassword("Adm1n!") + "','Savings',0.00,'Admin')");
            }
            st2.close();
            System.out.println("=== Database setup complete! ===");
            return con;
        } catch (ClassNotFoundException e) {
            String msg = "MySQL Driver not found!\n\nMake sure lib/mysql-connector-j-9.6.0.jar exists.";
            System.err.println(msg);
            JOptionPane.showMessageDialog(null, msg, "Driver Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException e) {
            String msg;
            if (e.getMessage().contains("Connection refused")
                    || e.getMessage().contains("Communications link failure")
                    || e.getErrorCode() == 0) {
                msg = "Cannot connect to MySQL!\n\n" +
                      "Fix: Open XAMPP and click START next to MySQL.\n\n" +
                      "Detail: " + e.getMessage();
            } else if (e.getErrorCode() == 1045) {
                msg = "Wrong MySQL username or password!\n\n" +
                      "Open DBConnection.java and set the correct PASSWORD.";
            } else {
                msg = "Database Error (code " + e.getErrorCode() + "):\n" + e.getMessage();
            }
            System.err.println(msg);
            JOptionPane.showMessageDialog(null, msg, "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public static Connection getConnection() { return con; }
}