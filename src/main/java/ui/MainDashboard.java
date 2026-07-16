package ui;

import ui.style.ThemeManager;
import model.Admin;
import ui.panel.*;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

public class MainDashboard extends JFrame implements I18nService.I18nChangeListener {

    private JPanel panelSidebar, panelKonten, panelUtamaKonten;
    private JLayeredPane layeredPaneKonten;
    private CardLayout cardLayout;
    private JButton btnNavBeranda, btnNavScan, btnNavData, btnNavAI, btnLogout;
    private JLabel lblLogo;
    private final Admin sessionAdmin;
    private PanelBeranda pnlBeranda;
    private PanelScanRfid pnlScan;
    private PanelDataAnabul pnlData;
    private PanelAiClinic pnlAi;
    private PanelInformasiAkun pnlAkun;
    private JLabel lblJam;

    public MainDashboard(Admin admin) {
        this.sessionAdmin = admin;
        I18nService.registerListener(this);
        initCustomComponents();
        applyTheme();
        tampilJam();
        switchPanel("cardBeranda");
    }

    @Override
    public void onLanguageChanged() {
        setTitle(I18nService.get("dashboard.title") + " - " + sessionAdmin.getNamaLengkap());
        btnNavBeranda.setText(I18nService.get("dashboard.nav.home"));
        btnNavScan.setText(I18nService.get("dashboard.nav.scan"));
        btnNavData.setText(I18nService.get("dashboard.nav.data"));
        btnNavAI.setText(I18nService.get("dashboard.nav.ai"));
        btnLogout.setText(I18nService.get("dashboard.nav.logout"));
        
        pnlBeranda.onLanguageChanged();
        pnlScan.onLanguageChanged();
        pnlData.onLanguageChanged();
        pnlAi.onLanguageChanged();
        pnlAkun.onLanguageChanged();
    }

    private void prosesLogout() {
        new WelcomeScreen().setVisible(true);
        this.dispose();
    }

    public void switchPanel(String cardName) {
        cardLayout.show(panelKonten, cardName);

        btnNavBeranda.setBackground(ThemeManager.NAVY);
        btnNavScan.setBackground(ThemeManager.NAVY);
        btnNavData.setBackground(ThemeManager.NAVY);
        btnNavAI.setBackground(ThemeManager.NAVY);

        Color activeColor = ThemeManager.LAVENDER;

        if (cardName.equals("cardBeranda")) {
            btnNavBeranda.setBackground(activeColor);
            pnlBeranda.refreshStats();
        } else if (cardName.equals("cardScan")) {
            btnNavScan.setBackground(activeColor);
        } else if (cardName.equals("cardData")) {
            btnNavData.setBackground(activeColor);
            pnlData.loadData();
        } else if (cardName.equals("cardAI")) {
            btnNavAI.setBackground(activeColor);
        }
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(I18nService.get("dashboard.title") + " - " + sessionAdmin.getNamaLengkap());
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

        btnNavBeranda = createNavButton(I18nService.get("dashboard.nav.home"), 150, "cardBeranda", Feather.HOME);
        btnNavScan = createNavButton(I18nService.get("dashboard.nav.scan"), 210, "cardScan", Feather.MAXIMIZE);
        btnNavData = createNavButton(I18nService.get("dashboard.nav.data"), 270, "cardData", Feather.DATABASE);
        btnNavAI = createNavButton(I18nService.get("dashboard.nav.ai"), 330, "cardAI", Feather.CPU);
        
        btnLogout = new JButton(I18nService.get("dashboard.nav.logout"));
        btnLogout.setIcon(FontIcon.of(Feather.LOG_OUT, 16, Color.WHITE));
        btnLogout.setIconTextGap(10);
        btnLogout.addActionListener(e -> prosesLogout());
        panelSidebar.add(btnLogout, new AbsoluteConstraints(20, 610, 220, 45));

        getContentPane().add(panelSidebar, BorderLayout.WEST);

        panelUtamaKonten = new JPanel(new BorderLayout());
        
        layeredPaneKonten = new JLayeredPane() {
            @Override
            public void doLayout() {
                for (Component c : getComponents()) {
                    if (c == panelKonten) {
                        c.setBounds(0, 0, getWidth(), getHeight());
                    } else if (c == lblJam) {
                        c.setBounds(390, 30, 110, 30);
                    }
                }
            }
        };

        cardLayout = new CardLayout();
        panelKonten = new JPanel(cardLayout);
        panelKonten.setOpaque(false);

        pnlBeranda = new PanelBeranda(this, sessionAdmin);
        pnlScan = new PanelScanRfid(sessionAdmin);
        pnlData = new PanelDataAnabul();
        pnlAi = new PanelAiClinic();
        pnlAkun = new PanelInformasiAkun(sessionAdmin);

        panelKonten.add(pnlBeranda, "cardBeranda");
        panelKonten.add(pnlScan, "cardScan");
        panelKonten.add(pnlData, "cardData");
        panelKonten.add(pnlAi, "cardAI");
        panelKonten.add(pnlAkun, "cardAkun");

        lblJam = new JLabel("00:00:00", SwingConstants.RIGHT);
        lblJam.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblJam.setForeground(Color.BLACK);

        layeredPaneKonten.add(panelKonten, JLayeredPane.DEFAULT_LAYER);
        layeredPaneKonten.add(lblJam, JLayeredPane.POPUP_LAYER);

        panelUtamaKonten.add(layeredPaneKonten, BorderLayout.CENTER);
        getContentPane().add(panelUtamaKonten, BorderLayout.CENTER);
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

        panelUtamaKonten.setBackground(ThemeManager.LAVENDER);
    }

    private void styleBtn(JButton b) {
        b.setBackground(ThemeManager.NAVY);
        b.setForeground(ThemeManager.WHITE);
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void tampilJam() {
        Timer timer = new Timer(1000, e -> {
            lblJam.setText(
                new SimpleDateFormat("HH:mm:ss")
                        .format(new Date())
            );
        });
        timer.start();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                timer.stop();
            }
        });
    }
}
