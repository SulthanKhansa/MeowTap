package ui;

import ui.style.ThemeManager;
import model.Admin;
import ui.panel.*;
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
    private final Admin sessionAdmin;
    private PanelBeranda pnlBeranda;
    private PanelScanRfid pnlScan;
    private PanelDataAnabul pnlData;
    private PanelAiClinic pnlAi;
    private boolean isEnglish = false;

    public MainDashboard(Admin admin) {
        this.sessionAdmin = admin;
        initCustomComponents();
        applyTheme();
    }

    // --- Logic & Actions ---

    public void setLanguage(boolean eng) {
        this.isEnglish = eng;
        if (isEnglish) {
            btnNavBeranda.setText("Home");
            btnNavScan.setText("Scan RFID");
            btnNavData.setText("Anabul Data");
            btnNavAI.setText("AI Clinic");
            btnLogout.setText("LOGOUT");
        } else {
            btnNavBeranda.setText("Beranda");
            btnNavScan.setText("Scan RFID");
            btnNavData.setText("Data Anabul");
            btnNavAI.setText("AI Clinic");
            btnLogout.setText("KELUAR");
        }
        
        pnlBeranda.setLanguage(eng);
        pnlScan.setLanguage(eng);
        pnlData.setLanguage(eng);
        pnlAi.setLanguage(eng);
    }

    private void prosesLogout() {
        new WelcomeScreen().setVisible(true);
        this.dispose();
    }

    public void switchPanel(String cardName) {
        cardLayout.show(panelKonten, cardName);
        if (cardName.equals("cardBeranda")) {
            pnlBeranda.refreshStats();
        } else if (cardName.equals("cardData")) {
            pnlData.loadData();
        }
    }

    // --- UI & Theme ---

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap Dashboard - " + sessionAdmin.getNamaLengkap());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());

        panelSidebar = new JPanel(new AbsoluteLayout());
        panelSidebar.setPreferredSize(new Dimension(260, 0));

        lblLogo = new JLabel("MeowTap");
        try {
            ImageIcon iconLogo = new ImageIcon(getClass().getResource("/Logo.png"));
            Image imgLogo = iconLogo.getImage().getScaledInstance(95, 95, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgLogo));
            lblLogo.setIconTextGap(3);
        } catch (Exception e) {}
        
        panelSidebar.add(lblLogo, new AbsoluteConstraints(10, 15, -1, -1));

        btnNavBeranda = createNavButton("Beranda", 150, "cardBeranda", Feather.HOME);
        btnNavScan = createNavButton("Scan RFID", 210, "cardScan", Feather.MAXIMIZE);
        btnNavData = createNavButton("Data Anabul", 270, "cardData", Feather.DATABASE);
        btnNavAI = createNavButton("AI Clinic", 330, "cardAI", Feather.CPU);
        
        btnLogout = new JButton("LOGOUT");
        btnLogout.setIcon(FontIcon.of(Feather.LOG_OUT, 16, Color.WHITE));
        btnLogout.setIconTextGap(10);
        btnLogout.addActionListener(e -> prosesLogout());
        panelSidebar.add(btnLogout, new AbsoluteConstraints(20, 610, 220, 45));

        getContentPane().add(panelSidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelKonten = new JPanel(cardLayout);
        
        pnlBeranda = new PanelBeranda(this, sessionAdmin);
        pnlScan = new PanelScanRfid(sessionAdmin);
        pnlData = new PanelDataAnabul();
        pnlAi = new PanelAiClinic();

        panelKonten.add(pnlBeranda, "cardBeranda");
        panelKonten.add(pnlScan, "cardScan");
        panelKonten.add(pnlData, "cardData");
        panelKonten.add(pnlAi, "cardAI");

        getContentPane().add(panelKonten, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, int y, String cardName, Feather icon) {
        JButton btn = new JButton(text);
        btn.setIcon(FontIcon.of(icon, 16, Color.WHITE));
        btn.setIconTextGap(15);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        btn.addActionListener(e -> switchPanel(cardName));
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
