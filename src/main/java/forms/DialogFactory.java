import javax.swing.*;
import java.awt.*;
public class DialogFactory {
    public static void showError(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    public static void showInfo(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    public static void showSuccess(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean showConfirm(Component parent, String title, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, title,
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    public static String showPasswordDialog(Component parent, String title, String message) {
        JPasswordField passField = new JPasswordField(20);
        Object[] messageObj = {message, passField};
        int option = JOptionPane.showConfirmDialog(parent, messageObj, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            return new String(passField.getPassword());
        }
        return null;
    }
    public static void showLoadingDialog(JDialog dialog, String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColorTheme.BG);
        JLabel lbl = new JLabel(message);
        lbl.setFont(ThemeManager.uiFont(Font.PLAIN, 14));
        lbl.setForeground(ColorTheme.MUTED);
        panel.add(lbl);
        dialog.setContentPane(panel);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(false);
    }
}