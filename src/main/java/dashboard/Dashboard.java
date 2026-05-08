import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class Dashboard {
    final String accountNumber;
    final String accountHolder;
    JLabel balanceLabel;
    JLabel heroBalLbl;
    JPanel contentArea;
    UIComponents.SideItem activeItem;
    final List<UIComponents.SideItem> sidebarItems = new ArrayList<>();
    JButton backBtn;
    String currentView = "Home";
    javax.swing.Timer balanceTimer;
    JComponent sidebarPanel;
    boolean balanceHidden = false;
    boolean sidebarCollapsed = false;
    double cachedBalance = 0.0;
    JButton sidebarBalToggleBtn;
    JButton heroBalToggleBtn;
    private DashboardSidebar sidebar;
    private DashboardViews views;
    public Dashboard(String accountNumber, String accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.sidebar = new DashboardSidebar(this);
        this.views = new DashboardViews(this);
        BankingConfig.applyTheme();
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BankingConfig.BG);
        sidebarPanel = sidebar.buildSidebar();
        JPanel mainArea = buildMainArea(sidebarPanel);
        root.add(sidebarPanel, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);
        AppFrame.get().showPage(root, "CBE Banking System - " + accountHolder);
        balanceTimer = new javax.swing.Timer(5000, e -> {
            try {
                updateBalance();
            } catch (Exception ignored) {
            }
        });
        balanceTimer.start();
        new Thread(() -> {
            try {
                cachedBalance = getBalance();
            } catch (Exception ignored) {}
            SwingUtilities.invokeLater(() -> views.showHome());
        }).start();
    }
    void showLoading() {
        contentArea.removeAll();
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BankingConfig.BG);
        JLabel lbl = new JLabel(BankingConfig.t("Loading..."));
        lbl.setFont(BankingConfig.uiFont(Font.PLAIN, 14));
        lbl.setForeground(BankingConfig.MUTED);
        p.add(lbl);
        contentArea.add(p, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void hotSwap() {
        JPanel root = (JPanel) AppFrame.get().getContentPane();
        root.removeAll();
        sidebarPanel = sidebar.buildSidebar();
        JPanel mainArea = buildMainArea(sidebarPanel);
        root.setBackground(BankingConfig.BG);
        root.add(sidebarPanel, BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);
        Color bg = BankingConfig.BG;
        Color card = BankingConfig.CARD_BG;
        Color fg = BankingConfig.FG;
        Color muted = BankingConfig.MUTED;
        Color bord = BankingConfig.BORDER;
        Color prim = BankingConfig.PRIMARY;
        Color psoft = BankingConfig.PRIMARY_SOFT;
        javax.swing.UIManager.put("Panel.background", bg);
        javax.swing.UIManager.put("ScrollPane.background", bg);
        javax.swing.UIManager.put("Viewport.background", bg);
        javax.swing.UIManager.put("ToolBar.background", card);
        javax.swing.UIManager.put("TextField.background", card);
        javax.swing.UIManager.put("TextField.foreground", fg);
        javax.swing.UIManager.put("TextField.caretForeground", fg);
        javax.swing.UIManager.put("TextField.selectionBackground", prim);
        javax.swing.UIManager.put("TextField.selectionForeground", Color.WHITE);
        javax.swing.UIManager.put("TextField.border",
                BorderFactory.createLineBorder(bord, 1));
        javax.swing.UIManager.put("PasswordField.background", card);
        javax.swing.UIManager.put("PasswordField.foreground", fg);
        javax.swing.UIManager.put("PasswordField.caretForeground", fg);
        javax.swing.UIManager.put("ComboBox.background", card);
        javax.swing.UIManager.put("ComboBox.foreground", fg);
        javax.swing.UIManager.put("ComboBox.selectionBackground", prim);
        javax.swing.UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        javax.swing.UIManager.put("ComboBox.border",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bord, 1),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        javax.swing.UIManager.put("Button.background", psoft);
        javax.swing.UIManager.put("Button.foreground", prim);
        javax.swing.UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        javax.swing.UIManager.put("Button.border",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(prim, 1, true),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        javax.swing.UIManager.put("ToggleButton.background", psoft);
        javax.swing.UIManager.put("ToggleButton.foreground", prim);
        javax.swing.UIManager.put("ToggleButton.border",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(prim, 1, true),
                        BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        javax.swing.UIManager.put("Label.foreground", fg);
        javax.swing.UIManager.put("TextArea.background", card);
        javax.swing.UIManager.put("TextArea.foreground", fg);
        javax.swing.UIManager.put("TextArea.caretForeground", fg);
        javax.swing.UIManager.put("TextArea.selectionBackground", prim);
        javax.swing.UIManager.put("TextArea.selectionForeground", Color.WHITE);
        javax.swing.UIManager.put("TextArea.border",
                BorderFactory.createLineBorder(bord, 1));
        javax.swing.UIManager.put("Table.background", bg);
        javax.swing.UIManager.put("Table.foreground", fg);
        javax.swing.UIManager.put("Table.selectionBackground", psoft);
        javax.swing.UIManager.put("Table.selectionForeground", prim);
        javax.swing.UIManager.put("Table.gridColor", bord);
        javax.swing.UIManager.put("TableHeader.background", prim);
        javax.swing.UIManager.put("TableHeader.foreground", Color.WHITE);
        javax.swing.UIManager.put("ScrollPane.viewportBorder", BorderFactory.createEmptyBorder());
        SwingUtilities.updateComponentTreeUI(AppFrame.get());
        AppFrame.get().revalidate();
        AppFrame.get().repaint();
        String view = currentView;
        switch (view) {
            case "Home":
                views.showHome();
                break;
            case "History":
                views.showHistory();
                break;
            case "Profile":
                views.showProfile();
                break;
            case "QR":
                views.showQR();
                break;
            case "FAQ":
                views.showFAQ();
                break;
            case "Deposit":
                deposit();
                break;
            case "Withdraw":
                withdraw();
                break;
            case "Transfer":
                transfer();
                break;
            case "ChangePin":
                changePin();
                break;
            case "Airtime":
                airtime();
                break;
            case "Travel":
                travel_booking();
                break;
            default:
                views.showHome();
                break;
        }
    }
    JPanel buildMainArea(JComponent sidebarComponent) {
        JPanel topBar = new JPanel(new BorderLayout(6, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(BankingConfig.BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 16));
        JButton hamburger = new JButton("☰") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(255, 255, 255, 40) : new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        hamburger.setFont(BankingConfig.uiFont(Font.BOLD, 16));
        hamburger.setForeground(new Color(200, 240, 215));
        hamburger.setOpaque(false);
        hamburger.setContentAreaFilled(false);
        hamburger.setBorderPainted(false);
        hamburger.setFocusPainted(false);
        hamburger.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        hamburger.setPreferredSize(new Dimension(36, 32));
        hamburger.setToolTipText("Toggle Sidebar");
        hamburger.addActionListener(e -> {
            sidebarCollapsed = !sidebarCollapsed;
            Dimension newSize = sidebarCollapsed
                    ? new Dimension(DashboardSidebar.SIDEBAR_MINI, 0)
                    : new Dimension(DashboardSidebar.SIDEBAR_FULL, 0);
            sidebarPanel.setPreferredSize(newSize);
            sidebarPanel.setMaximumSize(newSize);
            sidebarPanel.revalidate();
            JPanel wrapper = (JPanel) sidebarPanel.getClientProperty("wrapper");
            if (wrapper != null) {
                wrapper.setPreferredSize(newSize);
                wrapper.revalidate();
            }
            DashboardSidebar sidebarRef = sidebar;
            JPanel sidebarInner = sidebarRef.getSidebarInner();
            if (sidebarInner != null) {
                for (Component c : sidebarInner.getComponents()) {
                    if (c instanceof JPanel && ((JComponent) c).getClientProperty("nameLbl") != null) {
                        JPanel nameLblPanel = (JPanel) ((JComponent) c).getClientProperty("nameLbl");
                        JPanel lb1Panel = (JPanel) ((JComponent) c).getClientProperty("lb1");
                        JPanel lb2Panel = (JPanel) ((JComponent) c).getClientProperty("lb2");
                        JPanel balRowPanel = (JPanel) ((JComponent) c).getClientProperty("balRow");
                        JPanel bottomRowPanel = (JPanel) ((JComponent) c).getClientProperty("bottomRow");
                        JPanel logoTextPanel = (JPanel) ((JComponent) c).getClientProperty("logoTextPanel");
                        JPanel userInfoPanel = (JPanel) ((JComponent) c).getClientProperty("userInfo");
                        boolean hide = sidebarCollapsed;
                        if (nameLblPanel != null)
                            nameLblPanel.setVisible(!hide);
                        if (lb1Panel != null)
                            lb1Panel.setVisible(!hide);
                        if (lb2Panel != null)
                            lb2Panel.setVisible(!hide);
                        if (balRowPanel != null)
                            balRowPanel.setVisible(!hide);
                        if (bottomRowPanel != null)
                            bottomRowPanel.setVisible(!hide);
                        if (logoTextPanel != null)
                            logoTextPanel.setVisible(!hide);
                        if (userInfoPanel != null)
                            userInfoPanel.setVisible(!hide);
                        break;
                    }
                }
                for (Component c : sidebarInner.getComponents()) {
                    if (c instanceof JPanel && ((JComponent) c).getClientProperty("selected") != null) {
                        JPanel card = (JPanel) c;
                        JLabel textLbl = (JLabel) ((JComponent) card).getClientProperty("textLbl");
                        if (textLbl != null) {
                            textLbl.setVisible(!sidebarCollapsed);
                        }
                        card.setPreferredSize(new Dimension(
                                sidebarCollapsed ? DashboardSidebar.SIDEBAR_MINI - 12
                                        : DashboardSidebar.SIDEBAR_FULL - 12,
                                52));
                    }
                }
                sidebarInner.revalidate();
                sidebarInner.repaint();
            }
        });
        JButton langBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Color iconBg = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255,255,255,40) : new Color(255,255,255,20))
                        : (getModel().isRollover() ? BankingConfig.PRIMARY_SOFT.darker() : BankingConfig.PRIMARY_SOFT);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                if (!BankingConfig.darkMode) {
                    g2.setColor(BankingConfig.PRIMARY);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
                }
                String label = BankingConfig.isAmharic ? "EN" : "AM";
                Font labelFont = new Font("SansSerif", Font.BOLD, 13);
                g2.setFont(labelFont);
                g2.setColor(BankingConfig.darkMode ? new Color(200, 240, 215) : BankingConfig.PRIMARY_DARK);
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(label)) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(label, tx, ty);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        langBtn.setOpaque(false);
        langBtn.setContentAreaFilled(false);
        langBtn.setBorderPainted(false);
        langBtn.setFocusPainted(false);
        langBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        langBtn.setPreferredSize(new Dimension(48, 28));
        langBtn.setToolTipText(BankingConfig.isAmharic ? "Switch to English" : "Switch to Amharic");
        langBtn.addActionListener(e -> {
            BankingConfig.isAmharic = !BankingConfig.isAmharic;
            BankingConfig.resetEthFont();
            BankingConfig.applyTheme();
            hotSwap();
        });
        JButton darkBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color iconBg = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255,255,255,50) : new Color(255,255,255,22))
                        : (getModel().isRollover() ? BankingConfig.PRIMARY_SOFT.darker() : BankingConfig.PRIMARY_SOFT);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                if (!BankingConfig.darkMode) {
                    g2.setColor(BankingConfig.PRIMARY);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
                }
                int cx = getWidth() / 2, cy = getHeight() / 2;
                if (BankingConfig.darkMode) {
                    g2.setColor(new Color(220, 245, 225));
                    g2.fillOval(cx - 5, cy - 5, 10, 10);
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (int i = 0; i < 8; i++) {
                        double a = Math.PI * 2 * i / 8;
                        g2.drawLine(cx + (int)(Math.cos(a) * 7), cy + (int)(Math.sin(a) * 7),
                                cx + (int)(Math.cos(a) * 10), cy + (int)(Math.sin(a) * 10));
                    }
                } else {
                    Color moonColor = BankingConfig.PRIMARY_DARK;
                    Color cutColor  = BankingConfig.PRIMARY_SOFT;
                    g2.setColor(moonColor);
                    g2.fillOval(cx - 6, cy - 6, 12, 12);
                    g2.setColor(cutColor);
                    g2.fillOval(cx - 3, cy - 7, 12, 12);
                }
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        darkBtn.setOpaque(false);
        darkBtn.setContentAreaFilled(false);
        darkBtn.setBorderPainted(false);
        darkBtn.setFocusPainted(false);
        darkBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        darkBtn.setPreferredSize(new Dimension(40, 28));
        darkBtn.setToolTipText(BankingConfig.darkMode ? "Switch to Light Mode" : "Switch to Dark Mode");
        darkBtn.addActionListener(e -> {
            BankingConfig.darkMode = !BankingConfig.darkMode;
            BankingConfig.applyTheme();
            hotSwap();
        });
        JButton refreshBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color iconFg = BankingConfig.darkMode ? new Color(220, 245, 225) : BankingConfig.PRIMARY_DARK;
                Color iconBg = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255,255,255,40) : new Color(255,255,255,20))
                        : (getModel().isRollover() ? BankingConfig.PRIMARY_SOFT.darker() : BankingConfig.PRIMARY_SOFT);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                if (!BankingConfig.darkMode) {
                    g2.setColor(BankingConfig.PRIMARY);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
                }
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(iconFg);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx - 7, cy - 7, 14, 14, 40, 280);
                g2.drawLine(cx + 6, cy - 7, cx + 9, cy - 4);
                g2.drawLine(cx + 6, cy - 7, cx + 4, cy - 4);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        refreshBtn.setOpaque(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(40, 28));
        refreshBtn.setToolTipText("Refresh");
        refreshBtn.addActionListener(e -> {
            try {
                updateBalance();
            } catch (Exception ignored) {
            }
            hotSwap();
            showNotification("\u2713  " + BankingConfig.t("Refreshed"), true);
        });
        JButton logoutBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color hoverBg = BankingConfig.darkMode ? new Color(255,80,80,60) : new Color(255,220,220);
                Color restBg  = BankingConfig.darkMode ? new Color(255,255,255,20) : new Color(255,235,235);
                g2.setColor(getModel().isRollover() ? hoverBg : restBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                if (!BankingConfig.darkMode) {
                    g2.setColor(new Color(200, 60, 60));
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
                }
                Color iconColor = BankingConfig.darkMode ? new Color(255,110,110) : new Color(200,40,40);
                int ix = 10, cy = getHeight() / 2;
                g2.setColor(iconColor);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(ix - 5, cy - 6, 12, 12, -60, -240);
                g2.drawLine(ix + 1, cy - 8, ix + 1, cy - 2);
                g2.setFont(BankingConfig.uiFont(Font.BOLD, 11));
                g2.setColor(iconColor);
                g2.drawString("Logout", ix + 12, cy + 4);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        logoutBtn.setOpaque(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(80, 28));
        logoutBtn.setToolTipText("Log Out");
        logoutBtn.addActionListener(e -> {
            balanceTimer.stop();
            LoginRegister.showLogin();
        });
        JButton notifBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color iconFg = BankingConfig.darkMode ? new Color(220, 245, 225) : BankingConfig.PRIMARY_DARK;
                Color iconBg = BankingConfig.darkMode
                        ? (getModel().isRollover() ? new Color(255,255,255,40) : new Color(255,255,255,20))
                        : (getModel().isRollover() ? BankingConfig.PRIMARY_SOFT.darker() : BankingConfig.PRIMARY_SOFT);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                if (!BankingConfig.darkMode) {
                    g2.setColor(BankingConfig.PRIMARY);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
                }
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(iconFg);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(cx - 7, cy - 8, 14, 12, 0, 180);
                g2.drawLine(cx - 7, cy + 4, cx + 7, cy + 4);
                g2.drawLine(cx - 2, cy + 4, cx - 2, cy + 7);
                g2.drawLine(cx + 2, cy + 4, cx + 2, cy + 7);
                g2.drawLine(cx, cy - 8, cx, cy - 11);
                g2.setColor(BankingConfig.DANGER);
                g2.fillOval(cx + 3, cy - 12, 7, 7);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        notifBtn.setOpaque(false);
        notifBtn.setContentAreaFilled(false);
        notifBtn.setBorderPainted(false);
        notifBtn.setFocusPainted(false);
        notifBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        notifBtn.setPreferredSize(new Dimension(40, 28));
        notifBtn.setToolTipText("Notifications");
        notifBtn.addActionListener(e -> views.showNotificationsPanel());
        backBtn = makeTopBtn("← Back", BankingConfig.PRIMARY_SOFT, BankingConfig.PRIMARY);
        backBtn.setVisible(false);
        backBtn.addActionListener(e -> {
            if ("Home".equals(currentView) || "".equals(currentView))
                return;
            currentView = "Home";
            backBtn.setVisible(false);
            views.showHome();
        });
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(hamburger);
        left.add(Box.createHorizontalStrut(8));
        left.add(backBtn);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);
        right.add(langBtn);
        right.add(darkBtn);
        right.add(refreshBtn);
        right.add(notifBtn);
        right.add(logoutBtn);
        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BankingConfig.BG);
        contentArea.setBorder(BorderFactory.createEmptyBorder());
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BankingConfig.BG);
        main.add(topBar, BorderLayout.NORTH);
        main.add(contentArea, BorderLayout.CENTER);
        return main;
    }
    JButton makeTopBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        btn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return btn;
    }
    void showNotification(String message, boolean success) {
        Color bg = success ? BankingConfig.SUCCESS : BankingConfig.DANGER;
        Color fg = Color.WHITE;
        JPanel notif = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        notif.setOpaque(false);
        notif.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        JLabel lbl = new JLabel(message);
        lbl.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        lbl.setForeground(fg);
        JButton close = new JButton("×") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {}
        };
        close.setFont(BankingConfig.uiFont(Font.BOLD, 16));
        close.setForeground(fg);
        close.setOpaque(false);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setFocusPainted(false);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        close.setPreferredSize(new Dimension(22, 22));
        notif.add(lbl, BorderLayout.CENTER);
        notif.add(close, BorderLayout.EAST);
        JPanel toast = new JPanel(new BorderLayout());
        toast.setOpaque(false);
        toast.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        toast.add(notif, BorderLayout.CENTER);
        JLayeredPane layered = AppFrame.get().getLayeredPane();
        layered.add(toast, JLayeredPane.POPUP_LAYER);
        int frameW = AppFrame.get().getWidth();
        int toastW = Math.min(frameW - 40, 600);
        int toastH = 48;
        int toastX = (frameW - toastW) / 2;
        int toastY = 56; // just below the top nav bar
        toast.setBounds(toastX, toastY, toastW, toastH);
        toast.setVisible(true);
        layered.revalidate();
        layered.repaint();
        close.addActionListener(e -> {
            layered.remove(toast);
            layered.revalidate();
            layered.repaint();
        });
        javax.swing.Timer t = new javax.swing.Timer(5000, e -> {
            layered.remove(toast);
            layered.revalidate();
            layered.repaint();
        });
        t.setRepeats(false);
        t.start();
    }
    double getBalance() throws Exception {
        PreparedStatement ps = BankingConfig.con.prepareStatement(
                "SELECT balance FROM accounts WHERE account_number=?");
        ps.setString(1, accountNumber);
        ResultSet rs = ps.executeQuery();
        double b = rs.next() ? rs.getDouble("balance") : 0.0;
        rs.close();
        ps.close();
        return b;
    }
    void updateBalance() throws Exception {
        cachedBalance = getBalance();
        refreshBalanceDisplay();
    }
    void refreshBalanceDisplay() {
        String shown = balanceHidden
                ? "ETB  ••••••"
                : String.format("ETB  %,.2f", cachedBalance);
        if (balanceLabel != null)
            balanceLabel.setText(shown);
        if (heroBalLbl != null)
            heroBalLbl.setText(shown);
        if (sidebarBalToggleBtn != null)
            sidebarBalToggleBtn.repaint();
        if (heroBalToggleBtn != null)
            heroBalToggleBtn.repaint();
    }
    JButton makeEyeToggleBtn() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(255, 255, 255, 50) : new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (balanceHidden) {
                    g2.drawArc(cx - 8, cy - 4, 16, 10, 0, 180);
                    g2.drawLine(cx - 9, cy + 4, cx + 9, cy - 6);
                } else {
                    g2.drawArc(cx - 8, cy - 5, 16, 10, 0, 180);
                    g2.drawArc(cx - 8, cy - 5, 16, 10, 0, -180);
                    g2.fillOval(cx - 2, cy - 2, 5, 5);
                }
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        btn.setPreferredSize(new Dimension(28, 22));
        btn.addActionListener(e -> {
            balanceHidden = !balanceHidden;
            refreshBalanceDisplay();
        });
        return btn;
    }
    boolean confirmPassword() {
        JPasswordField pf = new JPasswordField();
        pf.setPreferredSize(new Dimension(220, 36));
        int result = JOptionPane.showConfirmDialog(null,
                new Object[] { BankingConfig.t("Enter your password to confirm:"), pf },
                BankingConfig.t("Confirm Password"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION)
            return false;
        String entered = new String(pf.getPassword());
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT password FROM accounts WHERE account_number=?");
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean ok = BankingConfig.verifyPassword(entered, rs.getString("password"));
                rs.close();
                ps.close();
                if (!ok)
                    BankingConfig.showDialog(null, BankingConfig.t("Incorrect password."), JOptionPane.ERROR_MESSAGE);
                return ok;
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    void changeProfilePicture(JPanel avatarPanel, java.util.function.Consumer<Image> onLoaded) {
        Color accent = BankingConfig.PRIMARY;
        JDialog picker = new JDialog(AppFrame.get(), BankingConfig.t("Select Profile Image"), true);
        picker.setSize(340, 200);
        picker.setLocationRelativeTo(AppFrame.get());
        picker.setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BankingConfig.BG);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel title = new JLabel(BankingConfig.t("Choose photo source"));
        title.setFont(BankingConfig.uiFont(Font.BOLD, 14));
        title.setForeground(BankingConfig.FG);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        root.add(title, BorderLayout.NORTH);
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 14, 0));
        btnRow.setOpaque(false);
        JButton galleryBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BankingConfig.PRIMARY_SOFT.darker() : BankingConfig.PRIMARY_SOFT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                int cx = getWidth() / 2, cy = getHeight() / 2 - 10;
                g2.setColor(BankingConfig.PRIMARY);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(cx - 18, cy - 12, 36, 28, 6, 6);
                g2.drawOval(cx - 6, cy - 6, 10, 10);
                int[] mx = { cx - 18, cx - 8, cx + 2, cx + 18 };
                int[] my = { cy + 16, cy + 4, cy + 10, cy + 16 };
                g2.drawPolyline(mx, my, 4);
                g2.setFont(BankingConfig.uiFont(Font.BOLD, 11));
                g2.setColor(BankingConfig.PRIMARY);
                String lbl = BankingConfig.t("Gallery");
                g2.drawString(lbl, cx - g2.getFontMetrics().stringWidth(lbl) / 2, cy + 30);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        galleryBtn.setPreferredSize(new Dimension(120, 90));
        galleryBtn.setOpaque(false);
        galleryBtn.setContentAreaFilled(false);
        galleryBtn.setBorderPainted(false);
        galleryBtn.setFocusPainted(false);
        galleryBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        galleryBtn.addActionListener(e -> {
            picker.dispose();
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(BankingConfig.t("Select Profile Image"));
            fc.setFileFilter(new FileNameExtensionFilter(
                    "Images (jpg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp"));
            if (fc.showOpenDialog(AppFrame.get()) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    Image img = javax.imageio.ImageIO.read(f);
                    if (img != null) {
                        PreparedStatement ps = BankingConfig.con.prepareStatement(
                                "UPDATE accounts SET profile_image=? WHERE account_number=?");
                        ps.setString(1, f.getAbsolutePath());
                        ps.setString(2, accountNumber);
                        ps.executeUpdate();
                        ps.close();
                        onLoaded.accept(img);
                        showNotification(BankingConfig.t("Profile updated successfully."), true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        JButton cameraBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(220, 240, 255) : new Color(230, 245, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                int cx = getWidth() / 2, cy = getHeight() / 2 - 10;
                g2.setColor(new Color(0, 120, 200));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(cx - 18, cy - 8, 36, 24, 6, 6);
                g2.drawOval(cx - 7, cy - 3, 14, 14);
                g2.drawRoundRect(cx - 6, cy - 14, 12, 8, 4, 4);
                g2.fillOval(cx + 10, cy - 6, 4, 4);
                g2.setFont(BankingConfig.uiFont(Font.BOLD, 11));
                g2.setColor(new Color(0, 120, 200));
                String lbl = BankingConfig.t("Camera");
                g2.drawString(lbl, cx - g2.getFontMetrics().stringWidth(lbl) / 2, cy + 30);
                g2.dispose();
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        cameraBtn.setPreferredSize(new Dimension(120, 90));
        cameraBtn.setOpaque(false);
        cameraBtn.setContentAreaFilled(false);
        cameraBtn.setBorderPainted(false);
        cameraBtn.setFocusPainted(false);
        cameraBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        cameraBtn.addActionListener(e -> {
            picker.dispose();
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(BankingConfig.t("Take or Select Photo"));
            fc.setFileFilter(new FileNameExtensionFilter(
                    "Images (jpg, png)", "jpg", "jpeg", "png"));
            fc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Pictures"));
            if (fc.showOpenDialog(AppFrame.get()) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    Image img = javax.imageio.ImageIO.read(f);
                    if (img != null) {
                        PreparedStatement ps = BankingConfig.con.prepareStatement(
                                "UPDATE accounts SET profile_image=? WHERE account_number=?");
                        ps.setString(1, f.getAbsolutePath());
                        ps.setString(2, accountNumber);
                        ps.executeUpdate();
                        ps.close();
                        onLoaded.accept(img);
                        showNotification(BankingConfig.t("Profile updated successfully."), true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnRow.add(galleryBtn);
        btnRow.add(cameraBtn);
        root.add(btnRow, BorderLayout.CENTER);
        JButton cancelBtn = new JButton(BankingConfig.t("Cancel"));
        cancelBtn.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        cancelBtn.setForeground(BankingConfig.MUTED);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.addActionListener(e -> picker.dispose());
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        foot.setOpaque(false);
        foot.add(cancelBtn);
        root.add(foot, BorderLayout.SOUTH);
        picker.setContentPane(root);
        picker.setVisible(true);
    }
    void deposit() {
        currentView = "Deposit";
        backBtn.setVisible(true);
        contentArea.removeAll();
        JPanel page = views.buildTxPage(BankingConfig.t("Deposit"), BankingConfig.t("Add funds to your account"),
                BankingConfig.SUCCESS);
        JPanel form = (JPanel) page.getClientProperty("form");
        JLabel amtLbl = new JLabel(BankingConfig.t("Amount") + " (ETB)");
        amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        amtLbl.setForeground(BankingConfig.FG);
        amtLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField amtField = new UIComponents.RoundedField(BankingConfig.t("Enter amount"));
        amtField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        amtField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel errLbl = new JLabel(" ");
        errLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        errLbl.setForeground(BankingConfig.DANGER);
        errLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(amtLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(amtField);
        form.add(Box.createVerticalStrut(6));
        form.add(errLbl);
        form.add(Box.createVerticalStrut(20));
        UIComponents.PurpleButton btn = new UIComponents.PurpleButton(BankingConfig.t("Deposit") + "  +",
                BankingConfig.SUCCESS);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btn);
        btn.addActionListener(e -> {
            errLbl.setText(" ");
            try {
                String amtText = amtField.getText().trim();
                if (amtText.isEmpty()) {
                    errLbl.setText(BankingConfig.t("Please enter an amount"));
                    return;
                }
                double a = Double.parseDouble(amtText);
                if (a <= 0) {
                    errLbl.setText(BankingConfig.t("Amount must be > 0"));
                    return;
                }
                if (!confirmPassword())
                    return;
                PreparedStatement ps = BankingConfig.con
                        .prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_number=?");
                ps.setDouble(1, a);
                ps.setString(2, accountNumber);
                ps.executeUpdate();
                ps.close();
                double nb = getBalance();
                BankingConfig.recordTransaction(accountNumber, "Deposit", a, nb, null, "Cash Deposit");
                updateBalance();
                BankingConfig.showDialog(null, "✅ " + BankingConfig.t("Deposit Successful!") + "\n\n"
                        + BankingConfig.t("Amount") + ": " + BankingConfig.formatCurrency(a),
                        BankingConfig.t("Deposit Successful!"), JOptionPane.INFORMATION_MESSAGE);
                views.showHome();
            } catch (NumberFormatException ex) {
                errLbl.setText(BankingConfig.t("Invalid amount."));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
        SwingUtilities.invokeLater(amtField::requestFocusInWindow);
    }
    void withdraw() {
        currentView = "Withdraw";
        backBtn.setVisible(true);
        contentArea.removeAll();
        JPanel page = views.buildTxPage(BankingConfig.t("Withdraw"), BankingConfig.t("Withdraw cash from your account"),
                BankingConfig.DANGER);
        JPanel form = (JPanel) page.getClientProperty("form");
        JLabel amtLbl = new JLabel(BankingConfig.t("Amount") + " (ETB)");
        amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        amtLbl.setForeground(BankingConfig.FG);
        amtLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField amtField = new UIComponents.RoundedField(BankingConfig.t("Enter amount"));
        amtField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        amtField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel errLbl = new JLabel(" ");
        errLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        errLbl.setForeground(BankingConfig.DANGER);
        errLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(amtLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(amtField);
        form.add(Box.createVerticalStrut(6));
        form.add(errLbl);
        form.add(Box.createVerticalStrut(20));
        UIComponents.PurpleButton btn = new UIComponents.PurpleButton(BankingConfig.t("Withdraw") + "  -",
                BankingConfig.DANGER);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btn);
        btn.addActionListener(e -> {
            errLbl.setText(" ");
            try {
                String amtText = amtField.getText().trim();
                if (amtText.isEmpty()) {
                    errLbl.setText(BankingConfig.t("Please enter an amount"));
                    return;
                }
                double a = Double.parseDouble(amtText);
                if (a <= 0) {
                    errLbl.setText(BankingConfig.t("Amount must be > 0"));
                    return;
                }
                double bal = getBalance();
                if (bal < a) {
                    errLbl.setText(String.format(BankingConfig.t("Insufficient balance. Available: ETB %.2f"), bal));
                    return;
                }
                if (!confirmPassword())
                    return;
                PreparedStatement ps = BankingConfig.con
                        .prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=?");
                ps.setDouble(1, a);
                ps.setString(2, accountNumber);
                ps.executeUpdate();
                ps.close();
                double nb = getBalance();
                BankingConfig.recordTransaction(accountNumber, "Withdrawal", a, nb, null, "Cash Withdrawal");
                updateBalance();
                BankingConfig.showDialog(null, "✅ " + BankingConfig.t("Withdrawal Successful!") + "\n\n"
                        + BankingConfig.t("Amount") + ": " + BankingConfig.formatCurrency(a),
                        BankingConfig.t("Withdrawal Successful!"), JOptionPane.INFORMATION_MESSAGE);
                views.showHome();
            } catch (NumberFormatException ex) {
                errLbl.setText(BankingConfig.t("Invalid amount."));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
        SwingUtilities.invokeLater(amtField::requestFocusInWindow);
    }
    void transfer() {
        currentView = "Transfer";
        backBtn.setVisible(true);
        contentArea.removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BankingConfig.BG);
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(BankingConfig.CARD_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(BankingConfig.BORDER);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        JPanel hLeft = new JPanel();
        hLeft.setOpaque(false);
        hLeft.setLayout(new BoxLayout(hLeft, BoxLayout.Y_AXIS));
        JLabel hTitle = new JLabel("$  " + BankingConfig.t("Transfer Money"));
        hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        hTitle.setForeground(BankingConfig.FG);
        JLabel hSub = new JLabel(BankingConfig.t("Choose a transfer type"));
        hSub.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        hSub.setForeground(BankingConfig.MUTED);
        hLeft.add(hTitle);
        hLeft.add(hSub);
        header.add(hLeft, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);
        Object[][] types = {
                { "bank", BankingConfig.t("CBE Account"), BankingConfig.t("Transfer to any CBE bank account"),
                        BankingConfig.PRIMARY, "CBE" },
                { "refresh", BankingConfig.t("Own Account"),
                        BankingConfig.t("Move money between your own CBE accounts"), BankingConfig.INFO, "OWN" },
                { "agent", BankingConfig.t("CBE Agent"), BankingConfig.t("Transfer via CBE agent network"),
                        BankingConfig.SUCCESS, "AGENT" },
                { "wallet", BankingConfig.t("CBE Wallet"), BankingConfig.t("Send to CBE mobile wallet"),
                        new Color(0, 185, 160), "WALLET_CBE" },
                { "card", BankingConfig.t("Other Wallet"), BankingConfig.t("Telebirr, M-Pesa and other wallets"),
                        new Color(140, 100, 220), "WALLET_OTHER" },
                { "globe", BankingConfig.t("Other Bank"), BankingConfig.t("Transfer to Awash, Dashen, Abyssinia..."),
                        BankingConfig.WARNING, "OTHER_BANK" },
        };
        JPanel grid = new JPanel(new GridLayout(3, 2, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));
        for (Object[] type : types) {
            String icon = (String) type[0];
            String title = (String) type[1];
            String desc = (String) type[2];
            Color color = (Color) type[3];
            String typeKey = (String) type[4];
            JPanel card = new JPanel(new BorderLayout(14, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, BankingConfig.darkMode ? 40 : 14));
                    g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, 16, 16);
                    g2.setColor(BankingConfig.CARD_BG);
                    g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                    g2.setColor(color);
                    g2.fillRoundRect(0, 12, 5, getHeight() - 28, 4, 4);
                    if (BankingConfig.darkMode) {
                        g2.setColor(BankingConfig.BORDER);
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 5, 16, 16);
                    }
                }
            };
            card.setOpaque(false);
            card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 16));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JPanel iconPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int alpha = BankingConfig.darkMode ? 90 : 30;
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(BankingConfig.darkMode ? new Color(220, 240, 230) : color);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int cx = getWidth() / 2, cy = getHeight() / 2;
                    switch (icon) {
                        case "bank":
                            g2.drawLine(cx - 10, cy + 6, cx + 10, cy + 6);
                            g2.drawRect(cx - 7, cy - 2, 4, 8);
                            g2.drawRect(cx - 1, cy - 2, 4, 8);
                            g2.drawRect(cx + 5, cy - 2, 4, 8);
                            g2.drawLine(cx - 10, cy - 4, cx, cy - 10);
                            g2.drawLine(cx, cy - 10, cx + 10, cy - 4);
                            g2.drawLine(cx - 10, cy - 4, cx + 10, cy - 4);
                            break;
                        case "refresh":
                            g2.drawArc(cx - 8, cy - 8, 16, 16, 40, 280);
                            g2.drawLine(cx + 7, cy - 8, cx + 10, cy - 5);
                            g2.drawLine(cx + 7, cy - 8, cx + 4, cy - 5);
                            break;
                        case "agent":
                            g2.drawOval(cx - 5, cy - 9, 10, 9);
                            g2.drawArc(cx - 9, cy, 18, 12, 0, 180);
                            g2.drawLine(cx + 4, cy + 2, cx + 10, cy - 2);
                            g2.drawLine(cx + 10, cy - 2, cx + 8, cy + 4);
                            break;
                        case "wallet":
                            g2.drawRoundRect(cx - 10, cy - 6, 20, 14, 4, 4);
                            g2.fillOval(cx + 4, cy - 1, 4, 4);
                            g2.drawLine(cx - 10, cy - 1, cx + 4, cy - 1);
                            break;
                        case "card":
                            g2.drawRoundRect(cx - 10, cy - 7, 20, 14, 4, 4);
                            g2.drawLine(cx - 10, cy - 2, cx + 10, cy - 2);
                            g2.fillRect(cx - 8, cy + 1, 5, 3);
                            break;
                        case "globe":
                            g2.drawOval(cx - 9, cy - 9, 18, 18);
                            g2.drawOval(cx - 5, cy - 9, 10, 18);
                            g2.drawLine(cx - 9, cy, cx + 9, cy);
                            g2.drawLine(cx - 8, cy - 5, cx + 8, cy - 5);
                            g2.drawLine(cx - 8, cy + 5, cx + 8, cy + 5);
                            break;
                    }
                    g2.dispose();
                }
            };
            iconPanel.setOpaque(false);
            iconPanel.setPreferredSize(new Dimension(44, 44));
            iconPanel.setMinimumSize(new Dimension(44, 44));
            JPanel textBox = new JPanel();
            textBox.setOpaque(false);
            textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(BankingConfig.uiFont(Font.BOLD, 14));
            titleLbl.setForeground(BankingConfig.FG);
            JLabel descLbl = new JLabel(desc);
            descLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
            descLbl.setForeground(BankingConfig.MUTED);
            textBox.add(titleLbl);
            textBox.add(Box.createVerticalStrut(3));
            textBox.add(descLbl);
            JLabel arrow = new JLabel("›");
            arrow.setFont(new Font("SansSerif", Font.BOLD, 22));
            arrow.setForeground(BankingConfig.darkMode ? BankingConfig.MUTED : BankingConfig.MUTED);
            card.add(iconPanel, BorderLayout.WEST);
            card.add(textBox, BorderLayout.CENTER);
            card.add(arrow, BorderLayout.EAST);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showTransferForm(typeKey);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(color, 2, true),
                            BorderFactory.createEmptyBorder(16, 18, 16, 14)));
                    card.repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 16));
                    card.repaint();
                }
            });
            grid.add(card);
        }
        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        root.add(scroll, BorderLayout.CENTER);
        contentArea.add(root, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void showTransferForm(String typeKey) {
        contentArea.removeAll();
        String formTitle, formHint;
        boolean isCBEInternal;
        if ("CBE".equals(typeKey)) {
            formTitle = BankingConfig.t("Transfer to CBE Account");
            formHint = BankingConfig.t("Enter recipient CBE account number");
            isCBEInternal = true;
        } else if ("OWN".equals(typeKey)) {
            formTitle = BankingConfig.t("Transfer to Own Account");
            formHint = BankingConfig.t("Enter your other CBE account number");
            isCBEInternal = true;
        } else if ("AGENT".equals(typeKey)) {
            formTitle = BankingConfig.t("Transfer via CBE Agent");
            formHint = BankingConfig.t("Enter CBE agent account number");
            isCBEInternal = true;
        } else if ("WALLET_CBE".equals(typeKey)) {
            formTitle = BankingConfig.t("Transfer to CBE Wallet");
            formHint = BankingConfig.t("Enter CBE wallet phone number");
            isCBEInternal = false;
        } else if ("WALLET_OTHER".equals(typeKey)) {
            formTitle = BankingConfig.t("Transfer to Other Wallet");
            formHint = BankingConfig.t("Enter wallet phone number");
            isCBEInternal = false;
        } else {
            formTitle = BankingConfig.t("Transfer to Other Bank");
            formHint = BankingConfig.t("Enter recipient account number");
            isCBEInternal = false;
        }
        JPanel page = views.buildTxPage(formTitle, BankingConfig.t("Fill in the details below"), BankingConfig.WARNING);
        JPanel form = (JPanel) page.getClientProperty("form");
        JComboBox<String> bankBox = null;
        if ("OTHER_BANK".equals(typeKey)) {
            JLabel bankLbl = new JLabel(BankingConfig.t("Select Bank"));
            bankLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            bankLbl.setForeground(BankingConfig.FG);
            bankLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            bankBox = new JComboBox<>(new String[] { "Awash Bank", "Dashen Bank", "Abyssinia Bank", "Wegagen Bank",
                    "United Bank", "Nib International Bank", "Cooperative Bank of Oromia", "Berhan Bank",
                    "Bunna International Bank", "Zemen Bank", "Oromia International Bank", "Amhara Bank",
                    "Enat Bank" });
            bankBox.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            bankBox.setBackground(BankingConfig.CARD_BG);
            bankBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            bankBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(bankLbl);
            form.add(Box.createVerticalStrut(6));
            form.add(bankBox);
            form.add(Box.createVerticalStrut(16));
        }
        JComboBox<String> walletBox = null;
        if ("WALLET_OTHER".equals(typeKey)) {
            JLabel wLbl = new JLabel(BankingConfig.t("Select Wallet"));
            wLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            wLbl.setForeground(BankingConfig.FG);
            wLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            walletBox = new JComboBox<>(new String[] { "Telebirr", "M-Pesa Ethiopia", "HelloCash", "Amole" });
            walletBox.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            walletBox.setBackground(BankingConfig.CARD_BG);
            walletBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            walletBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(wLbl);
            form.add(Box.createVerticalStrut(6));
            form.add(walletBox);
            form.add(Box.createVerticalStrut(16));
        }
        JLabel toLbl = new JLabel(formHint);
        toLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        toLbl.setForeground(BankingConfig.FG);
        toLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField toField = new UIComponents.RoundedField(formHint);
        toField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        toField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel recipientLbl = new JLabel(" ");
        recipientLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        recipientLbl.setForeground(BankingConfig.SUCCESS);
        recipientLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (isCBEInternal) {
            toField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                void lookup() {
                    String acc = toField.getText().trim();
                    if (acc.length() >= 8) {
                        new Thread(() -> {
                            try {
                                PreparedStatement ps = BankingConfig.con.prepareStatement(
                                        "SELECT account_holder_name FROM accounts WHERE account_number=? AND status='Active'");
                                ps.setString(1, acc);
                                ResultSet rs = ps.executeQuery();
                                String n = rs.next() ? "✓ " + rs.getString("account_holder_name")
                                        : "✗ " + BankingConfig.t("Account not found");
                                rs.close();
                                ps.close();
                                SwingUtilities.invokeLater(() -> {
                                    recipientLbl.setText(n);
                                    recipientLbl.setForeground(
                                            n.startsWith("✓") ? BankingConfig.SUCCESS : BankingConfig.DANGER);
                                });
                            } catch (Exception ignored) {
                            }
                        }).start();
                    } else {
                        recipientLbl.setText(" ");
                    }
                }
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    lookup();
                }
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    lookup();
                }
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    lookup();
                }
            });
        }
        JLabel amtLbl = new JLabel(BankingConfig.t("Amount") + " (ETB)");
        amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        amtLbl.setForeground(BankingConfig.FG);
        amtLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField amtField = new UIComponents.RoundedField(BankingConfig.t("Enter amount"));
        amtField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        amtField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel noteLbl = new JLabel(BankingConfig.t("Note (optional)"));
        noteLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        noteLbl.setForeground(BankingConfig.FG);
        noteLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField noteField = new UIComponents.RoundedField(BankingConfig.t("e.g. Rent payment"));
        noteField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        noteField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel errLbl = new JLabel(" ");
        errLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        errLbl.setForeground(BankingConfig.DANGER);
        errLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(toLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(toField);
        form.add(Box.createVerticalStrut(4));
        form.add(recipientLbl);
        form.add(Box.createVerticalStrut(12));
        form.add(amtLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(amtField);
        form.add(Box.createVerticalStrut(16));
        form.add(noteLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(noteField);
        form.add(Box.createVerticalStrut(6));
        form.add(errLbl);
        form.add(Box.createVerticalStrut(12));
        JButton backToMenu = makeTopBtn("← " + BankingConfig.t("Back"), BankingConfig.PRIMARY_SOFT,
                BankingConfig.PRIMARY);
        backToMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToMenu.addActionListener(e -> transfer());
        form.add(backToMenu);
        form.add(Box.createVerticalStrut(10));
        final JComboBox<String> fBankBox = bankBox;
        final JComboBox<String> fWalletBox = walletBox;
        UIComponents.PurpleButton btn = new UIComponents.PurpleButton(BankingConfig.t("Send") + "  ⇄",
                BankingConfig.WARNING);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btn);
        btn.addActionListener(e -> {
            errLbl.setText(" ");
            String to = toField.getText().trim();
            String amtTxt = amtField.getText().trim();
            String note = noteField.getText().trim();
            if (to.isEmpty() || amtTxt.isEmpty()) {
                errLbl.setText(BankingConfig.t("Fill all fields."));
                return;
            }
            if (isCBEInternal && to.equals(accountNumber)) {
                errLbl.setText(BankingConfig.t("Cannot transfer to same account."));
                return;
            }
            try {
                double a = Double.parseDouble(amtTxt);
                if (a <= 0) {
                    errLbl.setText(BankingConfig.t("Amount must be > 0"));
                    return;
                }
                double bal = getBalance();
                if (bal < a) {
                    errLbl.setText(String.format(BankingConfig.t("Insufficient balance. Available: ETB %.2f"), bal));
                    return;
                }
                if (!confirmPassword())
                    return;
                if (isCBEInternal) {
                    PreparedStatement cp = BankingConfig.con.prepareStatement(
                            "SELECT account_holder_name FROM accounts WHERE account_number=? AND status='Active'");
                    cp.setString(1, to);
                    ResultSet cr = cp.executeQuery();
                    if (!cr.next()) {
                        errLbl.setText(BankingConfig.t("Recipient not found."));
                        cr.close();
                        cp.close();
                        return;
                    }
                    String rName = cr.getString("account_holder_name");
                    cr.close();
                    cp.close();
                    BankingConfig.con.setAutoCommit(false);
                    PreparedStatement p1 = BankingConfig.con
                            .prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=?");
                    p1.setDouble(1, a);
                    p1.setString(2, accountNumber);
                    p1.executeUpdate();
                    p1.close();
                    PreparedStatement p2 = BankingConfig.con
                            .prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_number=?");
                    p2.setDouble(1, a);
                    p2.setString(2, to);
                    p2.executeUpdate();
                    p2.close();
                    BankingConfig.con.commit();
                    BankingConfig.con.setAutoCommit(true);
                    double nb = getBalance();
                    String desc = note.isEmpty() ? "Transfer to " + rName : note;
                    BankingConfig.recordTransaction(accountNumber, "Transfer Out", a, nb, to, desc);
                    BankingConfig.recordTransaction(to, "Transfer In", a, nb, accountNumber,
                            note.isEmpty() ? "Transfer from " + accountHolder : note);
                    updateBalance();
                    BankingConfig.showDialog(null,
                            "✅ " + BankingConfig.t("Transfer Successful!") + "\n\n"
                                    + BankingConfig.t("Amount") + ": " + BankingConfig.formatCurrency(a) + "\n"
                                    + BankingConfig.t("To") + ": " + rName + "\n"
                                    + BankingConfig.t("Account") + ": " + to,
                            BankingConfig.t("Transfer Successful!"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    PreparedStatement p1 = BankingConfig.con
                            .prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=?");
                    p1.setDouble(1, a);
                    p1.setString(2, accountNumber);
                    p1.executeUpdate();
                    p1.close();
                    double nb = getBalance();
                    String dest = fBankBox != null ? (String) fBankBox.getSelectedItem()
                            : fWalletBox != null ? (String) fWalletBox.getSelectedItem()
                                    : typeKey.replace("_", " ");
                    String desc = note.isEmpty() ? "Transfer to " + dest + " (" + to + ")" : note + " [" + dest + "]";
                    BankingConfig.recordTransaction(accountNumber, "Transfer Out", a, nb, to, desc);
                    updateBalance();
                    BankingConfig.showDialog(null,
                            "✅ " + BankingConfig.t("Transfer Successful!") + "\n\n"
                                    + BankingConfig.t("Amount") + ": " + BankingConfig.formatCurrency(a) + "\n"
                                    + BankingConfig.t("To") + ": " + dest + " (" + to + ")",
                            BankingConfig.t("Transfer Successful!"), JOptionPane.INFORMATION_MESSAGE);
                }
                views.showHome();
            } catch (NumberFormatException ex) {
                errLbl.setText(BankingConfig.t("Invalid amount."));
            } catch (Exception ex) {
                try {
                    BankingConfig.con.rollback();
                    BankingConfig.con.setAutoCommit(true);
                } catch (Exception ignored) {
                }
                errLbl.setText(BankingConfig.t("Transfer Error: ") + ex.getMessage());
            }
        });
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
        SwingUtilities.invokeLater(toField::requestFocusInWindow);
    }
    void airtime() {
        currentView = "Airtime";
        backBtn.setVisible(true);
        contentArea.removeAll();
        JPanel page = views.buildTxPage(BankingConfig.t("Airtime Top-Up"),
                BankingConfig.t("Buy airtime for any Ethiopian network"), new Color(180, 60, 220));
        JPanel form = (JPanel) page.getClientProperty("form");
        JLabel netLbl = new JLabel(BankingConfig.t("Select Network"));
        netLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        netLbl.setForeground(BankingConfig.FG);
        netLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] networks = { "Ethio Telecom", "Safaricom Ethiopia" };
        JComboBox<String> networkBox = new JComboBox<>(networks);
        networkBox.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        networkBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        networkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        networkBox.setBackground(BankingConfig.CARD_BG);
        networkBox.setForeground(BankingConfig.FG);
        JLabel phoneLbl = new JLabel(BankingConfig.t("Phone Number"));
        phoneLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        phoneLbl.setForeground(BankingConfig.FG);
        phoneLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        UIComponents.RoundedField phoneField = new UIComponents.RoundedField("09XXXXXXXX");
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel amtLbl = new JLabel(BankingConfig.t("Amount (ETB)"));
        amtLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        amtLbl.setForeground(BankingConfig.FG);
        amtLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        UIComponents.RoundedField amtField = new UIComponents.RoundedField("e.g. 50");
        amtField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        amtField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel chipsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chipsRow.setOpaque(false);
        chipsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        int[] presets = { 10, 25, 50, 100, 200 };
        for (int amt : presets) {
            JButton chip = new JButton("ETB " + amt) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover()
                            ? new Color(180, 60, 220, 40)
                            : new Color(180, 60, 220, 18));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(180, 60, 220));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
                @Override
                protected void paintBorder(Graphics g) {
                }
            };
            chip.setFont(BankingConfig.uiFont(Font.BOLD, 11));
            chip.setForeground(new Color(180, 60, 220));
            chip.setOpaque(false);
            chip.setContentAreaFilled(false);
            chip.setBorderPainted(false);
            chip.setFocusPainted(false);
            chip.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            chip.addActionListener(e -> amtField.setText(String.valueOf(amt)));
            chipsRow.add(chip);
        }
        UIComponents.PurpleButton buyBtn = new UIComponents.PurpleButton(
                BankingConfig.t("Buy Airtime"), new Color(180, 60, 220));
        buyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        buyBtn.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            String amtStr = amtField.getText().trim();
            String net = (String) networkBox.getSelectedItem();
            if (phone.isEmpty() || amtStr.isEmpty()) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Please enter phone number and amount."), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!phone.matches("0[79]\\d{8}")) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Invalid phone number. Use format: 09XXXXXXXX"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amtStr);
            } catch (NumberFormatException ex) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Invalid amount."), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (amount <= 0) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Amount must be > 0"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (amount > cachedBalance) {
                BankingConfig.showDialog(AppFrame.get(),
                        String.format(BankingConfig.t("Insufficient balance!\nBalance: ETB %.2f"), cachedBalance),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!confirmPassword())
                return;
            try {
                PreparedStatement ps = BankingConfig.con.prepareStatement(
                        "UPDATE accounts SET balance = balance - ? WHERE account_number = ?");
                ps.setDouble(1, amount);
                ps.setString(2, accountNumber);
                ps.executeUpdate();
                ps.close();
                double newBal = getBalance();
                BankingConfig.recordTransaction(accountNumber, "Withdrawal", amount, newBal,
                        null, "Airtime - " + net + " - " + phone);
                updateBalance();
                showNotification(String.format("✓ ETB %.0f airtime sent to %s (%s)", amount, phone, net), true);
                views.showHome();
            } catch (Exception ex) {
                ex.printStackTrace();
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Transfer Error: ") + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(netLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(networkBox);
        form.add(Box.createVerticalStrut(16));
        form.add(phoneLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(phoneField);
        form.add(Box.createVerticalStrut(16));
        form.add(amtLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(amtField);
        form.add(Box.createVerticalStrut(10));
        form.add(chipsRow);
        form.add(Box.createVerticalStrut(24));
        form.add(buyBtn);
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
    void travel_booking() {
        currentView = "Travel";
        backBtn.setVisible(true);
        contentArea.removeAll();
        Color airColor = new Color(0, 140, 200);
        Color landColor = new Color(180, 100, 0);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BankingConfig.BG);
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(0, 100, 160), getWidth(), 0, new Color(0, 140, 200)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));
        JPanel hText = new JPanel();
        hText.setOpaque(false);
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        JLabel hTitle = new JLabel("✈  " + BankingConfig.t("Travel Booking"));
        hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 20));
        hTitle.setForeground(Color.WHITE);
        JLabel hSub = new JLabel(BankingConfig.t("Book air or land transport tickets"));
        hSub.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        hSub.setForeground(new Color(200, 235, 255));
        hText.add(hTitle);
        hText.add(hSub);
        header.add(hText, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);
        final boolean[] isAir = { true };
        JPanel toggleBar = new JPanel(new GridLayout(1, 2, 0, 0));
        toggleBar.setBackground(BankingConfig.CARD_BG);
        toggleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BankingConfig.BORDER));
        toggleBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        JButton airTab = makeTransportTab("✈  " + BankingConfig.t("Air Transport"), true, airColor, isAir);
        JButton landTab = makeTransportTab("🚌  " + BankingConfig.t("Land Transport"), false, landColor, isAir);
        toggleBar.add(airTab);
        toggleBar.add(landTab);
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, 16, 16);
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                Color top = isAir[0] ? airColor : landColor;
                g2.setColor(top);
                g2.fillRoundRect(0, 0, getWidth() - 3, 4, 4, 4);
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        String[] airRoutes = {
                "Addis Ababa (ADD) → Dubai (DXB)",
                "Addis Ababa (ADD) → London (LHR)",
                "Addis Ababa (ADD) → New York (JFK)",
                "Addis Ababa (ADD) → Nairobi (NBO)",
                "Addis Ababa (ADD) → Cairo (CAI)",
                "Addis Ababa (ADD) → Johannesburg (JNB)",
                "Addis Ababa (ADD) → Beijing (PEK)",
                "Addis Ababa (ADD) → Frankfurt (FRA)",
                "Addis Ababa (ADD) → Dire Dawa (DIR)",
                "Addis Ababa (ADD) → Bahir Dar (BJR)",
                "Addis Ababa (ADD) → Hawassa (AWA)",
                "Addis Ababa (ADD) → Mekelle (MQX)",
        };
        double[] airPrices = {
                8500, 22000, 35000, 4200, 6800, 12000, 18000, 24000, 1800, 1500, 1600, 1700
        };
        String[] airClasses = { "Economy", "Business", "First Class" };
        double[] classMultiplier = { 1.0, 2.5, 4.5 };
        JPanel airForm = buildAirForm(airRoutes, airPrices, airClasses, classMultiplier, airColor);
        String[] landRoutes = {
                "Addis Ababa → Adama (Nazret)",
                "Addis Ababa → Hawassa",
                "Addis Ababa → Bahir Dar",
                "Addis Ababa → Gondar",
                "Addis Ababa → Dire Dawa",
                "Addis Ababa → Jimma",
                "Addis Ababa → Mekelle",
                "Addis Ababa → Dessie",
                "Addis Ababa → Shashamane",
                "Addis Ababa → Arba Minch",
        };
        double[] landPrices = { 120, 180, 350, 420, 480, 260, 550, 310, 200, 380 };
        String[] busTypes = { "Regular Bus", "Luxury Bus", "Minibus (Selam)" };
        double[] busMultiplier = { 1.0, 1.8, 1.4 };
        JPanel landForm = buildLandForm(landRoutes, landPrices, busTypes, busMultiplier, landColor);
        airTab.addActionListener(e -> {
            if (!isAir[0]) {
                isAir[0] = true;
                airTab.setBackground(airColor);
                airTab.setForeground(Color.WHITE);
                landTab.setBackground(BankingConfig.CARD_BG);
                landTab.setForeground(BankingConfig.MUTED);
                formCard.remove(landForm);
                formCard.add(airForm);
                formCard.repaint();
                formCard.revalidate();
            }
        });
        landTab.addActionListener(e -> {
            if (isAir[0]) {
                isAir[0] = false;
                landTab.setBackground(landColor);
                landTab.setForeground(Color.WHITE);
                airTab.setBackground(BankingConfig.CARD_BG);
                airTab.setForeground(BankingConfig.MUTED);
                formCard.remove(airForm);
                formCard.add(landForm);
                formCard.repaint();
                formCard.revalidate();
            }
        });
        formCard.add(airForm);
        JPanel formOuter = new JPanel(new GridBagLayout());
        formOuter.setBackground(BankingConfig.BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(20, 40, 20, 40);
        formOuter.add(formCard, gc);
        JScrollPane scroll = new JScrollPane(formOuter);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BankingConfig.BG);
        center.add(toggleBar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        contentArea.add(root, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
    JButton makeTransportTab(String text, boolean active, Color color, boolean[] isAir) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                if (getBackground().equals(color)) {
                    g2.setColor(color);
                    g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        btn.setBackground(active ? color : BankingConfig.CARD_BG);
        btn.setForeground(active ? Color.WHITE : BankingConfig.MUTED);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        return btn;
    }
    JPanel buildAirForm(String[] routes, double[] prices,
            String[] classes, double[] multipliers, Color accent) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel routeLbl = formLabel(BankingConfig.t("Flight Route"));
        JComboBox<String> routeBox = styledCombo(routes);
        JLabel classLbl = formLabel(BankingConfig.t("Class"));
        JComboBox<String> classBox = styledCombo(classes);
        JLabel dateLbl = formLabel(BankingConfig.t("Travel Date"));
        UIComponents.RoundedField dateField = new UIComponents.RoundedField("YYYY-MM-DD");
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel passLbl = formLabel(BankingConfig.t("Passengers"));
        Integer[] nums = { 1, 2, 3, 4, 5, 6 };
        JComboBox<Integer> passBox = new JComboBox<>(nums);
        passBox.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        passBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        passBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        passBox.setBackground(BankingConfig.CARD_BG);
        passBox.setForeground(BankingConfig.FG);
        JPanel priceCard = buildPriceCard(accent);
        JLabel priceVal = (JLabel) priceCard.getClientProperty("val");
        JLabel priceNote = (JLabel) priceCard.getClientProperty("note");
        Runnable updatePrice = () -> {
            int ri = routeBox.getSelectedIndex();
            int ci = classBox.getSelectedIndex();
            int ps = (Integer) passBox.getSelectedItem();
            double total = prices[ri] * multipliers[ci] * ps;
            priceVal.setText(String.format("ETB %,.2f", total));
            priceNote.setText(String.format("%.0f passenger(s) × %s × ETB %,.0f",
                    (double) ps, classes[ci], prices[ri] * multipliers[ci]));
        };
        routeBox.addActionListener(e -> updatePrice.run());
        classBox.addActionListener(e -> updatePrice.run());
        passBox.addActionListener(e -> updatePrice.run());
        updatePrice.run();
        UIComponents.PurpleButton bookBtn = new UIComponents.PurpleButton(
                "✈  " + BankingConfig.t("Book Flight"), accent);
        bookBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        bookBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookBtn.addActionListener(e -> {
            String date = dateField.getText().trim();
            if (date.isEmpty()) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Please enter travel date."), JOptionPane.WARNING_MESSAGE);
                return;
            }
            int ri = routeBox.getSelectedIndex();
            int ci = classBox.getSelectedIndex();
            int ps = (Integer) passBox.getSelectedItem();
            double total = prices[ri] * multipliers[ci] * ps;
            if (total > cachedBalance) {
                BankingConfig.showDialog(AppFrame.get(),
                        String.format(BankingConfig.t("Insufficient balance!\nBalance: ETB %.2f"), cachedBalance),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!confirmPassword())
                return;
            try {
                PreparedStatement ps2 = BankingConfig.con.prepareStatement(
                        "UPDATE accounts SET balance=balance-? WHERE account_number=?");
                ps2.setDouble(1, total);
                ps2.setString(2, accountNumber);
                ps2.executeUpdate();
                ps2.close();
                double nb = getBalance();
                BankingConfig.recordTransaction(accountNumber, "Withdrawal", total, nb, null,
                        "Flight: " + routes[ri] + " | " + classes[ci] + " | " + ps + " pax | " + date);
                updateBalance();
                showNotification(BankingConfig.t("Flight booked successfully!"), true);
                views.showHome();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        p.add(routeLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(routeBox);
        p.add(Box.createVerticalStrut(14));
        p.add(classLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(classBox);
        p.add(Box.createVerticalStrut(14));
        p.add(dateLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(dateField);
        p.add(Box.createVerticalStrut(14));
        p.add(passLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(passBox);
        p.add(Box.createVerticalStrut(18));
        p.add(priceCard);
        p.add(Box.createVerticalStrut(18));
        p.add(bookBtn);
        return p;
    }
    JPanel buildLandForm(String[] routes, double[] prices,
            String[] busTypes, double[] multipliers, Color accent) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel routeLbl = formLabel(BankingConfig.t("Route"));
        JComboBox<String> routeBox = styledCombo(routes);
        JLabel busLbl = formLabel(BankingConfig.t("Bus Type"));
        JComboBox<String> busBox = styledCombo(busTypes);
        JLabel dateLbl = formLabel(BankingConfig.t("Travel Date"));
        UIComponents.RoundedField dateField = new UIComponents.RoundedField("YYYY-MM-DD");
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel seatLbl = formLabel(BankingConfig.t("Seats"));
        Integer[] nums = { 1, 2, 3, 4, 5, 6, 7, 8 };
        JComboBox<Integer> seatBox = new JComboBox<>(nums);
        seatBox.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        seatBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        seatBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        seatBox.setBackground(BankingConfig.CARD_BG);
        seatBox.setForeground(BankingConfig.FG);
        JLabel seatTypeLbl = formLabel(BankingConfig.t("Seat Preference"));
        JComboBox<String> seatTypeBox = styledCombo(new String[] { "No Preference", "Window Seat", "Aisle Seat" });
        JPanel priceCard = buildPriceCard(accent);
        JLabel priceVal = (JLabel) priceCard.getClientProperty("val");
        JLabel priceNote = (JLabel) priceCard.getClientProperty("note");
        Runnable updatePrice = () -> {
            int ri = routeBox.getSelectedIndex();
            int bi = busBox.getSelectedIndex();
            int st = (Integer) seatBox.getSelectedItem();
            double total = prices[ri] * multipliers[bi] * st;
            priceVal.setText(String.format("ETB %,.2f", total));
            priceNote.setText(String.format("%d seat(s) × %s × ETB %,.0f",
                    st, busTypes[bi], prices[ri] * multipliers[bi]));
        };
        routeBox.addActionListener(e -> updatePrice.run());
        busBox.addActionListener(e -> updatePrice.run());
        seatBox.addActionListener(e -> updatePrice.run());
        updatePrice.run();
        UIComponents.PurpleButton bookBtn = new UIComponents.PurpleButton(
                "🚌  " + BankingConfig.t("Book Ticket"), accent);
        bookBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        bookBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookBtn.addActionListener(e -> {
            String date = dateField.getText().trim();
            if (date.isEmpty()) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Please enter travel date."), JOptionPane.WARNING_MESSAGE);
                return;
            }
            int ri = routeBox.getSelectedIndex();
            int bi = busBox.getSelectedIndex();
            int st = (Integer) seatBox.getSelectedItem();
            double total = prices[ri] * multipliers[bi] * st;
            if (total > cachedBalance) {
                BankingConfig.showDialog(AppFrame.get(),
                        String.format(BankingConfig.t("Insufficient balance!\nBalance: ETB %.2f"), cachedBalance),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!confirmPassword())
                return;
            try {
                PreparedStatement ps2 = BankingConfig.con.prepareStatement(
                        "UPDATE accounts SET balance=balance-? WHERE account_number=?");
                ps2.setDouble(1, total);
                ps2.setString(2, accountNumber);
                ps2.executeUpdate();
                ps2.close();
                double nb = getBalance();
                BankingConfig.recordTransaction(accountNumber, "Withdrawal", total, nb, null,
                        "Bus: " + routes[ri] + " | " + busTypes[bi] + " | " + st + " seat(s) | " + date);
                updateBalance();
                showNotification(BankingConfig.t("Ticket booked successfully!"), true);
                views.showHome();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        p.add(routeLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(routeBox);
        p.add(Box.createVerticalStrut(14));
        p.add(busLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(busBox);
        p.add(Box.createVerticalStrut(14));
        p.add(dateLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(dateField);
        p.add(Box.createVerticalStrut(14));
        p.add(seatLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(seatBox);
        p.add(Box.createVerticalStrut(14));
        p.add(seatTypeLbl);
        p.add(Box.createVerticalStrut(6));
        p.add(seatTypeBox);
        p.add(Box.createVerticalStrut(18));
        p.add(priceCard);
        p.add(Box.createVerticalStrut(18));
        p.add(bookBtn);
        return p;
    }
    JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        l.setForeground(BankingConfig.FG);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setBackground(BankingConfig.CARD_BG);
        box.setForeground(BankingConfig.FG);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return box;
    }
    JPanel buildPriceCard(Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 18));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 80));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel cap = new JLabel(BankingConfig.t("Total Price"));
        cap.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        cap.setForeground(BankingConfig.MUTED);
        JLabel val = new JLabel("ETB 0.00");
        val.setFont(BankingConfig.uiFont(Font.BOLD, 22));
        val.setForeground(accent);
        JLabel note = new JLabel("");
        note.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
        note.setForeground(BankingConfig.MUTED);
        left.add(cap);
        left.add(val);
        left.add(note);
        card.add(left, BorderLayout.CENTER);
        card.putClientProperty("val", val);
        card.putClientProperty("note", note);
        return card;
    }
    void changePin() {
        currentView = "ChangePin";
        backBtn.setVisible(true);
        contentArea.removeAll();
        JPanel page = views.buildTxPage(BankingConfig.t("Change PIN"), BankingConfig.t("Update your account password"),
                BankingConfig.GOLD);
        JPanel form = (JPanel) page.getClientProperty("form");
        JLabel l1 = new JLabel(BankingConfig.t("Current Password:"));
        l1.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        l1.setForeground(BankingConfig.FG);
        l1.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField oldP = UIComponents.styledPassField();
        oldP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        oldP.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l2 = new JLabel(BankingConfig.t("New Password:"));
        l2.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        l2.setForeground(BankingConfig.FG);
        l2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField newP = UIComponents.styledPassField();
        newP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        newP.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l3 = new JLabel(BankingConfig.t("Confirm New:"));
        l3.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        l3.setForeground(BankingConfig.FG);
        l3.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField confP = UIComponents.styledPassField();
        confP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        confP.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel hint = new JLabel(BankingConfig.passwordRuleMessage());
        hint.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        hint.setForeground(BankingConfig.MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel errLbl = new JLabel(" ");
        errLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        errLbl.setForeground(BankingConfig.DANGER);
        errLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(l1);
        form.add(Box.createVerticalStrut(6));
        form.add(oldP);
        form.add(Box.createVerticalStrut(14));
        form.add(l2);
        form.add(Box.createVerticalStrut(6));
        form.add(newP);
        form.add(Box.createVerticalStrut(6));
        form.add(hint);
        form.add(Box.createVerticalStrut(14));
        form.add(l3);
        form.add(Box.createVerticalStrut(6));
        form.add(confP);
        form.add(Box.createVerticalStrut(6));
        form.add(errLbl);
        form.add(Box.createVerticalStrut(20));
        UIComponents.PurpleButton btn = new UIComponents.PurpleButton(BankingConfig.t("Change Password"),
                BankingConfig.GOLD);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btn);
        btn.addActionListener(e -> {
            errLbl.setText(" ");
            String old = new String(oldP.getPassword());
            String nw = new String(newP.getPassword());
            String conf = new String(confP.getPassword());
            if (old.isEmpty() || nw.isEmpty()) {
                errLbl.setText(BankingConfig.t("Password cannot be empty."));
                return;
            }
            if (!nw.equals(conf)) {
                errLbl.setText(BankingConfig.t("New passwords do not match."));
                return;
            }
            if (!BankingConfig.isStrongPassword(nw)) {
                errLbl.setText(BankingConfig.passwordRuleMessage());
                return;
            }
            try {
                PreparedStatement check = BankingConfig.con
                        .prepareStatement("SELECT password FROM accounts WHERE account_number=?");
                check.setString(1, accountNumber);
                ResultSet rs = check.executeQuery();
                if (rs.next() && !BankingConfig.verifyPassword(old, rs.getString("password"))) {
                    rs.close();
                    check.close();
                    errLbl.setText(BankingConfig.t("Current password is incorrect."));
                    return;
                }
                rs.close();
                check.close();
                PreparedStatement ps = BankingConfig.con
                        .prepareStatement("UPDATE accounts SET password=? WHERE account_number=?");
                ps.setString(1, BankingConfig.hashPassword(nw));
                ps.setString(2, accountNumber);
                ps.executeUpdate();
                ps.close();
                BankingConfig.showDialog(null, "✅ " + BankingConfig.t("Password changed successfully!"),
                        BankingConfig.t("Success"), JOptionPane.INFORMATION_MESSAGE);
                views.showHome();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        contentArea.add(page, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
        SwingUtilities.invokeLater(oldP::requestFocusInWindow);
    }
    void showHome() {
        views.showHome();
    }
    void showHistory() {
        views.showHistory();
    }
    void showProfile() {
        views.showProfile();
    }
    void showQR() {
        views.showQR();
    }
    void showFAQ() {
        views.showFAQ();
    }
}