package ui.panel;

import ui.style.ThemeManager;
import ui.MainDashboard;
import model.Admin;
import dao.KucingDAO;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;
import java.util.Locale;

public final class PanelBeranda extends JPanel implements I18nService.I18nChangeListener {

    private final Admin sessionAdmin;
    private final KucingDAO dao;
    private final MainDashboard parent;
    private JLabel lblSudahMakan, lblBelumMakan, lblSakit, lblMeninggal, lblSehat;
    private JPanel pnlAdmin, pnlStats, pnlScanner;
    private JLabel lblAdmin, lblSummary, lblScanInfo, lblPreviewTitle, lblAiClinicTitle;
    private JTable tablePreview;
    private javax.swing.table.DefaultTableModel tableModel;
    private JButton btnID, btnFR, btnES;

    public PanelBeranda(MainDashboard parent, Admin admin) {
        this.parent = parent;
        this.sessionAdmin = admin;
        this.dao = new KucingDAO();
        I18nService.registerListener(this);
        initLayout();
        applyTheme();
        refreshStats();
        onLanguageChanged();
    }

    public void refreshStats() {
        lblSudahMakan.setText(String.valueOf(dao.countByStatus("Sudah Makan")));
        lblBelumMakan.setText(String.valueOf(dao.countByStatus("Belum Makan")));
        lblSakit.setText(String.valueOf(dao.countByKondisi("Sakit")));
        lblMeninggal.setText(String.valueOf(dao.countByKondisi("Meninggal")));
        lblSehat.setText(String.valueOf(dao.countByKondisi("Sehat")));
        
        loadTablePreview();
        tablePreview.repaint();
        tableModel.fireTableDataChanged();
    }

