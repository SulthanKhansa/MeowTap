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

    private Admin sessionAdmin;
    private KucingDAO dao;

    public PanelBeranda(Admin admin) {
        this.sessionAdmin = admin;
        this.dao = new KucingDAO();
        initLayout();
        applyTheme();
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        // Header Admin - Dinamis dari DB
        JPanel pnlAdmin = new JPanel(new BorderLayout());
        pnlAdmin.setBackground(ThemeManager.NAVY);
        JLabel lblAdmin = new JLabel(sessionAdmin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);
        add(pnlAdmin, new AbsoluteConstraints(680, 20, 240, 60));

        // 1. Barisan Statistik - DATA ASLI DARI MONGODB
        JPanel pnlStats = new JPanel(new AbsoluteLayout());
        pnlStats.setBackground(ThemeManager.DARK_BLUE);
        pnlStats.add(new JLabel("Ringkasan Hari Ini"), new AbsoluteConstraints(20, 10, -1, -1));
        
        // Ambil hitungan asli dari DB
        String sMakan = String.valueOf(dao.countByStatus("Sudah Makan"));
        String bMakan = String.valueOf(dao.countByStatus("Belum Makan"));
        String sakit = String.valueOf(dao.countByStatus("Sakit"));
        String tewas = String.valueOf(dao.countByStatus("Meninggal")); // Asumsi status di DB

        pnlStats.add(createStatBox("Sudah Makan", sMakan, ThemeManager.STAT_GREEN), new AbsoluteConstraints(20, 40, 150, 80));
        pnlStats.add(createStatBox("Belum Makan", bMakan, ThemeManager.STAT_YELLOW), new AbsoluteConstraints(185, 40, 150, 80));
        pnlStats.add(createStatBox("Sakit", sakit, ThemeManager.STAT_PINK), new AbsoluteConstraints(350, 40, 150, 80));
        pnlStats.add(createStatBox("Meninggal", tewas, ThemeManager.STAT_RED), new AbsoluteConstraints(515, 40, 150, 80));
        add(pnlStats, new AbsoluteConstraints(30, 90, 680, 140));

        // 2. Area Scanner
        JPanel pnlScanner = new JPanel(new BorderLayout());
        pnlScanner.setBackground(ThemeManager.DARK_BLUE);
        JLabel lblScanInfo = new JLabel("Scanner siap - Tap kalung ke USB Reader", SwingConstants.CENTER);
        lblScanInfo.setForeground(Color.WHITE);
        lblScanInfo.setIcon(FontIcon.of(Feather.MAXIMIZE, 18, Color.WHITE));
        lblScanInfo.setIconTextGap(15);
        pnlScanner.add(lblScanInfo, BorderLayout.CENTER);
        add(pnlScanner, new AbsoluteConstraints(30, 250, 680, 150));

        // 3. Grid Bawah
        add(createBottomBox("Data Anabul (Tabel Preview)"), new AbsoluteConstraints(30, 420, 330, 250));
        add(createBottomBox("AI Clinic (Chat Preview)"), new AbsoluteConstraints(380, 420, 330, 250));
    }

    private JPanel createStatBox(String title, String val, Color bg) {
        JPanel box = new JPanel(new GridLayout(2, 1));
        box.setBackground(bg);
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setForeground(Color.WHITE);
        JLabel v = new JLabel(val, SwingConstants.CENTER);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("Segoe UI", Font.BOLD, 20));
        box.add(t);
        box.add(v);
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
    }
}
