import java.awt.*;
import javax.swing.*;
public class SidebarSectionLabel extends JLabel {
    public SidebarSectionLabel(String text) {
        super(text);
        setFont(ThemeManager.uiFont(Font.BOLD, 9));
        setForeground(new Color(100, 160, 120));
        setBorder(BorderFactory.createEmptyBorder(14, 18, 4, 0));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(getText()) + 24;
        int y = getHeight() / 2 + 1;
        g2.setColor(new Color(255, 255, 255, 18));
        g2.setStroke(new BasicStroke(1f));
        g2.drawLine(textW, y, getWidth() - 16, y);
    }
}