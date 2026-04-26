package ui;

import ui.style.ThemeManager;
import model.Admin;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class MainDashboard extends JFrame {

    private JPanel panelSidebar, panelKonten;
    private CardLayout cardLayout;
    private JButton btnNavBeranda, btnNavScan, btnNavData, btnNavAI, btnLogout;
    private JLabel lblLogo;
    private Admin sessionAdmin;

    public MainDashboard(Admin admin) {
        this.sessionAdmin = admin;
        initCustomComponents();
        applyTheme();
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap Dashboard - " + sessionAdmin.getNamaLengkap());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        getContentPane().setLayout(new BorderLayout());

        panelSidebar = new JPanel(new AbsoluteLayout());
        panelSidebar.setPreferredSize(new Dimension(260, 0));

        lblLogo = new JLabel("MeowTap");
        panelSidebar.add(lblLogo, new AbsoluteConstraints(30, 40, -1, -1));

        btnNavBeranda = createNavButton("Beranda", 150, "cardBeranda", Feather.HOME);
        btnNavScan = createNavButton("Scan RFID", 210, "cardScan", Feather.MAXIMIZE);
        btnNavData = createNavButton("Data Anabul", 270, "cardData", Feather.DATABASE);
        btnNavAI = createNavButton("AI Clinic", 330, "cardAI", Feather.CPU);
        
        btnLogout = new JButton("LOGOUT");
        btnLogout.setIcon(FontIcon.of(Feather.LOG_OUT, 16, Color.WHITE));
        btnLogout.setIconTextGap(10);
        btnLogout.addActionListener(e -> {
            new WelcomeScreen().setVisible(true);
            this.dispose();
        });
        panelSidebar.add(btnLogout, new AbsoluteConstraints(20, 650, 220, 45));

        getContentPane().add(panelSidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelKonten = new JPanel(cardLayout);
        
        // Sekarang semua Panel menerima data Admin agar nama profil sinkron
        panelKonten.add(new ui.panel.PanelBeranda(sessionAdmin), "cardBeranda");
        panelKonten.add(new ui.panel.PanelScanRfid(sessionAdmin), "cardScan");
        panelKonten.add(new ui.panel.PanelDataAnabul(), "cardData");
        panelKonten.add(new ui.panel.PanelAiClinic(), "cardAI");

        getContentPane().add(panelKonten, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, int y, String cardName, Feather icon) {
        JButton btn = new JButton(text);
        btn.setIcon(FontIcon.of(icon, 16, Color.WHITE));
        btn.setIconTextGap(15);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        
        btn.addActionListener(e -> cardLayout.show(panelKonten, cardName));
        panelSidebar.add(btn, new AbsoluteConstraints(20, y, 220, 45));
        return btn;
    }

    private void applyTheme() {
        panelSidebar.setBackground(ThemeManager.NAVY);
        lblLogo.setFont(ThemeManager.FONT_BOLD_16);
        lblLogo.setForeground(ThemeManager.WHITE);

        styleBtn(btnNavBeranda);
        styleBtn(btnNavScan);
        styleBtn(btnNavData);
        styleBtn(btnNavAI);

        btnLogout.setBackground(new Color(165, 23, 23));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(ThemeManager.FONT_BOLD_14);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelKonten.setBackground(ThemeManager.LAVENDER);
    }

    private void styleBtn(JButton b) {
        b.setBackground(ThemeManager.NAVY);
        b.setForeground(ThemeManager.WHITE);
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
