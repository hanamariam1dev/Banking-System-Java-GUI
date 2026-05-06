
import java.awt.*;
import javax.swing.*;

public class TransactionRow extends JPanel {

    public TransactionRow(String type, String desc, String date, double amount, boolean isCredit) {
        setOpaque(false);
        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        Color txColor = isCredit ? BankingConfig.SUCCESS : BankingConfig.DANGER;
        String sign = isCredit ? "+" : "-";
        String emoji = isCredit ? "\u2B07" : "\u2B06";
        if (type.contains("Transfer")) {
            emoji = "\u21C4";
        }

        JPanel circle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int alpha = BankingConfig.darkMode ? 100 : 25;
                g2.setColor(new Color(txColor.getRed(), txColor.getGreen(), txColor.getBlue(), alpha));
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(42, 42));

        JLabel icLbl = new JLabel(emoji);
        icLbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        icLbl.setForeground(txColor);
        circle.add(icLbl);

        JPanel mid = new JPanel();
        mid.setOpaque(false);
        mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));

        JLabel typeLbl = new JLabel(type);
        typeLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        typeLbl.setForeground(BankingConfig.FG);

        JLabel descLbl = new JLabel(desc != null && !desc.isEmpty() ? desc : " ");
        descLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        descLbl.setForeground(BankingConfig.MUTED);

        mid.add(typeLbl);
        mid.add(descLbl);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        JLabel amtLbl = new JLabel(sign + " ETB " + String.format("%,.2f", amount));
        amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        amtLbl.setForeground(txColor);
        amtLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel dateLbl = new JLabel(date);
        dateLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
        dateLbl.setForeground(BankingConfig.MUTED);
        dateLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);

        right.add(amtLbl);
        right.add(dateLbl);

        add(circle, BorderLayout.WEST);
        add(mid, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BankingConfig.CARD_BG);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        if (BankingConfig.darkMode) {
            g2.setColor(BankingConfig.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        }
    }
}
