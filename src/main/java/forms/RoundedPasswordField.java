import java.awt.*;
import javax.swing.*;
public class RoundedPasswordField extends JPasswordField {
    public RoundedPasswordField() {
        super(20);
        setOpaque(false);
        setFont(BankingConfig.uiFont(Font.PLAIN, 14));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BankingConfig.CARD_BG);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        super.paintComponent(g);
    }
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean focused = isFocusOwner();
        g2.setColor(focused ? BankingConfig.PRIMARY : BankingConfig.BORDER);
        g2.setStroke(new BasicStroke(focused ? 2f : 1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
    }
}