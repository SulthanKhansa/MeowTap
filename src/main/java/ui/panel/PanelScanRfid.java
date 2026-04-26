package ui.panel;

import ui.style.ThemeManager;
import model.Admin;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelScanRfid extends JPanel {

    private final Admin sessionAdmin;
    private JPanel pnlAdmin;
    private JLabel lblAdmin, lblTitle, lblStatus, lblSub;

    public PanelScanRfid(Admin admin) {
        this.sessionAdmin = admin;
        initLayout();
        applyTheme();
    }

    // Logic & Actions

    public void startScanning() {
        // Implementasi integrasi hardware
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
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        pnlAdmin.setBackground(ThemeManager.NAVY);
        
        lblAdmin.setForeground(Color.WHITE);
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        
        lblStatus.setFont(ThemeManager.FONT_BOLD_16);
        lblStatus.setForeground(Color.WHITE);
        lblSub.setFont(ThemeManager.FONT_PLAIN_12);
        lblSub.setForeground(Color.LIGHT_GRAY);
    }
}
