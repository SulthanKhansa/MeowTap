package ui.panel;

import ui.style.ThemeManager;
import model.Admin;
import dao.KucingDAO;
import util.DigitalClockService;
import util.I18nService;
import util.I18nService.I18nChangeListener;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelBeranda extends JPanel implements I18nChangeListener {

    private Admin sessionAdmin;
    private KucingDAO dao;
    private JLabel lblClock, lblSMakan, lblBMakan, lblSakit, lblMeninggal;
    private JLabel lblSummary, lblScanner, lblPreview, lblAiPreview;
    private Thread clockThread;

    public PanelBeranda(Admin admin) {
        this.sessionAdmin = admin;
        this.dao = new KucingDAO();
        initLayout();
        applyTheme();
        I18nService.registerListener(this);
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        JPanel pnlAdmin = new JPanel(new BorderLayout());
        pnlAdmin.setBackground(ThemeManager.NAVY);
        JLabel lblAdmin = new JLabel(sessionAdmin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);
        add(pnlAdmin, new AbsoluteConstraints(680, 20, 240, 60));

        lblClock = new JLabel("", SwingConstants.CENTER);
        lblClock.setForeground(Color.WHITE);
        lblClock.setFont(ThemeManager.FONT_BOLD_16);
        add(lblClock, new AbsoluteConstraints(680, 0, 240, 20));

        JPanel pnlStats = new JPanel(new AbsoluteLayout());
        pnlStats.setBackground(ThemeManager.DARK_BLUE);
        lblSummary = new JLabel("Ringkasan Hari Ini");
        pnlStats.add(lblSummary, new AbsoluteConstraints(20, 10, -1, -1));

        lblSMakan = new JLabel("0");
        lblBMakan = new JLabel("0");
        lblSakit = new JLabel("0");
        lblMeninggal = new JLabel("0");

        pnlStats.add(createStatBox("Sudah Makan", lblSMakan, ThemeManager.STAT_GREEN), new AbsoluteConstraints(20, 40, 150, 80));
        pnlStats.add(createStatBox("Belum Makan", lblBMakan, ThemeManager.STAT_YELLOW), new AbsoluteConstraints(185, 40, 150, 80));
        pnlStats.add(createStatBox("Sakit", lblSakit, ThemeManager.STAT_PINK), new AbsoluteConstraints(350, 40, 150, 80));
        pnlStats.add(createStatBox("Meninggal", lblMeninggal, ThemeManager.STAT_RED), new AbsoluteConstraints(515, 40, 150, 80));
        add(pnlStats, new AbsoluteConstraints(30, 90, 680, 140));

        refreshStats();

        JPanel pnlScanner = new JPanel(new BorderLayout());
        pnlScanner.setBackground(ThemeManager.DARK_BLUE);
        lblScanner = new JLabel("Scanner siap - Tap kalung ke USB Reader", SwingConstants.CENTER);
        lblScanner.setForeground(Color.WHITE);
        lblScanner.setIcon(FontIcon.of(Feather.MAXIMIZE, 18, Color.WHITE));
        lblScanner.setIconTextGap(15);
        pnlScanner.add(lblScanner, BorderLayout.CENTER);
        add(pnlScanner, new AbsoluteConstraints(30, 250, 680, 150));

        lblPreview = new JLabel("Data Anabul (Tabel Preview)", SwingConstants.CENTER);
        lblPreview.setForeground(Color.WHITE);
        add(createBottomBox(lblPreview), new AbsoluteConstraints(30, 420, 330, 250));

        lblAiPreview = new JLabel("AI Clinic (Chat Preview)", SwingConstants.CENTER);
        lblAiPreview.setForeground(Color.WHITE);
        add(createBottomBox(lblAiPreview), new AbsoluteConstraints(380, 420, 330, 250));

        startClock();
    }

    private void startClock() {
        DigitalClockService clockService = new DigitalClockService(lblClock, "EEEE, d MMMM yyyy, HH:mm:ss");
        clockThread = clockService.getThread();
        clockThread.setName("Thread-Beranda-Clock");
        clockThread.setDaemon(true);
        clockThread.start();
    }

    public void refreshStats() {
        String sMakan = String.valueOf(dao.countByStatus("Sudah Makan"));
        String bMakan = String.valueOf(dao.countByStatus("Belum Makan"));
        String sakit = String.valueOf(dao.countByStatus("Sakit"));
        String tewas = String.valueOf(dao.countByStatus("Meninggal"));

        lblSMakan.setText(sMakan);
        lblBMakan.setText(bMakan);
        lblSakit.setText(sakit);
        lblMeninggal.setText(tewas);
    }

    private JPanel createStatBox(String title, JLabel valLabel, Color bg) {
        JPanel box = new JPanel(new GridLayout(2, 1));
        box.setBackground(bg);
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setForeground(Color.WHITE);
        valLabel.setForeground(Color.WHITE);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        box.add(t);
        box.add(valLabel);
        return box;
    }

    private JPanel createBottomBox(JLabel label) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ThemeManager.DARK_BLUE);
        p.add(label, BorderLayout.CENTER);
        return p;
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
    }

    @Override
    public void onLanguageChanged() {
        lblSummary.setText(I18nService.get("app.home.summary"));
        lblScanner.setText(I18nService.get("app.home.scanner"));
        lblPreview.setText(I18nService.get("app.home.preview"));
        lblAiPreview.setText(I18nService.get("app.home.ai"));
    }
}
