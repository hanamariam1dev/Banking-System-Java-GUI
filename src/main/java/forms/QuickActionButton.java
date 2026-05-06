import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class QuickActionButton extends JPanel {
    private boolean hover = false;
    public QuickActionButton(String emoji, String label, Color color, Runnable action) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        JPanel iconCircle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int alpha = hover ? 255 : (ThemeManager.isDarkMode() ? 120 : 40);
                Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                g2.setColor(c);
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(52, 52));
        iconCircle.setMaximumSize(new Dimension(52, 52));
        iconCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        emojiLbl.setForeground(ThemeManager.isDarkMode() ? Color.WHITE : color);
        iconCircle.add(emojiLbl);
        JLabel nameLbl = new JLabel(label, SwingConstants.CENTER);
        nameLbl.setFont(ThemeManager.uiFont(Font.BOLD, 11));
        nameLbl.setForeground(ColorTheme.FG);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(iconCircle);
        add(Box.createVerticalStrut(6));
        add(nameLbl);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.run(); }
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            public void mouseExited(MouseEvent e) { hover = false; repaint(); }
        });
    }
}