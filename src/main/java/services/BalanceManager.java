import java.sql.*;
public class BalanceManager {
    public static double getBalance(String accountNumber) {
        return AccountService.getBalance(accountNumber);
    }
    public static boolean deposit(String accountNumber, double amount, String description) {
        if (amount <= 0) return false;
        double currentBalance = getBalance(accountNumber);
        double newBalance = currentBalance + amount;
        if (AccountService.updateBalance(accountNumber, newBalance)) {
            TransactionService.recordTransaction(accountNumber, "Deposit", amount,
                newBalance, null, description);
            return true;
        }
        return false;
    }
    public static boolean withdraw(String accountNumber, double amount, String description) {
        if (amount <= 0) return false;
        double currentBalance = getBalance(accountNumber);
        if (amount > currentBalance) return false;
        double newBalance = currentBalance - amount;
        if (AccountService.updateBalance(accountNumber, newBalance)) {
            TransactionService.recordTransaction(accountNumber, "Withdrawal", amount,
                newBalance, null, description);
            return true;
        }
        return false;
    }
    public static boolean transfer(String fromAccount, String toAccount, double amount, String description) {
        if (amount <= 0) return false;
        if (fromAccount.equals(toAccount)) return false;
        if (!AccountService.accountExists(toAccount)) return false;
        double fromBalance = getBalance(fromAccount);
        if (amount > fromBalance) return false;
        double toBalance = getBalance(toAccount);
        double newFromBalance = fromBalance - amount;
        if (!AccountService.updateBalance(fromAccount, newFromBalance)) {
            return false;
        }
        double newToBalance = toBalance + amount;
        if (!AccountService.updateBalance(toAccount, newToBalance)) {
            AccountService.updateBalance(fromAccount, fromBalance);
            return false;
        }
        TransactionService.recordTransaction(fromAccount, "Transfer Out", amount,
            newFromBalance, toAccount, description);
        TransactionService.recordTransaction(toAccount, "Transfer In", amount,
            newToBalance, fromAccount, description);
        return true;
    }
}