import java.awt.*;
import javax.swing.*;
public class CardPanel extends JPanel {
    private final int radius;
    private final Color bgColor;
    public CardPanel(int radius) {
        this(radius, ColorTheme.CARD_BG);
    }
    public CardPanel(int radius, Color bg) {
        this.radius = radius;
        this.bgColor = bg;
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 18));
        g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, radius, radius);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, radius, radius);
        super.paintComponent(g);
    }
}