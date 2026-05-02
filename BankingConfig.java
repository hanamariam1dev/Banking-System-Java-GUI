import java.awt.*;
import java.io.*;
import java.security.SecureRandom;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
public class BankingConfig {
    static Connection con;
    static boolean darkMode = false;
    static boolean isAmharic = false;
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Color PRIMARY       = new Color(0,  100, 50);
    static Color PRIMARY_LIGHT = new Color(0,  150, 75);
    static Color PRIMARY_DARK  = new Color(0,  60, 30);
    static Color PRIMARY_SOFT  = new Color(230, 246, 238);
    static Color GOLD      = new Color(220, 155, 30);
    static Color GOLD_SOFT = new Color(255, 248, 220);
    static Color SUCCESS = new Color(0,  160, 70);
    static Color DANGER  = new Color(220, 50,  50);
    static Color WARNING = new Color(230, 120, 10);
    static Color INFO    = new Color(40,  130, 220);
    static Color BG      = new Color(245, 248, 246);
    static Color CARD_BG = new Color(255, 255, 255);
    static Color FG      = new Color(10,  25, 15);
    static Color MUTED   = new Color(80, 100, 85);
    static Color BORDER  = new Color(210, 230, 218);
    static Color DIVIDER = new Color(225, 240, 232);
    static Color SIDEBAR_BG     = new Color(8,  45, 25);
    static Color SIDEBAR_ACTIVE = new Color(0, 120, 60);
    static Color SIDEBAR_TEXT   = new Color(190, 235, 210);
    static Color SIDEBAR_ICON   = new Color(20, 210, 110);
    static Color PURPLE = PRIMARY;
    static Color PURPLE_LIGHT = PRIMARY_LIGHT;
    static Color PURPLE_DARK = PRIMARY_DARK;
    static Color PURPLE_SOFT = PRIMARY_SOFT;
    static Color ACCENT = GOLD;
    static Map<String, String> AM = new HashMap<>();
    static {
        AM.put("Commercial Bank of Ethiopia", "የኢትዮጵያ ንግድ ባንክ");
        AM.put("Your Trusted Financial Partner", "የሚታመን የፋይናንስ አጋርዎ");
        AM.put("Welcome Back", "እንኳን ደህና መጡ");
        AM.put("Sign in to your account", "ወደ መለያዎ ይግቡ");
        AM.put("Username", "የተጠቃሚ ስም");
        AM.put("Enter your username", "የተጠቃሚ ስምዎን ያስገቡ");
        AM.put("Password", "የይለፍ ቃል");
        AM.put("Sign In \u2192", "ግባ →");
        AM.put("Don't have an account? ", "መለያ የለዎትም? ");
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
        AM.put("Password must be at least 6 characters.", "የይለፍ ቃል ቢያንስ 6 ቁምፊ መሆን አለበት።");
        AM.put("Account Created!", "መለያ ተፈጥሯል!");
        AM.put("Show Password", "የይለፍ ቃል ንግር");
        AM.put("Invalid email address.", "የኢሜይል አድራሻ አልተሟላም።");
        AM.put("Invalid phone number.", "የስልክ ቁጥር አልተሟላም።");
        AM.put("Account Number", "የመለያ ቁጥር");
        AM.put("Holder", "ባለቤት");
        AM.put("Username already exists.", "የተጠቃሚ ስሙን አስቀድሞ አለ።");
        AM.put("Welcome, ", "እንኳን ደህና መጡ, ");
        AM.put("Available Balance", "ያለው ቀሪ ዓሳብ");
        AM.put("Quick Actions", "ፈጣን እርምጃዎች");
        AM.put("Recent Transactions", "የቅርብ ጊዜ ግብይቶች");
        AM.put("Total Deposits", "አጠቃላይ ክፍያዎች");
        AM.put("Total Withdrawals", "አጠቃላይ ወጪዎች");
        AM.put("Transactions", "ግብይቶች");
        AM.put("Last Activity", "የመጨረሻ እንቅስቃሴ");
        AM.put("No activity yet", "እስካሁን እንቅስቃሴ የለም");
        AM.put("No transactions yet.", "እስካሁን ድረስ ግብይቶች የሉም።");
        AM.put("See all transactions →", "ሁሉንም ግብይቶች ይመልከቱ →");
        AM.put("Secure Banking Platform", "የደህንነት የባንክ መስመር");
        AM.put("Transaction Receipt", "የግብይት ደረሰኝ");
        AM.put("Transaction ID", "የግብይት መለያ");
        AM.put("Transaction Type", "የግብይት ዓይነት");
        AM.put("Recipient Account", "የተቀባዩ መለያ");
        AM.put("Export CSV succeeded", "CSV ማውጣት ተሳካ");
        AM.put("Export CSV failed", "CSV ማውጣት አልተሳካም");
        AM.put("Click download button to get receipt", "ደረሰኝ ለማግኘት የማውረድ ቁልፍን ይጫኑ");
        AM.put("Receipt downloaded successfully", "ደረሰኝ በተሳካ ሁኔታ ወርዷል");
        AM.put("Receipt download failed", "ደረሰኝ ማውረድ አልተሳካም");
        AM.put("Download", "አውርድ");
        AM.put("Active", "ንቁ");
        AM.put("Search...", "ፈልግ...");
        AM.put("Required", "ያስፈልጋል");
        AM.put("All", "ሁሉም");
        AM.put("Transfer Out", "ወጪ ማስተላለፊያ");
        AM.put("Transfer In",  "ገቢ ማስተላለፊያ");
        AM.put("Deposit",      "ተቀማጥ");
        AM.put("Withdrawal",   "ወጪ");
        AM.put("History",      "ታሪክ");
        AM.put("Transactions This Month", "የዚህ ወር ግብይቶች");
        AM.put("Total Deposited",         "አጠቃላይ ተቀማጥ");
        AM.put("Total Withdrawn",         "አጠቃላይ ወጪ");
        AM.put("Cash Deposit",            "የጥሬ ገንዘብ ተቀማጥ");
        AM.put("Cash Withdrawal",         "የጥሬ ገንዘብ ወጪ");
        AM.put("Initial Deposit",         "የመጀመሪያ ተቀማጥ");
        AM.put("Salary Deposit",          "የደሞዝ ተቀማጥ");
        AM.put("Bonus Deposit",           "የጉርሻ ተቀማጥ");
        AM.put("Transfer to ",            "ዝውውር ወደ ");
        AM.put("Transfer from ",          "ዝውውር ከ ");
        AM.put("Date",                    "ቀን");
        AM.put("Back",                    "ተመለስ");
        AM.put("Transfer Money",                        "ገንዘብ ዝውውር");
        AM.put("Choose a transfer type",                "የዝውውር አይነት ይምረጡ");
        AM.put("CBE Account",                           "CBE መለያ");
        AM.put("Transfer to any CBE bank account",      "ወደ ማንኛውም CBE ባንክ መለያ ያዛውሩ");
        AM.put("Own Account",                           "የራስ መለያ");
        AM.put("Move money between your own CBE accounts", "በራስዎ CBE መለያዎች መካከል ያዛውሩ");
        AM.put("CBE Agent",                             "CBE ወኪል");
        AM.put("Transfer via CBE agent network",        "በCBE ወኪል አውታር ያዛውሩ");
        AM.put("CBE Wallet",                            "CBE ዋሌት");
        AM.put("Send to CBE mobile wallet",             "ወደ CBE ሞባይል ዋሌት ይላኩ");
        AM.put("Other Wallet",                          "ሌላ ዋሌት");
        AM.put("Telebirr, M-Pesa and other wallets",    "ቴሌብር፣ ኤም-ፔሳ እና ሌሎች ዋሌቶች");
        AM.put("Other Bank",                            "ሌላ ባንክ");
        AM.put("Transfer to Awash, Dashen, Abyssinia...","ወደ አዋሽ፣ ዳሽን፣ አቢሲኒያ... ያዛውሩ");
        AM.put("Transfer to CBE Account",               "ወደ CBE መለያ ዝውውር");
        AM.put("Transfer to Own Account",               "ወደ ራስ መለያ ዝውውር");
        AM.put("Transfer via CBE Agent",                "በCBE ወኪል ዝውውር");
        AM.put("Transfer to CBE Wallet",                "ወደ CBE ዋሌት ዝውውር");
        AM.put("Transfer to Other Wallet",              "ወደ ሌላ ዋሌት ዝውውር");
        AM.put("Transfer to Other Bank",                "ወደ ሌላ ባንክ ዝውውር");
        AM.put("Enter recipient CBE account number",    "የተቀባዩን CBE መለያ ቁጥር ያስገቡ");
        AM.put("Enter your other CBE account number",   "ሌላ CBE መለያ ቁጥርዎን ያስገቡ");
        AM.put("Enter CBE agent account number",        "የCBE ወኪል መለያ ቁጥር ያስገቡ");
        AM.put("Enter CBE wallet phone number",         "የCBE ዋሌት ስልክ ቁጥር ያስገቡ");
        AM.put("Enter wallet phone number",             "የዋሌት ስልክ ቁጥር ያስገቡ");
        AM.put("Enter recipient account number",        "የተቀባዩን መለያ ቁጥር ያስገቡ");
        AM.put("Select Bank",                           "ባንክ ይምረጡ");
        AM.put("Select Wallet",                         "ዋሌት ይምረጡ");
        AM.put("Fill in the details below",             "ዝርዝሮቹን ከዚህ በታች ይሙሉ");
        AM.put("Password must be at least 6 characters and include uppercase, lowercase, and a number.",
               "የይለፍ ቃሉ ቢያንስ 6 ቁምፊ ሆኖ ትልቅ ፊደል፣ ትንሽ ፊደል እና ቁጥር ሊኖረው ይገባል።");
        AM.put("How do I transfer money?",
               "ገንዘብ እንዴት አዛውራለሁ?");
        AM.put("How do I change my password?",
               "የይለፍ ቃሌን እንዴት እቀይራለሁ?");
        AM.put("Click 'Deposit' in the sidebar or Quick Actions. Enter the amount and confirm with your password.",
               "'ተቀማጥ' በጎን አሞሌ ወይም ፈጣን አማራጮች ላይ ይጫኑ። መጠኑን ያስገቡ እና በይለፍ ቃልዎ ያረጋግጡ።");
        AM.put("Click 'Withdraw', enter the amount (must not exceed your balance) and confirm with your password.",
               "'ወጪ' ይጫኑ፣ መጠኑን ያስገቡ (ካለው ቀሪ ሂሳብ መብለጥ የለበትም) እና በይለፍ ቃልዎ ያረጋግጡ።");
        AM.put("Click 'Transfer', choose the transfer type, enter the recipient account and amount, then confirm.",
               "'ዝውውር' ይጫኑ፣ የዝውውር አይነት ይምረጡ፣ የተቀባዩን መለያ እና መጠን ያስገቡ፣ ከዚያ ያረጋግጡ።");
        AM.put("Click 'History' in the sidebar or Quick Actions to see all past transactions.",
               "ያለፉ ግብይቶችን ሁሉ ለማየት 'ታሪክ' በጎን አሞሌ ወይም ፈጣን አማራጮች ላይ ይጫኑ።");
        AM.put("Go to TOOLS > Change PIN. Enter your current password, then your new password twice.",
               "መሳሪያዎች > ፒን ቀይር ይሂዱ። አሁን ያለዎትን የይለፍ ቃል ያስገቡ፣ ከዚያ አዲሱን ሁለት ጊዜ።");
        AM.put("Your QR code encodes your account number. Others can scan it to send money to you.",
               "QR ኮድዎ የመለያ ቁጥርዎን ያካትታል። ሌሎች ቃኝተው ወደ እርስዎ ገንዘብ ሊልኩ ይችላሉ።");
        AM.put("Yes. Passwords are stored using PBKDF2 hashing. Every transaction requires password confirmation.",
               "አዎ። የይለፍ ቃሎች PBKDF2 ሃሺንግ ተጠቅሞ ይቀመጣሉ። እያንዳንዱ ግብይት የይለፍ ቃል ማረጋገጫ ይፈልጋል።");
        AM.put("Add funds", "ገንዘብ ያስቀምጡ");
        AM.put("Withdraw", "ወጪ");
        AM.put("Cash out", "ገንዘብ ያውጡ");
        AM.put("Transfer", "ዝውውር");
        AM.put("Send money", "ገንዘብ ይላኩ");
        AM.put("Statement", "ዓሳብ ዝርዝር");
        AM.put("View history", "ታሪክ ይመልከቱ");
        AM.put("Profile", "መገለጫ");
        AM.put("Account details", "የመለያ ዝርዝር");
        AM.put("Change PIN", "ፒን ቀይር");
        AM.put("Update password", "የይለፍ ቃል ቀይር");
        AM.put("QR Code", "QR ኮድ");
        AM.put("Scan to pay", "ለመክፈል ይቃኙ");
        AM.put("FAQ", "ተደጋጋሚ ጥያቄዎች");
        AM.put("Refresh", "እንደገና አስጀምር");
        AM.put("Export CSV", "CSV ላክ");
        AM.put("Copy Account Number", "የመለያ ቁጥር ኮፒ አድርግ");
        AM.put("Account number copied to clipboard!", "የመለያ ቁጥር ወደ ክሊፕቦርድ ተቀምጧል!");
        AM.put("Copy failed.", "ኮፒ አልተሳካም።");
        AM.put("Refreshed", "ዳታው ተሻሽሏል");
        AM.put("Help & support", "እርዳታ");
        AM.put("Log Out", "ውጣ");
        AM.put("Sign out", "ይውጡ");
        AM.put("Home", "ዋና ገጽ");
        AM.put("Confirm Transaction", "ግብይቱን ያረጋግጡ");
        AM.put("Enter your password to confirm:", "ለማረጋገጥ የይለፍ ቃልዎን ያስገቡ:");
        AM.put("Password cannot be empty.", "የይለፍ ቃል ባዶ መሆን አይሻልም።");
        AM.put("Incorrect password. Transaction cancelled.", "ስህተት የይለፍ ቃል። ግብይቱ ተሰርቷል።");
        AM.put("Failed", "አልተሳካም");
        AM.put("Amount must be > 0", "መጠኑ ከ0 በላይ መሆን አለበት");
        AM.put("Deposit Successful!", "ተቀማጥ ተሳክቷል!");
        AM.put("Amount", "መጠኑ");
        AM.put("Balance", "ቀሪ");
        AM.put("Invalid amount.", "ትክክለኛ ያልሆነ መጠኑ።");
        AM.put("Insufficient balance!\nBalance: ETB %.2f", "በቃ ዓሳብ የለም!\nቀሪ: ETB %.2f");
        AM.put("Withdrawal Successful!", "ወጪ ተሳክቷል!");
        AM.put("Fill all fields.", "ሁሉንንም ይሙሉ።");
        AM.put("Cannot transfer to same account.", "ወደ ተመሳሳይ መለያ ማዛወር አይቻልም።");
        AM.put("Recipient not found.", "ተቀባዩ አልተገኘም።");
        AM.put("Transfer Successful!", "ዝውውር ተሳክቷል!");
        AM.put("To", "ወደ");
        AM.put("Transfer Error: ", "የዝውውር ስህተት: ");
        AM.put("Current Password:", "አሁን ያለ የይለፍ ቃል:");
        AM.put("New Password:", "አዲስ የይለፍ ቃል:");
        AM.put("Confirm New:", "አዲሱን ያረጋግጡ:");
        AM.put("Change Password", "የይለፍ ቃል ቀይር");
        AM.put("New passwords do not match.", "አዲሱ የይለፍ ቃሎች አይዛመዱም።");
        AM.put("Password changed successfully!", "የይለፍ ቃል በተሳካ ሁኔታ ተቀይሯል!");
        AM.put("Current password is incorrect.", "አሁን ያለው የይለፍ ቃል ትክክል አይደለም።");
        AM.put("Your Account QR Code", "የመለያዎ QR ኮድ");
        AM.put("Scan to send money to this account", "ወደዚህ መለያ ለመላክ ይቃኙ");
        AM.put("Frequently Asked Questions", "ተደጋጋሚ ጥያቄዎች");
        AM.put("Transaction History", "የግብይቱ ታሪክ");
        AM.put("Date & Time", "ቀን እና ሰዓት");
        AM.put("Type", "አይነት");
        AM.put("Amount (ETB)", "መጠኑ (ብር)");
        AM.put("Balance After", "ቀሪ ዓሳብ");
        AM.put("Description", "መግለጫ");
        AM.put("Account Profile", "የመለያ መገለጫ");
        AM.put("Account Holder", "ባለቤት");
        AM.put("Status", "ሁነታ");
        AM.put("Opened", "የተከፈተበት");
        AM.put("Please enter username and password.", "እባክዎ ስም እና የይለፍ ቃል ያስገቡ።");
        AM.put("Database not connected!", "ዳታቤዝ አልተገናኘም!");
        AM.put("Invalid username or password.", "ስም ወይም የይለፍ ቃል ትክክል አይደለም።");
        AM.put("Login Failed", "መግቢያ አልተሳካም");
        AM.put("Download Receipt", "ደረሰኝ ያውርዱ");
        AM.put("Update Profile", "መገለጫ ቀይር");
        AM.put("Select Profile Image", "የመገለጫ ምስል ይምረጡ");
        AM.put("Upload", "አስገባ");
        AM.put("Admin Dashboard", "የአድሚን ዳሽቦርድ");
        AM.put("Block Account", "መለያ አግድ");
        AM.put("Unblock Account", "መለያ አራግፍ");
        AM.put("Delete Account", "መለያ ሰርዝ");
        AM.put("Role", "ሚና");
        AM.put("Actions", "እርምጃዎች");
        AM.put("Block", "አግድ");
        AM.put("Unblock", "አራግፍ");
        AM.put("Delete", "ሰርዝ");
        AM.put("Confirm Delete", "ስርዝ ያረጋግጡ");
        AM.put("Are you sure you want to delete this account?", "ይህን መለያ መሰረዝ እንደሚፈልጉ እርግጠኛ ነዎት?");
        AM.put("Account deleted successfully.", "መለያ ተሰርዟል።");
        AM.put("Account blocked.", "መለያ ተከለለ።");
        AM.put("Account unblocked.", "መለያ ተራገፈ።");
        AM.put("Total Users", "አጠቃላይ ተጠቃሚዎች");
        AM.put("Active Users", "ንቁ ተጠቃሚዎች");
        AM.put("Blocked Users", "የተከለሉ ተጠቃሚዎች");
        AM.put("Total Transactions", "አጠቃላይ ግብይቶች");
        AM.put("Today", "ዛሬ");
        AM.put("This Month", "የዚህ ወር");
        AM.put("Dashboard", "ዳሽቦርድ");
        AM.put("Users Management", "ተጠቃሚዎች");
        AM.put("Transactions Management", "ግብይቶች");
        AM.put("Activity Monitor", "እንቅስቃሴ");
        AM.put("Frozen", "የታገደ");
        AM.put("Inactive", "ንቁ ያልሆነ");
        AM.put("Savings", "ቁጠባ");
        AM.put("Current", "ቀላጤ");
        AM.put("Fixed Deposit", "የተቆለፈ ቁጠባ");
        AM.put("Download", "አውርድ");
        AM.put("Customer", "ደንበኛ");
        AM.put("Admin", "አስተዳዳሪ");
        AM.put("System Administrator", "የስርዓት አስተዳዳሪ");
        AM.put("Admin Panel", "የአስተዳዳሪ ፓነል");
        AM.put("MANAGEMENT", "አስተዳደር");
        AM.put("Edit Account Details", "መለያ ዝርዝር ቀይር");
        AM.put("Receipt downloaded.", "ደረሰኝ ተወስዷል።");
        AM.put("User updated successfully.", "ተጠቃሚ ተሻሽሏል።");
        AM.put("Search Users", "ተጠቃሚ ፈልግ");
        AM.put("Search Transactions", "ግብይት ፈልግ");
        AM.put("Export Transactions", "ግብይቶችን ላክ");
        AM.put("Recent Activity", "የቅርብ ጊዜ እንቅስቃሴ");
        AM.put("Edit Account", "መለያ ቀይር");
        AM.put("Profile Image", "የፕሮፋይል ምስል");
        AM.put("Select Profile Image", "ምስል ምረጥ");
        AM.put("Send Money", "ገንዘብ ላክ");
        AM.put("Pay Bills", "ክፍያ ፈጽም");
        AM.put("My Cards", "ካርዶቼ");
        AM.put("Savings", "ቁጠባ");
        AM.put("Good Morning", "እንደምን አደሩ");
        AM.put("Good Afternoon", "እንደምን ዋሉ");
        AM.put("Good Evening", "እንደምን አመሹ");
        AM.put("Good Night", "መልካም ሌሊት");
        AM.put("Account number copied to clipboard!", "የመለያ ቁጥር ተቀድቷል!");
        AM.put("Profile updated successfully.", "ፕሮፋይል ተሻሽሏል።");
        AM.put("System Load", "የስርዓት ጫና");
        AM.put("Activity log cleared.", "የእንቅስቃሴ ምዝግብ ጸድቷል።");
        AM.put("Airtime Top-Up",                        "አየር ሰዓት ሙሌት");
        AM.put("Buy airtime for any Ethiopian network", "ለማንኛውም የኢትዮጵያ አውታረ መረብ አየር ሰዓት ይግዙ");
        AM.put("Select Network",                        "አውታረ መረብ ይምረጡ");
        AM.put("Phone Number",                          "ስልክ ቁጥር");
        AM.put("Amount (ETB)",                          "መጠን (ብር)");
        AM.put("Buy Airtime",                           "አየር ሰዓት ይግዙ");
        AM.put("Airtime",                               "አየር ሰዓት");
        AM.put("Invalid phone number. Use format: 09XXXXXXXX", "ትክክለኛ ያልሆነ ስልክ ቁጥር። ቅርጸት ይጠቀሙ: 09XXXXXXXX");
        AM.put("Please enter phone number and amount.", "እባክዎ ስልክ ቁጥር እና መጠን ያስገቡ።");
        AM.put("Travel Booking",                        "የጉዞ ቦታ ማስያዝ");
        AM.put("Book air or land transport tickets",    "የአየር ወይም የምድር ትራንስፖርት ትኬቶችን ያስይዙ");
        AM.put("Travel",                                "ጉዞ");
        AM.put("Air Transport",                         "የአየር ትራንስፖርት");
        AM.put("Land Transport",                        "የምድር ትራንስፖርት");
        AM.put("Flight Route",                          "የበረራ መስመር");
        AM.put("Class",                                 "ክፍል");
        AM.put("Travel Date",                           "የጉዞ ቀን");
        AM.put("Passengers",                            "ተሳፋሪዎች");
        AM.put("Total Price",                           "ጠቅላላ ዋጋ");
        AM.put("Book Flight",                           "በረራ ያስይዙ");
        AM.put("Route",                                 "መስመር");
        AM.put("Bus Type",                              "የአውቶቡስ አይነት");
        AM.put("Seats",                                 "መቀመጫዎች");
        AM.put("Seat Preference",                       "የመቀመጫ ምርጫ");
        AM.put("Book Ticket",                           "ትኬት ያስይዙ");
        AM.put("Flight booked successfully!",           "በረራ በተሳካ ሁኔታ ተይዟል!");
        AM.put("Ticket booked successfully!",           "ትኬት በተሳካ ሁኔታ ተይዟል!");
        AM.put("Gallery",                    "ጋለሪ");
        AM.put("Camera",                     "ካሜራ");
        AM.put("Choose photo source",        "የፎቶ ምንጭ ይምረጡ");
        AM.put("Take or Select Photo",       "ፎቶ ያንሱ ወይም ይምረጡ");
        AM.put("Change Photo",               "ፎቶ ቀይር");
        AM.put("Edit Profile",               "መገለጫ አርትዕ");
        AM.put("Save Changes",               "ለውጦችን አስቀምጥ");
        AM.put("Full Name",                  "ሙሉ ስም");
        AM.put("Full Name cannot be empty.", "ሙሉ ስም ባዶ መሆን አይሻልም።");
        AM.put("Update failed: ",            "ዝማኔ አልተሳካም: ");
        AM.put("Account Information",        "የሂሳብ መረጃ");
        AM.put("Member Since",               "አባልነት ከ");
        AM.put("MAIN",         "ዋና");
        AM.put("TRANSACTIONS", "ግብይቶች");
        AM.put("TOOLS",        "መሳሪያዎች");
        AM.put("Account No.",  "የመለያ ቁጥር");
        AM.put("Account not found",   "መለያ አልተገኘም");
        AM.put("Note (optional)",     "ማስታወሻ (አማራጭ)");
        AM.put("e.g. Rent payment",   "ለምሳሌ፡ የቤት ኪራይ ክፍያ");
        AM.put("Send",                "ላክ");
        AM.put("Recipient not found.", "ተቀባዩ አልተገኘም።");
        AM.put("CBE Banking",    "CBE ባንክ");
        AM.put("Account Holder", "ባለቤት");
        AM.put("Available Balance", "ያለው ቀሪ ሂሳብ");
        AM.put("Online",         "በመስመር ላይ");
        AM.put("Toggle Navigation", "ዳሰሳ ቀይር");
        AM.put("Add funds to your account",       "ወደ መለያዎ ገንዘብ ያስቀምጡ");
        AM.put("Withdraw cash from your account", "ከመለያዎ ገንዘብ ያውጡ");
        AM.put("Send money to another account",   "ወደ ሌላ መለያ ገንዘብ ይላኩ");
        AM.put("Update your account password",    "የመለያ የይለፍ ቃልዎን ያዘምኑ");
        AM.put("Recipient",                       "ተቀባይ");
        AM.put("Account Overview", "የሂሳብ ማጠቃለያ");
        AM.put("See all transactions \u2192", "ሁሉንም ግብይቶች ይመልከቱ \u2192");
        AM.put("Online",                    "በመስመር ላይ");
        AM.put("Active",                    "ንቁ");
        AM.put("Loading...",                "በመጫን ላይ...");
        AM.put("Commercial Bank of Ethiopia", "የኢትዮጵያ ንግድ ባንክ");
        AM.put("CBE Banking",               "CBE ባንክ");
        AM.put("CBE Banking System",        "CBE የባንክ ስርዓት");
        AM.put("Your Trusted Financial Partner", "የሚታመን የፋይናንስ አጋርዎ");
        AM.put("Instant Transfers",         "ፈጣን ዝውውር");
        AM.put("Secure Payments",           "ደህንነቱ የተጠበቀ ክፍያ");
        AM.put("24/7 Banking",              "24/7 የባንክ አገልግሎት");
        AM.put("Fill in your details to get started", "ዝርዝሮቹን ሞልተው ይጀምሩ");
        AM.put("transactions",              "ግብይቶች");
        AM.put("Export to CSV",             "ወደ CSV ላክ");
        AM.put("Export Statement PDF",      "ሂሳብ ዝርዝር PDF ላክ");
        AM.put("7-Day Activity",            "የ7 ቀን እንቅስቃሴ");
        AM.put("In",                        "ገቢ");
        AM.put("Out",                       "ወጪ");
        AM.put("Type:",                     "ዓይነት:");
        AM.put("Period:",                   "ጊዜ:");
        AM.put("All Time",                  "ሁሉም ጊዜ");
        AM.put("Today",                     "ዛሬ");
        AM.put("This Week",                 "በዚህ ሳምንት");
        AM.put("This Month",                "በዚህ ወር");
        AM.put("Last 3 Months",             "ባለፉት 3 ወራት");
        AM.put("Share your QR to receive payments instantly", "ክፍያ ለመቀበል QR ኮድዎን ያጋሩ");
        AM.put("How to use your QR Code",   "QR ኮድዎን እንዴት መጠቀም እንደሚቻል");
        AM.put("Share your QR",             "QR ኮድዎን ያጋሩ");
        AM.put("Show this QR code to the sender", "ይህን QR ኮድ ላኪው ያሳዩ");
        AM.put("Sender scans",              "ላኪው ይቃኛል");
        AM.put("They scan using CBE mobile app", "CBE ሞባይል አፕ ተጠቅመው ይቃኛሉ");
        AM.put("Receive instantly",         "ወዲያውኑ ይቀበሉ");
        AM.put("Funds arrive in seconds",   "ገንዘቡ በሰከንዶች ውስጥ ይደርሳል");
        AM.put("Quick Actions",             "ፈጣን አማራጮች");
        AM.put("Save QR Image",             "QR ምስል አስቀምጥ");
        AM.put("QR Code saved successfully!", "QR ኮድ ተቀምጧል!");
        AM.put("Failed to save QR image.",  "QR ምስልን ማስቀመጥ አልተሳካም።");
        AM.put("Security Note:",            "የደህንነት ማሳሰቢያ:");
        AM.put("This QR only allows receiving money.", "ይህ QR ገንዘብ ለመቀበል ብቻ ነው።");
        AM.put("Never share your PIN or password.", "ፒን ወይም የይለፍ ቃልዎን ፈጽሞ አያጋሩ።");
        AM.put("Find answers to common banking questions", "ለተለመዱ የባንክ ጥያቄዎች መልስ ያግኙ");
        AM.put("No results found",          "ምንም ውጤት አልተገኘም");
        AM.put("Try a different search term or category", "ሌላ የፍለጋ ቃል ወይም ምድብ ይሞክሩ");
        AM.put("Transactions",              "ግብይቶች");
        AM.put("Security",                  "ደህንነት");
        AM.put("QR & Payments",             "QR እና ክፍያዎች");
        AM.put("Account",                   "መለያ");
        AM.put("Export",                    "ላክ");
        AM.put("How do I deposit money?",   "ገንዘብ እንዴት አስቀምጣለሁ?");
        AM.put("How do I withdraw cash?",   "ገንዘብ እንዴት አወጣለሁ?");
        AM.put("How do I transfer money to another account?", "ወደ ሌላ መለያ ገንዘብ እንዴት አዛውራለሁ?");
        AM.put("How do I view my transaction history?", "የግብይት ታሪኬን እንዴት እመለከታለሁ?");
        AM.put("How do I change my password / PIN?", "የይለፍ ቃሌን / ፒኔን እንዴት እቀይራለሁ?");
        AM.put("Is my account secure?",     "መለያዬ ደህንነቱ የተጠበቀ ነው?");
        AM.put("What does the eye icon on my balance do?", "በሂሳቤ ላይ ያለው የዓይን አዶ ምን ያደርጋል?");
        AM.put("What is the QR Code for?",  "QR ኮዱ ለምን ነው?");
        AM.put("Can I save my QR code?",    "QR ኮዴን ማስቀመጥ እችላለሁ?");
        AM.put("How do I update my profile picture?", "የፕሮፋይል ምስሌን እንዴት አዘምናለሁ?");
        AM.put("How do I copy my account number?", "የመለያ ቁጥሬን እንዴት እቀዳለሁ?");
        AM.put("How do I export my statement?", "ሂሳቤን እንዴት ወደ ፋይል አወጣለሁ?");
        AM.put("Account Profile",           "የመለያ መገለጫ");
        AM.put("Switch to Light Mode",      "ወደ ብርሃን ሁነታ ቀይር");
        AM.put("Switch to Dark Mode",       "ወደ ጨለማ ሁነታ ቀይር");
        AM.put("Switch to English",         "ወደ እንግሊዝኛ ቀይር");
        AM.put("Switch to Amharic",         "ወደ አማርኛ ቀይር");
        AM.put("Show Balance",              "ሂሳብ አሳይ");
        AM.put("Hide Balance",              "ሂሳብ ደብቅ");
        AM.put("Show / Hide Balance",       "ሂሳብ አሳይ / ደብቅ");
        AM.put("Dashboard Overview",        "ዳሽቦርድ ማጠቃለያ");
        AM.put("Recent Activity",           "የቅርብ ጊዜ እንቅስቃሴ");
        AM.put("Switch to Light Mode",      "ወደ ብርሃን ሁነታ ቀይር");
        AM.put("Switch to Dark Mode",       "ወደ ጨለማ ሁነታ ቀይር");
        AM.put("Hint: ",                    "ፍንጭ: ");
        AM.put("Password must be exactly 6 characters and include uppercase, lowercase, number, and symbol.",
               "የይለፍ ቃሉ ትክክለኛ 6 ቁምፊ ሆኖ ትልቅ ፊደል፣ ትንሽ ፊደል፣ ቁጥር እና ምልክት ሊኖረው ይገባል።");
        AM.put("Tip: Set up automatic savings to grow your wealth effortlessly.",
               "ምክር: ሀብትዎን ለማሳደግ ራስ-ሰር ቁጠባ ያዘጋጁ።");
        AM.put("Security: Never share your PIN or password with anyone.",
               "ደህንነት: ፒን ወይም የይለፍ ቃልዎን ከማንም ጋር አያጋሩ።");
        AM.put("Insight: Review your statement monthly to track spending habits.",
               "ምክር: የወጪ ልምዶቻቸውን ለመከታተል ሂሳብዎን በወር ይፈትሹ።");
        AM.put("Reminder: Transfer fees are waived for CBE-to-CBE transfers.",
               "ማሳሰቢያ: ከCBE ወደ CBE ዝውውር ምንም ክፍያ የለም።");
        AM.put("Did you know? Your CBE account earns interest on every birr saved.",
               "ያውቃሉ? CBE መለያዎ በሚቆጠቡ ብሮች ሁሉ ወለድ ያስገኛል።");
        AM.put("Total In",                  "ጠቅላላ ገቢ");
        AM.put("Total Out",                 "ጠቅላላ ወጪ");
        AM.put("Net Flow",                  "የተጣራ ፍሰት");
        AM.put("7-Day Activity",            "የ7 ቀን እንቅስቃሴ");
        AM.put("transactions",              "ግብይቶች");
        AM.put("Export to CSV",             "ወደ CSV ላክ");
        AM.put("Export Statement PDF",      "ሂሳብ ዝርዝር PDF ላክ");
        AM.put("Statement PDF saved.",      "ሂሳብ ዝርዝር PDF ተቀምጧል።");
        AM.put("PDF export failed.",        "PDF ማውጣት አልተሳካም።");
        AM.put("Save QR Image",             "QR ምስል አስቀምጥ");
        AM.put("QR Code saved successfully!", "QR ኮድ ተቀምጧል!");
        AM.put("Failed to save QR image.",  "QR ምስልን ማስቀመጥ አልተሳካም።");
        AM.put("Save QR Code",              "QR ኮድ አስቀምጥ");
        AM.put("Scan to identify admin panel endpoint", "የአስተዳዳሪ ፓነልን ለመለየት ይቃኙ");
        AM.put("Find answers to common banking questions", "ለተለመዱ የባንክ ጥያቄዎች መልስ ያግኙ");
        AM.put("All",                       "ሁሉም");
        AM.put("Transactions",              "ግብይቶች");
        AM.put("Security",                  "ደህንነት");
        AM.put("QR & Payments",             "QR እና ክፍያዎች");
        AM.put("Account",                   "መለያ");
        AM.put("Export",                    "ላክ");
        AM.put("No results found",          "ምንም ውጤት አልተገኘም");
        AM.put("Try a different search term or category", "ሌላ የፍለጋ ቃል ወይም ምድብ ይሞክሩ");
        AM.put("How do I deposit money?",   "ገንዘብ እንዴት አስቀምጣለሁ?");
        AM.put("How do I withdraw cash?",   "ገንዘብ እንዴት አወጣለሁ?");
        AM.put("How do I transfer money to another account?", "ወደ ሌላ መለያ ገንዘብ እንዴት አዛውራለሁ?");
        AM.put("How do I view my transaction history?", "የግብይት ታሪኬን እንዴት እመለከታለሁ?");
        AM.put("How do I change my password / PIN?", "የይለፍ ቃሌን / ፒኔን እንዴት እቀይራለሁ?");
        AM.put("Is my account secure?",     "መለያዬ ደህንነቱ የተጠበቀ ነው?");
        AM.put("What does the eye icon on my balance do?", "በሂሳቤ ላይ ያለው የዓይን አዶ ምን ያደርጋል?");
        AM.put("What is the QR Code for?",  "QR ኮዱ ለምን ነው?");
        AM.put("Can I save my QR code?",    "QR ኮዴን ማስቀመጥ እችላለሁ?");
        AM.put("How do I update my profile picture?", "የፕሮፋይል ምስሌን እንዴት አዘምናለሁ?");
        AM.put("How do I copy my account number?", "የመለያ ቁጥሬን እንዴት እቀዳለሁ?");
        AM.put("How do I export my statement?", "ሂሳቤን እንዴት ወደ ፋይል አወጣለሁ?");
        AM.put("Click 'Deposit' in the sidebar or Quick Actions on the home screen. Enter the amount and confirm with your password. Funds are credited instantly.",
               "በጎን አሞሌ ወይም በዋና ገጽ ፈጣን አማራጮች ላይ 'ተቀማጥ' ይጫኑ። መጠኑን ያስገቡ እና በይለፍ ቃልዎ ያረጋግጡ። ገንዘቡ ወዲያውኑ ይጨምራል።");
        AM.put("Click 'Withdraw' in the sidebar. Enter the amount (must not exceed your available balance) and confirm with your password.",
               "በጎን አሞሌ ላይ 'ወጪ' ይጫኑ። መጠኑን ያስገቡ (ካለው ቀሪ ሂሳብ መብለጥ የለበትም) እና በይለፍ ቃልዎ ያረጋግጡ።");
        AM.put("Click 'Transfer', enter the recipient's CBE account number and the amount. The recipient must have an active account. Confirm with your password to complete.",
               "'ዝውውር' ይጫኑ፣ የተቀባዩን CBE መለያ ቁጥር እና መጠኑን ያስገቡ። ተቀባዩ ንቁ መለያ ሊኖረው ይገባል። ለማጠናቀቅ በይለፍ ቃልዎ ያረጋግጡ።");
        AM.put("Click 'Statement' in the sidebar. You can filter by type, date range, and search by keyword. Export to CSV or PDF is also available.",
               "በጎን አሞሌ ላይ 'ዓሳብ ዝርዝር' ይጫኑ። በዓይነት፣ ቀን ክልል ማጣራት እና በቁልፍ ቃል መፈለግ ይችላሉ። ወደ CSV ወይም PDF ማውጣትም ይቻላል።");
        AM.put("Go to TOOLS → Change PIN in the sidebar. Enter your current password, then your new password twice. Passwords must be exactly 6 characters with uppercase, lowercase, number, and symbol.",
               "በጎን አሞሌ ላይ መሳሪያዎች → ፒን ቀይር ይሂዱ። አሁን ያለዎትን የይለፍ ቃል ያስገቡ፣ ከዚያ አዲሱን ሁለት ጊዜ። የይለፍ ቃሉ ትክክለኛ 6 ቁምፊ ሆኖ ትልቅ ፊደል፣ ትንሽ ፊደል፣ ቁጥር እና ምልክት ሊኖረው ይገባል።");
        AM.put("Yes. Every transaction requires password confirmation. Passwords are stored using PBKDF2 hashing with a unique salt. Never share your credentials with anyone.",
               "አዎ። እያንዳንዱ ግብይት የይለፍ ቃል ማረጋገጫ ይፈልጋል። የይለፍ ቃሎች ልዩ ጨው ባለው PBKDF2 ሃሺንግ ይቀመጣሉ። ምስክርነቶቻቸውን ከማንም ጋር አያጋሩ።");
        AM.put("The eye icon hides or shows your balance. Tap it to toggle visibility — useful when others are nearby.",
               "የዓይን አዶ ሂሳብዎን ይደብቃል ወይም ያሳያል። ታይነቱን ለመቀያየር ይጫኑ — ሌሎች ሲቀርቡ ጠቃሚ ነው።");
        AM.put("Your QR code encodes your account number. Others can scan it using the CBE app to send money directly to you. It is safe to share — it only allows receiving, not sending.",
               "QR ኮድዎ የመለያ ቁጥርዎን ያካትታል። ሌሎች CBE አፕ ተጠቅመው ቃኝተው ቀጥታ ወደ እርስዎ ገንዘብ ሊልኩ ይችላሉ። ማጋራት ደህንነቱ የተጠበቀ ነው — ለመቀበል ብቻ ነው፣ ለመላክ አይደለም።");
        AM.put("Yes. Open QR Code from the sidebar, then click 'Save QR Image' to download it as a PNG file you can share or print.",
               "አዎ። ከጎን አሞሌ QR ኮድ ይክፈቱ፣ ከዚያ 'QR ምስል አስቀምጥ' ይጫኑ ማጋራት ወይም ማተም የሚችሉት PNG ፋይል ለማውረድ።");
        AM.put("Go to Profile in the sidebar, then click 'Update Profile'. Select an image file from your computer.",
               "ከጎን አሞሌ ፕሮፋይል ይሂዱ፣ ከዚያ 'ፕሮፋይል ቀይር' ይጫኑ። ከኮምፒዩተርዎ ምስል ፋይል ይምረጡ።");
        AM.put("Open Profile or QR Code and click 'Copy Account Number'. It will be copied to your clipboard.",
               "ፕሮፋይል ወይም QR ኮድ ይክፈቱ እና 'የመለያ ቁጥር ኮፒ አድርግ' ይጫኑ። ወደ ክሊፕቦርድዎ ይቀዳል።");
        AM.put("Open Statement, then click 'CSV' to export as a spreadsheet or 'PDF' to download a formatted statement document.",
               "ዓሳብ ዝርዝር ይክፈቱ፣ ከዚያ ወደ ሰንጠረዥ ለማውጣት 'CSV' ወይም ቅርጸት ያለው ሰነድ ለማውረድ 'PDF' ይጫኑ።");
        AM.put("How do I manage users?",    "ተጠቃሚዎችን እንዴት አስተዳድራለሁ?");
        AM.put("Open Users Management to edit, block, unblock, or delete accounts.",
               "ተጠቃሚዎች አስተዳደር ይክፈቱ ለማርትዕ፣ ለማገድ፣ ለማራገፍ ወይም መለያዎችን ለመሰረዝ።");
        AM.put("How do I monitor activity?", "እንቅስቃሴን እንዴት እከታተላለሁ?");
        AM.put("Open Activity Monitor to review recent transaction events.",
               "የቅርብ ጊዜ የግብይት ክስተቶችን ለመፈተሽ እንቅስቃሴ ተቆጣጣሪ ይክፈቱ።");
        AM.put("How do I export transactions?", "ግብይቶችን እንዴት አወጣለሁ?");
        AM.put("Open Transactions Management and click Export CSV.",
               "ግብይቶች አስተዳደር ይክፈቱ እና CSV ላክ ይጫኑ።");
        AM.put("Why can't I see customer password?", "የደንበኛ የይለፍ ቃል ለምን ማየት አልቻልኩም?");
        AM.put("Passwords are securely hashed and cannot be viewed in plain text.",
               "የይለፍ ቃሎች ደህንነቱ በተጠበቀ ሁኔታ ሃሽ ተደርገዋል እና በቀጥታ ጽሑፍ ሊታዩ አይችሉም።");
        AM.put("How do I switch language/theme?", "ቋንቋ/ገጽታ እንዴት እቀይራለሁ?");
        AM.put("Use EN/AM and dark mode toggles at the bottom of the sidebar.",
               "በጎን አሞሌ ታችኛው ክፍል ያሉ EN/AM እና ጨለማ ሁነታ ቁልፎችን ይጠቀሙ።");
        AM.put("QUICK VIEW",              "ፈጣን እይታ");
        AM.put("Today's Transactions",    "የዛሬ ግብይቶች");
        AM.put("Blocked Users",           "የታገዱ ተጠቃሚዎች");
        AM.put("Total Volume",            "አጠቃላይ መጠን");
        AM.put("Total Users",             "አጠቃላይ ተጠቃሚዎች");
        AM.put("Total Accounts",            "አጠቃላይ መለያዎች");
        AM.put("Transactions Today",        "የዛሬ ግብይቶች");
        AM.put("Error loading log",         "ምዝግብ ማስታወሻ መጫን ስህተት");
        AM.put("Export successful!",        "ማውጣት ተሳካ!");
        AM.put("Export failed: ",           "ማውጣት አልተሳካም: ");
        AM.put("Search:",                   "ፈልግ:");
        AM.put("Role:",                     "ሚና:");
        AM.put("Status:",                   "ሁነታ:");
        AM.put("Type:",                     "ዓይነት:");
        AM.put("Search users...",           "ተጠቃሚ ፈልግ...");
        AM.put("Search transactions...",    "ግብይት ፈልግ...");
        AM.put("New Today",                 "ዛሬ አዲስ");
        AM.put("Total Volume",              "አጠቃላይ መጠን");
        AM.put("Today's Transactions",      "የዛሬ ግብይቶች");
        AM.put("Update Profile",            "ፕሮፋይል ቀይር");
        AM.put("Name:",                     "ስም:");
        AM.put("Email:",                    "ኢሜይል:");
        AM.put("Phone:",                    "ስልክ:");
        AM.put("Account Type:",             "የመለያ አይነት:");
        AM.put("Account Number:",           "የመለያ ቁጥር:");
        AM.put("Member Since:",             "አባልነት ከ:");
        AM.put("Profile updated successfully.", "ፕሮፋይል ተሻሽሏል።");
        AM.put("Deposit Funds",             "ገንዘብ ያስቀምጡ");
        AM.put("Add funds to your account", "ወደ መለያዎ ገንዘብ ያስቀምጡ");
        AM.put("Withdraw Funds",            "ገንዘብ ያውጡ");
        AM.put("Withdraw cash from your account", "ከመለያዎ ገንዘብ ያውጡ");
        AM.put("Transfer Funds",            "ገንዘብ ያዛውሩ");
        AM.put("Send money to another account", "ወደ ሌላ መለያ ገንዘብ ይላኩ");
        AM.put("Update your account password", "የመለያ የይለፍ ቃልዎን ያዘምኑ");
        AM.put("Enter amount (ETB):",       "መጠን ያስገቡ (ብር):");
        AM.put("Recipient Account Number:", "የተቀባዩ መለያ ቁጥር:");
        AM.put("Recipient account number", "የተቀባዩ መለያ ቁጥር");
        AM.put("Description (optional):",   "መግለጫ (አማራጭ):");
        AM.put("Confirm",                   "አረጋግጥ");
        AM.put("Cancel",                    "ሰርዝ");
        AM.put("Change Password",           "የይለፍ ቃል ቀይር");
        AM.put("Update your account password", "የመለያ የይለፍ ቃልዎን ያዘምኑ");
        AM.put("← Back",                   "← ተመለስ");
        AM.put("\u2190 Back",               "← ተመለስ");
        AM.put("Welcome back",              "እንኳን ደህና መጡ");
        AM.put("●  Active  ",               "●  ንቁ  ");
        AM.put("● Online",                  "● በመስመር ላይ");
        AM.put("Fill in your details to get started", "ዝርዝሮቹን ሞልተው ይጀምሩ");
    }
    private static Font ethFontBase = null;
    static {
        ethFontBase = detectEthiopicFont();
    }
    private static Font detectEthiopicFont() {
        String[] candidates = { "Ebrima", "Nyala", "Noto Sans Ethiopic", "Abyssinica SIL" };
        java.util.Set<String> available = new java.util.HashSet<>(
            java.util.Arrays.asList(
                java.awt.GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames()));
        char probe = '\u12A5'; // Ethiopic syllable
        for (String name : candidates) {
            if (available.contains(name)) {
                Font f = new Font(name, Font.PLAIN, 14);
                if (f.canDisplay(probe)) return f;
            }
        }
        for (String name : available) {
            Font f = new Font(name, Font.PLAIN, 14);
            if (f.canDisplay(probe)) return f;
        }
        return new Font("Dialog", Font.PLAIN, 14);
    }
    static String hashPassword(String plainPassword) {
        return BankingUtils.hashPassword(plainPassword);
    }
    static boolean verifyPassword(String plainPassword, String storedPassword) {
        return BankingUtils.verifyPassword(plainPassword, storedPassword);
    }
    static String generateAccountNumber() {
        return BankingUtils.generateAccountNumber();
    }
    static String getGreeting() {
        int h = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (h >= 5 && h < 12)
            return t("Good Morning");
        if (h >= 12 && h < 17)
            return t("Good Afternoon");
        if (h >= 17 && h < 21)
            return t("Good Evening");
        return t("Good Night");
    }
    static int recordTransaction(String accNo, String type, double amount, double balAfter,
            String recipient, String desc) {
        return BankingUtils.recordTransaction(accNo, type, amount, balAfter, recipient, desc);
    }
    static void generateReceiptPDF(int transactionId, String filePath) {
        BankingUtils.generateReceiptPDF(transactionId, filePath);
    }
    static void generateReceiptPDF(int ignoredId, String filePath, String customContent) {
        BankingUtils.generateCustomPDF(filePath, customContent);
    }
    static String t(String key) {
        return isAmharic && AM.containsKey(key) ? AM.get(key) : key;
    }
    static Font uiFont(int style, int size) {
        if (isAmharic && ethFontBase != null) {
            return ethFontBase.deriveFont(style, size);
        }
        return new Font("Segoe UI", style, size);
    }
    static Font iconFont(int size) {
        for (String name : new String[]{"Segoe UI Emoji", "Segoe UI Symbol", "Arial Unicode MS", "SansSerif"}) {
            Font f = new Font(name, Font.PLAIN, size);
            if (f.canDisplay('\u2191')) return f;
        }
        return new Font("SansSerif", Font.PLAIN, size);
    }
    static boolean isStrongPassword(String password) {
        return BankingUtils.isStrongPassword(password);
    }
    static String passwordRuleMessage() {
        return BankingUtils.passwordRuleMessage();
    }
    static boolean validEmail(String email) {
        return BankingUtils.validEmail(email);
    }
    static boolean validPhone(String phone) {
        return BankingUtils.validPhone(phone);
    }
    static String formatCurrency(double amount) {
        return String.format("ETB %,.2f", amount);
    }
    static void applyTheme() {
        if (darkMode) {
            BG      = new Color(15, 20, 25);        // #0F1419 darker background
            CARD_BG = new Color(22, 30, 36);        // #161E24 enhanced card background
            FG      = new Color(235, 245, 250);      // #EBF5FA brighter foreground
            MUTED   = new Color(160, 180, 200);      // #A0B4C8 enhanced muted text
            BORDER  = new Color(70, 85, 95);         // #46555F clearer borders
            DIVIDER = new Color(45, 60, 70);         // #2D3C46 enhanced dividers
            SIDEBAR_BG     = new Color(10, 25, 15);   // #0A190F darker sidebar
            SIDEBAR_ACTIVE = new Color(0,  80, 40);   // #005028 enhanced active
            SIDEBAR_TEXT   = new Color(190, 235, 205); // brighter sidebar text
            SIDEBAR_ICON   = new Color(20, 210, 110);  // enhanced sidebar icons
        } else {
            BG      = new Color(245, 248, 246);       // #F5F8F6 cleaner background
            CARD_BG = new Color(255, 255, 255);       // Pure white cards
            FG      = new Color(10,  25, 15);         // #0A190F darker foreground
            MUTED   = new Color(80, 100, 85);         // #506455 enhanced muted
            BORDER  = new Color(210, 230, 218);       // #D2E6DA clearer borders
            DIVIDER = new Color(225, 240, 232);       // #E1F0E8 enhanced dividers
            SIDEBAR_BG     = new Color(5,  40, 20);   // #052814 light sidebar
            SIDEBAR_ACTIVE = new Color(0,  90, 45);   // #005A2D light active
            SIDEBAR_TEXT   = new Color(180, 225, 195); // light sidebar text
            SIDEBAR_ICON   = new Color(10, 200, 100);  // light sidebar icons
        }
    }
    static void showDialog(Component parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
    static void showDialog(Component parent, String message, int messageType) {
        showDialog(parent, message, "CBE Banking", messageType);
    }
    static void resetEthFont() {
        ethFontBase = detectEthiopicFont();
    }
}