package ui.panel;

import ui.style.ThemeManager;
import model.Admin;
import util.DigitalClockService;
import util.I18nService;
import util.I18nService.I18nChangeListener;
import util.SerialService;
import util.SerialDataHandler;
import util.SecurityUtils;
import dao.KucingDAO;
import model.Kucing;
import services.LogAktivitasService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelScanRfid extends JPanel implements I18nChangeListener {

    private Admin sessionAdmin;
    private JLabel lblTitle, lblStatus, lblSub, lblClock, lblTimestamp;
    private JLabel lblInfoValue;
    private JButton btnStart, btnSimulasi;
    private Thread clockThread;
    private SerialService serialService;
    private KucingDAO kucingDAO;
    private LogAktivitasService logService;
    private boolean scanning = false;

    public PanelScanRfid(Admin admin) {
        this.sessionAdmin = admin;
        this.kucingDAO = new KucingDAO();
        this.logService = new LogAktivitasService();
        this.serialService = SerialService.getInstance();
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

        lblTitle = new JLabel(I18nService.get("app.scan.title"), SwingConstants.CENTER);
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        add(lblTitle, new AbsoluteConstraints(0, 50, 1000, -1));

        JPanel pnlScanner = new JPanel(new GridBagLayout());
        pnlScanner.setBackground(new Color(18, 27, 59));

        FontIcon iconScan = FontIcon.of(Feather.MAXIMIZE, 120, Color.WHITE);
        JLabel lblIcon = new JLabel(iconScan, SwingConstants.CENTER);

        lblStatus = new JLabel(I18nService.get("app.scan.ready"), SwingConstants.CENTER);
        lblStatus.setFont(ThemeManager.FONT_BOLD_16);
        lblStatus.setForeground(Color.WHITE);

        lblSub = new JLabel(I18nService.get("app.scan.subtitle"), SwingConstants.CENTER);
        lblSub.setFont(ThemeManager.FONT_PLAIN_12);
        lblSub.setForeground(Color.LIGHT_GRAY);

        lblTimestamp = new JLabel("", SwingConstants.CENTER);
        lblTimestamp.setFont(ThemeManager.FONT_PLAIN_12);
        lblTimestamp.setForeground(Color.LIGHT_GRAY);

        lblInfoValue = new JLabel("", SwingConstants.CENTER);
        lblInfoValue.setFont(ThemeManager.FONT_BOLD_16);
        lblInfoValue.setForeground(ThemeManager.STAT_GREEN);

        JPanel infoContainer = new JPanel();
        infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
        infoContainer.setOpaque(false);

        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTimestamp.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInfoValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoContainer.add(lblIcon);
        infoContainer.add(Box.createVerticalStrut(20));
        infoContainer.add(lblStatus);
        infoContainer.add(Box.createVerticalStrut(10));
        infoContainer.add(lblSub);
        infoContainer.add(Box.createVerticalStrut(5));
        infoContainer.add(lblTimestamp);
        infoContainer.add(Box.createVerticalStrut(10));
        infoContainer.add(lblInfoValue);

        pnlScanner.add(infoContainer);
        add(pnlScanner, new AbsoluteConstraints(200, 130, 600, 320));

        btnStart = new JButton(I18nService.get("app.scan.btnstart"));
        btnStart.addActionListener(e -> toggleScan());
        add(btnStart, new AbsoluteConstraints(250, 470, 220, 45));

        btnSimulasi = new JButton(I18nService.get("app.scan.btnsim"));
        btnSimulasi.addActionListener(e -> simulasiScan());
        add(btnSimulasi, new AbsoluteConstraints(530, 470, 220, 45));

        startClock();
    }

    private void startClock() {
        DigitalClockService clockService = new DigitalClockService(lblClock, "EEEE, d MMMM yyyy, HH:mm:ss");
        clockThread = clockService.getThread();
        clockThread.setName("Thread-Scan-Clock");
        clockThread.setDaemon(true);
        clockThread.start();
    }

    private void toggleScan() {
        if (scanning) {
            stopScan();
        } else {
            startScan();
        }
    }

    private void startScan() {
        scanning = true;
        btnStart.setText("STOP");
        lblStatus.setText(I18nService.get("app.scan.scanning"));
        lblInfoValue.setText("");

        serialService.addHandler(dataRfid -> {
            SwingUtilities.invokeLater(() -> prosesScanRFID(dataRfid));
        });

        if (!serialService.isConnected()) {
            serialService.connect("COM3", 9600);
        }
    }

    private void stopScan() {
        scanning = false;
        btnStart.setText(I18nService.get("app.scan.btnstart"));
        lblStatus.setText(I18nService.get("app.scan.ready"));
        lblInfoValue.setText("");
        serialService.disconnect();
    }

    private void prosesScanRFID(String uidRfid) {
        lblTimestamp.setText(java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        lblStatus.setText(I18nService.get("app.scan.success"));

        Kucing k = kucingDAO.findById(uidRfid);
        if (k != null) {
            lblInfoValue.setText(k.getNama() + " - " + k.getRas());
            lblInfoValue.setForeground(ThemeManager.STAT_GREEN);
            logService.simpanLog(uidRfid, "Scan RFID");
            JOptionPane.showMessageDialog(this,
                I18nService.get("app.scan.dialog.success"),
                I18nService.get("app.scan.dialog.success.title"),
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblInfoValue.setText(I18nService.get("app.scan.notfound"));
            lblInfoValue.setForeground(ThemeManager.STAT_RED);
        }
    }

    private void simulasiScan() {
        String dummyId = JOptionPane.showInputDialog(this,
            I18nService.get("app.scan.dialog.input"),
            "Simulasi", JOptionPane.QUESTION_MESSAGE);
        if (dummyId != null && !dummyId.trim().isEmpty()) {
            serialService.simulateBroadcast(dummyId.trim());
        }
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        styleBtn(btnStart);
        styleBtn(btnSimulasi);
    }

    private void styleBtn(JButton b) {
        b.setBackground(ThemeManager.NAVY);
        b.setForeground(Color.WHITE);
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void onLanguageChanged() {
        lblTitle.setText(I18nService.get("app.scan.title"));
        lblStatus.setText(scanning ? I18nService.get("app.scan.scanning") : I18nService.get("app.scan.ready"));
        lblSub.setText(I18nService.get("app.scan.subtitle"));
        btnStart.setText(scanning ? "STOP" : I18nService.get("app.scan.btnstart"));
        btnSimulasi.setText(I18nService.get("app.scan.btnsim"));
    }
}
