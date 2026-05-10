
public class AirtimeService {

    public static final String[] NETWORKS = {"Ethio Telecom", "Safaricom"};

    public static boolean purchaseAirtime(String accountNumber, String phoneNumber,
            double amount, String network) {
        if (!ValidationService.validEthiopianPhone(phoneNumber)) {
            return false;
        }
        if (amount <= 0) {
            return false;
        }
        String description = "Airtime purchase for " + phoneNumber + " (" + network + ")";
        return BalanceManager.withdraw(accountNumber, amount, description);
    }

    public static boolean isValidAmount(double amount) {
        return amount >= 5 && amount <= 1000;
    }
}
