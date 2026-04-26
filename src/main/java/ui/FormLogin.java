package ui;

import ui.style.ThemeManager;
import dao.AdminDAO;
import model.Admin;
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
        btnMasuk.addActionListener(e -> prosesLogin());
        loginContainer.add(btnMasuk, new AbsoluteConstraints(350, 380, 310, 50));

        JButton btnKeDaftar = new JButton("Belum punya akun? Daftar");
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

        AdminDAO dao = new AdminDAO();
        Admin admin = dao.checkLogin(user, pass);

        if (admin != null) {
            // Berhasil Login: Buka Dashboard dengan data Admin asli
            new MainDashboard(admin).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
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
        txtUsername.putClientProperty("JTextField.placeholderText", "Username");
        
        txtPassword.setBackground(inputBg);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        txtPassword.putClientProperty("JTextField.placeholderText", "Password");

        btnMasuk.setBackground(ThemeManager.NAVY);
        btnMasuk.setForeground(ThemeManager.WHITE);
        btnMasuk.setFont(ThemeManager.FONT_BOLD_14);
        btnMasuk.setBorderPainted(false);
        btnMasuk.setFocusPainted(false);
        btnMasuk.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
