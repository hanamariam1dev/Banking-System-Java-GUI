import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class NotificationBanner extends JPanel {
    private final boolean success;
    public NotificationBanner(String message, boolean success) {
        this.success = success;
        setLayout(new FlowLayout(FlowLayout.LEFT, 14, 10));
        setOpaque(false);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        JLabel icon = new JLabel(success ? "\u2713" : "\u2715");
        icon.setFont(new Font("SansSerif", Font.BOLD, 14));
        icon.setForeground(Color.WHITE);
        JLabel lbl = new JLabel(message);
        lbl.setFont(ThemeManager.uiFont(Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        add(icon);
        add(lbl);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color base = success ? ColorTheme.SUCCESS : ColorTheme.DANGER;
        g2.setPaint(new GradientPaint(0, 0, base, getWidth(), 0, base.darker()));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
    }
}