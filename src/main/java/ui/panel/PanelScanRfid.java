package ui.panel;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelScanRfid extends JPanel {

    public PanelScanRfid() {
        initLayout();
        applyTheme();
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        // Header Admin
        JPanel pnlAdmin = new JPanel(new BorderLayout());
        pnlAdmin.setBackground(ThemeManager.NAVY);
        JLabel lblAdmin = new JLabel("Admin", SwingConstants.CENTER);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);
        add(pnlAdmin, new AbsoluteConstraints(680, 20, 240, 60));

        JLabel lblTitle = new JLabel("Sistem Pemindaian RFID", SwingConstants.CENTER);
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        add(lblTitle, new AbsoluteConstraints(0, 50, 1000, -1));

        // Area Box Scanner Tengah
        JPanel pnlScanner = new JPanel(new GridBagLayout());
        pnlScanner.setBackground(new Color(18, 27, 59));
        
        // Menggunakan Icon Vector Feather MAXIMIZE sebagai simbol scanner
        FontIcon iconScan = FontIcon.of(Feather.MAXIMIZE, 120, Color.WHITE);
        JLabel lblIcon = new JLabel(iconScan, SwingConstants.CENTER);
        
        JLabel lblStatus = new JLabel("Scanner siap", SwingConstants.CENTER);
        lblStatus.setFont(ThemeManager.FONT_BOLD_16);
        lblStatus.setForeground(Color.WHITE);
        
        JLabel lblSub = new JLabel("Silakan tempelkan kalung RFID anabul ke Reader", SwingConstants.CENTER);
        lblSub.setFont(ThemeManager.FONT_PLAIN_12);
        lblSub.setForeground(Color.LIGHT_GRAY);

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
    }
}
