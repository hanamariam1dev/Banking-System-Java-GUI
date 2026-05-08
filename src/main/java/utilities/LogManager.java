import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class LogManager {
    private static final String LOG_FILE = "logs/app.log";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void log(String level, String message) {
        String timestamp = DATE_FORMAT.format(new Date());
        String logEntry = "[" + timestamp + "] [" + level + "] " + message;
        System.out.println(logEntry);
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) logDir.mkdirs();
            FileWriter writer = new FileWriter(LOG_FILE, true);
            writer.write(logEntry + "\n");
            writer.close();
        } catch (Exception ignored) {}
    }
    public static void info(String message) {
        log("INFO", message);
    }
    public static void error(String message) {
        log("ERROR", message);
    }
    public static void warn(String message) {
        log("WARN", message);
    }
    public static void debug(String message) {
        log("DEBUG", message);
    }
}