import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
public class UIComponents {
    static class NotificationBanner extends JPanel {
        private final boolean success;
        NotificationBanner(String message, boolean success) {
            this.success = success;
            setLayout(new FlowLayout(FlowLayout.LEFT, 14, 10));
            setBackground(success ? BankingConfig.SUCCESS : BankingConfig.DANGER);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory
                            .createLineBorder(success ? BankingConfig.SUCCESS.darker() : BankingConfig.DANGER.darker()),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)));
            JLabel msg = new JLabel(message);
            msg.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            msg.setForeground(Color.WHITE);
            add(msg);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 30));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        }
    }
    static class CBELogoPanel extends JPanel {
        private Image logoImage;
        CBELogoPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(40, 40));
            try {
                java.net.URL url = getClass().getResource("image.png");
                if (url != null) {
                    logoImage = new javax.swing.ImageIcon(url).getImage();
                } else {
                    java.io.File f = new java.io.File("image.png");
                    if (!f.exists()) f = new java.io.File("Java Project/image.png");
                    if (f.exists()) logoImage = new javax.swing.ImageIcon(f.getAbsolutePath()).getImage();
                }
            } catch (Exception ignored) {}
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            int w = getWidth(), h = getHeight();
            int cx = w / 2, cy = h / 2;
            int size = Math.min(w, h);
            if (logoImage != null) {
                g2.setClip(new java.awt.geom.Ellipse2D.Float(cx - size/2f, cy - size/2f, size, size));
                g2.drawImage(logoImage, cx - size/2, cy - size/2, size, size, this);
            } else {
                g2.setColor(BankingConfig.BG);
                g2.fillOval(cx - 18, cy - 18, 36, 36);
                GradientPaint gp = new GradientPaint(0, cy - 15, BankingConfig.INFO, 0, cy + 15,
                        BankingConfig.INFO.darker());
                g2.setPaint(gp);
                g2.fillOval(cx - 15, cy - 15, 30, 30);
                g2.setColor(Color.WHITE);
                g2.setFont(BankingConfig.uiFont(Font.BOLD, 16));
                String txt = "CBE";
                FontMetrics fm = g2.getFontMetrics();
                int x = cx - fm.stringWidth(txt) / 2;
                int y = cy + fm.getAscent() / 2 - 2;
                g2.drawString(txt, x, y);
            }
            g2.dispose();
        }
    }
    static class GradientPanel extends JPanel {
        private final Color start, end;
        GradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, start, getWidth(), getHeight(), end);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        }
    }
    static class RoundedField extends JTextField {
        private final String placeholder;
        RoundedField(String placeholder) {
            super();
            this.placeholder = placeholder != null ? placeholder : "";
            setFont(BankingConfig.uiFont(Font.PLAIN, 14));
            setBackground(BankingConfig.CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)));
            setOpaque(false);
            setForeground(BankingConfig.FG);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
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
    }
    static class RoundedPassField extends JPasswordField {
        RoundedPassField() {
            super(18);
            setFont(BankingConfig.uiFont(Font.PLAIN, 14));
            setBackground(BankingConfig.CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(10, 14, 10, 14)));
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            super.paintComponent(g);
        }
    }
    private static Color readableForeground(Color bg) {
        double luminance = (0.2126 * bg.getRed() + 0.7152 * bg.getGreen() + 0.0722 * bg.getBlue());
        return luminance > 180 ? new Color(30, 30, 30) : Color.WHITE;
    }
    static class PurpleButton extends JButton {
        PurpleButton(String text, Color color) {
            super(text);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBackground(color);
            setForeground(readableForeground(color));
            setFont(BankingConfig.uiFont(Font.BOLD, 13));
            setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            super.paintComponent(g);
        }
    }
    static class CardPanel extends JPanel {
        CardPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BankingConfig.CARD_BG);
            g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
        }
    }
    static class QuickActionBtn extends JButton {
        QuickActionBtn(String icon, String text, Color color, Runnable action) {
            super();
            setContentAreaFilled(false);
            setOpaque(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBackground(color);
            setForeground(readableForeground(color));
            setFont(BankingConfig.uiFont(Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(150, 90));
            setMinimumSize(new Dimension(140, 90));
            setLayout(new BorderLayout(8, 0));
            JLabel iconLbl = new JLabel(icon);
            iconLbl.setFont(BankingConfig.iconFont(18));
            iconLbl.setForeground(readableForeground(color));
            JLabel textLbl = new JLabel(text);
            textLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
            textLbl.setForeground(readableForeground(color));
            add(iconLbl, BorderLayout.WEST);
            add(textLbl, BorderLayout.CENTER);
            if (action != null) {
                addActionListener(e -> action.run());
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            super.paintComponent(g);
        }
    }
    static class TxRow extends JPanel {
        TxRow(String date, String desc, String amount, double amountValue, boolean isCredit) {
            setLayout(new BorderLayout(10, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            JLabel dateLbl = new JLabel(date);
            dateLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            dateLbl.setForeground(BankingConfig.MUTED);
            JLabel descLbl = new JLabel(desc);
            descLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            descLbl.setForeground(BankingConfig.FG);
            JLabel amtLbl = new JLabel(amount);
            amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
            amtLbl.setForeground(isCredit ? BankingConfig.SUCCESS : BankingConfig.DANGER);
            JPanel left = new JPanel(new BorderLayout(5, 0));
            left.setOpaque(false);
            left.add(dateLbl, BorderLayout.NORTH);
            left.add(descLbl, BorderLayout.CENTER);
            add(left, BorderLayout.CENTER);
            add(amtLbl, BorderLayout.EAST);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BankingConfig.DIVIDER);
            g2.drawLine(20, 0, getWidth() - 20, 0);
        }
    }
    static class StatCard extends JPanel {
        StatCard(String icon, String value, String label, Color color) {
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
    static JPasswordField styledPassField() {
        return new UIComponents.RoundedPassField();
    }
    static BufferedImage generateQR(String data, int size) {
        System.out.println("QR code generation disabled - Data: " + data + ", Size: " + size);
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, size, size);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, size / 4));
        String text = "QR";
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(text)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, x, y);
        g2.dispose();
        return img;
    }
    static JPanel adminSummaryCard(String label, int count, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, 16, 16);
                g2.setPaint(new GradientPaint(0, 0, color, getWidth(), getHeight(), color.darker()));
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth() - 3, (getHeight() - 4) / 2, 16, 16);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        card.setPreferredSize(new Dimension(190, 100));
        JLabel valLbl = new JLabel(String.valueOf(count));
        valLbl.setFont(BankingConfig.uiFont(Font.BOLD, 30));
        valLbl.setForeground(Color.WHITE);
        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        lblLbl.setForeground(new Color(255, 255, 255, 220));
        card.add(valLbl, BorderLayout.CENTER);
        card.add(lblLbl, BorderLayout.SOUTH);
        return card;
    }
    static JLabel sidebarSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        label.setForeground(BankingConfig.MUTED);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        return label;
    }
    static JTextField styledField(String placeholder) {
        return new UIComponents.RoundedField(placeholder);
    }
    static class SideItem extends JButton {
        private final String icon;
        private final String label;
        private boolean selected = false;
        private final Color color;
        SideItem(String icon, String label, Color color, Runnable action) {
            this.icon = icon;
            this.label = label;
            this.color = color;
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
        public void setSelected(boolean selected) {
            this.selected = selected;
            for (Component comp : getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel lbl = (JLabel) comp;
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
                g2.setColor(new Color(BankingConfig.PRIMARY_LIGHT.getRed(), BankingConfig.PRIMARY_LIGHT.getGreen(),
                        BankingConfig.PRIMARY_LIGHT.getBlue(), 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            } else if (getMousePosition() != null) {
                g2.setColor(new Color(BankingConfig.BORDER.getRed(), BankingConfig.BORDER.getGreen(),
                        BankingConfig.BORDER.getBlue(), 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        }
    }
}