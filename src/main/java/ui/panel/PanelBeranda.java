package ui.panel;

import ui.style.ThemeManager;
import model.Admin;
import dao.KucingDAO;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelBeranda extends JPanel {

    private final Admin sessionAdmin;
    private final KucingDAO dao;
    private JLabel lblSudahMakan, lblBelumMakan, lblSakit, lblMeninggal;
    private JPanel pnlAdmin, pnlStats, pnlScanner;
    private JLabel lblAdmin, lblSummary, lblScanInfo;
    private JTable tablePreview;
    private javax.swing.table.DefaultTableModel tableModel;

    public PanelBeranda(Admin admin) {
        this.sessionAdmin = admin;
        this.dao = new KucingDAO();
        initLayout();
        applyTheme();
        refreshStats();
    }

    // Login & Actions

    public void refreshStats() {
        lblSudahMakan.setText(String.valueOf(dao.countByStatus("Sudah Makan")));
        lblBelumMakan.setText(String.valueOf(dao.countByStatus("Belum Makan")));
        lblSakit.setText(String.valueOf(dao.countByKondisi("Sakit")));
        lblMeninggal.setText(String.valueOf(dao.countByKondisi("Meninggal")));
        
        loadTablePreview();
    }

    // UI & Theme

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        pnlAdmin = new JPanel(new BorderLayout());
        lblAdmin = new JLabel(sessionAdmin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);
        add(pnlAdmin, new AbsoluteConstraints(680, 20, 240, 60));

        pnlStats = new JPanel(new AbsoluteLayout());
        lblSummary = new JLabel("Ringkasan Hari Ini");
        pnlStats.add(lblSummary, new AbsoluteConstraints(20, 10, -1, -1));
        
        lblSudahMakan = new JLabel("0", SwingConstants.CENTER);
        lblBelumMakan = new JLabel("0", SwingConstants.CENTER);
        lblSakit = new JLabel("0", SwingConstants.CENTER);
        lblMeninggal = new JLabel("0", SwingConstants.CENTER);

        pnlStats.add(createStatBox("Sudah Makan", lblSudahMakan, ThemeManager.STAT_GREEN), new AbsoluteConstraints(20, 40, 150, 80));
        pnlStats.add(createStatBox("Belum Makan", lblBelumMakan, ThemeManager.STAT_YELLOW), new AbsoluteConstraints(185, 40, 150, 80));
        pnlStats.add(createStatBox("Sakit", lblSakit, ThemeManager.STAT_PINK), new AbsoluteConstraints(350, 40, 150, 80));
        pnlStats.add(createStatBox("Meninggal", lblMeninggal, ThemeManager.STAT_RED), new AbsoluteConstraints(515, 40, 150, 80));
        add(pnlStats, new AbsoluteConstraints(30, 90, 680, 140));

        pnlScanner = new JPanel(new BorderLayout());
        pnlScanner.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblScanInfo = new JLabel("Scanner siap - Tap kalung ke USB Reader", SwingConstants.CENTER);
        lblScanInfo.setIcon(FontIcon.of(Feather.MAXIMIZE, 18, Color.WHITE));
        lblScanInfo.setIconTextGap(15);
        pnlScanner.add(lblScanInfo, BorderLayout.CENTER);
        
        pnlScanner.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Window topFrame = SwingUtilities.getWindowAncestor(PanelBeranda.this);
                if (topFrame instanceof ui.MainDashboard) {
                    ((ui.MainDashboard) topFrame).switchPanel("cardScan");
                }
            }
        });
        
        add(pnlScanner, new AbsoluteConstraints(30, 250, 680, 150));

        add(createTablePreviewBox(), new AbsoluteConstraints(30, 420, 330, 250));
        add(createBottomBox("AI Clinic (Segera Hadir)"), new AbsoluteConstraints(380, 420, 330, 250));
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
        JLabel l = new JLabel(title, SwingConstants.CENTER);
        l.setForeground(Color.WHITE);
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    private JPanel createTablePreviewBox() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ThemeManager.DARK_BLUE);
        
        JLabel lblTop = new JLabel("  Preview Data Anabul");
        lblTop.setForeground(Color.WHITE);
        lblTop.setFont(ThemeManager.FONT_BOLD_14);
        lblTop.setPreferredSize(new Dimension(0, 30));
        p.add(lblTop, BorderLayout.NORTH);

        String[] cols = {"ID", "Nama", "Kandang", "Status"};
        tableModel = new javax.swing.table.DefaultTableModel(cols, 0);
        tablePreview = new JTable(tableModel);
        tablePreview.setRowHeight(25);
        
        JScrollPane scroll = new JScrollPane(tablePreview);
        p.add(scroll, BorderLayout.CENTER);
        
        return p;
    }

    private void loadTablePreview() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        java.util.List<model.Kucing> list = dao.findAll();
        
        // Tampilkan maksimal 10 data saja untuk preview
        int limit = Math.min(list.size(), 10);
        for (int i = 0; i < limit; i++) {
            model.Kucing k = list.get(i);
            tableModel.addRow(new Object[]{
                k.getIdRfid(),
                k.getNama(),
                k.getKandang(),
                k.getStatusMakan()
            });
        }
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
