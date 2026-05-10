package ui.panel;

import ui.style.ThemeManager;
import ui.dialog.DialogAnabul;
import model.Admin;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelScanRfid extends JPanel {

    private final Admin sessionAdmin;
    private JPanel pnlScanner;
    private JLabel lblTitle, lblStatus, lblSub;
    private JButton btnStart, btnSimulasi;
    private boolean isScanning = false;

    public PanelScanRfid(Admin admin) {
        this.sessionAdmin = admin;
        initLayout();
        applyTheme();
    }

    // Logic & Actions

    public void startScanning() {
        if (isScanning) return;
        isScanning = true;
        btnStart.setEnabled(false);

        lblStatus.setText("Sedang mencari perangkat...");
        lblStatus.setForeground(ThemeManager.STAT_YELLOW);
        
        // Jalankan scanner di background (Thread baru) agar layar tidak freeze (Not Responding)
        Thread scannerThread = new Thread(() -> {
            util.HardwareScanner scanner = new util.HardwareScanner();
            scanner.mulaiScanning(new util.HardwareScanner.ScanCallback() {
                
                @Override
                public void onScanSuccess(model.Kucing kucing) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setText("Terdeteksi: " + kucing.getNama());
                        lblStatus.setForeground(ThemeManager.STAT_GREEN);
                        lblSub.setText("Status: " + kucing.getStatusMakan() + " | Kandang: " + kucing.getKandang());
                        
                        // Popup Berhasil
                        JOptionPane.showMessageDialog(PanelScanRfid.this, 
                            "Scan RFID berhasil.\nSilahkan klik 'OK' untuk melihat data anabul.", 
                            "Scan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Buka Dialog Konfirmasi/Edit Data
                        openDetailDialog(kucing);
                        
                        isScanning = false;
                        if (btnStart != null) btnStart.setEnabled(true);
                    });
                }

                @Override
                public void onScanNotFound(String rawId) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setText("Anabul Tidak Dikenal!");
                        lblStatus.setForeground(ThemeManager.STAT_RED);
                        lblSub.setText("ID Kalung: " + rawId + " belum terdaftar di database.");
                        
                        // Popup Gagal
                        JOptionPane.showMessageDialog(PanelScanRfid.this, 
                            "Mohon maaf proses scan gagal, silahkan scan ulang", 
                            "Scan Gagal", JOptionPane.ERROR_MESSAGE);
                        
                        isScanning = false;
                        if (btnStart != null) btnStart.setEnabled(true);
                    });
                }

                @Override
                public void onScanError(String message) {
                    SwingUtilities.invokeLater(() -> {
                        lblStatus.setText("Koneksi Terputus / Error");
                        lblStatus.setForeground(ThemeManager.STAT_RED);
                        lblSub.setText(message);
                        isScanning = false;
                        if (btnStart != null) btnStart.setEnabled(true);
                    });
                }
            });
        });
        
        // Jadikan daemon agar thread otomatis mati kalau aplikasi diclose
        scannerThread.setDaemon(true); 
        scannerThread.start();
    }

    // UI & Theme

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel("Sistem Pemindaian RFID", SwingConstants.CENTER);
        add(lblTitle, new AbsoluteConstraints(0, 50, 1000, -1));

        JPanel pnlScanner = new JPanel(new GridBagLayout());
        pnlScanner.setBackground(new Color(18, 27, 59));
        
        FontIcon iconScan = FontIcon.of(Feather.MAXIMIZE, 120, Color.WHITE);
        JLabel lblIcon = new JLabel(iconScan, SwingConstants.CENTER);
        
        lblStatus = new JLabel("Scanner siap", SwingConstants.CENTER);
        lblSub = new JLabel("Silakan tempelkan kalung RFID anabul ke Reader", SwingConstants.CENTER);

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

        btnStart = new JButton("MULAI SCAN");
        btnStart.addActionListener(e -> startScanning());
        add(btnStart, new AbsoluteConstraints(400, 580, 200, 45));

        btnSimulasi = new JButton("Simulasi Scan");
        btnSimulasi.addActionListener(e -> simulasiScan());
        add(btnSimulasi, new AbsoluteConstraints(400, 635, 200, 30));
    }

    private void simulasiScan() {
        String fakeId = JOptionPane.showInputDialog(this, "Masukkan ID RFID simulasi:");
        if (fakeId != null && !fakeId.trim().isEmpty()) {
            dao.KucingDAO dao = new dao.KucingDAO();
            model.Kucing k = dao.findById(fakeId.trim());
            if (k != null) {
                lblStatus.setText("Terdeteksi: " + k.getNama());
                lblStatus.setForeground(ThemeManager.STAT_GREEN);
                lblSub.setText("Status: " + k.getStatusMakan() + " | Kandang: " + k.getKandang());
                
                JOptionPane.showMessageDialog(this, 
                    "Scan RFID berhasil.\nSilahkan klik 'OK' untuk melihat data anabul.", 
                    "Scan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                
                openDetailDialog(k);
            } else {
                lblStatus.setText("Anabul Tidak Dikenal!");
                lblStatus.setForeground(ThemeManager.STAT_RED);
                lblSub.setText("ID Kalung: " + fakeId + " belum terdaftar di database.");
                
                JOptionPane.showMessageDialog(this, 
                    "Mohon maaf proses scan gagal, silahkan scan ulang", 
                    "Scan Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openDetailDialog(model.Kucing kucing) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogAnabul dialog = new DialogAnabul(parent, DialogAnabul.Mode.EDIT);
        dialog.prepareData(kucing);
        dialog.setVisible(true);
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
