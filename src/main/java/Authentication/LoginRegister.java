import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
public class LoginRegister {
    public static void showLogin() {
        BankingConfig.applyTheme();
        Login login = new Login();
        AppFrame.get().showPage(login.buildPanel(), "CBE Banking System");
        SwingUtilities.invokeLater(() -> { if (login.userField != null) login.userField.requestFocusInWindow(); });
    }
    static class Login {
        UIComponents.RoundedField userField;
        UIComponents.RoundedPassField passField;
        private JPanel rootPanel;
        private UIComponents.PurpleButton loginBtn;
        JPanel buildPanel() {
            rootPanel = new JPanel(new GridLayout(1, 2, 0, 0));
            rootPanel.setBackground(BankingConfig.BG);
            buildUI(rootPanel);
            return rootPanel;
        }
        private void buildUI(JPanel root) {
            root.removeAll();
            UIComponents.GradientPanel left = new UIComponents.GradientPanel(BankingConfig.PRIMARY, BankingConfig.PRIMARY_DARK);
            left.setLayout(new GridBagLayout());
            JPanel brandBox = new JPanel();
            brandBox.setOpaque(false);
            brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
            JPanel logoWrap = new JPanel(new GridBagLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 22));
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            logoWrap.setOpaque(false);
            logoWrap.setPreferredSize(new Dimension(110, 110));
            logoWrap.setMaximumSize(new Dimension(110, 110));
            logoWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
            UIComponents.CBELogoPanel logo = new UIComponents.CBELogoPanel();
            logo.setPreferredSize(new Dimension(80, 80));
            logoWrap.add(logo);
            brandBox.add(logoWrap);
            brandBox.add(Box.createVerticalStrut(22));
            JLabel appName = new JLabel("CBE Banking System");
            appName.setFont(BankingConfig.uiFont(Font.BOLD, 24));
            appName.setForeground(Color.WHITE);
            appName.setAlignmentX(Component.CENTER_ALIGNMENT);
            brandBox.add(appName);
            brandBox.add(Box.createVerticalStrut(6));
            JLabel bankName = new JLabel(BankingConfig.t("Commercial Bank of Ethiopia"));
            bankName.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            bankName.setForeground(new Color(200, 240, 215));
            bankName.setAlignmentX(Component.CENTER_ALIGNMENT);
            brandBox.add(bankName);
            brandBox.add(Box.createVerticalStrut(6));
            JLabel tagLine = new JLabel(BankingConfig.t("Your Trusted Financial Partner"));
            tagLine.setFont(BankingConfig.uiFont(Font.ITALIC, 11));
            tagLine.setForeground(new Color(160, 220, 185));
            tagLine.setAlignmentX(Component.CENTER_ALIGNMENT);
            brandBox.add(tagLine);
            brandBox.add(Box.createVerticalStrut(44));
            String[] featureTexts = {
                BankingConfig.t("Instant Transfers"),
                BankingConfig.t("Secure Payments"),
                BankingConfig.t("24/7 Banking")
            };
            for (String feat : featureTexts) {
                JPanel featureRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
                featureRow.setOpaque(false);
                featureRow.setAlignmentX(Component.CENTER_ALIGNMENT);
                JPanel checkPanel = new JPanel() {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(100, 220, 150));
                        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        int cx = getWidth() / 2, cy = getHeight() / 2;
                        g2.drawLine(cx - 5, cy, cx - 1, cy + 4);
                        g2.drawLine(cx - 1, cy + 4, cx + 5, cy - 4);
                        g2.dispose();
                    }
                };
                checkPanel.setOpaque(false);
                checkPanel.setPreferredSize(new Dimension(16, 16));
                JLabel fl = new JLabel(feat);
                fl.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
                fl.setForeground(new Color(180, 235, 205));
                featureRow.add(checkPanel);
                featureRow.add(fl);
                brandBox.add(featureRow);
                brandBox.add(Box.createVerticalStrut(8));
            }
            left.add(brandBox);
            JPanel right = new JPanel();
            right.setBackground(BankingConfig.BG);
            right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
            right.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            JScrollPane rightScroll = new JScrollPane(right,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            rightScroll.setBorder(BorderFactory.createEmptyBorder());
            rightScroll.getVerticalScrollBar().setUnitIncrement(16);
            rightScroll.setBackground(BankingConfig.BG);
            rightScroll.getViewport().setBackground(BankingConfig.BG);
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            toolbar.setOpaque(false);
            toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            JButton langBtn = makeIconBtn(
                BankingConfig.isAmharic ? "EN" : "AM",
                BankingConfig.isAmharic ? "Switch to English" : "Switch to Amharic",
                BankingConfig.PRIMARY_SOFT, BankingConfig.PRIMARY);
            langBtn.addActionListener(e -> {
                BankingConfig.isAmharic = !BankingConfig.isAmharic;
                BankingConfig.resetEthFont();
                BankingConfig.applyTheme();
                buildUI(rootPanel);
                rootPanel.revalidate();
                rootPanel.repaint();
                SwingUtilities.invokeLater(() -> { if (userField != null) userField.requestFocusInWindow(); });
            });
            JButton darkBtn = makeDarkToggleBtn(BankingConfig.darkMode);
            darkBtn.addActionListener(e -> {
                BankingConfig.darkMode = !BankingConfig.darkMode;
                BankingConfig.applyTheme();
                buildUI(rootPanel);
                rootPanel.revalidate();
                rootPanel.repaint();
            });
            JButton notifBtn = new JButton() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getModel().isRollover() ? BankingConfig.PRIMARY_SOFT.darker() : BankingConfig.PRIMARY_SOFT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                    int cx = getWidth() / 2, cy = getHeight() / 2;
                    g2.setColor(BankingConfig.PRIMARY);
                    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.drawArc(cx - 7, cy - 8, 14, 12, 0, 180);
                    g2.drawLine(cx - 7, cy + 4, cx + 7, cy + 4);
                    g2.drawLine(cx - 2, cy + 4, cx - 2, cy + 7);
                    g2.drawLine(cx + 2, cy + 4, cx + 2, cy + 7);
                    g2.drawLine(cx, cy - 8, cx, cy - 11);
                    g2.setColor(BankingConfig.DANGER);
                    g2.fillOval(cx + 4, cy - 11, 7, 7);
                }
                @Override protected void paintBorder(Graphics g) {}
            };
            notifBtn.setOpaque(false); notifBtn.setContentAreaFilled(false);
            notifBtn.setBorderPainted(false); notifBtn.setFocusPainted(false);
            notifBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            notifBtn.setPreferredSize(new Dimension(42, 32));
            notifBtn.setToolTipText(BankingConfig.t("Notifications"));
            notifBtn.addActionListener(e -> showLoginNotifications(rootPanel));
            toolbar.add(langBtn); toolbar.add(darkBtn); toolbar.add(notifBtn);
            right.add(toolbar);
            right.add(Box.createVerticalStrut(20));
            JLabel welcome = new JLabel(BankingConfig.t("Welcome Back"));
            welcome.setFont(BankingConfig.uiFont(Font.BOLD, 28));
            welcome.setForeground(BankingConfig.FG);
            welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(welcome);
            JLabel sub = new JLabel(BankingConfig.t("Sign in to your account"));
            sub.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            sub.setForeground(BankingConfig.MUTED);
            sub.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(sub);
            right.add(Box.createVerticalStrut(30));
            JLabel uLbl = new JLabel(BankingConfig.t("Username"));
            uLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            uLbl.setForeground(BankingConfig.FG);
            uLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(uLbl);
            right.add(Box.createVerticalStrut(6));
            userField = new UIComponents.RoundedField(BankingConfig.t("Enter your username"));
            userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            userField.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(userField);
            right.add(Box.createVerticalStrut(18));
            JLabel pLbl = new JLabel(BankingConfig.t("Password"));
            pLbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            pLbl.setForeground(BankingConfig.FG);
            pLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(pLbl);
            right.add(Box.createVerticalStrut(6));
            JPanel passRow = new JPanel(new BorderLayout());
            passRow.setOpaque(false);
            passRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            passField = new UIComponents.RoundedPassField();
            JButton eyeBtn = new JButton() {
                boolean show = false;
                { addActionListener(e -> { show = !show; passField.setEchoChar(show ? (char)0 : (char)42); repaint(); }); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int cx = getWidth()/2, cy = getHeight()/2;
                    g2.setColor(BankingConfig.MUTED);
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    if (show) { g2.drawArc(cx-8,cy-5,16,10,0,180); g2.drawArc(cx-8,cy-5,16,10,0,-180); g2.fillOval(cx-2,cy-2,5,5); }
                    else { g2.drawArc(cx-8,cy-4,16,10,0,180); g2.drawLine(cx-9,cy+4,cx+9,cy-5); }
                    g2.dispose();
                }
                @Override protected void paintBorder(Graphics g) {}
            };
            eyeBtn.setOpaque(false); eyeBtn.setContentAreaFilled(false);
            eyeBtn.setBorderPainted(false); eyeBtn.setFocusPainted(false);
            eyeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            eyeBtn.setPreferredSize(new Dimension(40, 48));
            passRow.add(passField, BorderLayout.CENTER);
            passRow.add(eyeBtn, BorderLayout.EAST);
            passRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(passRow);
            right.add(Box.createVerticalStrut(24));
            loginBtn = new UIComponents.PurpleButton(
                BankingConfig.t("Sign In \u2192"), BankingConfig.PRIMARY);
            loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            right.add(loginBtn);
            right.add(Box.createVerticalStrut(20));
            JPanel regRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            regRow.setOpaque(false);
            JLabel noAcc = new JLabel(BankingConfig.t("Don't have an account? "));
            noAcc.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            noAcc.setForeground(BankingConfig.MUTED);
            JLabel regLink = new JLabel(BankingConfig.t("Open Account"));
            regLink.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            regLink.setForeground(BankingConfig.PRIMARY);
            regLink.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            regLink.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { new Register(); }
                public void mouseEntered(MouseEvent e) { regLink.setText("<html><u>" + BankingConfig.t("Open Account") + "</u></html>"); }
                public void mouseExited(MouseEvent e)  { regLink.setText(BankingConfig.t("Open Account")); }
            });
            regRow.add(noAcc); regRow.add(regLink);
            right.add(regRow);
            right.add(Box.createVerticalStrut(24));
            JPanel notifBanner = new JPanel(new BorderLayout(10, 0)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK, getWidth(), 0, BankingConfig.PRIMARY));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }
            };
            notifBanner.setOpaque(false);
            notifBanner.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
            notifBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
            notifBanner.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel notifLbl = new JLabel("\uD83D\uDD12  " + BankingConfig.t("CBE will NEVER ask for your PIN or password. Stay safe."));
            notifLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            notifLbl.setForeground(Color.WHITE);
            notifBanner.add(notifLbl, BorderLayout.CENTER);
            right.add(notifBanner);
            root.add(left);
            root.add(rightScroll);
            loginBtn.addActionListener(e -> {
                loginBtn.setText(BankingConfig.t("Signing in..."));
                loginBtn.setEnabled(false);
                AppFrame.get().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                loginUser();
            });
            passField.addActionListener(e -> loginUser());
        }
        private void showLoginNotifications(JPanel parent) {
            Object[][] notifData = {
                { "\uD83D\uDD12", "SECURITY",
                  BankingConfig.t("Security Alert"),
                  BankingConfig.t("Dear Customer,") + "\n"
                  + BankingConfig.t("CBE will NEVER call, SMS, or email asking for your:") + "\n"
                  + "  \u2022 " + BankingConfig.t("PIN or Password") + "\n"
                  + "  \u2022 " + BankingConfig.t("OTP (One-Time Password)") + "\n"
                  + "  \u2022 " + BankingConfig.t("Account Number or Card Details") + "\n\n"
                  + BankingConfig.t("If anyone asks, it is a FRAUD. Hang up immediately.") + "\n"
                  + BankingConfig.t("Report to CBE: 8397"),
                  "DANGER" },
                { "\u26A0\uFE0F", "FRAUD ALERT",
                  BankingConfig.t("Phishing Warning"),
                  BankingConfig.t("Dear Customer,") + "\n"
                  + BankingConfig.t("Beware of fake CBE websites and apps.") + "\n\n"
                  + BankingConfig.t("Always verify:") + "\n"
                  + "  \u2022 " + BankingConfig.t("Official website: www.combanketh.et") + "\n"
                  + "  \u2022 " + BankingConfig.t("Official app: CBE Birr on Play Store / App Store") + "\n\n"
                  + BankingConfig.t("Do not click suspicious links sent via SMS or email.") + "\n"
                  + BankingConfig.t("CBE Fraud Hotline: 8397"),
                  "WARNING" },
                { "\uD83D\uDCF1", "ANNOUNCEMENT",
                  BankingConfig.t("CBE Mobile Banking"),
                  BankingConfig.t("Dear Valued Customer,") + "\n"
                  + BankingConfig.t("CBE Mobile Banking is available 24/7.") + "\n\n"
                  + BankingConfig.t("Features:") + "\n"
                  + "  \u2022 " + BankingConfig.t("Instant transfers to any CBE account") + "\n"
                  + "  \u2022 " + BankingConfig.t("Zero fees on CBE-to-CBE transfers") + "\n"
                  + "  \u2022 " + BankingConfig.t("Bill payments and airtime top-up") + "\n"
                  + "  \u2022 " + BankingConfig.t("Account statements and QR payments") + "\n\n"
                  + BankingConfig.t("Download CBE Birr today!"),
                  "INFO" },
                { "\u2705", "SYSTEM",
                  BankingConfig.t("System Status: Normal"),
                  BankingConfig.t("Dear Customer,") + "\n"
                  + BankingConfig.t("All CBE banking services are operating normally.") + "\n\n"
                  + BankingConfig.t("Service Hours:") + "\n"
                  + "  \u2022 " + BankingConfig.t("Online Banking: 24/7") + "\n"
                  + "  \u2022 " + BankingConfig.t("Branch Services: Mon-Fri 8:30am - 5:30pm") + "\n"
                  + "  \u2022 " + BankingConfig.t("ATM Services: 24/7") + "\n\n"
                  + BankingConfig.t("For support call: 8397 or visit any CBE branch."),
                  "SUCCESS" },
                { "\uD83C\uDF81", "PROMOTION",
                  BankingConfig.t("Special Offer for You"),
                  BankingConfig.t("Dear Valued Customer,") + "\n"
                  + BankingConfig.t("CBE is pleased to offer:") + "\n\n"
                  + "  \u2022 " + BankingConfig.t("Zero transfer fees on CBE-to-CBE transfers") + "\n"
                  + "  \u2022 " + BankingConfig.t("Competitive interest rates on savings") + "\n"
                  + "  \u2022 " + BankingConfig.t("Free account opening with zero minimum balance") + "\n"
                  + "  \u2022 " + BankingConfig.t("Instant account number generation") + "\n\n"
                  + BankingConfig.t("Thank you for choosing Commercial Bank of Ethiopia."),
                  "PRIMARY" },
            };
            JDialog dialog = new JDialog(AppFrame.get(), BankingConfig.t("Notifications"), false);
            dialog.setSize(460, 540);
            dialog.setLocationRelativeTo(AppFrame.get());
            dialog.setResizable(false);
            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(BankingConfig.BG);
            JPanel header = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new GradientPaint(0,0,BankingConfig.PRIMARY_DARK,getWidth(),0,BankingConfig.PRIMARY));
                    g2.fillRect(0,0,getWidth(),getHeight());
                }
            };
            header.setOpaque(false);
            header.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));
            JPanel hText = new JPanel(); hText.setOpaque(false);
            hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
            JLabel hTitle = new JLabel("\uD83D\uDD14  " + BankingConfig.t("CBE Notifications"));
            hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 16)); hTitle.setForeground(Color.WHITE);
            JLabel hSub = new JLabel(BankingConfig.t("Security alerts & announcements"));
            hSub.setFont(BankingConfig.uiFont(Font.PLAIN, 11)); hSub.setForeground(new Color(200,240,215));
            hText.add(hTitle); hText.add(hSub);
            header.add(hText, BorderLayout.CENTER);
            root.add(header, BorderLayout.NORTH);
            JPanel list = new JPanel();
            list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
            list.setBackground(BankingConfig.BG);
            list.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
            for (Object[] n : notifData) {
                String icon     = (String) n[0];
                String category = (String) n[1];
                String title    = (String) n[2];
                String body     = (String) n[3];
                String colorKey = (String) n[4];
                Color accent;
                if      ("DANGER".equals(colorKey))  accent = BankingConfig.DANGER;
                else if ("WARNING".equals(colorKey)) accent = BankingConfig.WARNING;
                else if ("SUCCESS".equals(colorKey)) accent = BankingConfig.SUCCESS;
                else if ("INFO".equals(colorKey))    accent = BankingConfig.INFO;
                else                                 accent = BankingConfig.PRIMARY;
                JPanel card = new JPanel(new BorderLayout(10,0)) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(0,0,0,10));
                        g2.fillRoundRect(2,3,getWidth()-4,getHeight()-2,14,14);
                        g2.setColor(BankingConfig.CARD_BG);
                        g2.fillRoundRect(0,0,getWidth()-2,getHeight()-3,14,14);
                        g2.setColor(accent);
                        g2.fillRoundRect(0,10,5,getHeight()-22,4,4);
                    }
                };
                card.setOpaque(false);
                card.setBorder(BorderFactory.createEmptyBorder(12,18,12,14));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                JLabel iconLbl = new JLabel(icon);
                iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
                iconLbl.setPreferredSize(new Dimension(36,36));
                iconLbl.setHorizontalAlignment(SwingConstants.CENTER);
                JPanel textBox = new JPanel(); textBox.setOpaque(false);
                textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
                JLabel catLbl = new JLabel(category);
                catLbl.setFont(BankingConfig.uiFont(Font.BOLD, 9)); catLbl.setForeground(accent);
                JLabel titleLbl = new JLabel(title);
                titleLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13)); titleLbl.setForeground(BankingConfig.FG);
                JLabel previewLbl = new JLabel(body.split("\n")[0]);
                previewLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 11)); previewLbl.setForeground(BankingConfig.MUTED);
                textBox.add(catLbl); textBox.add(Box.createVerticalStrut(2));
                textBox.add(titleLbl); textBox.add(Box.createVerticalStrut(2));
                textBox.add(previewLbl);
                JLabel arrow = new JLabel("\u203A");
                arrow.setFont(new Font("SansSerif", Font.BOLD, 20)); arrow.setForeground(BankingConfig.MUTED);
                card.add(iconLbl, BorderLayout.WEST);
                card.add(textBox, BorderLayout.CENTER);
                card.add(arrow,   BorderLayout.EAST);
                final Color fAccent = accent;
                card.addMouseListener(new MouseAdapter() {
                    @Override public void mouseClicked(MouseEvent e) {
                        showDetail(dialog, icon, category, title, body, fAccent);
                    }
                    @Override public void mouseEntered(MouseEvent e) {
                        card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(fAccent, 1, true),
                            BorderFactory.createEmptyBorder(11,17,11,13)));
                        card.repaint();
                    }
                    @Override public void mouseExited(MouseEvent e) {
                        card.setBorder(BorderFactory.createEmptyBorder(12,18,12,14));
                        card.repaint();
                    }
                });
                list.add(card); list.add(Box.createVerticalStrut(8));
            }
            JScrollPane scroll = new JScrollPane(list);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.setBackground(BankingConfig.BG);
            scroll.getViewport().setBackground(BankingConfig.BG);
            root.add(scroll, BorderLayout.CENTER);
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            footer.setBackground(BankingConfig.CARD_BG);
            footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,BankingConfig.BORDER));
            JButton closeBtn = new JButton(BankingConfig.t("Close"));
            closeBtn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            closeBtn.setForeground(BankingConfig.PRIMARY);
            closeBtn.addActionListener(e -> dialog.dispose());
            footer.add(closeBtn);
            root.add(footer, BorderLayout.SOUTH);
            dialog.setContentPane(root);
            dialog.setVisible(true);
        }
        private void showDetail(JDialog parent, String icon, String category,
                                String title, String body, Color accent) {
            JDialog detail = new JDialog(parent, title, true);
            detail.setSize(420, 380);
            detail.setLocationRelativeTo(parent);
            detail.setResizable(false);
            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(BankingConfig.BG);
            JPanel header = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(new GradientPaint(0,0,accent.darker(),getWidth(),0,accent));
                    g2.fillRect(0,0,getWidth(),getHeight());
                }
            };
            header.setOpaque(false);
            header.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));
            JPanel hRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            hRow.setOpaque(false);
            JLabel iconLbl = new JLabel(icon);
            iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            JPanel hText = new JPanel(); hText.setOpaque(false);
            hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
            JLabel hCat = new JLabel(category);
            hCat.setFont(BankingConfig.uiFont(Font.BOLD, 10)); hCat.setForeground(new Color(220,255,230));
            JLabel hTitle = new JLabel(title);
            hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 14)); hTitle.setForeground(Color.WHITE);
            hText.add(hCat); hText.add(hTitle);
            hRow.add(iconLbl); hRow.add(hText);
            header.add(hRow, BorderLayout.CENTER);
            root.add(header, BorderLayout.NORTH);
            JPanel bodyPanel = new JPanel();
            bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
            bodyPanel.setBackground(BankingConfig.BG);
            bodyPanel.setBorder(BorderFactory.createEmptyBorder(20,24,20,24));
            for (String line : body.split("\n")) {
                JLabel lbl = new JLabel("<html>" + line.replace("  •", "&nbsp;&nbsp;&bull;") + "</html>");
                lbl.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
                lbl.setForeground(line.trim().isEmpty() ? BankingConfig.BG : BankingConfig.FG);
                lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
                bodyPanel.add(lbl);
                bodyPanel.add(Box.createVerticalStrut(line.trim().isEmpty() ? 6 : 3));
            }
            JScrollPane scroll = new JScrollPane(bodyPanel);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().setUnitIncrement(12);
            scroll.setBackground(BankingConfig.BG);
            scroll.getViewport().setBackground(BankingConfig.BG);
            root.add(scroll, BorderLayout.CENTER);
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            footer.setBackground(BankingConfig.CARD_BG);
            footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,BankingConfig.BORDER));
            JButton okBtn = new JButton(BankingConfig.isAmharic ? "\u1245\u1261\u120D" : "OK");
            okBtn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            okBtn.setForeground(BankingConfig.PRIMARY);
            okBtn.addActionListener(e -> detail.dispose());
            footer.add(okBtn);
            root.add(footer, BorderLayout.SOUTH);
            detail.setContentPane(root);
            detail.setVisible(true);
        }
        private JButton makeIconBtn(String label, String tooltip, Color bgColor, Color fgColor) {
            JButton btn = new JButton(label) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color bg = getModel().isRollover() ? bgColor.darker() : bgColor;
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                    g2.setColor(fgColor.darker());
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
                    g2.dispose();
                    super.paintComponent(g);
                }
                @Override protected void paintBorder(Graphics g) {}
            };
            btn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            btn.setForeground(fgColor);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            btn.setPreferredSize(new Dimension(52, 32));
            btn.setToolTipText(tooltip);
            return btn;
        }
        private JButton makeDarkToggleBtn(boolean isDark) {
            JButton btn = new JButton() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color bgColor = isDark ? new Color(255, 248, 200) : new Color(230, 230, 255);
                    Color fgColor = isDark ? new Color(180, 120, 0) : new Color(60, 60, 140);
                    Color bg = getModel().isRollover() ? bgColor.darker() : bgColor;
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                    g2.setColor(fgColor.darker());
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;
                    g2.setColor(fgColor);
                    if (isDark) {
                        int r = 5;
                        g2.fillOval(cx - r, cy - r, r * 2, r * 2);
                        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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
                        g2.setColor(bg);
                        g2.fillOval(cx - r + 3, cy - r - 1, r * 2, r * 2);
                    }
                    g2.dispose();
                }
                @Override protected void paintBorder(Graphics g) {}
            };
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            btn.setPreferredSize(new Dimension(52, 32));
            btn.setToolTipText(isDark ? "Switch to Light Mode" : "Switch to Dark Mode");
            return btn;
        }
        void loginUser() {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
                        String ph = BankingConfig.t("Enter your username");
            if (username.equals(ph) || username.isEmpty() || password.isEmpty()) {
                BankingConfig.showDialog(AppFrame.get(),
                    BankingConfig.t("Please enter username and password."), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (BankingConfig.con == null) {
                BankingConfig.showDialog(AppFrame.get(),
                    BankingConfig.t("Database not connected!"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            userField.setEnabled(false);
            passField.setEnabled(false);
            new Thread(() -> {
                try {
                    if (BankingConfig.con == null || BankingConfig.con.isClosed()) {
                        BankingConfig.con = DBConnection.connect();
                    }
                    if (BankingConfig.con == null) {
                        SwingUtilities.invokeLater(() -> {
                            userField.setEnabled(true); passField.setEnabled(true);
                            loginBtn.setText(BankingConfig.t("Sign In \u2192")); loginBtn.setEnabled(true);
                            AppFrame.get().setCursor(Cursor.getDefaultCursor());
                            BankingConfig.showDialog(AppFrame.get(),
                                BankingConfig.t("Database not connected!\nOpen XAMPP and start MySQL."),
                                BankingConfig.t("Connection Error"), JOptionPane.ERROR_MESSAGE);
                        });
                        return;
                    }
                    PreparedStatement ps = BankingConfig.con.prepareStatement(
                        "SELECT account_number, account_holder_name, password, role, status FROM accounts WHERE username=?");
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        rs.close(); ps.close();
                        SwingUtilities.invokeLater(() -> {
                            userField.setEnabled(true); passField.setEnabled(true);
                            loginBtn.setText(BankingConfig.t("Sign In \u2192")); loginBtn.setEnabled(true);
                            AppFrame.get().setCursor(Cursor.getDefaultCursor());
                            userField.requestFocusInWindow();
                            BankingConfig.showDialog(AppFrame.get(),
                                BankingConfig.t("Invalid username or password."),
                                BankingConfig.t("Login Failed"), JOptionPane.ERROR_MESSAGE);
                        });
                        return;
                    }
                    String storedPassword = rs.getString("password");
                    String accNo  = rs.getString("account_number");
                    String holder = rs.getString("account_holder_name");
                    String role   = rs.getString("role");
                    String status = rs.getString("status");
                    rs.close(); ps.close();
                    if (!"Active".equals(status)) {
                        SwingUtilities.invokeLater(() -> {
                            userField.setEnabled(true); passField.setEnabled(true);
                            loginBtn.setText(BankingConfig.t("Sign In \u2192")); loginBtn.setEnabled(true);
                            AppFrame.get().setCursor(Cursor.getDefaultCursor());
                            BankingConfig.showDialog(AppFrame.get(),
                                BankingConfig.t("Account is not active. Contact support."),
                                BankingConfig.t("Account Inactive"), JOptionPane.ERROR_MESSAGE);
                        });
                        return;
                    }
                    boolean ok = BankingConfig.verifyPassword(password, storedPassword);
                                        if (!ok) {
                        SwingUtilities.invokeLater(() -> {
                            userField.setEnabled(true); passField.setEnabled(true);
                            loginBtn.setText(BankingConfig.t("Sign In \u2192")); loginBtn.setEnabled(true);
                            AppFrame.get().setCursor(Cursor.getDefaultCursor());
                            userField.requestFocusInWindow();
                            BankingConfig.showDialog(AppFrame.get(),
                                BankingConfig.t("Invalid username or password."),
                                BankingConfig.t("Login Failed"), JOptionPane.ERROR_MESSAGE);
                        });
                        return;
                    }
                    if (storedPassword != null && !storedPassword.contains(":")) {
                        try {
                            PreparedStatement up = BankingConfig.con.prepareStatement(
                                "UPDATE accounts SET password=? WHERE account_number=?");
                            up.setString(1, BankingConfig.hashPassword(password));
                            up.setString(2, accNo);
                            up.executeUpdate();
                            up.close();
                        } catch (Exception ignored) {
                        }
                    }
                    final String fAccNo = accNo, fHolder = holder, fRole = role;
                    SwingUtilities.invokeLater(() -> {
                        AppFrame.get().setCursor(Cursor.getDefaultCursor());
                        if ("Admin".equals(fRole)) {
                            new AdminDashboard();
                        } else {
                            new Dashboard(fAccNo, fHolder);
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() -> {
                        userField.setEnabled(true); passField.setEnabled(true);
                        loginBtn.setText(BankingConfig.t("Sign In \u2192")); loginBtn.setEnabled(true);
                        AppFrame.get().setCursor(Cursor.getDefaultCursor());
                        BankingConfig.showDialog(AppFrame.get(),
                            BankingConfig.t("Login error: ") + ex.getMessage(),
                            BankingConfig.t("Error"), JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }
    static class Register extends JFrame {
        JTextField name, email, phone, username;
        JPasswordField password, confirmPass;
        JComboBox<String> accountType;
        Register() {
            setTitle(BankingConfig.t("Open New Account"));
            setSize(580, 640);
            setMinimumSize(new Dimension(400, 480));
            setLocationRelativeTo(null);
            setResizable(true);
            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(BankingConfig.BG);
            JPanel header = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK,
                        getWidth(), getHeight(), BankingConfig.PRIMARY));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            header.setOpaque(false);
            header.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));
            JLabel hl = new JLabel("\u2302  " + BankingConfig.t("Open New Account"));
            hl.setFont(BankingConfig.uiFont(Font.BOLD, 20));
            hl.setForeground(Color.WHITE);
            JLabel sub = new JLabel(BankingConfig.t("Fill in your details to get started"));
            sub.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            sub.setForeground(new Color(200, 240, 215));
            JPanel hText = new JPanel();
            hText.setOpaque(false);
            hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
            hText.add(hl); hText.add(Box.createVerticalStrut(4)); hText.add(sub);
            header.add(hText, BorderLayout.CENTER);
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(BankingConfig.BG);
            form.setBorder(BorderFactory.createEmptyBorder(20, 36, 10, 36));
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(6, 6, 6, 6);
            gc.fill = GridBagConstraints.HORIZONTAL;
            name        = addField(form, gc, BankingConfig.t("Full Name"), 0);
            email       = addField(form, gc, BankingConfig.t("Email"), 1);
            phone       = addField(form, gc, BankingConfig.t("Phone"), 2);
            username    = addField(form, gc, BankingConfig.t("Username"), 3);
            password    = addPassField(form, gc, BankingConfig.t("Password"), 4);
            confirmPass = addPassField(form, gc, BankingConfig.t("Confirm Password"), 5);
            password.setEchoChar('*');
            confirmPass.setEchoChar('*');
            gc.gridx = 0; gc.gridy = 6; gc.gridwidth = 2;
            JCheckBox showPass = new JCheckBox(BankingConfig.t("Show Password"));
            showPass.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            showPass.setBackground(BankingConfig.BG);
            showPass.setForeground(BankingConfig.MUTED);
            showPass.addActionListener(e -> {
                char echo = showPass.isSelected() ? (char) 0 : '*';
                password.setEchoChar(echo);
                confirmPass.setEchoChar(echo);
            });
            form.add(showPass, gc);
            gc.gridwidth = 1;
            gc.gridx = 0; gc.gridy = 7; gc.gridwidth = 2;
            JLabel passHint = new JLabel("Hint: " + BankingConfig.passwordRuleMessage());
            passHint.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
            passHint.setForeground(BankingConfig.MUTED);
            form.add(passHint, gc);
            gc.gridwidth = 1;
            gc.gridx = 0; gc.gridy = 8;
            form.add(mkLbl(BankingConfig.t("Account Type")), gc);
            accountType = new JComboBox<>(new String[]{"Savings", "Current", "Fixed Deposit"});
            accountType.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            accountType.setBackground(BankingConfig.CARD_BG);
            gc.gridx = 1; gc.gridy = 8;
            form.add(accountType, gc);
            JPanel btnP = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
            btnP.setBackground(BankingConfig.BG);
            UIComponents.PurpleButton create = new UIComponents.PurpleButton(
                BankingConfig.t("Create Account"), BankingConfig.PRIMARY);
            create.setPreferredSize(new Dimension(240, 48));
            create.setBorderPainted(true);
            btnP.add(create);
            create.addActionListener(e -> registerUser());
            final javax.swing.border.Border invalidBorder =
                BorderFactory.createLineBorder(BankingConfig.DANGER, 2, true);
            final javax.swing.border.Border validBorder =
                BorderFactory.createLineBorder(BankingConfig.SUCCESS, 2, true);
            final Runnable updatePasswordUi = () -> {
                String passText = new String(password.getPassword());
                boolean ok = BankingConfig.isStrongPassword(passText);
                passHint.setForeground(ok ? BankingConfig.SUCCESS : BankingConfig.DANGER);
                create.setBorder(ok ? validBorder : invalidBorder);
            };
            password.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePasswordUi.run(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePasswordUi.run(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePasswordUi.run(); }
            });
            updatePasswordUi.run();
            root.add(header, BorderLayout.NORTH);
            root.add(form, BorderLayout.CENTER);
            root.add(btnP, BorderLayout.SOUTH);
            JScrollPane scroll = new JScrollPane(root,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            add(scroll);
            setVisible(true);
        }
        JLabel mkLbl(String text) {
            JLabel l = new JLabel(text);
            l.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            l.setForeground(BankingConfig.FG);
            return l;
        }
        JTextField addField(JPanel p, GridBagConstraints gc, String label, int row) {
            gc.gridx = 0; gc.gridy = row;
            p.add(mkLbl(label), gc);
            JTextField f = new JTextField(18);
            f.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            f.setBackground(BankingConfig.CARD_BG);
            f.setForeground(BankingConfig.FG);
            f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            gc.gridx = 1; gc.gridy = row;
            p.add(f, gc);
            return f;
        }
        JPasswordField addPassField(JPanel p, GridBagConstraints gc, String label, int row) {
            gc.gridx = 0; gc.gridy = row;
            p.add(mkLbl(label), gc);
            JPasswordField f = new JPasswordField(18);
            f.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
            f.setBackground(BankingConfig.CARD_BG);
            f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BankingConfig.BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
            gc.gridx = 1; gc.gridy = row;
            p.add(f, gc);
            return f;
        }
        void registerUser() {
            String fullName = name.getText().trim(), user = username.getText().trim();
            String emailText = email.getText().trim(), phoneText = phone.getText().trim();
            String pass = new String(password.getPassword()), confirm = new String(confirmPass.getPassword());
            if (fullName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                BankingConfig.showDialog(this, BankingConfig.t("Fill all required fields."), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!BankingConfig.validEmail(emailText)) {
                BankingConfig.showDialog(this, BankingConfig.t("Invalid email address."), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!BankingConfig.validPhone(phoneText)) {
                BankingConfig.showDialog(this, BankingConfig.t("Invalid phone number."), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!pass.equals(confirm)) {
                BankingConfig.showDialog(this, BankingConfig.t("Passwords do not match."), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!BankingConfig.isStrongPassword(pass)) {
                BankingConfig.showDialog(this, BankingConfig.passwordRuleMessage(), JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (BankingConfig.con == null) {
                BankingConfig.showDialog(this, BankingConfig.t("Database not connected!\nOpen XAMPP and start MySQL."), JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                try {
                    BankingConfig.con.createStatement().executeUpdate(
                        "ALTER TABLE accounts MODIFY password VARCHAR(255) NOT NULL");
                } catch (Exception ignored) {}
                String accNo = BankingConfig.generateAccountNumber();
                String hashedPassword = BankingConfig.hashPassword(pass);
                PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "INSERT INTO accounts(account_number,account_holder_name,email,phone,username,password,account_type,balance,status,role) VALUES(?,?,?,?,?,?,?,0,'Active','Customer')");
                ps.setString(1, accNo); ps.setString(2, fullName);
                ps.setString(3, emailText); ps.setString(4, phoneText);
                ps.setString(5, user); ps.setString(6, hashedPassword);
                ps.setString(7, (String) accountType.getSelectedItem());
                ps.executeUpdate(); ps.close();
                PreparedStatement check = BankingConfig.con.prepareStatement(
                    "SELECT username, password, status, role FROM accounts WHERE username=?");
                check.setString(1, user);
                ResultSet cr = check.executeQuery();
                String savedPwd = null;
                if (cr.next()) savedPwd = cr.getString("password");
                cr.close(); check.close();
                boolean loginWillWork = BankingConfig.verifyPassword(pass, savedPwd);
                BankingConfig.showDialog(this,
                    "\u2705  " + BankingConfig.t("Account Created!") + "\n\n" +
                    BankingConfig.t("Account Number") + ": " + accNo + "\n" +
                    BankingConfig.t("Holder") + ": " + fullName + "\n" +
                    BankingConfig.t("Username") + ": " + user,
                    BankingConfig.t("Account Created!"), JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (SQLIntegrityConstraintViolationException e) {
                BankingConfig.showDialog(this, BankingConfig.t("Username already exists."), JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                BankingConfig.showDialog(this, BankingConfig.t("Registration error:") + "\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}