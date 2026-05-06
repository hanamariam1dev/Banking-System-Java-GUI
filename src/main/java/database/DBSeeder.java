import java.sql.*;
public class DBSeeder {
    public static void seedIfEmpty(Connection con) throws SQLException {
        Statement st = con.createStatement();
        ResultSet seedCheck = st.executeQuery("SELECT COUNT(*) FROM accounts");
        boolean isFirstRun = false;
        if (seedCheck.next()) isFirstRun = seedCheck.getInt(1) == 0;
        seedCheck.close();
        if (isFirstRun) {
            if (!userExists("admin")) {
                st.executeUpdate(
                    "INSERT INTO accounts " +
                    "(account_number,account_holder_name,email,phone,username,password,account_type,balance,role)" +
                    " VALUES ('CBE1234567890','Admin User','admin@cbe.com','0911000000'," +
                    "'admin','" + PasswordService.hashPassword("Adm1n!") + "','Savings',10000.00,'Admin')");
            }
            if (!userExists("customer")) {
                st.executeUpdate(
                    "INSERT INTO accounts " +
                    "(account_number,account_holder_name,email,phone,username,password,account_type,balance,role)" +
                    " VALUES ('CBE9876543210','Test Customer','test@cbe.com','0922000000'," +
                    "'test','" + PasswordService.hashPassword("Test1!") + "','Savings',50000.00,'Customer')");
            }
            seedTransactions();
            st.executeUpdate(
                "INSERT INTO transactions " +
                "(account_number,transaction_type,amount,balance_after,recipient_account,description)" +
                " VALUES ('CBE9876543210','Deposit',10000.00,10000.00,NULL,'Initial Deposit')");
            st.executeUpdate(
                "INSERT INTO transactions " +
                "(account_number,transaction_type,amount,balance_after,recipient_account,description)" +
                " VALUES ('CBE9876543210','Deposit',25000.00,35000.00,NULL,'Salary Deposit')");
            st.executeUpdate(
                "INSERT INTO transactions " +
                "(account_number,transaction_type,amount,balance_after,recipient_account,description)" +
                " VALUES ('CBE9876543210','Withdrawal',5000.00,30000.00,NULL,'Cash Withdrawal')");
            st.executeUpdate(
                "INSERT INTO transactions " +
                "(account_number,transaction_type,amount,balance_after,recipient_account,description)" +
                " VALUES ('CBE9876543210','Transfer Out',3000.00,27000.00,'CBE1234567890','Transfer to Admin User')");
            st.executeUpdate(
                "INSERT INTO transactions " +
                "(account_number,transaction_type,amount,balance_after,recipient_account,description)" +
                " VALUES ('CBE1234567890','Transfer In',3000.00,13000.00,'CBE9876543210','Transfer from Test Customer')");
            st.executeUpdate(
                "INSERT INTO transactions " +
                "(account_number,transaction_type,amount,balance_after,recipient_account,description)" +
                " VALUES ('CBE9876543210','Deposit',23000.00,50000.00,NULL,'Bonus Deposit')");
        }
        st.close();
    }
}