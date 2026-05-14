package ui.panel;

import ui.style.ThemeManager;
import ui.MainDashboard;
import model.Admin;
import dao.KucingDAO;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public final class PanelBeranda extends JPanel {

    private final Admin sessionAdmin;
    private final KucingDAO dao;
    private final MainDashboard parent;
    private JLabel lblSudahMakan, lblBelumMakan, lblSakit, lblMeninggal, lblSehat;
    private JPanel pnlAdmin, pnlStats, pnlScanner;
    private JLabel lblAdmin, lblSummary, lblScanInfo, lblPreviewTitle, lblAiClinicTitle;
    private JTable tablePreview;
    private javax.swing.table.DefaultTableModel tableModel;
    private JToggleButton btnLang;
    private boolean isEnglish = false;

    public PanelBeranda(MainDashboard parent, Admin admin) {
        this.parent = parent;
        this.sessionAdmin = admin;
        this.dao = new KucingDAO();
        initLayout();
        applyTheme();
        refreshStats();
    }

    // --- Logic & Data ---

    public void refreshStats() {
        lblSudahMakan.setText(String.valueOf(dao.countByStatus("Sudah Makan")));
        lblBelumMakan.setText(String.valueOf(dao.countByStatus("Belum Makan")));
        lblSakit.setText(String.valueOf(dao.countByKondisi("Sakit")));
        lblMeninggal.setText(String.valueOf(dao.countByKondisi("Meninggal")));
        lblSehat.setText(String.valueOf(dao.countByKondisi("Sehat")));
        
        loadTablePreview();
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
        if (!isEnglish) return val;
        String v = val.trim().toLowerCase();
        switch (v) {
            case "sudah makan": return "fed";
            case "belum makan": return "not fed";
            case "sehat": return "healthy";
            case "sakit": return "sick";
            case "meninggal": return "dead";
            default: return val;
        }
    }

    // --- UI & Theme ---

    public void setLanguage(boolean eng) {
        this.isEnglish = eng;
        if (isEnglish) {
            lblSummary.setText("Today's Summary");
            lblScanInfo.setText("Scanner Ready - Tap necklace to identify anabul");
            lblPreviewTitle.setText("  Anabul Data Preview");
            lblAiClinicTitle.setText("AI Clinic (Coming Soon)");
            btnLang.setIcon(FontIcon.of(Feather.TOGGLE_RIGHT, 24, Color.WHITE));
            btnLang.setText("EN ");
            btnLang.setSelected(true);
            
            updateStatBoxTitles("Fed", "Not Fed", "Healthy", "Sick", "Deceased");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Name", "Cage", "Status"});
        } else {
            lblSummary.setText("Ringkasan Hari Ini");
            lblScanInfo.setText("Scanner siap - Tap kalung ke USB Reader");
            lblPreviewTitle.setText("  Preview Data Anabul");
            lblAiClinicTitle.setText("AI Clinic (Segera Hadir)");
            btnLang.setIcon(FontIcon.of(Feather.TOGGLE_LEFT, 24, new Color(200, 200, 200)));
            btnLang.setText("ID ");
            btnLang.setSelected(false);
            
            updateStatBoxTitles("Sudah Makan", "Belum Makan", "Sehat", "Sakit", "Meninggal");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nama", "Kandang", "Status"});
        }
        
        // Refresh table alignment
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
        add(pnlAdmin, new AbsoluteConstraints(offsetX + 380, 20, 240, 60));

        btnLang = new JToggleButton("ID ", FontIcon.of(Feather.TOGGLE_LEFT, 24, new Color(200, 200, 200)));
        btnLang.setSelected(false);
        btnLang.setBackground(ThemeManager.NAVY);
        btnLang.setForeground(Color.WHITE);
        btnLang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLang.setBorderPainted(false);
        btnLang.setFocusPainted(false);
        btnLang.setHorizontalTextPosition(SwingConstants.LEFT);
        btnLang.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLang.addActionListener(e -> {
            parent.setLanguage(btnLang.isSelected());
        });
        add(btnLang, new AbsoluteConstraints(offsetX + 630, 20, 90, 60));

        pnlStats = new JPanel(new AbsoluteLayout());
        lblSummary = new JLabel("Ringkasan Hari Ini");
        pnlStats.add(lblSummary, new AbsoluteConstraints(20, 10, -1, -1));
        
        lblSudahMakan = new JLabel("0", SwingConstants.CENTER);
        lblBelumMakan = new JLabel("0", SwingConstants.CENTER);
        lblSakit = new JLabel("0", SwingConstants.CENTER);
        lblMeninggal = new JLabel("0", SwingConstants.CENTER);
        lblSehat = new JLabel("0", SwingConstants.CENTER);

        int boxW = 125, boxH = 80, gap = 15;
        pnlStats.add(createStatBox("Sudah Makan", lblSudahMakan, new Color(46, 139, 87)), new AbsoluteConstraints(20, 40, boxW, boxH));
        pnlStats.add(createStatBox("Belum Makan", lblBelumMakan, new Color(218, 165, 32)), new AbsoluteConstraints(20 + (boxW + gap), 40, boxW, boxH));
        pnlStats.add(createStatBox("Sehat", lblSehat, new Color(30, 144, 255)), new AbsoluteConstraints(20 + (boxW + gap) * 2, 40, boxW, boxH));
        pnlStats.add(createStatBox("Sakit", lblSakit, new Color(199, 21, 133)), new AbsoluteConstraints(20 + (boxW + gap) * 3, 40, boxW, boxH));
        pnlStats.add(createStatBox("Meninggal", lblMeninggal, new Color(220, 20, 60)), new AbsoluteConstraints(20 + (boxW + gap) * 4, 40, boxW, boxH));
        add(pnlStats, new AbsoluteConstraints(offsetX, 90, 720, 140));

        pnlScanner = new JPanel(new BorderLayout());
        pnlScanner.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblScanInfo = new JLabel("Scanner siap - Tap kalung ke USB Reader", SwingConstants.CENTER);
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
        add(createBottomBox("AI Clinic (Segera Hadir)"), new AbsoluteConstraints(offsetX + 370, 420, 350, 250));
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
        lblPreviewTitle = new JLabel("  Preview Data Anabul");
        lblPreviewTitle.setForeground(Color.WHITE);
        lblPreviewTitle.setFont(ThemeManager.FONT_BOLD_14);
        lblPreviewTitle.setPreferredSize(new Dimension(0, 30));
        p.add(lblPreviewTitle, BorderLayout.NORTH);

        String[] cols = {"ID", "Nama", "Kandang", "Status"};
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
