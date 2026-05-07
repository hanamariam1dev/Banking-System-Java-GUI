
import java.awt.*;
import javax.swing.*;

public class StatCard extends JPanel {

    public StatCard(String icon, String value, String label, Color color) {
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(BankingConfig.iconFont(16));
        iconLbl.setForeground(color);

        JLabel valLbl = new JLabel(value);
        valLbl.setFont(BankingConfig.uiFont(Font.BOLD, 28));
        valLbl.setForeground(color);

        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        lblLbl.setForeground(BankingConfig.MUTED);

        JPanel top = new JPanel(new BorderLayout(5, 0));
        top.setOpaque(false);
        top.add(iconLbl, BorderLayout.WEST);
        top.add(valLbl, BorderLayout.CENTER);

        add(top, BorderLayout.CENTER);
        add(lblLbl, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BankingConfig.CARD_BG);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
    }
}
