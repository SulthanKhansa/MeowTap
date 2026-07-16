package ui;

import ui.style.ThemeManager;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class WelcomeScreen extends JFrame implements I18nService.I18nChangeListener {

    private JPanel mainPanel, contentContainer;
    private JLabel lblImage, lblTitle, lblAppName;
    private JButton btnMasuk, btnDaftar;

    public WelcomeScreen() {
        I18nService.registerListener(this);
        initCustomComponents();
        applyTheme();
        onLanguageChanged();
    }

    @Override
    public void onLanguageChanged() {
        setTitle(I18nService.get("welcome.title"));
        lblTitle.setText(I18nService.get("welcome.greeting"));
        lblAppName.setText(I18nService.get("welcome.appname"));
        btnMasuk.setText(I18nService.get("welcome.btn.login"));
        btnDaftar.setText(I18nService.get("welcome.btn.register"));
    }

    private void openLogin() {
        new FormLogin().setVisible(true);
        this.dispose();
    }

    private void openDaftar() {
        new FormDaftar().setVisible(true);
        this.dispose();
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(I18nService.get("welcome.title"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);

        contentContainer = new JPanel(new AbsoluteLayout());
        contentContainer.setPreferredSize(new Dimension(1000, 700));

        lblTitle = new JLabel(I18nService.get("welcome.greeting"), SwingConstants.CENTER);
        contentContainer.add(lblTitle, new AbsoluteConstraints(0, 80, 1000, -1));

        lblAppName = new JLabel(I18nService.get("welcome.appname"), SwingConstants.CENTER);
        contentContainer.add(lblAppName, new AbsoluteConstraints(0, 130, 1000, -1));

        try {
            ImageIcon iconCat = new ImageIcon(getClass().getResource("/kucing-login.png"));
            Image img = iconCat.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            lblImage = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
            contentContainer.add(lblImage, new AbsoluteConstraints(0, 180, 1000, 250));
        } catch (Exception e) {
            System.out.println("Gambar kucing-login.png tidak ditemukan!");
        }

        btnMasuk = new JButton(I18nService.get("welcome.btn.login"));
        btnMasuk.addActionListener(e -> openLogin());
        contentContainer.add(btnMasuk, new AbsoluteConstraints(350, 450, 300, 50));

        btnDaftar = new JButton(I18nService.get("welcome.btn.register"));
        btnDaftar.addActionListener(e -> openDaftar());
        contentContainer.add(btnDaftar, new AbsoluteConstraints(350, 520, 300, 50));

        mainPanel.add(contentContainer);
    }

    private void applyTheme() {
        mainPanel.setBackground(ThemeManager.LAVENDER);
        contentContainer.setOpaque(false);

        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        lblAppName.setFont(ThemeManager.FONT_LOGO);
        lblAppName.setForeground(ThemeManager.WHITE);

        styleBtn(btnMasuk, ThemeManager.WHITE, ThemeManager.NAVY);
        styleBtn(btnDaftar, ThemeManager.NAVY, ThemeManager.WHITE);
    }

    private void styleBtn(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
