import java.awt.*;
import javax.swing.*;
public class AdminSummaryCard extends JPanel {
    public AdminSummaryCard(String label, int count, Color color) {
        setLayout(new BorderLayout(0, 6));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        JLabel countLbl = new JLabel(String.valueOf(count));
        countLbl.setFont(ThemeManager.uiFont(Font.BOLD, 24));
        countLbl.setForeground(color);
        countLbl.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(ThemeManager.uiFont(Font.PLAIN, 11));
        labelLbl.setForeground(ColorTheme.MUTED);
        labelLbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(countLbl, BorderLayout.CENTER);
        add(labelLbl, BorderLayout.SOUTH);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 15));
        g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 2, 14, 14);
        g2.setColor(ColorTheme.CARD_BG);
        g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 14, 14);
    }
}