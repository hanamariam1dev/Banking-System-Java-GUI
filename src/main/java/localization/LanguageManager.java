import java.util.HashMap;
import java.util.Map;
public class LanguageManager {
    private static final Map<String, String> AM = new HashMap<>();
    private static boolean isAmharic = false;
    static {
        AM.put("Commercial Bank of Ethiopia", "የኢትዮጵያ ንግድ ባንክ");
        AM.put("Your Trusted Financial Partner", "የሚታመን የፋይናንስ አጋርዎ");
        AM.put("Welcome Back", "እንኳን ደህና መጡ");
        AM.put("Sign in to your account", "ወደ መለያዎ ይግቡ");
        AM.put("Username", "የተጠቃሚ ስም");
        AM.put("Enter your username", "የተጠቃሚ ስምዎን ያስገቡ");
        AM.put("Password", "የይለፍ ቃል");
        AM.put("Sign In", "ግባ");
        AM.put("Don't have an account?", "መለያ የለዎትም?");
        AM.put("Open Account", "መለያ ይክፈቱ");
        AM.put("Open New Account", "አዲስ መለያ ይክፈቱ");
        AM.put("Full Name", "ሙሉ ስም");
        AM.put("Email", "ኢሜይል");
        AM.put("Phone", "ስልክ");
        AM.put("Account Type", "የመለያ አይነት");
        AM.put("Confirm Password", "የይለፍ ቃል ያረጋግጡ");
        AM.put("Create Account", "መለያ ፍጠር");
        AM.put("Fill all required fields.", "ሁሉንንም መስኮችን ይሙሉ።");
        AM.put("Passwords do not match.", "የይለፍ ቃሎች አይዛመዱም።");
        AM.put("Account Created!", "መለያ ተፈጥሯል!");
        AM.put("Show Password", "የይለፍ ቃል ንግር");
        AM.put("Invalid email address.", "የኢሜይል አድራሻ አልተሟላም።");
        AM.put("Invalid phone number.", "የስልክ ቁጥር አልተሟላም።");
        AM.put("Account Number", "የመለያ ቁጥር");
        AM.put("Holder", "ባለቤት");
        AM.put("Username already exists.", "የተጠቃሚ ስሙን አስቀድሞ አለ።");
        AM.put("Welcome", "እንኳን ደህና መጡ");
        AM.put("Available Balance", "ያለው ቀሪ ዓሳብ");
        AM.put("Quick Actions", "ፈጣን እርምጃዎች");
        AM.put("Recent Transactions", "የቅርብ ጊዜ ግብይቶች");
        AM.put("Total Deposits", "አጠቃላይ ክፍያዎች");
        AM.put("Total Withdrawals", "አጠቃላይ ወጪዎች");
        AM.put("Transactions", "ግብይቶች");
        AM.put("Last Activity", "የመጨረሻ እንቅስቃሴ");
        AM.put("No activity yet", "እስካሁን እንቅስቃሴ የለም");
        AM.put("No transactions yet.", "እስካሁን ድረስ ግብይቶች የሉም።");
        AM.put("See all transactions", "ሁሉንም ግብይቶች ይመልከቱ");
        AM.put("Secure Banking Platform", "የደህንነት የባንክ መስመር");
        AM.put("Transfer Out", "ወጪ ማስተላለፊያ");
        AM.put("Transfer In", "ገቢ ማስተላለፊያ");
        AM.put("Deposit", "ተቀማጭ");
        AM.put("Withdrawal", "ወጪ");
        AM.put("History", "ታሪክ");
        AM.put("Add funds", "ገንዘብ ያስቀምጡ");
        AM.put("Withdraw", "ወጪ");
        AM.put("Transfer", "ዝውውር");
        AM.put("Send money", "ገንዘብ ይላኩ");
        AM.put("Statement", "ዓሳብ ዝርዝር");
        AM.put("Profile", "መገለጫ");
        AM.put("Change PIN", "ፒን ቀይር");
        AM.put("QR Code", "QR ኮድ");
        AM.put("FAQ", "ተደጋጋሚ ጥያቄዎች");
        AM.put("Log Out", "ውጣ");
        AM.put("Home", "ዋና ገጽ");
        AM.put("Admin Dashboard", "የአድሚን ዳሽቦርድ");
        AM.put("Block Account", "መለያ አግድ");
        AM.put("Unblock Account", "መለያ አራግፍ");
        AM.put("Delete Account", "መለያ ሰርዝ");
        AM.put("Role", "ሚና");
        AM.put("Actions", "እርምጃዎች");
        AM.put("Block", "አግድ");
        AM.put("Unblock", "አራግፍ");
        AM.put("Delete", "ሰርዝ");
        AM.put("Customer", "ደንበኛ");
        AM.put("Admin", "አስተዳዳሪ");
        AM.put("Total Users", "አጠቃላይ ተጠቃሚዎች");
        AM.put("Active Users", "ንቁ ተጠቃሚዎች");
        AM.put("Blocked Users", "የተከለሉ ተጠቃሚዎች");
        AM.put("Total Transactions", "አጠቃላይ ግብይቶች");
        AM.put("Today", "ዛሬ");
        AM.put("This Month", "የዚህ ወር");
        AM.put("Dashboard", "ዳሽቦርድ");
        AM.put("Users Management", "ተጠቃሚዎች");
        AM.put("Transactions Management", "ግብይቶች");
    }
    public static String t(String key) {
        if (isAmharic && AM.containsKey(key)) {
            return AM.get(key);
        }
        return key;
    }
    public static void setAmharic(boolean amharic) {
        isAmharic = amharic;
    }
    public static boolean isAmharic() {
        return isAmharic;
    }
}