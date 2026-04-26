package ui;

import ui.style.ThemeManager;
import dao.AdminDAO;
import model.Admin;
import util.SecurityUtils;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class FormDaftar extends JFrame {

    private JTextField txtNama, txtUsername;
    private JPasswordField txtPassword;
    private JButton btnDaftar, btnBack;
    private JPanel mainPanel, container;
    private JLabel lblTitle, lblAppName;

    public FormDaftar() {
        initCustomComponents();
        applyTheme();
    }

    // Logic & Actions

    private void prosesDaftar() {
        String nama = txtNama.getText();
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (nama.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        Admin newAdmin = new Admin(user, SecurityUtils.hashSHA256(pass), nama);
        AdminDAO dao = new AdminDAO();
        
        if (dao.register(newAdmin)) {
            JOptionPane.showMessageDialog(this, "Pendaftaran Berhasil! Silakan Login.");
            new FormLogin().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Pendaftaran Gagal! Username mungkin sudah ada.");
        }
    }

    // UI & Theme

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MeowTap - Daftar Akun");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);

        container = new JPanel(new AbsoluteLayout());
        container.setPreferredSize(new Dimension(1000, 700));

        lblTitle = new JLabel("Welcome to the", SwingConstants.CENTER);
        container.add(lblTitle, new AbsoluteConstraints(0, 80, 1000, -1));

        lblAppName = new JLabel("MeowTap", SwingConstants.CENTER);
        container.add(lblAppName, new AbsoluteConstraints(0, 120, 1000, -1));

        txtNama = new JTextField();
        container.add(txtNama, new AbsoluteConstraints(350, 200, 310, 45));

        txtUsername = new JTextField();
        container.add(txtUsername, new AbsoluteConstraints(350, 260, 310, 45));

        txtPassword = new JPasswordField();
        container.add(txtPassword, new AbsoluteConstraints(350, 320, 310, 45));

        btnDaftar = new JButton("Daftar");
        btnDaftar.addActionListener(e -> prosesDaftar());
        container.add(btnDaftar, new AbsoluteConstraints(350, 400, 310, 50));

        btnBack = new JButton("Sudah punya akun? Login");
        btnBack.addActionListener(e -> {
            new FormLogin().setVisible(true);
            this.dispose();
        });
        container.add(btnBack, new AbsoluteConstraints(350, 460, 310, -1));

        mainPanel.add(container);
    }

    private void applyTheme() {
        mainPanel.setBackground(ThemeManager.LAVENDER);
        container.setOpaque(false);

        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        lblAppName.setFont(ThemeManager.FONT_LOGO);
        lblAppName.setForeground(ThemeManager.WHITE);

        styleInput(txtNama, "Nama Lengkap");
        styleInput(txtUsername, "Username");
        styleInput(txtPassword, "Password");

        btnDaftar.setBackground(ThemeManager.NAVY);
        btnDaftar.setForeground(ThemeManager.WHITE);
        btnDaftar.setFont(ThemeManager.FONT_BOLD_14);
        btnDaftar.setBorderPainted(false);
        btnDaftar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.setForeground(ThemeManager.WHITE);
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleInput(JTextField field, String placeholder) {
        field.setBackground(new Color(217, 217, 217));
        field.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        field.putClientProperty("JTextField.placeholderText", placeholder);
    }
}
