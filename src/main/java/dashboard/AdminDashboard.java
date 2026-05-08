import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.List;
public class AdminDashboard {
    private JPanel contentArea;
    private JComponent sidebar;
    private UIComponents.SideItem activeSidebarItem;
    private javax.swing.Timer refreshTimer;
    private String adminCurrentView = "Dashboard";
    AdminDashboard() {
        BankingConfig.applyTheme();
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BankingConfig.BG);
        sidebar = buildSidebar();
        JPanel mainArea = buildMainArea();
        root.add(sidebar, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);
        AppFrame.get().showPage(root, "CBE Banking System - Admin");
        showDashboard();
        refreshTimer = new javax.swing.Timer(30000, e -> {
            if ("Dashboard".equals(adminCurrentView))
                showDashboard();
        });
        refreshTimer.start();
    }
    private void hotSwap() {
        JPanel root = (JPanel) AppFrame.get().getContentPane();
        root.removeAll();
        sidebar = buildSidebar();
        JPanel mainArea = buildMainArea();
        root.setBackground(BankingConfig.BG);
        root.add(sidebar, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);
        showDashboard();
        SwingUtilities.updateComponentTreeUI(AppFrame.get());
        root.revalidate();
        root.repaint();
    }
    private JComponent buildSidebar() {
        JPanel sb = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BankingConfig.SIDEBAR_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BankingConfig.BORDER);
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
            }
        };
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setPreferredSize(new Dimension(252, 780));
        sb.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel logoHeader = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BankingConfig.PRIMARY_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 12));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        logoHeader.setOpaque(false);
        logoHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        logoHeader.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        JPanel logoLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoLeft.setOpaque(false);
        UIComponents.CBELogoPanel hLogo = new UIComponents.CBELogoPanel();
        hLogo.setPreferredSize(new Dimension(36, 36));
        hLogo.setMinimumSize(new Dimension(36, 36));
        JPanel hText = new JPanel();
        hText.setOpaque(false);
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        JLabel hBank = new JLabel(BankingConfig.t("CBE Banking"));
        hBank.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        hBank.setForeground(Color.WHITE);
        JLabel hSub = new JLabel(BankingConfig.t("Admin Dashboard"));
        hSub.setFont(BankingConfig.uiFont(Font.PLAIN, 9));
        hSub.setForeground(new Color(140, 200, 170));
        hText.add(hBank);
        hText.add(hSub);
        logoLeft.add(hLogo);
        logoLeft.add(hText);
        logoHeader.add(logoLeft, BorderLayout.CENTER);
        JLabel verLbl = new JLabel("Admin");
        verLbl.setFont(BankingConfig.uiFont(Font.BOLD, 9));
        verLbl.setForeground(BankingConfig.GOLD);
        JPanel verWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        verWrap.setOpaque(false);
        verWrap.add(verLbl);
        logoHeader.add(verWrap, BorderLayout.EAST);
        sb.add(logoHeader);
        JPanel adminCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK,
                        getWidth(), getHeight(), BankingConfig.PRIMARY));
                g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 10, 14, 14);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(6, 6, getWidth() - 12, (getHeight() - 10) / 2, 14, 14);
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillOval(getWidth() - 60, -20, 100, 100);
            }
        };
        adminCard.setOpaque(false);
        adminCard.setLayout(new BorderLayout(10, 0));
        adminCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 14));
        JPanel avatar = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 60),
                        getWidth(), getHeight(), new Color(255, 255, 255, 30)));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(1, 1, getWidth() - 2, getHeight() - 2);
                g2.setColor(Color.WHITE);
                g2.setFont(BankingConfig.uiFont(Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                String init = "A";
                g2.drawString(init, (getWidth() - fm.stringWidth(init)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setMinimumSize(new Dimension(38, 38));
        avatar.setMaximumSize(new Dimension(38, 38));
        JPanel nameBox = new JPanel();
        nameBox.setOpaque(false);
        nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.Y_AXIS));
        JLabel adminNameLbl = new JLabel(BankingConfig.t("Admin"));
        adminNameLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        adminNameLbl.setForeground(Color.WHITE);
        JLabel adminRoleLbl = new JLabel(BankingConfig.t("System Administrator"));
        adminRoleLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
        adminRoleLbl.setForeground(BankingConfig.PRIMARY_SOFT);
        nameBox.add(adminNameLbl);
        nameBox.add(adminRoleLbl);
        JLabel goldBadge = new JLabel(BankingConfig.t("Admin Panel")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(215, 145, 20, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                super.paintComponent(g);
            }
        };
        goldBadge.setFont(BankingConfig.uiFont(Font.BOLD, 9));
        goldBadge.setForeground(Color.WHITE);
        goldBadge.setOpaque(false);
        goldBadge.setBorder(BorderFactory.createEmptyBorder(2, 7, 2, 7));
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        badgeWrap.setOpaque(false);
        badgeWrap.add(goldBadge);
        adminCard.add(avatar, BorderLayout.WEST);
        adminCard.add(nameBox, BorderLayout.CENTER);
        adminCard.add(badgeWrap, BorderLayout.EAST);
        JPanel adminCardWrap = new JPanel(new BorderLayout());
        adminCardWrap.setOpaque(false);
        adminCardWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminCardWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        adminCardWrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
        adminCardWrap.add(adminCard, BorderLayout.CENTER);
        sb.add(adminCardWrap);
        JLabel managementLabel = UIComponents.sidebarSectionLabel(BankingConfig.t("MANAGEMENT"));
        managementLabel.setForeground(Color.WHITE);
        managementLabel.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        sb.add(managementLabel);
        addNavItem(sb, "@", BankingConfig.t("Dashboard"), () -> {
            adminCurrentView = "Dashboard";
            showDashboard();
        }, true);
        addNavItem(sb, "&", BankingConfig.t("Users Management"), () -> {
            adminCurrentView = "Users";
            showUsers();
        }, false);
        addNavItem(sb, "\u21C4", BankingConfig.t("Transactions Management"), () -> {
            adminCurrentView = "Transactions";
            showTransactions();
        }, false);
        addNavItem(sb, "\u25B2", BankingConfig.t("Activity Monitor"), () -> {
            adminCurrentView = "Activity";
            showActivity();
        }, false);
        JLabel quickViewLabel = UIComponents.sidebarSectionLabel(BankingConfig.t("QUICK VIEW"));
        quickViewLabel.setForeground(Color.WHITE);
        quickViewLabel.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        sb.add(quickViewLabel);
        addNavItem(sb, "\u2605", BankingConfig.t("Today's Transactions"), () -> {
            adminCurrentView = "TodayTx";
            showTodayTransactions();
        }, false);
        addNavItem(sb, "\u25CF", BankingConfig.t("Blocked Users"), () -> {
            adminCurrentView = "Blocked";
            showBlockedUsers();
        }, false);
        JLabel toolsLabel = UIComponents.sidebarSectionLabel(BankingConfig.t("TOOLS"));
        toolsLabel.setForeground(Color.WHITE);
        toolsLabel.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        sb.add(toolsLabel);
        addNavItem(sb, "#", BankingConfig.t("QR Code"), () -> {
            adminCurrentView = "QR";
            showAdminQRPanel();
        }, false);
        addNavItem(sb, "?", BankingConfig.t("FAQ"), () -> {
            adminCurrentView = "FAQ";
            showAdminFAQPanel();
        }, false);
        sb.add(Box.createVerticalGlue());
        JPanel bottomSep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 25));
                g.drawLine(14, 0, getWidth() - 14, 0);
            }
        };
        bottomSep.setOpaque(false);
        bottomSep.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomSep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sb.add(bottomSep);
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 10));
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        JButton langBtn = sidebarBtn(BankingConfig.isAmharic ? "EN" : "AM");
        langBtn.setToolTipText(BankingConfig.isAmharic ? "Switch to English" : "Switch to Amharic");
        langBtn.addActionListener(e -> {
            BankingConfig.isAmharic = !BankingConfig.isAmharic;
            BankingConfig.resetEthFont();
            BankingConfig.applyTheme();
            hotSwap();
        });
        JButton darkBtn = sidebarDarkBtn(BankingConfig.darkMode);
        darkBtn.addActionListener(e -> {
            BankingConfig.darkMode = !BankingConfig.darkMode;
            BankingConfig.applyTheme();
            hotSwap();
        });
        JButton logoutSideBtn = new JButton("\u23FB") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isRollover()
                        ? new Color(200, 40, 40, 80)
                        : new Color(200, 40, 40, 35);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(new Color(255, 100, 100, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        logoutSideBtn.setFont(BankingConfig.uiFont(Font.BOLD, 14));
        logoutSideBtn.setForeground(new Color(255, 130, 130));
        logoutSideBtn.setOpaque(false);
        logoutSideBtn.setContentAreaFilled(false);
        logoutSideBtn.setBorderPainted(false);
        logoutSideBtn.setFocusPainted(false);
        logoutSideBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        logoutSideBtn.setPreferredSize(new Dimension(36, 30));
        logoutSideBtn.setToolTipText(BankingConfig.t("Log Out"));
        logoutSideBtn.addActionListener(e -> {
            refreshTimer.stop();
            LoginRegister.showLogin();
        });
        bottomRow.add(langBtn);
        bottomRow.add(darkBtn);
        bottomRow.add(logoutSideBtn);
        sb.add(bottomRow);
        JPanel bottomPad = new JPanel();
        bottomPad.setOpaque(false);
        bottomPad.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        sb.add(bottomPad);
        JScrollPane sidebarScroll = new JScrollPane(sb,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setBorder(BorderFactory.createEmptyBorder());
        sidebarScroll.getVerticalScrollBar().setUnitIncrement(16);
        sidebarScroll.getVerticalScrollBar().setBackground(BankingConfig.SIDEBAR_BG);
        sidebarScroll.getVerticalScrollBar().setForeground(BankingConfig.SIDEBAR_TEXT);
        sidebarScroll.setPreferredSize(new Dimension(252, 780));
        sidebarScroll.setBackground(BankingConfig.SIDEBAR_BG);
        sidebarScroll.getViewport().setBackground(BankingConfig.SIDEBAR_BG);
        return sidebarScroll;
    }
    private void addNavItem(JPanel sb, String icon, String label, Runnable action, boolean selectedByDefault) {
        UIComponents.SideItem item = new UIComponents.SideItem(icon, label, BankingConfig.PRIMARY, action);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (activeSidebarItem != null)
                    activeSidebarItem.setSelected(false);
                item.setSelected(true);
                activeSidebarItem = item;
            }
        });
        if (selectedByDefault) {
            item.setSelected(true);
            activeSidebarItem = item;
        }
        sb.add(item);
    }
    private JButton sidebarBtn(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255, 255, 255, 40) : new Color(255, 255, 255, 20))
                        : (getModel().isRollover() ? new Color(255, 255, 255, 60) : new Color(255, 255, 255, 35));
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(new Color(255, 255, 255, 120));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        btn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btn.setPreferredSize(new Dimension(54, 30));
        return btn;
    }
    private JButton sidebarDarkBtn(boolean isDark) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255, 255, 255, 50) : new Color(255, 255, 255, 22))
                        : (getModel().isRollover() ? new Color(255, 255, 255, 60) : new Color(255, 255, 255, 35));
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(new Color(255, 255, 255, 120));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(new Color(255, 255, 255, 220));
                if (isDark) {
                    int r = 5;
                    g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 0; i < 8; i++) {
                        double angle = Math.PI * 2 * i / 8;
                        int x1 = cx + (int)(Math.cos(angle) * (r + 2));
                        int y1 = cy + (int)(Math.sin(angle) * (r + 2));
                        int x2 = cx + (int)(Math.cos(angle) * (r + 5));
                        int y2 = cy + (int)(Math.sin(angle) * (r + 5));
                        g2.drawLine(x1, y1, x2, y2);
                    }
                } else {
                    int r = 6;
                    g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                    g2.setColor(new Color(0, 61, 33)); // sidebar bg colour for crescent cutout
                    g2.fillOval(cx - r + 3, cy - r - 1, r * 2, r * 2);
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
        btn.setPreferredSize(new Dimension(54, 30));
        btn.setToolTipText(isDark ? "Switch to Light Mode" : "Switch to Dark Mode");
        return btn;
    }
    private JPanel buildMainArea() {
        JPanel topBar = new JPanel(new BorderLayout(6, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(BankingConfig.CARD_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(BankingConfig.BORDER);
                ((Graphics2D) g).drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 24));
        JLabel titleLbl = new JLabel(BankingConfig.t("Admin Dashboard"));
        titleLbl.setFont(BankingConfig.uiFont(Font.BOLD, 16));
        titleLbl.setForeground(BankingConfig.FG);
        topBar.add(titleLbl, BorderLayout.WEST);
        JButton logoutBtn = makeTopBtn(BankingConfig.t("Log Out"), new Color(255, 235, 235), BankingConfig.DANGER);
        logoutBtn.addActionListener(e -> {
            refreshTimer.stop();
            LoginRegister.showLogin();
        });
        JButton adminLangBtn = makeTopBtn(
                BankingConfig.isAmharic ? "EN" : "AM",
                BankingConfig.PRIMARY_SOFT, BankingConfig.PRIMARY);
        adminLangBtn.setFont(new Font("SansSerif", Font.BOLD, 11)); // always Latin font for EN/AM text
        adminLangBtn.setPreferredSize(new Dimension(60, 32));
        adminLangBtn.setToolTipText(BankingConfig.isAmharic ? "Switch to English" : "Switch to Amharic");
        adminLangBtn.addActionListener(e -> {
            BankingConfig.isAmharic = !BankingConfig.isAmharic;
            BankingConfig.resetEthFont();
            BankingConfig.applyTheme();
            hotSwap();
        });
        JButton adminDarkBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()
                        ? BankingConfig.PRIMARY_SOFT.darker()
                        : BankingConfig.PRIMARY_SOFT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(BankingConfig.PRIMARY);
                if (BankingConfig.darkMode) {
                    int r = 5;
                    g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 0; i < 8; i++) {
                        double a = Math.PI * 2 * i / 8;
                        g2.drawLine(cx + (int) (Math.cos(a) * (r + 2)), cy + (int) (Math.sin(a) * (r + 2)),
                                cx + (int) (Math.cos(a) * (r + 5)), cy + (int) (Math.sin(a) * (r + 5)));
                    }
                } else {
                    int r = 6;
                    g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                    g2.setColor(BankingConfig.PRIMARY_SOFT);
                    g2.fillOval(cx - r + 3, cy - r - 1, r * 2, r * 2);
                }
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        adminDarkBtn.setOpaque(false);
        adminDarkBtn.setContentAreaFilled(false);
        adminDarkBtn.setBorderPainted(false);
        adminDarkBtn.setFocusPainted(false);
        adminDarkBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        adminDarkBtn.setPreferredSize(new Dimension(36, 32));
        adminDarkBtn.setToolTipText(BankingConfig.darkMode ? "Switch to Light Mode" : "Switch to Dark Mode");
        adminDarkBtn.addActionListener(e -> {
            BankingConfig.darkMode = !BankingConfig.darkMode;
            BankingConfig.applyTheme();
            adminDarkBtn.setToolTipText(BankingConfig.darkMode ? "Switch to Light Mode" : "Switch to Dark Mode");
            hotSwap();
        });
        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightBtns.setOpaque(false);
        rightBtns.add(adminLangBtn);
        rightBtns.add(adminDarkBtn);
        rightBtns.add(logoutBtn);
        topBar.add(rightBtns, BorderLayout.EAST);
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BankingConfig.BG);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BankingConfig.CARD_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BankingConfig.BORDER));
        JLabel fl = new JLabel("\u00A9 2024 " + BankingConfig.t("Commercial Bank of Ethiopia") + "  \u00B7  "
                + BankingConfig.t("Secure Banking Platform"));
        fl.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
        fl.setForeground(BankingConfig.MUTED);
        footer.add(fl);
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(BankingConfig.BG);
        mainArea.add(topBar, BorderLayout.NORTH);
        mainArea.add(contentArea, BorderLayout.CENTER);
        mainArea.add(footer, BorderLayout.SOUTH);
        return mainArea;
    }
    private JButton makeTopBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        btn.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btn.setPreferredSize(new Dimension(110, 32));
        return btn;
    }
    void showDashboard() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BankingConfig.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel title = new JLabel("" + BankingConfig.t("Dashboard Overview"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 20));
        title.setForeground(BankingConfig.FG);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        JPanel cardsGrid = new JPanel(new GridLayout(2, 3, 16, 16));
        cardsGrid.setOpaque(false);
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        cardsGrid.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Users"),
                getCount("SELECT COUNT(*) FROM accounts"), new Color(0, 107, 60)));
        cardsGrid.add(UIComponents.adminSummaryCard(BankingConfig.t("Active Users"),
                getCount("SELECT COUNT(*) FROM accounts WHERE status='Active'"), new Color(30, 136, 229)));
        cardsGrid.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Transactions"),
                getCount("SELECT COUNT(*) FROM transactions"), new Color(245, 166, 35)));
        cardsGrid.add(UIComponents.adminSummaryCard(BankingConfig.t("Today's Transactions"),
                getCount("SELECT COUNT(*) FROM transactions WHERE DATE(transaction_date)=CURDATE()"),
                new Color(156, 39, 176)));
        cardsGrid.add(UIComponents.adminSummaryCard(BankingConfig.t("Blocked Users"),
                getCount("SELECT COUNT(*) FROM accounts WHERE status='Frozen'"), new Color(229, 57, 53)));
        int totalBalance = 0;
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement("SELECT SUM(balance) as total FROM accounts");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                totalBalance = (int) rs.getDouble("total");
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardsGrid.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Balance"),
                totalBalance, new Color(0, 150, 136)));
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(cardsGrid);
        JLabel recentTitle = new JLabel("" + BankingConfig.t("Recent Transactions"));
        recentTitle.setFont(BankingConfig.uiFont(Font.BOLD, 16));
        recentTitle.setForeground(BankingConfig.FG);
        recentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(recentTitle);
        topSection.add(Box.createVerticalStrut(12));
        JPanel recentPanel = new JPanel();
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.setOpaque(false);
        recentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT t.*, a.account_holder_name FROM transactions t " +
                            "JOIN accounts a ON t.account_number = a.account_number " +
                            "ORDER BY t.transaction_date DESC LIMIT 8");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String user = rs.getString("account_holder_name");
                String type = rs.getString("transaction_type");
                double amt = rs.getDouble("amount");
                String time = new java.text.SimpleDateFormat("dd MMM, HH:mm")
                        .format(rs.getTimestamp("transaction_date"));
                JPanel row = new JPanel(new BorderLayout(12, 0)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(BankingConfig.CARD_BG);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    }
                };
                row.setOpaque(false);
                row.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                String icon = type.contains("Deposit") ? "+" : type.contains("Withdrawal") ? "\u2212" : "\u21C4";
                JLabel iconLbl = new JLabel(icon);
                iconLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
                JPanel mid = new JPanel();
                mid.setOpaque(false);
                mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
                JLabel userLbl = new JLabel(user + " • " + BankingConfig.t(type));
                userLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
                userLbl.setForeground(BankingConfig.FG);
                JLabel timeLbl = new JLabel(time);
                timeLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
                timeLbl.setForeground(BankingConfig.MUTED);
                mid.add(userLbl);
                mid.add(timeLbl);
                JLabel amtLbl = new JLabel(String.format("ETB %,.2f", amt));
                amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
                amtLbl.setForeground(type.contains("Deposit") ? BankingConfig.SUCCESS : BankingConfig.DANGER);
                row.add(iconLbl, BorderLayout.WEST);
                row.add(mid, BorderLayout.CENTER);
                row.add(amtLbl, BorderLayout.EAST);
                recentPanel.add(row);
                recentPanel.add(Box.createVerticalStrut(6));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        topSection.add(recentPanel);
        panel.add(topSection, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        contentArea.add(scroll);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showUsers() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BankingConfig.BG);
        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        cardsRow.setOpaque(false);
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Users"),
                getCount("SELECT COUNT(*) FROM accounts"), new Color(0, 107, 60)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Active Users"),
                getCount("SELECT COUNT(*) FROM accounts WHERE status='Active'"), new Color(30, 136, 229)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Blocked Users"),
                getCount("SELECT COUNT(*) FROM accounts WHERE status='Frozen'"), new Color(229, 57, 53)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("New Today"),
                getCount("SELECT COUNT(*) FROM accounts WHERE DATE(created_at)=CURDATE()"), new Color(156, 39, 176)));
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchBar.setOpaque(false);
        JTextField searchField = UIComponents.styledField("Search users...");
        searchField.setPreferredSize(new Dimension(200, 32));
        JComboBox<String> roleFilter = new JComboBox<>(new String[] {
                BankingConfig.t("All"), BankingConfig.t("Customer"), BankingConfig.t("Admin") });
        JComboBox<String> statusFilter = new JComboBox<>(new String[] {
                BankingConfig.t("All"), BankingConfig.t("Active"), BankingConfig.t("Frozen"),
                BankingConfig.t("Inactive") });
        roleFilter.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        statusFilter.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        searchBar.add(new JLabel(BankingConfig.t("Search:")));
        searchBar.add(searchField);
        searchBar.add(new JLabel(BankingConfig.t("Role:")));
        searchBar.add(roleFilter);
        searchBar.add(new JLabel(BankingConfig.t("Status:")));
        searchBar.add(statusFilter);
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        JLabel title = new JLabel(BankingConfig.t("Users Management"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        title.setForeground(BankingConfig.FG);
        top.add(title, BorderLayout.NORTH);
        top.add(cardsRow, BorderLayout.CENTER);
        top.add(searchBar, BorderLayout.SOUTH);
        String[] cols = { BankingConfig.t("Account Number"), BankingConfig.t("Holder"),
                BankingConfig.t("Username"), BankingConfig.t("Role"),
                BankingConfig.t("Status"), BankingConfig.t("Actions") };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 5;
            }
        };
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM accounts ORDER BY created_at DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("account_number"), rs.getString("account_holder_name"),
                        rs.getString("username"), BankingConfig.t(rs.getString("role")),
                        BankingConfig.t(rs.getString("status")), BankingConfig.t("Actions") });
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JTable table = buildStyledTable(model);
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new UserActionEditor(table));
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        Runnable filter = () -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            String q = searchField.getText().trim();
            if (!q.isEmpty())
                filters.add(RowFilter.regexFilter("(?i)" + q));
            String role = (String) roleFilter.getSelectedItem();
            if (!BankingConfig.t("All").equals(role))
                filters.add(RowFilter.regexFilter("(?i)" + role, 3));
            String status = (String) statusFilter.getSelectedItem();
            if (!BankingConfig.t("All").equals(status))
                filters.add(RowFilter.regexFilter("(?i)" + status, 4));
            sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
        };
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter.run();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter.run();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter.run();
            }
        });
        roleFilter.addActionListener(e -> filter.run());
        statusFilter.addActionListener(e -> filter.run());
        panel.add(top, BorderLayout.NORTH);
        panel.add(buildStyledScroll(table), BorderLayout.CENTER);
        contentArea.add(panel);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showTransactions() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BankingConfig.BG);
        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        cardsRow.setOpaque(false);
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Transactions"),
                getCount("SELECT COUNT(*) FROM transactions"), new Color(0, 107, 60)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Today"),
                getCount("SELECT COUNT(*) FROM transactions WHERE DATE(transaction_date)=CURDATE()"),
                new Color(30, 136, 229)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("This Month"),
                getCount("SELECT COUNT(*) FROM transactions WHERE MONTH(transaction_date)=MONTH(CURDATE())"),
                new Color(245, 166, 35)));
        int totalVolume = 0;
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement("SELECT SUM(amount) as total FROM transactions");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                totalVolume = (int) rs.getDouble("total");
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Volume"),
                totalVolume, new Color(156, 39, 176)));
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchBar.setOpaque(false);
        JTextField searchField = UIComponents.styledField("Search transactions...");
        searchField.setPreferredSize(new Dimension(200, 32));
        JComboBox<String> typeFilter = new JComboBox<>(new String[] {
                BankingConfig.t("All"), BankingConfig.t("Deposit"), BankingConfig.t("Withdrawal"),
                BankingConfig.t("Transfer Out"), BankingConfig.t("Transfer In") });
        typeFilter.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        JButton exportBtn = makeTopBtn(BankingConfig.t("Export CSV"), BankingConfig.PRIMARY_SOFT,
                BankingConfig.PRIMARY);
        exportBtn.addActionListener(e -> exportTransactions());
        searchBar.add(new JLabel(BankingConfig.t("Search:")));
        searchBar.add(searchField);
        searchBar.add(new JLabel(BankingConfig.t("Type:")));
        searchBar.add(typeFilter);
        searchBar.add(exportBtn);
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        JLabel title = new JLabel(BankingConfig.t("Transactions Management"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        title.setForeground(BankingConfig.FG);
        top.add(title, BorderLayout.NORTH);
        top.add(cardsRow, BorderLayout.CENTER);
        top.add(searchBar, BorderLayout.SOUTH);
        String[] cols = { "ID", BankingConfig.t("Account Number"), BankingConfig.t("Type"),
                BankingConfig.t("Amount (ETB)"), BankingConfig.t("Date & Time"),
                BankingConfig.t("Description"), BankingConfig.t("Download") };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 6;
            }
        };
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT 500");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("transaction_id"), rs.getString("account_number"),
                        BankingConfig.t(rs.getString("transaction_type")),
                        String.format("%,.2f", rs.getDouble("amount")),
                        BankingConfig.dateFormat.format(rs.getTimestamp("transaction_date")),
                        rs.getString("description"), BankingConfig.t("Download") });
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JTable table = buildStyledTable(model);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                if (sel) {
                    setBackground(BankingConfig.PRIMARY_SOFT);
                    setForeground(BankingConfig.PRIMARY_DARK);
                } else {
                    setBackground(row % 2 == 0 ? BankingConfig.CARD_BG : BankingConfig.BG);
                    setForeground(BankingConfig.FG);
                    if (col == 2 && v != null) {
                        String tp = v.toString();
                        if (tp.contains("Deposit") || tp.contains("Transfer In"))
                            setForeground(BankingConfig.SUCCESS);
                        else if (tp.contains("Withdrawal") || tp.contains("Transfer Out"))
                            setForeground(BankingConfig.DANGER);
                    }
                    if (col == 3)
                        setHorizontalAlignment(JLabel.RIGHT);
                    else
                        setHorizontalAlignment(JLabel.LEFT);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new DownloadButtonEditor(table));
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        Runnable filter = () -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            String q = searchField.getText().trim();
            if (!q.isEmpty())
                filters.add(RowFilter.regexFilter("(?i)" + q));
            String type = (String) typeFilter.getSelectedItem();
            if (!BankingConfig.t("All").equals(type))
                filters.add(RowFilter.regexFilter("(?i)" + type, 2));
            sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
        };
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter.run();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter.run();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter.run();
            }
        });
        typeFilter.addActionListener(e -> filter.run());
        panel.add(top, BorderLayout.NORTH);
        panel.add(buildStyledScroll(table), BorderLayout.CENTER);
        contentArea.add(panel);
        contentArea.revalidate();
        contentArea.repaint();
    }
    private void exportTransactions() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("transactions_export.csv"));
        if (chooser.showSaveDialog(contentArea) != JFileChooser.APPROVE_OPTION)
            return;
        try (java.io.FileWriter w = new java.io.FileWriter(chooser.getSelectedFile())) {
            w.write("ID,Account,Type,Amount,Date,Description\n");
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions ORDER BY transaction_date DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                w.write(String.format("%d,%s,%s,%.2f,%s,\"%s\"\n",
                        rs.getInt("transaction_id"), rs.getString("account_number"),
                        rs.getString("transaction_type"), rs.getDouble("amount"),
                        BankingConfig.dateFormat.format(rs.getTimestamp("transaction_date")),
                        rs.getString("description").replace("\"", "\"\"")));
            }
            rs.close();
            ps.close();
            JOptionPane.showMessageDialog(contentArea, BankingConfig.t("Export successful!"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentArea, BankingConfig.t("Export failed: ") + e.getMessage());
        }
    }
    void showTodayTransactions() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BankingConfig.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);
        JLabel title = new JLabel("\u2605  " + BankingConfig.t("Today's Transactions"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        title.setForeground(BankingConfig.FG);
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        statsRow.setOpaque(false);
        int todayCount = getCount("SELECT COUNT(*) FROM transactions WHERE DATE(transaction_date)=CURDATE()");
        double todayVol = 0;
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT SUM(amount) FROM transactions WHERE DATE(transaction_date)=CURDATE()");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                todayVol = rs.getDouble(1);
            rs.close();
            ps.close();
        } catch (Exception ignored) {
        }
        statsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Today's Transactions"), todayCount,
                new Color(30, 136, 229)));
        statsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Volume"),
                (int) todayVol, new Color(0, 150, 136)));
        statsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("New Today"),
                getCount("SELECT COUNT(*) FROM accounts WHERE DATE(created_at)=CURDATE()"), new Color(156, 39, 176)));
        top.add(title, BorderLayout.NORTH);
        top.add(statsRow, BorderLayout.CENTER);
        String[] cols = { "#", BankingConfig.t("Account Number"), BankingConfig.t("Type"),
                BankingConfig.t("Amount (ETB)"), BankingConfig.t("Date & Time"), BankingConfig.t("Description") };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions WHERE DATE(transaction_date)=CURDATE() ORDER BY transaction_date DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("transaction_id"),
                        rs.getString("account_number"),
                        BankingConfig.t(rs.getString("transaction_type")),
                        String.format("%,.2f", rs.getDouble("amount")),
                        BankingConfig.dateFormat.format(rs.getTimestamp("transaction_date")),
                        rs.getString("description") != null ? rs.getString("description") : ""
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        panel.add(top, BorderLayout.NORTH);
        panel.add(buildStyledScroll(buildStyledTable(model)), BorderLayout.CENTER);
        contentArea.add(panel);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showBlockedUsers() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BankingConfig.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);
        JLabel title = new JLabel("\u25CF  " + BankingConfig.t("Blocked Users"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        title.setForeground(BankingConfig.FG);
        int blockedCount = getCount("SELECT COUNT(*) FROM accounts WHERE status='Frozen'");
        JPanel statsRow = new JPanel(new GridLayout(1, 2, 12, 0));
        statsRow.setOpaque(false);
        statsRow.add(
                UIComponents.adminSummaryCard(BankingConfig.t("Blocked Users"), blockedCount, new Color(229, 57, 53)));
        statsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Users"),
                getCount("SELECT COUNT(*) FROM accounts"), new Color(30, 136, 229)));
        top.add(title, BorderLayout.NORTH);
        top.add(statsRow, BorderLayout.CENTER);
        String[] cols = { BankingConfig.t("Account Number"), BankingConfig.t("Account Holder"),
                BankingConfig.t("Email"), BankingConfig.t("Phone"),
                BankingConfig.t("Account Type"), BankingConfig.t("Actions") };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 5;
            }
        };
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM accounts WHERE status='Frozen' ORDER BY created_at DESC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("account_number"),
                        rs.getString("account_holder_name"),
                        rs.getString("email") != null ? rs.getString("email") : "-",
                        rs.getString("phone") != null ? rs.getString("phone") : "-",
                        rs.getString("account_type"),
                        BankingConfig.t("Actions")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JTable table = buildStyledTable(model);
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionCellRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new UserActionEditor(table));
        panel.add(top, BorderLayout.NORTH);
        panel.add(buildStyledScroll(table), BorderLayout.CENTER);
        contentArea.add(panel);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showActivity() {
        contentArea.removeAll();
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(BankingConfig.BG);
        JLabel title = new JLabel(BankingConfig.t("Activity Monitor"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        title.setForeground(BankingConfig.FG);
        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        cardsRow.setOpaque(false);
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Active Users"),
                getCount("SELECT COUNT(*) FROM accounts WHERE status='Active'"), new Color(0, 107, 60)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Total Accounts"),
                getCount("SELECT COUNT(*) FROM accounts"), new Color(30, 136, 229)));
        cardsRow.add(UIComponents.adminSummaryCard(BankingConfig.t("Transactions Today"),
                getCount("SELECT COUNT(*) FROM transactions WHERE DATE(transaction_date)=CURDATE()"),
                new Color(245, 166, 35)));
        JTextArea log = new JTextArea();
        log.setFont(new Font("Consolas", Font.PLAIN, 12));
        log.setEditable(false);
        log.setBackground(BankingConfig.CARD_BG);
        log.setForeground(BankingConfig.FG);
        log.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        StringBuilder sb = new StringBuilder();
        sb.append("=== " + BankingConfig.t("Commercial Bank of Ethiopia") + " - " + BankingConfig.t("Recent Activity")
                + " ===\n\n");
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions ORDER BY transaction_date DESC LIMIT 20");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sb.append(String.format("%s  |  %-16s  |  ETB %,.2f  |  %s\n",
                        BankingConfig.dateFormat.format(rs.getTimestamp("transaction_date")),
                        rs.getString("transaction_type"),
                        rs.getDouble("amount"),
                        rs.getString("account_number")));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            sb.append(BankingConfig.t("Error loading log") + "\n");
        }
        log.setText(sb.toString());
        JScrollPane scroll = new JScrollPane(log);
        scroll.setBorder(BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true));
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(cardsRow, BorderLayout.SOUTH);
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        contentArea.add(panel);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showAdminQRPanel() {
        contentArea.removeAll();
        JPanel wrap = new JPanel(new BorderLayout(0, 0));
        wrap.setBackground(BankingConfig.BG);
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BankingConfig.BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("@  " + BankingConfig.t("Admin Dashboard"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 15));
        title.setForeground(BankingConfig.FG);
        titleBar.add(title);
        String qrData = "CBE|ADMIN|DASHBOARD";
        java.awt.image.BufferedImage qrImg = QRCodeGenerator.generate(qrData, 260);
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(34, 0, 24, 0));
        JPanel card = new JPanel(new BorderLayout(0, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 12));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, 18, 18);
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 18, 18);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(24, 30, 20, 30));
        card.setMaximumSize(new Dimension(360, 420));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel qrLbl = new JLabel(new ImageIcon(qrImg));
        qrLbl.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(qrLbl, BorderLayout.CENTER);
        JLabel hint = new JLabel(BankingConfig.t("Scan to identify admin panel endpoint"));
        hint.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        hint.setForeground(BankingConfig.MUTED);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(hint, BorderLayout.SOUTH);
        center.add(card);
        wrap.add(titleBar, BorderLayout.NORTH);
        wrap.add(center, BorderLayout.CENTER);
        contentArea.add(wrap);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showAdminFAQPanel() {
        contentArea.removeAll();
        JPanel wrap = new JPanel(new BorderLayout(0, 0));
        wrap.setBackground(BankingConfig.BG);
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BankingConfig.BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("?  " + BankingConfig.t("Frequently Asked Questions"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 15));
        title.setForeground(BankingConfig.FG);
        titleBar.add(title);
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(BankingConfig.BG);
        list.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        String[][] faqs = {
                { BankingConfig.t("How do I manage users?"),
                        BankingConfig.t("Open Users Management to edit, block, unblock, or delete accounts.") },
                { BankingConfig.t("How do I monitor activity?"),
                        BankingConfig.t("Open Activity Monitor to review recent transaction events.") },
                { BankingConfig.t("How do I export transactions?"),
                        BankingConfig.t("Open Transactions Management and click Export CSV.") },
                { BankingConfig.t("Why can't I see customer password?"),
                        BankingConfig.t("Passwords are securely hashed and cannot be viewed in plain text.") },
                { BankingConfig.t("How do I switch language/theme?"),
                        BankingConfig.t("Use EN/AM and dark mode toggles at the bottom of the sidebar.") }
        };
        for (String[] faq : faqs) {
            JPanel item = new JPanel(new BorderLayout(0, 6)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, 8));
                    g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 2, 12, 12);
                    g2.setColor(BankingConfig.CARD_BG);
                    g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 12, 12);
                    g2.setColor(BankingConfig.PRIMARY);
                    g2.fillRoundRect(0, 8, 4, getHeight() - 16, 4, 4);
                }
            };
            item.setOpaque(false);
            item.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 14));
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            JLabel q = new JLabel("Q: " + faq[0]);
            q.setFont(BankingConfig.uiFont(Font.BOLD, 13));
            q.setForeground(BankingConfig.PRIMARY);
            JLabel a = new JLabel("<html><body style='width:620px'>" + faq[1] + "</body></html>");
            a.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            a.setForeground(BankingConfig.FG);
            item.add(q, BorderLayout.NORTH);
            item.add(a, BorderLayout.CENTER);
            list.add(item);
            list.add(Box.createVerticalStrut(10));
        }
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        wrap.add(titleBar, BorderLayout.NORTH);
        wrap.add(scroll, BorderLayout.CENTER);
        contentArea.add(wrap);
        contentArea.revalidate();
        contentArea.repaint();
    }
    private JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 4));
        table.setBackground(BankingConfig.CARD_BG);
        table.setForeground(BankingConfig.FG);
        table.setSelectionBackground(BankingConfig.PRIMARY_SOFT);
        table.setSelectionForeground(BankingConfig.PRIMARY_DARK);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                if (sel) {
                    setBackground(BankingConfig.PRIMARY_SOFT);
                    setForeground(BankingConfig.PRIMARY_DARK);
                } else {
                    setBackground(row % 2 == 0 ? BankingConfig.CARD_BG : BankingConfig.BG);
                    setForeground(BankingConfig.FG);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
        JTableHeader header = table.getTableHeader();
        header.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        header.setBackground(BankingConfig.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                setBackground(BankingConfig.PRIMARY);
                setForeground(Color.WHITE);
                setFont(BankingConfig.uiFont(Font.BOLD, 12));
                setHorizontalAlignment(JLabel.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
        return table;
    }
    private JScrollPane buildStyledScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true));
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        scroll.setBackground(BankingConfig.BG);
        return scroll;
    }
    private int getCount(String query) {
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            rs.close();
            ps.close();
            return count;
        } catch (Exception e) {
            return 0;
        }
    }
    class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel lbl = new JLabel();
        public ActionCellRenderer() {
            setLayout(new GridBagLayout());
            setOpaque(true);
            lbl.setFont(BankingConfig.uiFont(Font.BOLD, 11));
            lbl.setForeground(Color.WHITE);
            lbl.setOpaque(true);
            lbl.setBackground(BankingConfig.PRIMARY);
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BankingConfig.PRIMARY_DARK, 1, true),
                    BorderFactory.createEmptyBorder(5, 14, 5, 14)));
            add(lbl);
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            lbl.setText("⚙  " + (v != null ? v.toString() : BankingConfig.t("Actions")));
            setBackground(sel ? BankingConfig.PRIMARY_SOFT : (row % 2 == 0 ? BankingConfig.CARD_BG : BankingConfig.BG));
            return this;
        }
    }
    class UserActionEditor extends DefaultCellEditor {
        private final JButton button;
        private final JTable table;
        public UserActionEditor(JTable table) {
            super(new JCheckBox());
            this.table = table;
            button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? BankingConfig.PRIMARY_LIGHT : BankingConfig.PRIMARY);
                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                    g2.setColor(BankingConfig.PRIMARY_DARK);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 8, 8);
                    super.paintComponent(g);
                }
                @Override
                protected void paintBorder(Graphics g) {
                }
            };
            button.setFont(BankingConfig.uiFont(Font.BOLD, 11));
            button.setForeground(Color.WHITE);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            button.addActionListener(e -> showActionsMenu());
        }
        private void showActionsMenu() {
            int viewRow = table.getSelectedRow();
            if (viewRow < 0)
                return;
            int modelRow = table.convertRowIndexToModel(viewRow);
            String accNo = (String) table.getModel().getValueAt(modelRow, 0);
            JPopupMenu menu = new JPopupMenu();
            menu.setBackground(BankingConfig.CARD_BG);
            Font menuFont = BankingConfig.uiFont(Font.PLAIN, 13);
            JMenuItem edit = new JMenuItem(BankingConfig.t("Edit Account"));
            edit.setFont(menuFont);
            edit.setBackground(BankingConfig.CARD_BG);
            edit.setForeground(BankingConfig.FG);
            JMenuItem block = new JMenuItem(BankingConfig.t("Block"));
            block.setFont(menuFont);
            block.setBackground(BankingConfig.CARD_BG);
            block.setForeground(BankingConfig.DANGER);
            JMenuItem unblock = new JMenuItem(BankingConfig.t("Unblock"));
            unblock.setFont(menuFont);
            unblock.setBackground(BankingConfig.CARD_BG);
            unblock.setForeground(BankingConfig.SUCCESS);
            JMenuItem delete = new JMenuItem(BankingConfig.t("Delete"));
            delete.setFont(menuFont);
            delete.setBackground(BankingConfig.CARD_BG);
            delete.setForeground(BankingConfig.DANGER);
            edit.addActionListener(e -> editAccount(accNo));
            block.addActionListener(e -> blockAccount(accNo));
            unblock.addActionListener(e -> unblockAccount(accNo));
            delete.addActionListener(e -> deleteAccount(accNo));
            menu.add(edit);
            menu.addSeparator();
            menu.add(block);
            menu.add(unblock);
            menu.addSeparator();
            menu.add(delete);
            menu.show(button, 0, button.getHeight());
        }
        @Override
        public Component getTableCellEditorComponent(
                JTable t, Object v, boolean sel, int row, int col) {
            button.setText(v != null ? v.toString() : BankingConfig.t("Actions"));
            return button;
        }
        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
    class DownloadButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final JTable table;
        public DownloadButtonEditor(JTable table) {
            super(new JCheckBox());
            this.table = table;
            button = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? BankingConfig.INFO.brighter() : BankingConfig.INFO);
                    g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
                    g2.setColor(BankingConfig.INFO.darker());
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 8, 8);
                    super.paintComponent(g);
                }
                @Override
                protected void paintBorder(Graphics g) {
                }
            };
            button.setFont(BankingConfig.uiFont(Font.BOLD, 11));
            button.setForeground(Color.WHITE);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            button.addActionListener(e -> saveReceipt());
        }
        private void saveReceipt() {
            int viewRow = table.getSelectedRow();
            if (viewRow < 0)
                return;
            int modelRow = table.convertRowIndexToModel(viewRow);
            int txId = Integer.parseInt(table.getModel().getValueAt(modelRow, 0).toString());
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File("receipt_" + txId + ".pdf"));
            if (chooser.showSaveDialog(AppFrame.get()) == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                BankingConfig.generateReceiptPDF(txId, filePath);
                JOptionPane.showMessageDialog(AppFrame.get(), BankingConfig.t("Receipt downloaded.") + "\n" + filePath);
                try {
                    java.awt.Desktop.getDesktop().open(new File(filePath));
                } catch (Exception openEx) {
                }
            }
        }
        @Override
        public Component getTableCellEditorComponent(
                JTable t, Object v, boolean sel, int row, int col) {
            button.setText(v != null ? v.toString() : BankingConfig.t("Download"));
            return button;
        }
        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
    private void editAccount(String accNo) {
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=?");
            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return;
            }
            String holder = rs.getString("account_holder_name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            String role = rs.getString("role");
            String status = rs.getString("status");
            rs.close();
            ps.close();
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(BankingConfig.CARD_BG);
            form.setPreferredSize(new Dimension(420, 280));
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(8, 8, 8, 8);
            gc.anchor = GridBagConstraints.WEST;
            gc.fill = GridBagConstraints.HORIZONTAL;
            JTextField holderField = UIComponents.styledField(holder);
            holderField.setText(holder);
            JTextField emailField = UIComponents.styledField(email != null ? email : "");
            emailField.setText(email != null ? email : "");
            JTextField phoneField = UIComponents.styledField(phone != null ? phone : "");
            phoneField.setText(phone != null ? phone : "");
            JComboBox<String> roleBox = new JComboBox<>(new String[] { "Customer", "Admin" });
            roleBox.setSelectedItem(role);
            JComboBox<String> statusBox = new JComboBox<>(new String[] { "Active", "Frozen", "Inactive" });
            statusBox.setSelectedItem(status);
            String[] labels = { BankingConfig.t("Account Holder"), BankingConfig.t("Email"),
                    BankingConfig.t("Phone"), BankingConfig.t("Role"), BankingConfig.t("Status") };
            Component[] fields = { holderField, emailField, phoneField, roleBox, statusBox };
            for (int i = 0; i < labels.length; i++) {
                gc.gridx = 0;
                gc.gridy = i;
                JLabel lbl = new JLabel(labels[i] + ":");
                lbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
                lbl.setForeground(BankingConfig.FG);
                form.add(lbl, gc);
                gc.gridx = 1;
                form.add(fields[i], gc);
            }
            int confirmed = JOptionPane.showConfirmDialog(AppFrame.get(), form,
                    BankingConfig.t("Edit Account Details"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (confirmed != JOptionPane.OK_OPTION)
                return;
            PreparedStatement update = BankingConfig.con.prepareStatement(
                    "UPDATE accounts SET account_holder_name=?, email=?, phone=?, role=?, status=? WHERE account_number=?");
            update.setString(1, holderField.getText().trim());
            update.setString(2, emailField.getText().trim());
            update.setString(3, phoneField.getText().trim());
            update.setString(4, (String) roleBox.getSelectedItem());
            update.setString(5, (String) statusBox.getSelectedItem());
            update.setString(6, accNo);
            update.executeUpdate();
            update.close();
            JOptionPane.showMessageDialog(AppFrame.get(), BankingConfig.t("User updated successfully."));
            showUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void blockAccount(String accNo) {
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "UPDATE accounts SET status='Frozen' WHERE account_number=?");
            ps.setString(1, accNo);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(AppFrame.get(), BankingConfig.t("Account blocked."));
            showUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void unblockAccount(String accNo) {
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "UPDATE accounts SET status='Active' WHERE account_number=?");
            ps.setString(1, accNo);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(AppFrame.get(), BankingConfig.t("Account unblocked."));
            showUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteAccount(String accNo) {
        int confirm = JOptionPane.showConfirmDialog(AppFrame.get(),
                BankingConfig.t("Are you sure you want to delete this account?"),
                BankingConfig.t("Confirm Delete"), JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "DELETE FROM accounts WHERE account_number=?");
            ps.setString(1, accNo);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(AppFrame.get(), BankingConfig.t("Account deleted successfully."));
            showUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}