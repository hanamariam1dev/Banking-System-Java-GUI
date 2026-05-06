import java.awt.*;
import javax.swing.*;
public class GradientPanel extends JPanel {
    public GradientPanel() {
        setLayout(new GridBagLayout());
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(0, 60, 30), ColorTheme.PRIMARY_DARK,
            getWidth(), getHeight(), ColorTheme.PRIMARY));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(255, 255, 255, 14));
        g2.fillOval(-60, -60, 220, 220);
        g2.fillOval(getWidth() - 100, getHeight() - 120, 200, 200);
        g2.setColor(new Color(255, 255, 255, 7));
        g2.fillOval(getWidth() / 2 - 80, getHeight() / 2 - 80, 160, 160);
    }
}