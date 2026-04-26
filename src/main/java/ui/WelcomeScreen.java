package ui;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class WelcomeScreen extends JFrame {

    private JPanel mainPanel, contentContainer;
    private JLabel lblTitle, lblAppName, lblKucing;
    private JButton btnMasuk, btnDaftar;

    public WelcomeScreen() {
        initCustomComponents();
        applyTheme();
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap - Welcome");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);

        // Container untuk menengahkan konten di layar
        contentContainer = new JPanel(new AbsoluteLayout());
        contentContainer.setPreferredSize(new Dimension(1000, 700));

        lblTitle = new JLabel("Welcome to the", SwingConstants.CENTER);
        contentContainer.add(lblTitle, new AbsoluteConstraints(0, 80, 1000, -1));

        lblAppName = new JLabel("MeowTap", SwingConstants.CENTER);
        contentContainer.add(lblAppName, new AbsoluteConstraints(0, 120, 1000, -1));

        lblKucing = new JLabel();
        contentContainer.add(lblKucing, new AbsoluteConstraints(392, 200, 215, 215));

        btnMasuk = new JButton("Masuk");
        btnMasuk.addActionListener(e -> {
            new FormLogin().setVisible(true);
            this.dispose();
        });
        contentContainer.add(btnMasuk, new AbsoluteConstraints(350, 450, 300, 50));

        btnDaftar = new JButton("Daftar");
        contentContainer.add(btnDaftar, new AbsoluteConstraints(350, 520, 300, 50));

        mainPanel.add(contentContainer);
    }

    // Metod untuk pengaturan tema dan dekorasi UI
    private void applyTheme() {
        mainPanel.setBackground(ThemeManager.LAVENDER);
        contentContainer.setOpaque(false);

        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        lblAppName.setFont(ThemeManager.FONT_LOGO);
        lblAppName.setForeground(ThemeManager.WHITE);

        // Load asset gambar kucing
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/kucing-login.png"));
            Image img = icon.getImage().getScaledInstance(215, 215, Image.SCALE_SMOOTH);
            lblKucing.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblKucing.setText("[Gambar Kucing]");
            lblKucing.setForeground(Color.WHITE);
        }

        styleBtn(btnMasuk, ThemeManager.NAVY, ThemeManager.WHITE);
        styleBtn(btnDaftar, ThemeManager.WHITE, ThemeManager.NAVY);
    }

    private void styleBtn(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void main(String args[]) {
        com.formdev.flatlaf.FlatIntelliJLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}
