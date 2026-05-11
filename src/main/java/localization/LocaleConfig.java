import java.text.SimpleDateFormat;
import java.util.Date;
public class LocaleConfig {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat SHORT_DATE = new SimpleDateFormat("MMM dd, yyyy");
    private static final SimpleDateFormat TIME_ONLY = new SimpleDateFormat("HH:mm");
    public static String formatDateTime(Date date) {
        return DATE_FORMAT.format(date);
    }
    public static String formatShortDate(Date date) {
        return SHORT_DATE.format(date);
    }
    public static String formatTime(Date date) {
        return TIME_ONLY.format(date);
    }
    public static String formatTimestamp(java.sql.Timestamp ts) {
        if (ts == null) return "";
        return DATE_FORMAT.format(ts);
    }
    public static SimpleDateFormat getDateFormat() {
        return DATE_FORMAT;
    }
}