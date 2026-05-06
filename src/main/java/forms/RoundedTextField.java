import java.awt.*;
import javax.swing.*;
public class RoundedTextField extends JTextField {
    private final String placeholder;
    public RoundedTextField(String placeholder) {
        super();
        this.placeholder = placeholder != null ? placeholder : "";
        setOpaque(false);
        setFont(BankingConfig.uiFont(Font.PLAIN, 14));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setForeground(BankingConfig.FG);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BankingConfig.CARD_BG);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        if (getText().isEmpty() && !placeholder.isEmpty()) {
            Insets insets = getInsets();
            g2.setColor(BankingConfig.MUTED);
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = insets.left;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(placeholder, x, y);
        }
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