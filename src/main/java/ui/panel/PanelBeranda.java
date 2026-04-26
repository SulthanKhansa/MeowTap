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
        lblSakit.setText(String.valueOf(dao.countByStatus("Sakit")));
        lblMeninggal.setText(String.valueOf(dao.countByStatus("Meninggal")));
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
        lblScanInfo = new JLabel("Scanner siap - Tap kalung ke USB Reader", SwingConstants.CENTER);
        lblScanInfo.setIcon(FontIcon.of(Feather.MAXIMIZE, 18, Color.WHITE));
        lblScanInfo.setIconTextGap(15);
        pnlScanner.add(lblScanInfo, BorderLayout.CENTER);
        add(pnlScanner, new AbsoluteConstraints(30, 250, 680, 150));

        add(createBottomBox("Data Anabul (Tabel Preview)"), new AbsoluteConstraints(30, 420, 330, 250));
        add(createBottomBox("AI Clinic (Chat Preview)"), new AbsoluteConstraints(380, 420, 330, 250));
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