    private void loadTablePreview() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        java.util.List<model.Kucing> list = dao.findAll();
        int limit = Math.min(list.size(), 10);
        for (int i = 0; i < limit; i++) {
            model.Kucing k = list.get(i);
            tableModel.addRow(new Object[]{ 
                k.getIdRfid(), 
                k.getNama(), 
                k.getKandang(), 
                trans(k.getStatusMakan()) 
            });
        }
    }

    private void updateStatBoxTitles(String t1, String t2, String t3, String t4, String t5) {
        ((JLabel)((JPanel)pnlStats.getComponent(1)).getComponent(0)).setText(t1);
        ((JLabel)((JPanel)pnlStats.getComponent(2)).getComponent(0)).setText(t2);
        ((JLabel)((JPanel)pnlStats.getComponent(3)).getComponent(0)).setText(t3);
        ((JLabel)((JPanel)pnlStats.getComponent(4)).getComponent(0)).setText(t4);
        ((JLabel)((JPanel)pnlStats.getComponent(5)).getComponent(0)).setText(t5);
    }

    private String trans(String val) {
        if (val == null) return "-";
        String lang = I18nService.getCurrentLocale().getLanguage();
        String v = val.trim().toLowerCase();
        if (lang.equals("fr")) {
            return switch (v) {
                case "sudah makan" -> "Nourri";
                case "belum makan" -> "Non nourri";
                case "sehat" -> "En bonne santé";
                case "sakit" -> "Malade";
                case "meninggal" -> "Décédé";
                default -> val;
            };
        } else if (lang.equals("es")) {
            return switch (v) {
                case "sudah makan" -> "Alimentado";
                case "belum makan" -> "No alimentado";
                case "sehat" -> "Sano";
                case "sakit" -> "Enfermo";
                case "meninggal" -> "Fallecido";
                default -> val;
            };
        }
        return val;
    }

    @Override
    public void onLanguageChanged() {
        String lang = I18nService.getCurrentLocale().getLanguage();
        
        lblSummary.setText(I18nService.get("home.summary"));
        lblScanInfo.setText(I18nService.get("home.scanner.ready"));
        lblPreviewTitle.setText("  " + I18nService.get("home.preview.title"));
        lblAiClinicTitle.setText(I18nService.get("home.ai.title"));
        
        btnID.setBackground(lang.equals("id") ? ThemeManager.LAVENDER : ThemeManager.NAVY);
        btnFR.setBackground(lang.equals("fr") ? ThemeManager.LAVENDER : ThemeManager.NAVY);
        btnES.setBackground(lang.equals("es") ? ThemeManager.LAVENDER : ThemeManager.NAVY);
        
        updateStatBoxTitles(
            I18nService.get("home.stat.fed"),
            I18nService.get("home.stat.notfed"),
            I18nService.get("home.stat.healthy"),
            I18nService.get("home.stat.sick"),
            I18nService.get("home.stat.deceased")
        );
        tableModel.setColumnIdentifiers(new Object[]{
            I18nService.get("home.table.id"),
            I18nService.get("home.table.name"),
            I18nService.get("home.table.cage"),
            I18nService.get("home.table.status")
        });
        
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        if (tablePreview.getColumnModel().getColumnCount() > 0) {
            tablePreview.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            tablePreview.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        }
        
        loadTablePreview();
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());
        int offsetX = 135;

        pnlAdmin = new JPanel(new BorderLayout());
        lblAdmin = new JLabel(sessionAdmin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);

        pnlAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnlAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Window topFrame = SwingUtilities.getWindowAncestor(PanelBeranda.this);
                if (topFrame instanceof ui.MainDashboard mainDashboard) {
                    mainDashboard.switchPanel("cardAkun");
                }
            }
        });
        add(pnlAdmin, new AbsoluteConstraints(offsetX + 380, 20, 240, 60));

        int langX = offsetX + 650;
        btnID = createLangButton("ID", langX, () -> I18nService.setLocale(new Locale("id", "ID")));
        btnFR = createLangButton("FR", langX + 55, () -> I18nService.setLocale(new Locale("fr", "FR")));
        btnES = createLangButton("ES", langX + 110, () -> I18nService.setLocale(new Locale("es", "ES")));

        pnlStats = new JPanel(new AbsoluteLayout());
        lblSummary = new JLabel(I18nService.get("home.summary"));
        pnlStats.add(lblSummary, new AbsoluteConstraints(20, 10, -1, -1));
        
        lblSudahMakan = new JLabel("0", SwingConstants.CENTER);
        lblBelumMakan = new JLabel("0", SwingConstants.CENTER);
        lblSakit = new JLabel("0", SwingConstants.CENTER);
        lblMeninggal = new JLabel("0", SwingConstants.CENTER);
        lblSehat = new JLabel("0", SwingConstants.CENTER);

        int boxW = 125, boxH = 80, gap = 15;
        pnlStats.add(createStatBox(I18nService.get("home.stat.fed"), lblSudahMakan, new Color(46, 139, 87)), new AbsoluteConstraints(20, 40, boxW, boxH));
        pnlStats.add(createStatBox(I18nService.get("home.stat.notfed"), lblBelumMakan, new Color(218, 165, 32)), new AbsoluteConstraints(20 + (boxW + gap), 40, boxW, boxH));
        pnlStats.add(createStatBox(I18nService.get("home.stat.healthy"), lblSehat, new Color(30, 144, 255)), new AbsoluteConstraints(20 + (boxW + gap) * 2, 40, boxW, boxH));
        pnlStats.add(createStatBox(I18nService.get("home.stat.sick"), lblSakit, new Color(199, 21, 133)), new AbsoluteConstraints(20 + (boxW + gap) * 3, 40, boxW, boxH));
        pnlStats.add(createStatBox(I18nService.get("home.stat.deceased"), lblMeninggal, new Color(220, 20, 60)), new AbsoluteConstraints(20 + (boxW + gap) * 4, 40, boxW, boxH));
        add(pnlStats, new AbsoluteConstraints(offsetX, 90, 720, 140));

        pnlScanner = new JPanel(new BorderLayout());
        pnlScanner.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblScanInfo = new JLabel(I18nService.get("home.scanner.ready"), SwingConstants.CENTER);
        lblScanInfo.setIcon(FontIcon.of(Feather.MAXIMIZE, 18, Color.WHITE));
        lblScanInfo.setIconTextGap(15);
        pnlScanner.add(lblScanInfo, BorderLayout.CENTER);
        pnlScanner.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Window topFrame = SwingUtilities.getWindowAncestor(PanelBeranda.this);
                if (topFrame instanceof ui.MainDashboard) { ((ui.MainDashboard) topFrame).switchPanel("cardScan"); }
            }
        });
        add(pnlScanner, new AbsoluteConstraints(offsetX, 250, 720, 150));

        add(createTablePreviewBox(), new AbsoluteConstraints(offsetX, 420, 350, 250));
        add(createBottomBox(I18nService.get("home.ai.title")), new AbsoluteConstraints(offsetX + 370, 420, 350, 250));
    }

    private JButton createLangButton(String text, int x, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBackground(ThemeManager.NAVY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        add(btn, new AbsoluteConstraints(x, 30, 50, 40));
        return btn;
    }

    private JPanel createStatBox(String title, JLabel lblValue, Color bg) {
        JPanel box = new JPanel(new GridLayout(2, 1));
        box.setBackground(bg);
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setForeground(Color.WHITE);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        box.add(t);
        box.add(lblValue);
        return box;
    }

    private JPanel createBottomBox(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ThemeManager.DARK_BLUE);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblAiClinicTitle = new JLabel(title, SwingConstants.CENTER);
        lblAiClinicTitle.setForeground(Color.WHITE);
        p.add(lblAiClinicTitle, BorderLayout.CENTER);
        p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Window topFrame = SwingUtilities.getWindowAncestor(PanelBeranda.this);
                if (topFrame instanceof ui.MainDashboard) { ((ui.MainDashboard) topFrame).switchPanel("cardAi"); }
            }
        });
        return p;
    }

    private JPanel createTablePreviewBox() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ThemeManager.DARK_BLUE);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblPreviewTitle = new JLabel("  " + I18nService.get("home.preview.title"));
        lblPreviewTitle.setForeground(Color.WHITE);
        lblPreviewTitle.setFont(ThemeManager.FONT_BOLD_14);
        lblPreviewTitle.setPreferredSize(new Dimension(0, 30));
        p.add(lblPreviewTitle, BorderLayout.NORTH);

        String[] cols = {
            I18nService.get("home.table.id"),
            I18nService.get("home.table.name"),
            I18nService.get("home.table.cage"),
            I18nService.get("home.table.status")
        };
        tableModel = new javax.swing.table.DefaultTableModel(cols, 0);
        tablePreview = new JTable(tableModel);
        tablePreview.setRowHeight(25);
        tablePreview.setCursor(new Cursor(Cursor.HAND_CURSOR));
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablePreview.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablePreview.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        JScrollPane scroll = new JScrollPane(tablePreview);
        p.add(scroll, BorderLayout.CENTER);
        java.awt.event.MouseAdapter nav;
        nav = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Window topFrame = SwingUtilities.getWindowAncestor(PanelBeranda.this);
                if (topFrame instanceof ui.MainDashboard mainDashboard) { mainDashboard.switchPanel("cardData"); }
            }
        };
        p.addMouseListener(nav);
        tablePreview.addMouseListener(nav);
        return p;
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        pnlAdmin.setBackground(ThemeManager.NAVY);
        pnlStats.setBackground(ThemeManager.DARK_BLUE);
        pnlScanner.setBackground(ThemeManager.DARK_BLUE);
        lblAdmin.setForeground(Color.WHITE);
        lblSummary.setForeground(Color.WHITE);
        lblSummary.setFont(ThemeManager.FONT_BOLD_14);
        lblScanInfo.setForeground(Color.WHITE);
    }
}
