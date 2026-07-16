package ui.panel;

import ui.style.ThemeManager;
import ui.dialog.DialogAnabul;
import model.Admin;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelScanRfid extends JPanel implements I18nService.I18nChangeListener {

    private JPanel pnlScanner;
    private JLabel lblTitle, lblStatus, lblSub;
    private JButton btnStart, btnSimulasi;
    private boolean isScanning = false;

    public PanelScanRfid(Admin admin) {
        I18nService.registerListener(this);
        initLayout();
        applyTheme();
        onLanguageChanged();
    }

    @Override
    public void onLanguageChanged() {
        lblTitle.setText(I18nService.get("scan.title"));
        if (!isScanning) lblStatus.setText(I18nService.get("scan.status.ready"));
        lblSub.setText(I18nService.get("scan.status.hint"));
        btnStart.setText(I18nService.get("scan.btn.start"));
        btnSimulasi.setText(I18nService.get("scan.btn.simulate"));
    }

    public void startScanning() {
        if (isScanning) return;
        isScanning = true;
        btnStart.setEnabled(false);

        lblStatus.setText(I18nService.get("scan.status.searching"));
        lblStatus.setForeground(ThemeManager.STAT_YELLOW);
        
        Thread scannerThread = new Thread(() -> {
            util.HardwareScanner scanner = new util.HardwareScanner();
            scanner.mulaiScanning(new util.HardwareScanner.ScanCallback() {
                
                @Override
                public void onScanSuccess(model.Kucing kucing) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setText(I18nService.get("scan.status.detected") + kucing.getNama());
                        lblStatus.setForeground(ThemeManager.STAT_GREEN);
                        lblSub.setText(I18nService.get("home.table.status") + ": " + trans(kucing.getStatusMakan()) + " | " + I18nService.get("home.table.cage") + ": " + kucing.getKandang());
                        
                        JOptionPane.showMessageDialog(PanelScanRfid.this, I18nService.get("scan.msg.success"), I18nService.get("scan.msg.success.title"), JOptionPane.INFORMATION_MESSAGE);
                        
                        openDetailDialog(kucing);
                        
                        isScanning = false;
                        if (btnStart != null) btnStart.setEnabled(true);
                    });
                }

                @Override
                public void onScanNotFound(String rawId) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setText(I18nService.get("scan.status.unknown"));
                        lblStatus.setForeground(ThemeManager.STAT_RED);
                        lblSub.setText(I18nService.get("scan.status.id") + rawId + I18nService.get("scan.status.notregistered"));
                        
                        JOptionPane.showMessageDialog(PanelScanRfid.this, I18nService.get("scan.msg.failed"), I18nService.get("scan.msg.failed.title"), JOptionPane.ERROR_MESSAGE);
                        
                        isScanning = false;
                        if (btnStart != null) btnStart.setEnabled(true);
                    });
                }

                @Override
                public void onScanError(String message) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setText(I18nService.get("scan.status.error"));
                        lblStatus.setForeground(ThemeManager.STAT_RED);
                        lblSub.setText(message);
                        isScanning = false;
                        if (btnStart != null) btnStart.setEnabled(true);
                    });
                }
            });
        });
        
        scannerThread.setDaemon(true); 
        scannerThread.start();
    }

    private void simulasiScan() {
        String fakeId = JOptionPane.showInputDialog(this, I18nService.get("scan.msg.simulate"));
        if (fakeId != null && !fakeId.trim().isEmpty()) {
            dao.KucingDAO dao = new dao.KucingDAO();
            model.Kucing k = dao.findById(fakeId.trim());
            if (k != null) {
                lblStatus.setText(I18nService.get("scan.status.detected") + k.getNama());
                lblStatus.setForeground(ThemeManager.STAT_GREEN);
                lblSub.setText(I18nService.get("home.table.status") + ": " + trans(k.getStatusMakan()) + " | " + I18nService.get("home.table.cage") + ": " + k.getKandang());
                
                JOptionPane.showMessageDialog(this, I18nService.get("scan.msg.success"), I18nService.get("scan.msg.success.title"), JOptionPane.INFORMATION_MESSAGE);
                
                openDetailDialog(k);
            } else {
                lblStatus.setText(I18nService.get("scan.status.unknown"));
                lblStatus.setForeground(ThemeManager.STAT_RED);
                lblSub.setText(I18nService.get("scan.status.id") + fakeId + I18nService.get("scan.status.notregistered"));
                
                JOptionPane.showMessageDialog(this, I18nService.get("scan.msg.failed"), I18nService.get("scan.msg.failed.title"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openDetailDialog(model.Kucing kucing) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogAnabul dialog = new DialogAnabul(parent, DialogAnabul.Mode.EDIT);
        dialog.prepareData(kucing);
        dialog.setVisible(true);
    }

    private String trans(String val) {
        if (val == null) return "-";
        String lang = I18nService.getCurrentLocale().getLanguage();
        String v = val.trim().toLowerCase();
        if (lang.equals("en")) {
            return switch (v) {
                case "sudah makan" -> "Fed";
                case "belum makan" -> "Not Fed";
                default -> val;
            };
        } else if (lang.equals("es")) {
            return switch (v) {
                case "sudah makan" -> "Alimentado";
                case "belum makan" -> "No alimentado";
                default -> val;
            };
        }
        return val;
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel(I18nService.get("scan.title"), SwingConstants.CENTER);
        add(lblTitle, new AbsoluteConstraints(0, 50, 1000, -1));

        pnlScanner = new JPanel(new GridBagLayout());
        pnlScanner.setBackground(new Color(18, 27, 59));
        
        FontIcon iconScan = FontIcon.of(Feather.MAXIMIZE, 120, Color.WHITE);
        JLabel lblIcon = new JLabel(iconScan, SwingConstants.CENTER);
        
        lblStatus = new JLabel(I18nService.get("scan.status.ready"), SwingConstants.CENTER);
        lblSub = new JLabel(I18nService.get("scan.status.hint"), SwingConstants.CENTER);

        JPanel infoContainer = new JPanel();
        infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
        infoContainer.setOpaque(false);
        
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoContainer.add(lblIcon);
        infoContainer.add(Box.createVerticalStrut(30));
        infoContainer.add(lblStatus);
        infoContainer.add(Box.createVerticalStrut(10));
        infoContainer.add(lblSub);

        pnlScanner.add(infoContainer);
        add(pnlScanner, new AbsoluteConstraints(200, 150, 600, 400));

        btnStart = new JButton(I18nService.get("scan.btn.start"));
        btnStart.addActionListener(e -> startScanning());
        add(btnStart, new AbsoluteConstraints(400, 580, 200, 45));

        btnSimulasi = new JButton(I18nService.get("scan.btn.simulate"));
        btnSimulasi.addActionListener(e -> simulasiScan());
        add(btnSimulasi, new AbsoluteConstraints(400, 635, 200, 30));
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        
        lblStatus.setFont(ThemeManager.FONT_BOLD_16);
        lblStatus.setForeground(Color.WHITE);
        lblSub.setFont(ThemeManager.FONT_PLAIN_12);
        lblSub.setForeground(Color.LIGHT_GRAY);

        btnStart.setBackground(ThemeManager.STAT_GREEN);
        btnStart.setForeground(Color.WHITE);
        btnStart.setFont(ThemeManager.FONT_BOLD_14);
        btnStart.setBorderPainted(false);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSimulasi.setBackground(new Color(200, 200, 200));
        btnSimulasi.setForeground(Color.BLACK);
        btnSimulasi.setFont(ThemeManager.FONT_PLAIN_12);
        btnSimulasi.setBorderPainted(false);
        btnSimulasi.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
