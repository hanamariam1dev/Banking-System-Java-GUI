
import java.awt.*;
import javax.swing.*;

public class SidebarItem extends JButton {

    private boolean selected = false;

    public SidebarItem(String icon, String label, Color color, Runnable action) {
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setLayout(new BorderLayout(15, 0));
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(BankingConfig.iconFont(18));
        iconLbl.setForeground(Color.WHITE);

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 14));
        textLbl.setForeground(Color.WHITE);

        add(iconLbl, BorderLayout.WEST);
        add(textLbl, BorderLayout.CENTER);

        if (action != null) {
            addActionListener(e -> action.run());
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        for (Component comp : getComponents()) {
            if (comp instanceof JLabel lbl) {
                lbl.setForeground(Color.WHITE);
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (selected) {
            g2.setColor(new Color(
                    BankingConfig.PRIMARY_LIGHT.getRed(),
                    BankingConfig.PRIMARY_LIGHT.getGreen(),
                    BankingConfig.PRIMARY_LIGHT.getBlue(), 50));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        } else if (getMousePosition() != null) {
            g2.setColor(new Color(
                    BankingConfig.BORDER.getRed(),
                    BankingConfig.BORDER.getGreen(),
                    BankingConfig.BORDER.getBlue(), 60));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        }
    }
}
