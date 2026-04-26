package ui;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class WelcomeScreen extends JFrame {

    private JPanel mainPanel, contentContainer;
    private JLabel lblTitle, lblAppName;
    private JButton btnMasuk, btnDaftar;

    public WelcomeScreen() {
        initCustomComponents();
        applyTheme();
    }

    // Logic & Actions

    private void openLogin() {
        new FormLogin().setVisible(true);
        this.dispose();
    }

    private void openDaftar() {
        new FormDaftar().setVisible(true);
        this.dispose();
    }

    // UI & Theme

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap - Welcome");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);

        contentContainer = new JPanel(new AbsoluteLayout());
        contentContainer.setPreferredSize(new Dimension(1000, 700));

        lblTitle = new JLabel("Welcome to the", SwingConstants.CENTER);
        contentContainer.add(lblTitle, new AbsoluteConstraints(0, 150, 1000, -1));

        lblAppName = new JLabel("MeowTap", SwingConstants.CENTER);
        contentContainer.add(lblAppName, new AbsoluteConstraints(0, 200, 1000, -1));

        btnMasuk = new JButton("LOGIN");
        btnMasuk.addActionListener(e -> openLogin());
        contentContainer.add(btnMasuk, new AbsoluteConstraints(350, 450, 300, 50));

        btnDaftar = new JButton("DAFTAR");
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
