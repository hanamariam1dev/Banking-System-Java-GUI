import java.awt.*;
import javax.swing.*;
public class FormBuilder {
    public static JTextField styledField(String placeholder) {
        return new RoundedTextField(placeholder);
    }
    public static JPasswordField styledPasswordField() {
        return new RoundedPasswordField();
    }
    public static JButton styledButton(String text) {
        return new PrimaryButton(text);
    }
    public static JLabel createLabel(String text, int style, int size, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(ThemeManager.uiFont(style, size));
        lbl.setForeground(color);
        return lbl;
    }
    public static JLabel createLabel(String text) {
        return createLabel(text, Font.PLAIN, 12, ColorTheme.FG);
    }
    public static JPanel createFormRow(JComponent label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
    public static JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);
        for (JButton btn : buttons) {
            panel.add(btn);
        }
        return panel;
    }
    public static JPanel createSection(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setOpaque(false);
        section.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(ThemeManager.uiFont(Font.BOLD, 16));
        titleLbl.setForeground(ColorTheme.PRIMARY);
        section.add(titleLbl);
        section.add(Box.createVerticalStrut(10));
        return section;
    }
}