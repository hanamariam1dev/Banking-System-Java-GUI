import java.util.Calendar;
public class GreetingGenerator {
    public static String getGreeting() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = LanguageManager.t("Good Morning");
        } else if (hour >= 12 && hour < 17) {
            greeting = LanguageManager.t("Good Afternoon");
        } else if (hour >= 17 && hour < 21) {
            greeting = LanguageManager.t("Good Evening");
        } else {
            greeting = LanguageManager.t("Good Night");
        }
        return greeting;
    }
    public static String getGreetingWithName(String name) {
        return getGreeting() + ", " + name;
    }
}