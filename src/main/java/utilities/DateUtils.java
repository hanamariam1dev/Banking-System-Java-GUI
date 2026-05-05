import java.text.SimpleDateFormat;
import java.util.*;
public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }
    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }
    public static Date getStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    public static Date getEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar check = Calendar.getInstance();
        check.setTime(date);
        return today.get(Calendar.YEAR) == check.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == check.get(Calendar.DAY_OF_YEAR);
    }
    public static String getRelativeTime(Date date) {
        long diff = System.currentTimeMillis() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        if (seconds < 60) return "Just now";
        if (minutes < 60) return minutes + " min ago";
        if (hours < 24) return hours + " hours ago";
        if (days < 7) return days + " days ago";
        return formatDate(date);
    }
}