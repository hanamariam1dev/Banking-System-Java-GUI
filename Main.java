import javax.swing.*;
import java.awt.Font;
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            if (BankingConfig.isAmharic) {
                try {
                    Font amFont;
                    if (new Font("Nyala", Font.PLAIN, 14).canDisplay('ሀ')) {
                        amFont = new Font("Nyala", Font.PLAIN, 14);
                    } else {
                        amFont = new Font("Abyssinica SIL", Font.PLAIN, 14);
                    }
                    UIManager.put("OptionPane.messageFont", amFont);
                    UIManager.put("OptionPane.buttonFont", amFont);
                } catch (Exception e) {
                }
            }
            AppFrame.get();
            BankingConfig.con = DBConnection.connect();
            if (BankingConfig.con == null)
                JOptionPane.showMessageDialog(null,
                    "\u26A0  Database Not Connected!\n\n1. Open XAMPP\n2. Start MySQL\n3. Re-run",
                    "", JOptionPane.ERROR_MESSAGE);
            LoginRegister.showLogin();
        });
    }
}