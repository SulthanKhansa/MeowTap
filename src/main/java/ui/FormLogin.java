package ui;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class FormLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnMasuk;
    private JPanel mainPanel, loginContainer;
    private JLabel lblTitle, lblAppName;

    public FormLogin() {
        initCustomComponents();
        applyTheme();
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);

        loginContainer = new JPanel(new AbsoluteLayout());
        loginContainer.setPreferredSize(new Dimension(1000, 700));

        lblTitle = new JLabel("Welcome to the", SwingConstants.CENTER);
        loginContainer.add(lblTitle, new AbsoluteConstraints(0, 100, 1000, -1));

        lblAppName = new JLabel("MeowTap", SwingConstants.CENTER);
        loginContainer.add(lblAppName, new AbsoluteConstraints(0, 140, 1000, -1));

        txtUsername = new JTextField();
        loginContainer.add(txtUsername, new AbsoluteConstraints(350, 240, 310, 45));

        txtPassword = new JPasswordField();
        loginContainer.add(txtPassword, new AbsoluteConstraints(350, 300, 310, 45));

        btnMasuk = new JButton("Masuk");
        btnMasuk.addActionListener(e -> {
            new MainDashboard().setVisible(true);
            this.dispose();
        });
        loginContainer.add(btnMasuk, new AbsoluteConstraints(350, 380, 310, 50));

        mainPanel.add(loginContainer);
    }

    // Bagian pengaturan tema dan styling komponen
    private void applyTheme() {
        mainPanel.setBackground(ThemeManager.LAVENDER);
        loginContainer.setOpaque(false);
        Color inputBg = new Color(217, 217, 217);

        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        lblAppName.setFont(ThemeManager.FONT_LOGO);
        lblAppName.setForeground(ThemeManager.WHITE);

        // Styling input fields
        txtUsername.setBackground(inputBg);
        txtUsername.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        txtUsername.putClientProperty("JTextField.placeholderText", "Email / No Telepon");
        
        txtPassword.setBackground(inputBg);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        txtPassword.putClientProperty("JTextField.placeholderText", "Kata Sandi");

        // Styling tombol masuk
        btnMasuk.setBackground(ThemeManager.NAVY);
        btnMasuk.setForeground(ThemeManager.WHITE);
        btnMasuk.setFont(ThemeManager.FONT_BOLD_14);
        btnMasuk.setBorderPainted(false);
        btnMasuk.setFocusPainted(false);
        btnMasuk.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
