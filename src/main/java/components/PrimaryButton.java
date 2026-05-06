import java.awt.*;
import javax.swing.*;
public class PrimaryButton extends JButton {
    private final Color bg;
    public PrimaryButton(String text) {
        this(text, ColorTheme.PRIMARY);
    }
    public PrimaryButton(String text, Color bg) {
        super(text);
        this.bg = bg;
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(ThemeManager.uiFont(Font.BOLD, 14));
        setForeground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color top = getModel().isPressed() ? bg.darker().darker()
                  : getModel().isRollover() ? bg.brighter() : bg;
        Color bot = getModel().isPressed() ? bg.darker() : bg.darker();
        g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bot));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        g2.setColor(new Color(255, 255, 255, 22));
        g2.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 12, 12);
        g2.setColor(new Color(0, 0, 0, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        super.paintComponent(g);
    }
}