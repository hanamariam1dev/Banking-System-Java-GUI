public class DBConfig {
    public static final String HOST     = "localhost";
    public static final String PORT     = "3306";
    public static final String DB_NAME  = "banking_system1";
    public static final String USER     = "root";
    public static final String PASSWORD = "";
    public static final String OPTS =
        "useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" +
        "&useUnicode=true&characterEncoding=UTF-8" +
        "&autoReconnect=true&connectTimeout=5000";
    public static String getConnectionUrl() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?" + OPTS;
    }
    public static String getServerUrl() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/?" + OPTS;
    }
}