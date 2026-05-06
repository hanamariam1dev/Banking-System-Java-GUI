import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DashboardSidebar {
    private final Dashboard dashboard;
    boolean sidebarCollapsed = false;
    static final int SIDEBAR_FULL = 260;
    static final int SIDEBAR_MINI = 68;
    JPanel sidebarInner;
    List<JPanel> globalNavCards = new ArrayList<>();
    JLabel balanceLabel;
    JButton sidebarBalToggleBtn;
    public DashboardSidebar(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
    JComponent buildSidebar() {
        sidebarInner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color sideTop = BankingConfig.darkMode
                        ? new Color(13, 17, 23)
                        : new Color(22, 100, 55);
                Color sideBot = BankingConfig.darkMode
                        ? new Color(13, 17, 23)
                        : new Color(18, 88, 46);
                g2.setPaint(new GradientPaint(0, 0, sideTop, 0, getHeight(), sideBot));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebarInner.setLayout(new BoxLayout(sidebarInner, BoxLayout.Y_AXIS));
        sidebarInner.setBorder(BorderFactory.createEmptyBorder(0, 6, 12, 6));
        globalNavCards.clear();
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        logoRow.setOpaque(false);
        logoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        logoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        UIComponents.CBELogoPanel logo = new UIComponents.CBELogoPanel();
        logo.setPreferredSize(new Dimension(36, 36));
        logo.setMinimumSize(new Dimension(36, 36));
        JPanel logoTextPanel = new JPanel();
        logoTextPanel.setOpaque(false);
        logoTextPanel.setLayout(new BoxLayout(logoTextPanel, BoxLayout.Y_AXIS));
        JLabel lb1 = new JLabel(BankingConfig.t("CBE Banking"));
        lb1.setFont(BankingConfig.uiFont(Font.BOLD, 14));
        lb1.setForeground(Color.WHITE);
        JLabel lb2 = new JLabel(BankingConfig.t("Commercial Bank of Ethiopia"));
        lb2.setFont(BankingConfig.uiFont(Font.PLAIN, 9));
        lb2.setForeground(new Color(120, 185, 150));
        logoTextPanel.add(lb1);
        logoTextPanel.add(lb2);
        logoRow.add(logo);
        logoRow.add(logoTextPanel);
        sidebarInner.add(logoRow);
        sidebarInner.add(makeSidebarSep());
        JPanel userCard = new JPanel(new BorderLayout(10, 0));
        userCard.setOpaque(false);
        userCard.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        userCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        userCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BankingConfig.PRIMARY_LIGHT);
                g2.fillOval(0, 0, getWidth(), getHeight());
                Image img = null;
                try {
                    PreparedStatement psi = BankingConfig.con.prepareStatement(
                            "SELECT profile_image FROM accounts WHERE account_number=?");
                    psi.setString(1, dashboard.accountNumber);
                    ResultSet rsi = psi.executeQuery();
                    if (rsi.next()) {
                        String path = rsi.getString("profile_image");
                        if (path != null && !path.isEmpty()) {
                            File f = new File(path);
                            if (f.exists())
                                img = javax.imageio.ImageIO.read(f);
                        }
                    }
                    rsi.close();
                    psi.close();
                } catch (Exception ignored) {
                }
                if (img != null) {
                    Image scaled = img.getScaledInstance(getWidth() - 4, getHeight() - 4, Image.SCALE_SMOOTH);
                    g2.setClip(new java.awt.geom.Ellipse2D.Float(2, 2, getWidth() - 4, getHeight() - 4));
                    g2.drawImage(scaled, 2, 2, null);
                    g2.setClip(null);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.setFont(BankingConfig.uiFont(Font.BOLD, 18));
                    FontMetrics fm = g2.getFontMetrics();
                    String initial = dashboard.accountHolder.isEmpty() ? "?"
                            : String.valueOf(dashboard.accountHolder.charAt(0)).toUpperCase();
                    g2.drawString(initial, (getWidth() - fm.stringWidth(initial)) / 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                }
            }
        };
        avatar.setName("sidebarAvatar");
        avatar.setPreferredSize(new Dimension(44, 44));
        avatar.setMinimumSize(new Dimension(44, 44));
        avatar.setMaximumSize(new Dimension(44, 44));
        avatar.setOpaque(false);
        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        JLabel nameLbl = new JLabel(dashboard.accountHolder);
        nameLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        nameLbl.setForeground(Color.WHITE);
        JPanel balRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        balRow.setOpaque(false);
        balanceLabel = new JLabel("ETB 0.00");
        balanceLabel.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        balanceLabel.setForeground(new Color(140, 210, 170));
        sidebarBalToggleBtn = dashboard.makeEyeToggleBtn();
        dashboard.balanceLabel = balanceLabel;
        dashboard.sidebarBalToggleBtn = sidebarBalToggleBtn;
        balRow.add(balanceLabel);
        balRow.add(sidebarBalToggleBtn);
        userInfo.add(nameLbl);
        userInfo.add(balRow);
        userCard.add(avatar, BorderLayout.WEST);
        userCard.add(userInfo, BorderLayout.CENTER);
        sidebarInner.add(userCard);
        sidebarInner.add(makeSidebarSep());
        sidebarInner.add(Box.createVerticalStrut(6));
        addNavSection(sidebarInner, BankingConfig.t("MAIN"));
        addNavItem(sidebarInner, globalNavCards, "home", BankingConfig.t("Home"), BankingConfig.PRIMARY_LIGHT,
                dashboard::showHome);
        addNavItem(sidebarInner, globalNavCards, "history", BankingConfig.t("History"), BankingConfig.INFO, () -> {
            dashboard.currentView = "History";
            dashboard.showHistory();
        });
        addNavItem(sidebarInner, globalNavCards, "profile", BankingConfig.t("Profile"), BankingConfig.SUCCESS, () -> {
            dashboard.currentView = "Profile";
            dashboard.showProfile();
        });
        addNavSection(sidebarInner, BankingConfig.t("TRANSACTIONS"));
        addNavItem(sidebarInner, globalNavCards, "deposit", BankingConfig.t("Deposit"), BankingConfig.SUCCESS,
                dashboard::deposit);
        addNavItem(sidebarInner, globalNavCards, "withdraw", BankingConfig.t("Withdraw"), BankingConfig.DANGER,
                dashboard::withdraw);
        addNavItem(sidebarInner, globalNavCards, "transfer", BankingConfig.t("Transfer"), BankingConfig.WARNING,
                dashboard::transfer);
        addNavSection(sidebarInner, BankingConfig.t("TOOLS"));
        addNavItem(sidebarInner, globalNavCards, "pin", BankingConfig.t("Change PIN"), BankingConfig.GOLD,
                dashboard::changePin);
        addNavItem(sidebarInner, globalNavCards, "qr", BankingConfig.t("QR Code"), BankingConfig.INFO, () -> {
            dashboard.currentView = "QR";
            dashboard.showQR();
        });
        addNavItem(sidebarInner, globalNavCards, "faq", BankingConfig.t("FAQ"), BankingConfig.PRIMARY_LIGHT, () -> {
            dashboard.currentView = "FAQ";
            dashboard.showFAQ();
        });
        if (!globalNavCards.isEmpty())
            selectNavCard(globalNavCards, globalNavCards.get(0));
        sidebarInner.add(Box.createVerticalStrut(16));
        sidebarInner.add(makeSidebarSep());
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        bottomRow.setOpaque(false);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton langBtn = new JButton(BankingConfig.isAmharic ? "EN" : "AM") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255, 255, 255, 40) : new Color(255, 255, 255, 20))
                        : (getModel().isRollover() ? new Color(0, 0, 0, 24) : new Color(0, 0, 0, 14));
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        langBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        langBtn.setForeground(BankingConfig.darkMode ? new Color(200, 240, 215) : new Color(55, 80, 65));
        langBtn.setOpaque(false);
        langBtn.setContentAreaFilled(false);
        langBtn.setBorderPainted(false);
        langBtn.setFocusPainted(false);
        langBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        langBtn.setPreferredSize(new Dimension(50, 30));
        langBtn.setToolTipText(BankingConfig.isAmharic ? "Switch to English" : "Switch to Amharic");
        langBtn.addActionListener(e -> {
            BankingConfig.isAmharic = !BankingConfig.isAmharic;
            BankingConfig.resetEthFont();
            BankingConfig.applyTheme();
            dashboard.hotSwap();
        });
        JButton darkBtn = sidebarDarkBtn(BankingConfig.darkMode);
        darkBtn.setToolTipText(BankingConfig.darkMode ? "Switch to Light Mode" : "Switch to Dark Mode");
        darkBtn.addActionListener(e -> {
            BankingConfig.darkMode = !BankingConfig.darkMode;
            BankingConfig.applyTheme();
            dashboard.hotSwap();
        });
        JButton logoutBtn = new JButton("Log Out") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255, 80, 80, 60) : new Color(255, 255, 255, 20))
                        : (getModel().isRollover() ? new Color(255, 220, 220, 120) : new Color(255, 235, 235, 180));
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                int cx = 16;
                int cy = getHeight() / 2;
                g2.setColor(BankingConfig.darkMode ? new Color(255, 110, 110) : new Color(185, 45, 50));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx - 7, cy - 6, 14, 14, -60, -240);
                g2.drawLine(cx, cy - 9, cx, cy - 3);
                g2.setFont(BankingConfig.uiFont(Font.BOLD, 11));
                g2.setColor(BankingConfig.darkMode ? new Color(255, 220, 220) : new Color(115, 20, 25));
                g2.drawString("Log Out", 34, cy + 4);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        logoutBtn.setOpaque(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(104, 30));
        logoutBtn.setToolTipText("Log Out");
        logoutBtn.addActionListener(e -> {
            dashboard.balanceTimer.stop();
            LoginRegister.showLogin();
        });
        bottomRow.add(langBtn);
        bottomRow.add(darkBtn);
        bottomRow.add(logoutBtn);
        sidebarInner.add(bottomRow);
        javax.swing.plaf.basic.BasicScrollBarUI thinBar = new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(255, 255, 255, 55);
                trackColor = new Color(0, 0, 0, 0);
            }
            @Override
            protected JButton createDecreaseButton(int o) {
                return zeroBtn();
            }
            @Override
            protected JButton createIncreaseButton(int o) {
                return zeroBtn();
            }
            private JButton zeroBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 6, 6);
                g2.dispose();
            }
            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }
        };
        JScrollPane scroll = new JScrollPane(sidebarInner,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getVerticalScrollBar().setUI(thinBar);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        scroll.setBackground(BankingConfig.darkMode ? new Color(13, 17, 23) : new Color(22, 100, 55));
        scroll.getViewport().setBackground(BankingConfig.darkMode ? new Color(13, 17, 23) : new Color(22, 100, 55));
        scroll.getViewport().setOpaque(true);
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                for (int i = 0; i < 10; i++) {
                    g2.setColor(new Color(0, 0, 0, 20 - i * 2));
                    g2.drawLine(getWidth() - 10 + i, 0, getWidth() - 10 + i, getHeight());
                }
            }
        };
        wrapper.setBackground(BankingConfig.darkMode ? new Color(13, 17, 23) : new Color(22, 100, 55));
        wrapper.setPreferredSize(new Dimension(SIDEBAR_FULL, 0));
        wrapper.add(scroll, BorderLayout.CENTER);
        wrapper.putClientProperty("nameLbl", nameLbl);
        wrapper.putClientProperty("lb1", lb1);
        wrapper.putClientProperty("lb2", lb2);
        wrapper.putClientProperty("balRow", balRow);
        wrapper.putClientProperty("bottomRow", bottomRow);
        wrapper.putClientProperty("logoTextPanel", logoTextPanel);
        wrapper.putClientProperty("userInfo", userInfo);
        return wrapper;
    }
    JButton sidebarBtn(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255, 255, 255, 40) : new Color(255, 255, 255, 20))
                        : (getModel().isRollover() ? new Color(0, 0, 0, 24) : new Color(0, 0, 0, 14));
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        btn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        btn.setForeground(BankingConfig.darkMode ? new Color(200, 240, 215) : new Color(55, 80, 65));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btn.setPreferredSize(new Dimension(50, 30));
        return btn;
    }
    JButton sidebarDarkBtn(boolean isDark) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255, 255, 255, 50) : new Color(255, 255, 255, 22))
                        : (getModel().isRollover() ? new Color(255, 255, 255, 60) : new Color(255, 255, 255, 35));
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                if (!BankingConfig.darkMode) {
                    g2.setColor(new Color(255, 255, 255, 120));
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                }
                int cx = getWidth() / 2, cy = getHeight() / 2;
                Color iconColor = new Color(255, 255, 255, 220);
                g2.setColor(iconColor);
                if (isDark) {
                    g2.fillOval(cx - 5, cy - 5, 10, 10);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 0; i < 8; i++) {
                        double a = Math.PI * 2 * i / 8;
                        g2.drawLine(cx + (int)(Math.cos(a) * 7), cy + (int)(Math.sin(a) * 7),
                                cx + (int)(Math.cos(a) * 10), cy + (int)(Math.sin(a) * 10));
                    }
                } else {
                    g2.fillOval(cx - 6, cy - 6, 12, 12);
                    g2.setColor(new Color(22, 100, 55));
                    g2.fillOval(cx - 3, cy - 7, 12, 12);
                }
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btn.setPreferredSize(new Dimension(50, 30));
        return btn;
    }
    void addNavSection(JPanel sidebar, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(BankingConfig.isAmharic ? text : text.toUpperCase());
        lbl.setFont(BankingConfig.uiFont(Font.BOLD, 9));
        lbl.setForeground(BankingConfig.darkMode ? new Color(100, 130, 110) : new Color(180, 220, 195));
        row.add(lbl);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(row);
    }
    void paintNavIcon(Graphics2D g2, String key, int cx, int cy, boolean active, Color strokeColor) {
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        switch (key) {
            case "home":
                int[] hx = { cx - 8, cx, cx + 8, cx + 6, cx + 6, cx - 6, cx - 6 };
                int[] hy = { cy + 1, cy - 7, cy + 1, cy + 1, cy + 7, cy + 7, cy + 1 };
                g2.drawPolygon(hx, hy, 7);
                g2.drawRect(cx - 3, cy + 2, 6, 5);
                break;
            case "history":
                g2.drawOval(cx - 7, cy - 7, 14, 14);
                g2.drawLine(cx, cy - 5, cx, cy);
                g2.drawLine(cx, cy, cx + 4, cy + 3);
                break;
            case "profile":
                g2.drawOval(cx - 4, cy - 7, 8, 8);
                g2.drawArc(cx - 7, cy + 1, 14, 10, 0, 180);
                break;
            case "deposit":
                g2.drawRoundRect(cx - 7, cy - 5, 14, 10, 4, 4);
                g2.drawLine(cx, cy - 9, cx, cy - 5);
                g2.drawLine(cx - 3, cy - 8, cx, cy - 5);
                g2.drawLine(cx + 3, cy - 8, cx, cy - 5);
                break;
            case "withdraw":
                g2.drawRoundRect(cx - 7, cy - 5, 14, 10, 4, 4);
                g2.drawLine(cx, cy + 5, cx, cy + 9);
                g2.drawLine(cx - 3, cy + 8, cx, cy + 9);
                g2.drawLine(cx + 3, cy + 8, cx, cy + 9);
                break;
            case "transfer":
                g2.drawLine(cx - 7, cy - 3, cx + 7, cy - 3);
                g2.drawLine(cx + 4, cy - 6, cx + 7, cy - 3);
                g2.drawLine(cx + 4, cy, cx + 7, cy - 3);
                g2.drawLine(cx + 7, cy + 3, cx - 7, cy + 3);
                g2.drawLine(cx - 4, cy, cx - 7, cy + 3);
                g2.drawLine(cx - 4, cy + 6, cx - 7, cy + 3);
                break;
            case "pin":
                g2.drawOval(cx - 5, cy - 7, 10, 9);
                g2.drawLine(cx - 5, cy + 2, cx - 5, cy + 7);
                g2.drawLine(cx + 5, cy + 2, cx + 5, cy + 7);
                g2.drawLine(cx - 5, cy + 7, cx + 5, cy + 7);
                g2.drawLine(cx, cy + 2, cx, cy + 5);
                break;
            case "qr":
                g2.drawRect(cx - 7, cy - 7, 5, 5);
                g2.drawRect(cx + 2, cy - 7, 5, 5);
                g2.drawRect(cx - 7, cy + 2, 5, 5);
                g2.fillRect(cx - 5, cy - 5, 2, 2);
                g2.fillRect(cx + 3, cy - 5, 2, 2);
                g2.fillRect(cx - 5, cy + 3, 2, 2);
                g2.drawLine(cx + 2, cy + 2, cx + 7, cy + 2);
                g2.drawLine(cx + 7, cy + 2, cx + 7, cy + 7);
                g2.drawLine(cx + 4, cy + 5, cx + 7, cy + 5);
                break;
            case "faq":
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("?", cx - fm.stringWidth("?") / 2, cy + fm.getAscent() / 2 - 1);
                break;
            default:
                g2.drawRect(cx - 5, cy - 5, 10, 10);
        }
    }
    void addNavItem(JPanel sidebar, List<JPanel> navCards,
            String iconKey, String label, Color iconColor, Runnable action) {
        final boolean[] selected = { false };
        final boolean[] hovered = { false };
        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                if (selected[0]) {
                    g2.setColor(new Color(255, 255, 255, 22));
                    g2.fillRoundRect(8, 3, w - 16, h - 6, 12, 12);
                    g2.setColor(iconColor);
                    g2.fillRoundRect(0, h / 2 - 12, 4, 24, 4, 4);
                } else if (hovered[0]) {
                    g2.setColor(new Color(255, 255, 255, 10));
                    g2.fillRoundRect(8, 3, w - 16, h - 6, 12, 12);
                }
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        card.setPreferredSize(new Dimension(SIDEBAR_FULL - 12, 52));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                Color bgCol = selected[0]
                        ? iconColor
                        : BankingConfig.darkMode
                                ? new Color(iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), 70)
                                : new Color(0, 0, 0, 40);
                g2.setColor(bgCol);
                g2.fillRoundRect(0, 0, w, h, 10, 10);
                Color iconStroke = Color.WHITE;
                paintNavIcon(g2, iconKey, w / 2, h / 2, selected[0], iconStroke);
                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(38, 38));
        iconPanel.setMinimumSize(new Dimension(38, 38));
        iconPanel.setMaximumSize(new Dimension(38, 38));
        JLabel textLbl = new JLabel(label);
        textLbl.setFont(BankingConfig.uiFont(selected[0] ? Font.BOLD : Font.PLAIN, 13));
        textLbl.setForeground(Color.WHITE);
        textLbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        card.add(iconPanel, BorderLayout.WEST);
        card.add(textLbl, BorderLayout.CENTER);
        card.putClientProperty("selected", selected);
        card.putClientProperty("hovered", hovered);
        card.putClientProperty("textLbl", textLbl);
        card.putClientProperty("iconPanel", iconPanel);
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectNavCard(navCards, card);
                action.run();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered[0] = true;
                card.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovered[0] = false;
                card.repaint();
            }
        });
        navCards.add(card);
        sidebar.add(card);
        sidebar.add(Box.createVerticalStrut(2));
    }
    void selectNavCard(List<JPanel> navCards, JPanel target) {
        for (JPanel c : navCards) {
            boolean[] sel = (boolean[]) c.getClientProperty("selected");
            JLabel tl = (JLabel) c.getClientProperty("textLbl");
            JPanel ip = (JPanel) c.getClientProperty("iconPanel");
            boolean isTarget = (c == target);
            if (sel != null)
                sel[0] = isTarget;
            if (tl != null) {
                tl.setFont(BankingConfig.uiFont(isTarget ? Font.BOLD : Font.PLAIN, 13));
                tl.setForeground(Color.WHITE);
            }
            if (ip != null)
                ip.repaint();
            c.repaint();
        }
    }
    JSeparator makeSidebarSep() {
        JSeparator sep = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 18));
                g.drawLine(14, 0, getWidth() - 14, 0);
            }
        };
        sep.setOpaque(false);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
    JLabel sidebarSectionLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(BankingConfig.uiFont(Font.BOLD, 9));
        lbl.setForeground(Color.WHITE);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
    public JLabel getBalanceLabel() {
        return balanceLabel;
    }
    public JButton getSidebarBalToggleBtn() {
        return sidebarBalToggleBtn;
    }
    public JPanel getSidebarInner() {
        return sidebarInner;
    }
    public List<JPanel> getGlobalNavCards() {
        return globalNavCards;
    }
}