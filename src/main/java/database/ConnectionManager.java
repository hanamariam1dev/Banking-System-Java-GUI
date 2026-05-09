import java.sql.*;
public class ConnectionManager {
    private static Connection connection;
    public static void setConnection(Connection con) {
        connection = con;
    }
    public static Connection getConnection() {
        return connection;
    }
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }
}