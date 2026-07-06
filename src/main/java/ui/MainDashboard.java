package ui;

import ui.style.ThemeManager;
import model.Admin;
import util.I18nService;
import util.I18nService.I18nChangeListener;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class MainDashboard extends JFrame implements I18nChangeListener {

    private JPanel panelSidebar, panelKonten;
    private CardLayout cardLayout;
    private JButton btnNavBeranda, btnNavScan, btnNavData, btnNavAI, btnLogout, btnDemoThread;
    private JLabel lblLogo;
    private Admin sessionAdmin;
    private JButton btnLangId, btnLangEn, btnLangMs;

    public MainDashboard(Admin admin) {
        this.sessionAdmin = admin;
        initCustomComponents();
        applyTheme();
        I18nService.registerListener(this);
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

        btnNavBeranda = createNavButton(I18nService.get("app.sidebar.home"), 150, "cardBeranda", Feather.HOME);
        btnNavScan = createNavButton(I18nService.get("app.sidebar.scan"), 210, "cardScan", Feather.MAXIMIZE);
        btnNavData = createNavButton(I18nService.get("app.sidebar.data"), 270, "cardData", Feather.DATABASE);
        btnNavAI = createNavButton(I18nService.get("app.sidebar.ai"), 330, "cardAI", Feather.CPU);

        btnDemoThread = new JButton(I18nService.get("app.sidebar.demothread"));
        btnDemoThread.setIcon(FontIcon.of(Feather.CLOCK, 16, Color.WHITE));
        btnDemoThread.setIconTextGap(15);
        btnDemoThread.setHorizontalAlignment(SwingConstants.LEFT);
        btnDemoThread.setMargin(new Insets(0, 20, 0, 0));
        btnDemoThread.addActionListener(e -> {
            DemoThread dt = new DemoThread();
            dt.setVisible(true);
        });
        panelSidebar.add(btnDemoThread, new AbsoluteConstraints(20, 390, 220, 45));

        btnLogout = new JButton(I18nService.get("app.sidebar.logout"));
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

        panelKonten.add(new ui.panel.PanelBeranda(sessionAdmin), "cardBeranda");
        panelKonten.add(new ui.panel.PanelScanRfid(sessionAdmin), "cardScan");
        panelKonten.add(new ui.panel.PanelDataAnabul(), "cardData");
        panelKonten.add(new ui.panel.PanelAiClinic(), "cardAI");

        getContentPane().add(panelKonten, BorderLayout.CENTER);
    }

    private void addLanguageSelector() {
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        langPanel.setOpaque(false);

        btnLangId = new JButton("ID");
        btnLangEn = new JButton("EN");
        btnLangMs = new JButton("MS");

        Font langFont = ThemeManager.FONT_BOLD_14;
        for (JButton btn : new JButton[]{btnLangId, btnLangEn, btnLangMs}) {
            btn.setFont(langFont);
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBackground(ThemeManager.NAVY);
        }

        btnLangId.addActionListener(e -> I18nService.setLocaleByCode("id"));
        btnLangEn.addActionListener(e -> I18nService.setLocaleByCode("en"));
        btnLangMs.addActionListener(e -> I18nService.setLocaleByCode("ms"));

        langPanel.add(btnLangId);
        langPanel.add(btnLangEn);
        langPanel.add(btnLangMs);
        getContentPane().add(langPanel, BorderLayout.NORTH);
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
        styleBtn(btnDemoThread);

        btnLogout.setBackground(new Color(165, 23, 23));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(ThemeManager.FONT_BOLD_14);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelKonten.setBackground(ThemeManager.LAVENDER);
        addLanguageSelector();
    }

    private void styleBtn(JButton b) {
        b.setBackground(ThemeManager.NAVY);
        b.setForeground(ThemeManager.WHITE);
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void onLanguageChanged() {
        btnNavBeranda.setText(I18nService.get("app.sidebar.home"));
        btnNavScan.setText(I18nService.get("app.sidebar.scan"));
        btnNavData.setText(I18nService.get("app.sidebar.data"));
        btnNavAI.setText(I18nService.get("app.sidebar.ai"));
        btnDemoThread.setText(I18nService.get("app.sidebar.demothread"));
        btnLogout.setText(I18nService.get("app.sidebar.logout"));
    }
}
