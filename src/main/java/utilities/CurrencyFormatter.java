import java.text.DecimalFormat;
public class CurrencyFormatter {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.00");
    public static String format(double value) {
        return "ETB " + FORMAT.format(value);
    }
    public static String formatNumber(double value) {
        return FORMAT.format(value);
    }
    public static String generateAccountNumber() {
        return "CBE" + (1000000000L + (long)(Math.random() * 9000000000L));
    }
}