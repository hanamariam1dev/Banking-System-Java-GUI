import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
public class DashboardViews {
    private final Dashboard dashboard;
    public DashboardViews(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
    void showHome() {
        dashboard.currentView = "Home";
        if (dashboard.backBtn != null)
            dashboard.backBtn.setVisible(false);
        dashboard.contentArea.removeAll();
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BankingConfig.BG);
        JPanel heroCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK, getWidth(), getHeight(),
                        BankingConfig.PRIMARY));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(getWidth() - 120, -40, 200, 200);
            }
        };
        heroCard.setOpaque(false);
        heroCard.setLayout(new BorderLayout());
        heroCard.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        JPanel heroLeft = new JPanel();
        heroLeft.setOpaque(false);
        heroLeft.setLayout(new BoxLayout(heroLeft, BoxLayout.Y_AXIS));
        JLabel heroName = new JLabel(dashboard.accountHolder);
        heroName.setFont(BankingConfig.uiFont(Font.BOLD, 20));
        heroName.setForeground(Color.WHITE);
        JLabel heroBalCap = new JLabel(BankingConfig.t("Available Balance"));
        heroBalCap.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        heroBalCap.setForeground(BankingConfig.PRIMARY_SOFT);
        dashboard.heroBalLbl = new JLabel(String.format("ETB  %,.2f", dashboard.cachedBalance));
        dashboard.heroBalLbl.setFont(BankingConfig.uiFont(Font.BOLD, 28));
        dashboard.heroBalLbl.setForeground(Color.WHITE);
        heroLeft.add(heroName);
        heroLeft.add(Box.createVerticalStrut(16));
        heroLeft.add(heroBalCap);
        heroLeft.add(Box.createVerticalStrut(4));
        JPanel heroBalRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        heroBalRow.setOpaque(false);
        heroBalRow.add(dashboard.heroBalLbl);
        dashboard.heroBalToggleBtn = dashboard.makeEyeToggleBtn();
        heroBalRow.add(dashboard.heroBalToggleBtn);
        heroLeft.add(heroBalRow);
        heroCard.add(heroLeft, BorderLayout.CENTER);
        JPanel heroRight = new JPanel();
        heroRight.setOpaque(false);
        heroRight.setLayout(new BoxLayout(heroRight, BoxLayout.Y_AXIS));
        JLabel accNumLbl = new JLabel(BankingConfig.t("Account No."));
        accNumLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
        accNumLbl.setForeground(BankingConfig.PRIMARY_SOFT);
        accNumLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JLabel accNumVal = new JLabel(dashboard.accountNumber);
        accNumVal.setFont(BankingConfig.uiFont(Font.BOLD, 12));
        accNumVal.setForeground(Color.WHITE);
        accNumVal.setAlignmentX(Component.RIGHT_ALIGNMENT);
        heroRight.add(accNumLbl);
        heroRight.add(accNumVal);
        heroCard.add(heroRight, BorderLayout.EAST);
        JPanel heroWrap = new JPanel(new BorderLayout());
        heroWrap.setOpaque(false);
        heroWrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        heroWrap.add(heroCard, BorderLayout.CENTER);
        JPanel quickPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        quickPanel.setOpaque(false);
        quickPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        quickPanel.add(new UIComponents.QuickActionBtn("\u2191", BankingConfig.t("Deposit"), BankingConfig.SUCCESS,
                dashboard::deposit));
        quickPanel.add(new UIComponents.QuickActionBtn("\u2193", BankingConfig.t("Withdraw"), BankingConfig.DANGER,
                dashboard::withdraw));
        quickPanel.add(new UIComponents.QuickActionBtn("\u2192", BankingConfig.t("Transfer"), BankingConfig.WARNING,
                dashboard::transfer));
        quickPanel.add(
                new UIComponents.QuickActionBtn("\u25a1", BankingConfig.t("QR Code"), BankingConfig.PRIMARY, () -> {
                    dashboard.currentView = "QR";
                    dashboard.showQR();
                }));
        quickPanel.add(new UIComponents.QuickActionBtn("\ud83d\udcf1", BankingConfig.t("Airtime"), BankingConfig.INFO,
                dashboard::airtime));
        quickPanel.add(new UIComponents.QuickActionBtn("\u2708\ufe0f", BankingConfig.t("Travel"),
                new Color(0, 160, 220), dashboard::travel_booking));
        quickPanel.add(new UIComponents.QuickActionBtn("\ud83d\udcca", BankingConfig.t("History"),
                BankingConfig.PRIMARY_LIGHT, () -> {
                    dashboard.currentView = "History";
                    dashboard.showHistory();
                }));
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT COUNT(*) as cnt, " +
                            "SUM(CASE WHEN transaction_type='Deposit' THEN amount ELSE 0 END) as dep, " +
                            "SUM(CASE WHEN transaction_type='Withdrawal' THEN amount ELSE 0 END) as wdw " +
                            "FROM transactions WHERE account_number=? AND MONTH(transaction_date)=MONTH(CURDATE())");
            ps.setString(1, dashboard.accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                statsRow.add(new UIComponents.StatCard("\ud83d\udcca", String.valueOf(rs.getInt("cnt")),
                        BankingConfig.t("Transactions This Month"), BankingConfig.PRIMARY_LIGHT));
                statsRow.add(new UIComponents.StatCard("\u2191", BankingConfig.formatCurrency(rs.getDouble("dep")),
                        BankingConfig.t("Total Deposited"), BankingConfig.SUCCESS));
                statsRow.add(new UIComponents.StatCard("\u2193", BankingConfig.formatCurrency(rs.getDouble("wdw")),
                        BankingConfig.t("Total Withdrawn"), BankingConfig.DANGER));
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            statsRow.add(new UIComponents.StatCard("\ud83d\udcca", "0", BankingConfig.t("Transactions This Month"),
                    BankingConfig.PRIMARY_LIGHT));
            statsRow.add(new UIComponents.StatCard("\u2191", "ETB 0.00", BankingConfig.t("Total Deposited"),
                    BankingConfig.SUCCESS));
            statsRow.add(new UIComponents.StatCard("\u2193", "ETB 0.00", BankingConfig.t("Total Withdrawn"),
                    BankingConfig.DANGER));
        }
        JLabel recentLbl = new JLabel(BankingConfig.t("Recent Transactions"));
        recentLbl.setFont(BankingConfig.uiFont(Font.BOLD, 15));
        recentLbl.setForeground(BankingConfig.FG);
        recentLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JPanel recentPanel = new JPanel();
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.setOpaque(false);
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_number=? ORDER BY transaction_date DESC LIMIT 5");
            ps.setString(1, dashboard.accountNumber);
            ResultSet rs = ps.executeQuery();
            boolean any = false;
            while (rs.next()) {
                any = true;
                String type = rs.getString("transaction_type");
                double amt = rs.getDouble("amount");
                String desc = rs.getString("description");
                String date = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm")
                        .format(rs.getTimestamp("transaction_date"));
                boolean isCredit = type.equals("Deposit") || type.equals("Transfer In");
                UIComponents.TxRow row = new UIComponents.TxRow(
                        BankingConfig.t(type),
                        desc != null ? BankingConfig.t(desc) : BankingConfig.t(type),
                        date, amt, isCredit);
                row.setAlignmentX(Component.LEFT_ALIGNMENT);
                recentPanel.add(row);
                recentPanel.add(Box.createVerticalStrut(6));
            }
            rs.close();
            ps.close();
            if (!any) {
                JLabel noTx = new JLabel(BankingConfig.t("No transactions yet."));
                noTx.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
                noTx.setForeground(BankingConfig.MUTED);
                recentPanel.add(noTx);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BankingConfig.BG);
        heroWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        recentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(heroWrap);
        contentPanel.add(quickPanel);
        contentPanel.add(statsRow);
        contentPanel.add(recentLbl);
        contentPanel.add(recentPanel);
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        page.add(scroll, BorderLayout.CENTER);
        dashboard.contentArea.add(page, BorderLayout.CENTER);
        dashboard.contentArea.revalidate();
        dashboard.contentArea.repaint();
        dashboard.refreshBalanceDisplay();
    }
    void showHistory() {
        dashboard.currentView = "History";
        dashboard.backBtn.setVisible(true);
        dashboard.showLoading();
        dashboard.contentArea.removeAll();
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
        header.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));
        JLabel hTitle = new JLabel("\u2261  " + BankingConfig.t("Transaction History"));
        hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 17));
        hTitle.setForeground(BankingConfig.FG);
        JLabel hSubtitle = new JLabel(BankingConfig.t("Click download button to get receipt"));
        hSubtitle.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        hSubtitle.setForeground(BankingConfig.MUTED);
        JPanel headerLeft = new JPanel();
        headerLeft.setLayout(new BoxLayout(headerLeft, BoxLayout.Y_AXIS));
        headerLeft.setOpaque(false);
        headerLeft.add(hTitle);
        headerLeft.add(hSubtitle);
        header.add(headerLeft, BorderLayout.WEST);
        JButton exportBtn = dashboard.makeTopBtn("\u2193 CSV", BankingConfig.PRIMARY_SOFT, BankingConfig.PRIMARY);
        exportBtn.addActionListener(e -> exportHistory());
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setOpaque(false);
        headerRight.add(exportBtn);
        header.add(headerRight, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);
        String[] cols = { "#", BankingConfig.t("Date"), BankingConfig.t("Type"),
                BankingConfig.t("Amount (ETB)"), BankingConfig.t("Balance After"), BankingConfig.t("Description"),
                BankingConfig.t("Download") };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_number=? ORDER BY transaction_date DESC LIMIT 200");
            ps.setString(1, dashboard.accountNumber);
            ResultSet rs = ps.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                model.addRow(new Object[] {
                        rs.getInt("transaction_id"),
                        BankingConfig.dateFormat.format(rs.getTimestamp("transaction_date")),
                        BankingConfig.t(rs.getString("transaction_type")),
                        String.format("%,.2f", rs.getDouble("amount")),
                        String.format("%,.2f", rs.getDouble("balance_after")),
                        rs.getString("description") != null ? BankingConfig.t(rs.getString("description")) : "",
                        "Download"
                });
            }
            rs.close();
            ps.close();
            if (rowCount == 0) {
                dashboard.showNotification("No transactions found for account: " + dashboard.accountNumber, false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            dashboard.showNotification("Error loading transactions: " + ex.getMessage(), false);
        }
        JTable table = buildStyledTable(model);
        JScrollPane tableScroll = buildStyledScroll(table);
        root.add(tableScroll, BorderLayout.CENTER);
        dashboard.contentArea.add(root, BorderLayout.CENTER);
        dashboard.contentArea.revalidate();
        dashboard.contentArea.repaint();
    }
    void exportHistory() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("statement_" + dashboard.accountNumber + ".csv"));
        if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
            return;
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_number=? ORDER BY transaction_date DESC");
            ps.setString(1, dashboard.accountNumber);
            ResultSet rs = ps.executeQuery();
            try (FileWriter w = new FileWriter(fc.getSelectedFile())) {
                w.write("ID,Date,Type,Amount,Balance After,Description\n");
                while (rs.next()) {
                    String desc = rs.getString("description");
                    if (desc == null)
                        desc = "";
                    w.write(String.format("%d,%s,%s,%.2f,%.2f,\"%s\"\n",
                            rs.getInt("transaction_id"),
                            BankingConfig.dateFormat.format(rs.getTimestamp("transaction_date")),
                            rs.getString("transaction_type"),
                            rs.getDouble("amount"), rs.getDouble("balance_after"),
                            desc.replace("\"", "\"\"")));
                }
            }
            rs.close();
            ps.close();
            dashboard.showNotification(BankingConfig.t("Export CSV succeeded"), true);
        } catch (Exception ex) {
            dashboard.showNotification(BankingConfig.t("Export failed"), false);
        }
    }
    void downloadTransactionReceipt(int transactionId) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("receipt_" + transactionId + ".pdf"));
        if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
            return;
        String filePath = fc.getSelectedFile().getAbsolutePath();
        new Thread(() -> {
            try {
                BankingUtils.generateReceiptPDF(transactionId, filePath);
                SwingUtilities.invokeLater(() ->
                    dashboard.showNotification(
                        BankingConfig.t("Receipt downloaded successfully"), true));
                try {
                    java.awt.Desktop.getDesktop().open(new File(filePath));
                } catch (Exception openEx) {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                    dashboard.showNotification(BankingConfig.t("Receipt download failed"), false));
            }
        }).start();
    }
    JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 3));
        table.setBackground(BankingConfig.BG);
        table.setForeground(BankingConfig.FG);
        table.setSelectionBackground(BankingConfig.PRIMARY_SOFT);
        table.setSelectionForeground(BankingConfig.PRIMARY_DARK);
        table.setFillsViewportHeight(true);
        JTableHeader th = table.getTableHeader();
        th.setPreferredSize(new Dimension(th.getPreferredSize().width, 38));
        th.setReorderingAllowed(false);
        th.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                    int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                lbl.setBackground(BankingConfig.PRIMARY);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(BankingConfig.uiFont(Font.BOLD, 12));
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return lbl;
            }
        });
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row,
                    int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setOpaque(true);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (col == 6) {
                    setText("\u2193 " + BankingConfig.t("Download"));
                    setHorizontalAlignment(JLabel.CENTER);
                    setBackground(BankingConfig.PRIMARY);
                    setForeground(Color.WHITE);
                    setFont(BankingConfig.uiFont(Font.BOLD, 11));
                    setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    return this;
                }
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
                    if (col == 3) {
                        setHorizontalAlignment(JLabel.RIGHT);
                        setForeground(BankingConfig.SUCCESS);
                    } else if (col == 4) {
                        setHorizontalAlignment(JLabel.RIGHT);
                        setForeground(BankingConfig.PRIMARY);
                    } else
                        setHorizontalAlignment(JLabel.LEFT);
                }
                return this;
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 6 && row >= 0) {
                    int transactionId = (Integer) table.getValueAt(row, 0);
                    downloadTransactionReceipt(transactionId);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                table.setCursor(Cursor.getDefaultCursor());
            }
        });
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                if (col == 6 && row >= 0) {
                    table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    table.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        return table;
    }
    JScrollPane buildStyledScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        return scroll;
    }
    void showProfile() {
        dashboard.currentView = "Profile";
        dashboard.backBtn.setVisible(true);
        dashboard.contentArea.removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BankingConfig.BG);
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK, getWidth(), 0, BankingConfig.PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        final Image[] profileImg = { null };
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
                        profileImg[0] = javax.imageio.ImageIO.read(f);
                }
            }
            rsi.close();
            psi.close();
        } catch (Exception ignored) {
        }
        JPanel avatarSection = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK, getWidth(), 0, BankingConfig.PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        avatarSection.setOpaque(false);
        avatarSection.setLayout(new BoxLayout(avatarSection, BoxLayout.Y_AXIS));
        avatarSection.setBorder(BorderFactory.createEmptyBorder(28, 0, 24, 0));
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 180));
                g2.setStroke(new BasicStroke(3f));
                g2.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                if (profileImg[0] != null) {
                    java.awt.geom.Ellipse2D clip = new java.awt.geom.Ellipse2D.Float(4, 4, getWidth() - 8,
                            getHeight() - 8);
                    g2.setClip(clip);
                    Image scaled = profileImg[0].getScaledInstance(getWidth() - 8, getHeight() - 8, Image.SCALE_SMOOTH);
                    g2.drawImage(scaled, 4, 4, null);
                    g2.setClip(null);
                } else {
                    g2.setColor(BankingConfig.PRIMARY_LIGHT);
                    g2.fillOval(4, 4, getWidth() - 8, getHeight() - 8);
                    g2.setColor(Color.WHITE);
                    g2.setFont(BankingConfig.uiFont(Font.BOLD, 36));
                    FontMetrics fm = g2.getFontMetrics();
                    String init = dashboard.accountHolder.isEmpty() ? "?"
                            : String.valueOf(dashboard.accountHolder.charAt(0)).toUpperCase();
                    g2.drawString(init, (getWidth() - fm.stringWidth(init)) / 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                }
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillArc(0, getHeight() / 2, getWidth(), getHeight() / 2, 180, 180);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                FontMetrics fm2 = g2.getFontMetrics();
                String cam = "\uD83D\uDCF7";
                g2.drawString(cam, (getWidth() - fm2.stringWidth(cam)) / 2, getHeight() - 8);
            }
        };
        avatarCircle.setOpaque(false);
        avatarCircle.setPreferredSize(new Dimension(100, 100));
        avatarCircle.setMaximumSize(new Dimension(100, 100));
        avatarCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarCircle.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        avatarCircle.setToolTipText(BankingConfig.t("Change profile photo"));
        avatarCircle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dashboard.changeProfilePicture(avatarCircle, img -> {
                    profileImg[0] = img;
                    avatarCircle.repaint();
                });
            }
        });
        JLabel nameBig = new JLabel(dashboard.accountHolder, SwingConstants.CENTER);
        nameBig.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        nameBig.setForeground(Color.WHITE);
        nameBig.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel accTag = new JLabel(dashboard.accountNumber, SwingConstants.CENTER);
        accTag.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        accTag.setForeground(new Color(190, 230, 210));
        accTag.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton changePhotoBtn = new JButton("\uD83D\uDCF7  " + BankingConfig.t("Change Photo")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        changePhotoBtn.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        changePhotoBtn.setForeground(Color.WHITE);
        changePhotoBtn.setOpaque(false);
        changePhotoBtn.setContentAreaFilled(false);
        changePhotoBtn.setBorderPainted(false);
        changePhotoBtn.setFocusPainted(false);
        changePhotoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        changePhotoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePhotoBtn.addActionListener(e -> dashboard.changeProfilePicture(avatarCircle, img -> {
            profileImg[0] = img;
            avatarCircle.repaint();
        }));
        avatarSection.add(avatarCircle);
        avatarSection.add(Box.createVerticalStrut(10));
        avatarSection.add(nameBig);
        avatarSection.add(Box.createVerticalStrut(4));
        avatarSection.add(accTag);
        avatarSection.add(Box.createVerticalStrut(10));
        avatarSection.add(changePhotoBtn);
        header.add(avatarSection, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, 18, 18);
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 18, 18);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 28, 24, 28));
        JTextField[] editFields = new JTextField[3];
        String[] editKeys = { BankingConfig.t("Full Name"), BankingConfig.t("Email"), BankingConfig.t("Phone") };
        String[] dbCols = { "account_holder_name", "email", "phone" };
        String[] dbVals = { dashboard.accountHolder, "", "" };
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT * FROM accounts WHERE account_number=?");
            ps.setString(1, dashboard.accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dbVals[0] = rs.getString("account_holder_name");
                dbVals[1] = rs.getString("email") != null ? rs.getString("email") : "";
                dbVals[2] = rs.getString("phone") != null ? rs.getString("phone") : "";
                String[][] infoRows = {
                        { BankingConfig.t("Account Number"), rs.getString("account_number") },
                        { BankingConfig.t("Account Type"), rs.getString("account_type") },
                        { BankingConfig.t("Balance"), "ETB " + String.format("%,.2f", rs.getDouble("balance")) },
                        { BankingConfig.t("Status"), rs.getString("status") },
                        { BankingConfig.t("Member Since"), rs.getString("created_at") != null
                                ? rs.getString("created_at").substring(0, 10)
                                : "-" },
                };
                Color[] infoColors = {
                        BankingConfig.FG, BankingConfig.FG,
                        BankingConfig.SUCCESS, BankingConfig.INFO, BankingConfig.MUTED
                };
                JLabel infoTitle = new JLabel(BankingConfig.t("Account Information"));
                infoTitle.setFont(BankingConfig.uiFont(Font.BOLD, 13));
                infoTitle.setForeground(BankingConfig.PRIMARY);
                infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.add(infoTitle);
                card.add(Box.createVerticalStrut(10));
                for (int i = 0; i < infoRows.length; i++) {
                    card.add(buildInfoRow(infoRows[i][0], infoRows[i][1], infoColors[i]));
                    card.add(Box.createVerticalStrut(6));
                }
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        card.add(Box.createVerticalStrut(14));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BankingConfig.BORDER);
        card.add(sep);
        card.add(Box.createVerticalStrut(14));
        JLabel editTitle = new JLabel(BankingConfig.t("Edit Profile"));
        editTitle.setFont(BankingConfig.uiFont(Font.BOLD, 13));
        editTitle.setForeground(BankingConfig.PRIMARY);
        editTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(editTitle);
        card.add(Box.createVerticalStrut(12));
        for (int i = 0; i < editKeys.length; i++) {
            JLabel lbl = new JLabel(editKeys[i]);
            lbl.setFont(BankingConfig.uiFont(Font.BOLD, 11));
            lbl.setForeground(BankingConfig.MUTED);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            UIComponents.RoundedField field = new UIComponents.RoundedField(editKeys[i]);
            field.setText(dbVals[i]);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            editFields[i] = field;
            card.add(lbl);
            card.add(Box.createVerticalStrut(4));
            card.add(field);
            card.add(Box.createVerticalStrut(10));
        }
        card.add(Box.createVerticalStrut(6));
        UIComponents.PurpleButton saveBtn = new UIComponents.PurpleButton(
                BankingConfig.t("Save Changes"), BankingConfig.PRIMARY);
        saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        final String[] fDbVals = dbVals;
        saveBtn.addActionListener(e -> {
            String newName = editFields[0].getText().trim();
            String newEmail = editFields[1].getText().trim();
            String newPhone = editFields[2].getText().trim();
            if (newName.isEmpty()) {
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Full Name cannot be empty."), JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                PreparedStatement ps = BankingConfig.con.prepareStatement(
                        "UPDATE accounts SET account_holder_name=?, email=?, phone=? WHERE account_number=?");
                ps.setString(1, newName);
                ps.setString(2, newEmail);
                ps.setString(3, newPhone);
                ps.setString(4, dashboard.accountNumber);
                ps.executeUpdate();
                ps.close();
                dashboard.showNotification(BankingConfig.t("Profile updated successfully."), true);
                showProfile();
            } catch (Exception ex) {
                ex.printStackTrace();
                BankingConfig.showDialog(AppFrame.get(),
                        BankingConfig.t("Update failed: ") + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });
        card.add(saveBtn);
        card.add(Box.createVerticalStrut(8));
        JButton copyBtn = dashboard.makeTopBtn("\u2398  " + BankingConfig.t("Copy Account Number"),
                BankingConfig.PRIMARY_SOFT, BankingConfig.PRIMARY);
        copyBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        copyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        copyBtn.addActionListener(e -> {
            try {
                StringSelection sel = new StringSelection(dashboard.accountNumber);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
                dashboard.showNotification(BankingConfig.t("Account number copied to clipboard!"), true);
            } catch (Exception ex) {
                dashboard.showNotification(BankingConfig.t("Copy failed."), false);
            }
        });
        card.add(copyBtn);
        JPanel formOuter = new JPanel(new GridBagLayout());
        formOuter.setBackground(BankingConfig.BG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(20, 40, 20, 40);
        formOuter.add(card, gc);
        JScrollPane scroll = new JScrollPane(formOuter);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        root.add(scroll, BorderLayout.CENTER);
        dashboard.contentArea.add(root, BorderLayout.CENTER);
        dashboard.contentArea.revalidate();
        dashboard.contentArea.repaint();
    }
    JPanel buildInfoRow(String key, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel k = new JLabel(key);
        k.setFont(BankingConfig.uiFont(Font.BOLD, 11));
        k.setForeground(BankingConfig.MUTED);
        k.setPreferredSize(new Dimension(140, 24));
        JLabel v = new JLabel(value);
        v.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
        v.setForeground(valueColor);
        row.add(k, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        return row;
    }
    void showQR() {
        dashboard.currentView = "QR";
        dashboard.backBtn.setVisible(true);
        dashboard.contentArea.removeAll();
        try {
            String qrData = "CBE|" + dashboard.accountNumber + "|" + dashboard.accountHolder;
            java.awt.image.BufferedImage qrImg = QRCodeGenerator.generate(qrData, 280);
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
            header.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));
            JLabel hTitle = new JLabel("#  " + BankingConfig.t("Your Account QR Code"));
            hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 17));
            hTitle.setForeground(BankingConfig.FG);
            header.add(hTitle, BorderLayout.WEST);
            root.add(header, BorderLayout.NORTH);
            JPanel body = new JPanel(new GridBagLayout());
            body.setBackground(BankingConfig.BG);
            JPanel qrCard = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, BankingConfig.darkMode ? 60 : 18));
                    g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 6, 22, 22);
                    g2.setColor(BankingConfig.CARD_BG);
                    g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 5, 22, 22);
                    if (BankingConfig.darkMode) {
                        g2.setColor(BankingConfig.BORDER);
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 6, 22, 22);
                    }
                }
            };
            qrCard.setOpaque(false);
            qrCard.setLayout(new BoxLayout(qrCard, BoxLayout.Y_AXIS));
            qrCard.setPreferredSize(new Dimension(340, 520));
            JPanel cardHeader = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(
                            new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK, getWidth(), 0, BankingConfig.PRIMARY));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 22, 22);
                }
            };
            cardHeader.setOpaque(false);
            cardHeader.setLayout(new BoxLayout(cardHeader, BoxLayout.Y_AXIS));
            cardHeader.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
            cardHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            JLabel bankName = new JLabel(BankingConfig.t("Commercial Bank of Ethiopia"), SwingConstants.CENTER);
            bankName.setFont(BankingConfig.uiFont(Font.BOLD, 13));
            bankName.setForeground(Color.WHITE);
            bankName.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel tagLine = new JLabel(BankingConfig.t("Scan to send money to this account"), SwingConstants.CENTER);
            tagLine.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
            tagLine.setForeground(new Color(190, 235, 210));
            tagLine.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardHeader.add(bankName);
            cardHeader.add(Box.createVerticalStrut(4));
            cardHeader.add(tagLine);
            JPanel qrWrap = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                    g2.setColor(new Color(200, 225, 208));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                }
            };
            qrWrap.setOpaque(false);
            qrWrap.setLayout(new GridBagLayout());
            qrWrap.setPreferredSize(new Dimension(280, 280));
            qrWrap.setMaximumSize(new Dimension(280, 280));
            qrWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel qrLbl = new JLabel(new ImageIcon(qrImg));
            qrWrap.add(qrLbl);
            JLabel accLbl = new JLabel(dashboard.accountNumber, SwingConstants.CENTER);
            accLbl.setFont(BankingConfig.uiFont(Font.BOLD, 16));
            accLbl.setForeground(BankingConfig.PRIMARY);
            accLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel nameLbl = new JLabel(dashboard.accountHolder, SwingConstants.CENTER);
            nameLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
            nameLbl.setForeground(BankingConfig.MUTED);
            nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            JSeparator div = new JSeparator();
            div.setForeground(BankingConfig.BORDER);
            div.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
            btnRow.setOpaque(false);
            btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            btnRow.setAlignmentX(Component.CENTER_ALIGNMENT);
            UIComponents.PurpleButton copyBtn = new UIComponents.PurpleButton(
                    BankingConfig.t("Copy Account Number"), BankingConfig.PRIMARY);
            copyBtn.addActionListener(e -> {
                try {
                    StringSelection sel = new StringSelection(dashboard.accountNumber);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
                    dashboard.showNotification(BankingConfig.t("Account number copied to clipboard!"), true);
                } catch (Exception ex) {
                    dashboard.showNotification(BankingConfig.t("Copy failed."), false);
                }
            });
            UIComponents.PurpleButton saveBtn = new UIComponents.PurpleButton(
                    BankingConfig.t("Save QR Image"), BankingConfig.INFO);
            saveBtn.addActionListener(e -> {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File("CBE_QR_" + dashboard.accountNumber + ".png"));
                if (fc.showSaveDialog(AppFrame.get()) == JFileChooser.APPROVE_OPTION) {
                    try {
                        javax.imageio.ImageIO.write(qrImg, "PNG", fc.getSelectedFile());
                        dashboard.showNotification(BankingConfig.t("QR Code saved successfully!"), true);
                    } catch (Exception ex) {
                        dashboard.showNotification(BankingConfig.t("Failed to save QR image."), false);
                    }
                }
            });
            btnRow.add(copyBtn);
            btnRow.add(saveBtn);
            JLabel secNote = new JLabel(
                    "<html><center>" + BankingConfig.t("This QR only allows receiving money.") +
                            "<br>" + BankingConfig.t("Never share your PIN or password.") + "</center></html>",
                    SwingConstants.CENTER);
            secNote.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
            secNote.setForeground(BankingConfig.MUTED);
            secNote.setAlignmentX(Component.CENTER_ALIGNMENT);
            qrCard.add(cardHeader);
            qrCard.add(Box.createVerticalStrut(20));
            qrCard.add(qrWrap);
            qrCard.add(Box.createVerticalStrut(14));
            qrCard.add(accLbl);
            qrCard.add(Box.createVerticalStrut(4));
            qrCard.add(nameLbl);
            qrCard.add(Box.createVerticalStrut(16));
            qrCard.add(div);
            qrCard.add(Box.createVerticalStrut(14));
            JPanel btnWrap = new JPanel(new BorderLayout());
            btnWrap.setOpaque(false);
            btnWrap.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            btnWrap.add(btnRow, BorderLayout.CENTER);
            btnWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            qrCard.add(btnWrap);
            qrCard.add(Box.createVerticalStrut(14));
            JPanel noteWrap = new JPanel(new BorderLayout());
            noteWrap.setOpaque(false);
            noteWrap.setBorder(BorderFactory.createEmptyBorder(0, 20, 16, 20));
            noteWrap.add(secNote, BorderLayout.CENTER);
            qrCard.add(noteWrap);
            body.add(qrCard);
            root.add(body, BorderLayout.CENTER);
            dashboard.contentArea.add(root, BorderLayout.CENTER);
            dashboard.contentArea.revalidate();
            dashboard.contentArea.repaint();
        } catch (Exception e) {
            JPanel errorPanel = new JPanel(new BorderLayout());
            errorPanel.setBackground(BankingConfig.BG);
            errorPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            JLabel errorLabel = new JLabel("QR Code generation failed. Please try again.", SwingConstants.CENTER);
            errorLabel.setFont(BankingConfig.uiFont(Font.PLAIN, 14));
            errorLabel.setForeground(BankingConfig.FG);
            JButton retryBtn = new JButton("Retry");
            retryBtn.setFont(BankingConfig.uiFont(Font.BOLD, 12));
            retryBtn.setBackground(BankingConfig.PRIMARY);
            retryBtn.setForeground(Color.WHITE);
            retryBtn.setFocusPainted(false);
            retryBtn.addActionListener(evt -> showQR());
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            buttonPanel.add(retryBtn);
            errorPanel.add(errorLabel, BorderLayout.CENTER);
            errorPanel.add(buttonPanel, BorderLayout.SOUTH);
            dashboard.contentArea.add(errorPanel, BorderLayout.CENTER);
            dashboard.contentArea.revalidate();
            dashboard.contentArea.repaint();
        }
    }
    void showFAQ() {
        dashboard.currentView = "FAQ";
        dashboard.backBtn.setVisible(true);
        dashboard.contentArea.removeAll();
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
        header.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));
        JLabel hTitle = new JLabel("?  " + BankingConfig.t("Frequently Asked Questions"));
        hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 17));
        hTitle.setForeground(BankingConfig.FG);
        header.add(hTitle, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);
        String[][] faqs = {
                { BankingConfig.t("How do I deposit money?"),
                        BankingConfig.t(
                                "Click 'Deposit' in the sidebar or Quick Actions. Enter the amount and confirm with your password.") },
                { BankingConfig.t("How do I withdraw cash?"),
                        BankingConfig.t(
                                "Click 'Withdraw', enter the amount (must not exceed your balance) and confirm with your password.") },
                { BankingConfig.t("How do I transfer money?"),
                        BankingConfig.t(
                                "Click 'Transfer', choose the transfer type, enter the recipient account and amount, then confirm.") },
                { BankingConfig.t("How do I view my transaction history?"),
                        BankingConfig
                                .t("Click 'History' in the sidebar or Quick Actions to see all past transactions.") },
                { BankingConfig.t("How do I change my password?"),
                        BankingConfig.t(
                                "Go to TOOLS > Change PIN. Enter your current password, then your new password twice.") },
                { BankingConfig.t("What is the QR Code for?"),
                        BankingConfig.t(
                                "Your QR code encodes your account number. Others can scan it to send money to you.") },
                { BankingConfig.t("Is my account secure?"),
                        BankingConfig.t(
                                "Yes. Passwords are stored using PBKDF2 hashing. Every transaction requires password confirmation.") },
        };
        JPanel faqList = new JPanel();
        faqList.setLayout(new BoxLayout(faqList, BoxLayout.Y_AXIS));
        faqList.setBackground(BankingConfig.BG);
        faqList.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        boolean[] expanded = new boolean[faqs.length];
        Runnable[] render = new Runnable[1];
        render[0] = () -> {
            faqList.removeAll();
            for (int i = 0; i < faqs.length; i++) {
                final int idx = i;
                final boolean open = expanded[i];
                JPanel card = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(0, 0, 0, 10));
                        g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 2, 14, 14);
                        g2.setColor(BankingConfig.CARD_BG);
                        g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 14, 14);
                        if (open) {
                            g2.setColor(BankingConfig.PRIMARY);
                            g2.fillRoundRect(0, 10, 4, getHeight() - 20, 4, 4);
                        }
                    }
                };
                card.setOpaque(false);
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, open ? 200 : 52));
                JPanel qRow = new JPanel(new BorderLayout(10, 0));
                qRow.setOpaque(false);
                qRow.setBorder(BorderFactory.createEmptyBorder(14, 18, open ? 8 : 14, 16));
                qRow.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                JLabel qLbl = new JLabel(faqs[idx][0]);
                qLbl.setFont(BankingConfig.uiFont(open ? Font.BOLD : Font.PLAIN, 13));
                qLbl.setForeground(open ? BankingConfig.PRIMARY : BankingConfig.FG);
                JLabel chevron = new JLabel(open ? "\u25B2" : "\u25BC");
                chevron.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
                chevron.setForeground(BankingConfig.MUTED);
                qRow.add(qLbl, BorderLayout.CENTER);
                qRow.add(chevron, BorderLayout.EAST);
                card.add(qRow);
                if (open) {
                    JPanel aPanel = new JPanel(new BorderLayout());
                    aPanel.setOpaque(false);
                    aPanel.setBorder(BorderFactory.createEmptyBorder(0, 18, 14, 16));
                    JLabel aLbl = new JLabel("<html><body style='width:500px'>" + faqs[idx][1] + "</body></html>");
                    aLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
                    aLbl.setForeground(BankingConfig.FG);
                    aPanel.add(aLbl, BorderLayout.CENTER);
                    card.add(aPanel);
                }
                MouseAdapter toggle = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        expanded[idx] = !expanded[idx];
                        render[0].run();
                    }
                };
                qRow.addMouseListener(toggle);
                faqList.add(card);
                faqList.add(Box.createVerticalStrut(8));
            }
            faqList.revalidate();
            faqList.repaint();
        };
        render[0].run();
        JScrollPane scroll = new JScrollPane(faqList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        root.add(scroll, BorderLayout.CENTER);
        dashboard.contentArea.add(root, BorderLayout.CENTER);
        dashboard.contentArea.revalidate();
        dashboard.contentArea.repaint();
    }
    void showNotificationsPanel() {
        List<String[]> notifs = new ArrayList<>();
        try {
            PreparedStatement ps = BankingConfig.con.prepareStatement(
                    "SELECT transaction_type, amount, balance_after, description, transaction_date " +
                            "FROM transactions WHERE account_number=? ORDER BY transaction_date DESC LIMIT 3");
            ps.setString(1, dashboard.accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("transaction_type");
                double amt = rs.getDouble("amount");
                double bal = rs.getDouble("balance_after");
                String desc = rs.getString("description");
                String time = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm")
                        .format(rs.getTimestamp("transaction_date"));
                boolean credit = type.equals("Deposit") || type.equals("Transfer In");
                String icon = credit ? "\uD83D\uDCB0" : "\uD83D\uDCB8";
                String title = credit
                        ? BankingConfig.t("Credit Alert") + " - " + BankingConfig.formatCurrency(amt)
                        : BankingConfig.t("Debit Alert") + " - " + BankingConfig.formatCurrency(amt);
                String detail = BankingConfig.t("Account") + ": " + dashboard.accountNumber + "\n"
                        + BankingConfig.t("Amount") + ": " + (credit ? "+" : "-") + BankingConfig.formatCurrency(amt)
                        + "\n"
                        + BankingConfig.t("Balance After") + ": " + BankingConfig.formatCurrency(bal) + "\n"
                        + BankingConfig.t("Description") + ": " + (desc != null ? desc : type) + "\n"
                        + BankingConfig.t("Date") + ": " + time;
                notifs.add(new String[] { icon, credit ? "CREDIT" : "DEBIT", title, detail, time,
                        credit ? "SUCCESS" : "DANGER" });
            }
            rs.close();
            ps.close();
            if (dashboard.cachedBalance < 500) {
                notifs.add(0, new String[] { "\u26A0\uFE0F", "ALERT",
                        BankingConfig.t("Low Balance Alert"),
                        BankingConfig.t("Account") + ": " + dashboard.accountNumber + "\n"
                                + BankingConfig.t("Current Balance") + ": "
                                + BankingConfig.formatCurrency(dashboard.cachedBalance) + "\n"
                                + BankingConfig.t("Your account balance is below ETB 500.") + "\n"
                                + BankingConfig.t("Please deposit funds to avoid service interruption."),
                        BankingConfig.t("Now"), "WARNING" });
            }
            notifs.add(new String[] { "\uD83D\uDD12", "SECURITY",
                    BankingConfig.t("Security Reminder"),
                    BankingConfig.t("Dear") + " " + dashboard.accountHolder + ",\n"
                            + BankingConfig.t("CBE will NEVER ask for your PIN or password.") + "\n"
                            + BankingConfig.t("Do not share your credentials with anyone.") + "\n"
                            + BankingConfig.t("Report suspicious activity to CBE immediately."),
                    BankingConfig.t("CBE Security Team"), "INFO" });
            notifs.add(new String[] { "\u2705", "ACCOUNT",
                    BankingConfig.t("Account Active"),
                    BankingConfig.t("Dear") + " " + dashboard.accountHolder + ",\n"
                            + BankingConfig.t("Your CBE account is active and in good standing.") + "\n"
                            + BankingConfig.t("Account Number") + ": " + dashboard.accountNumber + "\n"
                            + BankingConfig.t("Available Balance") + ": "
                            + BankingConfig.formatCurrency(dashboard.cachedBalance),
                    BankingConfig.t("CBE Banking"), "SUCCESS" });
            notifs.add(new String[] { "\uD83C\uDF81", "PROMO",
                    BankingConfig.t("CBE Special Offer"),
                    BankingConfig.t("Dear valued customer,") + "\n"
                            + BankingConfig.t("Enjoy zero transfer fees on all CBE-to-CBE transfers.") + "\n"
                            + BankingConfig.t("Use CBE mobile banking for faster, easier transactions.") + "\n"
                            + BankingConfig.t("Thank you for banking with CBE."),
                    BankingConfig.t("CBE Promotions"), "PRIMARY" });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JDialog dialog = new JDialog(AppFrame.get(), BankingConfig.t("Notifications"), false);
        dialog.setSize(460, 560);
        dialog.setLocationRelativeTo(AppFrame.get());
        dialog.setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BankingConfig.BG);
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BankingConfig.PRIMARY_DARK, getWidth(), 0, BankingConfig.PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JPanel hText = new JPanel();
        hText.setOpaque(false);
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        JLabel hTitle = new JLabel("\uD83D\uDD14  " + BankingConfig.t("Notifications"));
        hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 16));
        hTitle.setForeground(Color.WHITE);
        JLabel hSub = new JLabel(notifs.size() + " " + BankingConfig.t("messages"));
        hSub.setFont(BankingConfig.uiFont(Font.PLAIN, 11));
        hSub.setForeground(new Color(200, 240, 215));
        hText.add(hTitle);
        hText.add(hSub);
        header.add(hText, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(BankingConfig.BG);
        list.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        for (String[] n : notifs) {
            Color accent;
            if ("SUCCESS".equals(n[5]))
                accent = BankingConfig.SUCCESS;
            else if ("DANGER".equals(n[5]))
                accent = BankingConfig.DANGER;
            else if ("WARNING".equals(n[5]))
                accent = BankingConfig.WARNING;
            else if ("INFO".equals(n[5]))
                accent = BankingConfig.INFO;
            else
                accent = BankingConfig.PRIMARY;
            JPanel card = new JPanel(new BorderLayout(10, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, 10));
                    g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 2, 14, 14);
                    g2.setColor(BankingConfig.CARD_BG);
                    g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, 14, 14);
                    g2.setColor(accent);
                    g2.fillRoundRect(0, 10, 5, getHeight() - 22, 4, 4);
                }
            };
            card.setOpaque(false);
            card.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 14));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JLabel iconLbl = new JLabel(n[0]);
            iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
            iconLbl.setPreferredSize(new Dimension(36, 36));
            iconLbl.setHorizontalAlignment(SwingConstants.CENTER);
            JPanel textBox = new JPanel();
            textBox.setOpaque(false);
            textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
            JLabel catLbl = new JLabel(n[1]);
            catLbl.setFont(BankingConfig.uiFont(Font.BOLD, 9));
            catLbl.setForeground(accent);
            JLabel titleLbl = new JLabel(n[2]);
            titleLbl.setFont(BankingConfig.uiFont(Font.BOLD, 13));
            titleLbl.setForeground(BankingConfig.FG);
            JLabel timeLbl = new JLabel(n[4]);
            timeLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 10));
            timeLbl.setForeground(BankingConfig.MUTED);
            textBox.add(catLbl);
            textBox.add(Box.createVerticalStrut(2));
            textBox.add(titleLbl);
            textBox.add(Box.createVerticalStrut(2));
            textBox.add(timeLbl);
            JLabel arrow = new JLabel("\u203A");
            arrow.setFont(new Font("SansSerif", Font.BOLD, 20));
            arrow.setForeground(BankingConfig.MUTED);
            card.add(iconLbl, BorderLayout.WEST);
            card.add(textBox, BorderLayout.CENTER);
            card.add(arrow, BorderLayout.EAST);
            final String[] fn = n;
            final Color fAccent = accent;
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showNotificationDetail(fn, fAccent, dialog);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(fAccent, 1, true),
                            BorderFactory.createEmptyBorder(11, 17, 11, 13)));
                    card.repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 14));
                    card.repaint();
                }
            });
            list.add(card);
            list.add(Box.createVerticalStrut(8));
        }
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        root.add(scroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BankingConfig.CARD_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BankingConfig.BORDER));
        JButton closeBtn = dashboard.makeTopBtn(BankingConfig.t("Close"), BankingConfig.PRIMARY_SOFT,
                BankingConfig.PRIMARY);
        closeBtn.addActionListener(e -> dialog.dispose());
        footer.add(closeBtn);
        root.add(footer, BorderLayout.SOUTH);
        dialog.setContentPane(root);
        dialog.setVisible(true);
    }
    void showNotificationDetail(String[] n, Color accent, JDialog parent) {
        JDialog detail = new JDialog(parent, n[2], true);
        detail.setSize(400, 340);
        detail.setLocationRelativeTo(parent);
        detail.setResizable(false);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BankingConfig.BG);
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, accent.darker(), getWidth(), 0, accent));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JPanel hRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        hRow.setOpaque(false);
        JLabel iconLbl = new JLabel(n[0]);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JPanel hText = new JPanel();
        hText.setOpaque(false);
        hText.setLayout(new BoxLayout(hText, BoxLayout.Y_AXIS));
        JLabel hCat = new JLabel(n[1]);
        hCat.setFont(BankingConfig.uiFont(Font.BOLD, 10));
        hCat.setForeground(new Color(220, 255, 230));
        JLabel hTitle = new JLabel(n[2]);
        hTitle.setFont(BankingConfig.uiFont(Font.BOLD, 14));
        hTitle.setForeground(Color.WHITE);
        hText.add(hCat);
        hText.add(hTitle);
        hRow.add(iconLbl);
        hRow.add(hText);
        header.add(hRow, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BankingConfig.BG);
        body.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        for (String line : n[3].split("\n")) {
            if (line.trim().isEmpty()) {
                body.add(Box.createVerticalStrut(6));
                continue;
            }
            boolean isKeyVal = line.contains(": ");
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (isKeyVal) {
                int sep = line.indexOf(": ");
                JLabel key = new JLabel(line.substring(0, sep) + ":");
                key.setFont(BankingConfig.uiFont(Font.BOLD, 12));
                key.setForeground(BankingConfig.MUTED);
                key.setPreferredSize(new Dimension(140, 22));
                JLabel val = new JLabel(line.substring(sep + 2));
                val.setFont(BankingConfig.uiFont(Font.PLAIN, 13));
                val.setForeground(BankingConfig.FG);
                row.add(key, BorderLayout.WEST);
                row.add(val, BorderLayout.CENTER);
            } else {
                JLabel lbl = new JLabel("<html>" + line + "</html>");
                lbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
                lbl.setForeground(BankingConfig.FG);
                row.add(lbl, BorderLayout.CENTER);
            }
            body.add(row);
            body.add(Box.createVerticalStrut(4));
        }
        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        root.add(scroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BankingConfig.CARD_BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BankingConfig.BORDER));
        JButton okBtn = dashboard.makeTopBtn(BankingConfig.t("OK"), BankingConfig.PRIMARY_SOFT, BankingConfig.PRIMARY);
        okBtn.addActionListener(e -> detail.dispose());
        footer.add(okBtn);
        root.add(footer, BorderLayout.SOUTH);
        detail.setContentPane(root);
        detail.setVisible(true);
    }
    JPanel buildTxPage(String title, String subtitle, Color accentColor) {
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
        header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        JPanel accentBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 4, 4, getHeight() - 8, 4, 4);
            }
        };
        accentBar.setOpaque(false);
        accentBar.setPreferredSize(new Dimension(10, 40));
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(BankingConfig.uiFont(Font.BOLD, 18));
        titleLbl.setForeground(BankingConfig.FG);
        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(BankingConfig.uiFont(Font.PLAIN, 12));
        subLbl.setForeground(BankingConfig.MUTED);
        titleBox.add(titleLbl);
        titleBox.add(subLbl);
        header.add(accentBar, BorderLayout.WEST);
        header.add(titleBox, BorderLayout.CENTER);
        JPanel formOuter = new JPanel(new GridBagLayout());
        formOuter.setBackground(BankingConfig.BG);
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, BankingConfig.darkMode ? 40 : 12));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 4, 16, 16);
                g2.setColor(BankingConfig.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, 16, 16);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth() - 3, 4, 4, 4);
                if (BankingConfig.darkMode) {
                    g2.setColor(BankingConfig.BORDER);
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 5, 16, 16);
                }
            }
        };
        formCard.setOpaque(false);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(30, 60, 30, 60);
        formOuter.add(formCard, gc);
        JScrollPane scroll = new JScrollPane(formOuter);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BankingConfig.BG);
        scroll.getViewport().setBackground(BankingConfig.BG);
        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.putClientProperty("form", formCard);
        return root;
    }
}