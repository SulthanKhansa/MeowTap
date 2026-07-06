package ui;

import ui.style.ThemeManager;
import model.Admin;
import services.AuthService;
import util.I18nService;
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
    private AuthService authService;

    public FormLogin() {
        this.authService = new AuthService();
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

        lblTitle = new JLabel(I18nService.get("app.login.title"), SwingConstants.CENTER);
        loginContainer.add(lblTitle, new AbsoluteConstraints(0, 100, 1000, -1));

        lblAppName = new JLabel(I18nService.get("app.login.name"), SwingConstants.CENTER);
        loginContainer.add(lblAppName, new AbsoluteConstraints(0, 140, 1000, -1));

        txtUsername = new JTextField();
        loginContainer.add(txtUsername, new AbsoluteConstraints(350, 240, 310, 45));

        txtPassword = new JPasswordField();
        loginContainer.add(txtPassword, new AbsoluteConstraints(350, 300, 310, 45));

        btnMasuk = new JButton(I18nService.get("app.login.button"));
        btnMasuk.addActionListener(e -> prosesLogin());
        loginContainer.add(btnMasuk, new AbsoluteConstraints(350, 380, 310, 50));

        JButton btnKeDaftar = new JButton(I18nService.get("app.login.noaccount"));
        btnKeDaftar.setForeground(Color.WHITE);
        btnKeDaftar.setContentAreaFilled(false);
        btnKeDaftar.setBorderPainted(false);
        btnKeDaftar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKeDaftar.addActionListener(e -> {
            new FormDaftar().setVisible(true);
            this.dispose();
        });
        loginContainer.add(btnKeDaftar, new AbsoluteConstraints(350, 440, 310, -1));

        mainPanel.add(loginContainer);
    }

    private void prosesLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        Admin admin = authService.login(user, pass);

        if (admin != null) {
            new MainDashboard(admin).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                I18nService.get("app.login.error"),
                "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyTheme() {
        mainPanel.setBackground(ThemeManager.LAVENDER);
        loginContainer.setOpaque(false);
        Color inputBg = new Color(217, 217, 217);

        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        lblAppName.setFont(ThemeManager.FONT_LOGO);
        lblAppName.setForeground(ThemeManager.WHITE);

        txtUsername.setBackground(inputBg);
        txtUsername.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        txtUsername.putClientProperty("JTextField.placeholderText", I18nService.get("app.login.username"));

        txtPassword.setBackground(inputBg);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        txtPassword.putClientProperty("JTextField.placeholderText", I18nService.get("app.login.password"));

        btnMasuk.setBackground(ThemeManager.NAVY);
        btnMasuk.setForeground(ThemeManager.WHITE);
        btnMasuk.setFont(ThemeManager.FONT_BOLD_14);
        btnMasuk.setBorderPainted(false);
        btnMasuk.setFocusPainted(false);
        btnMasuk.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
