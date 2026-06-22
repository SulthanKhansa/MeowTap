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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

public class MainDashboard extends JFrame {

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
    private boolean isEnglish = false;
    private JLabel lblJam;

    public MainDashboard(Admin admin) {
        this.sessionAdmin = admin;
        initCustomComponents();
        applyTheme();
        tampilJam();
        switchPanel("cardBeranda");
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
        pnlAkun.setLanguage(eng);
    }

    private void prosesLogout() {
        new WelcomeScreen().setVisible(true);
        this.dispose();
    }

    public void switchPanel(String cardName) {
        cardLayout.show(panelKonten, cardName);

        // reset warna semua tombol
        btnNavBeranda.setBackground(ThemeManager.NAVY);
        btnNavScan.setBackground(ThemeManager.NAVY);
        btnNavData.setBackground(ThemeManager.NAVY);
        btnNavAI.setBackground(ThemeManager.NAVY);

        // warna active
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

    // --- UI & Theme ---

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap Dashboard - " + sessionAdmin.getNamaLengkap());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new BorderLayout());

        // 1. Setup Sidebar (Kiri)
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

        // 2. Setup Panel Utama Kanan (Warna Ungu Lavender Dasar)
        panelUtamaKonten = new JPanel(new BorderLayout());
        
        // Menggunakan JLayeredPane agar Jam bisa melayang tepat di atas halaman konten
        layeredPaneKonten = new JLayeredPane() {
            @Override
            public void doLayout() {
                // Memastikan konten utama selalu berukuran penuh mengikuti layar
                for (Component c : getComponents()) {
                    if (c == panelKonten) {
                        c.setBounds(0, 0, getWidth(), getHeight());
                    } else if (c == lblJam) {
                        // Mengatur posisi melayang Jam di pojok kanan atas secara presisi
                        // Jarak: 35 piksel dari tepi kanan layar, 15 piksel dari atas layar
                        c.setBounds(getWidth() - 120, 15, 100, 30);
                    }
                }
            }
        };

        // 3. Setup CardLayout Konten Halaman (Lapisan Bawah / DEFAULT_LAYER)
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

        // 4. Setup Label Jam (Lapisan Atas / POPUP_LAYER)
        lblJam = new JLabel("00:00:00", SwingConstants.RIGHT);
        lblJam.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblJam.setForeground(Color.BLACK);

        // Masukkan komponen ke JLayeredPane sesuai urutan lapisannya
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
    }
}