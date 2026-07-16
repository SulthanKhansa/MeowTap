package ui;

import ui.style.ThemeManager;
import dao.AdminDAO;
import model.Admin;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class FormLogin extends JFrame implements I18nService.I18nChangeListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnMasuk, btnKeDaftar;
    private JPanel mainPanel, loginContainer;
    private JLabel lblTitle, lblAppName;

    public FormLogin() {
        I18nService.registerListener(this);
        initCustomComponents();
        applyTheme();
        onLanguageChanged();
    }

    @Override
    public void onLanguageChanged() {
        setTitle(I18nService.get("login.title"));
        lblTitle.setText(I18nService.get("login.greeting"));
        lblAppName.setText(I18nService.get("welcome.appname"));
        btnMasuk.setText(I18nService.get("login.btn.login"));
        btnKeDaftar.setText(I18nService.get("login.btn.register"));
        txtUsername.putClientProperty("JTextField.placeholderText", I18nService.get("login.username"));
        txtPassword.putClientProperty("JTextField.placeholderText", I18nService.get("login.password"));
    }

    private void prosesLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, I18nService.get("login.error.empty"), I18nService.get("login.error.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        AdminDAO dao = new AdminDAO();
        Admin admin = dao.checkLogin(user, pass);

        if (admin != null) {
            new MainDashboard(admin).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, I18nService.get("login.error.wrong"), I18nService.get("login.error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initCustomComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(I18nService.get("login.title"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new JPanel(new GridBagLayout());
        setContentPane(mainPanel);

        loginContainer = new JPanel(new AbsoluteLayout());
        loginContainer.setPreferredSize(new Dimension(1000, 700));

        lblTitle = new JLabel(I18nService.get("login.greeting"), SwingConstants.CENTER);
        loginContainer.add(lblTitle, new AbsoluteConstraints(0, 100, 1000, -1));

        lblAppName = new JLabel(I18nService.get("welcome.appname"), SwingConstants.CENTER);
        loginContainer.add(lblAppName, new AbsoluteConstraints(0, 140, 1000, -1));

        txtUsername = new JTextField();
        loginContainer.add(txtUsername, new AbsoluteConstraints(350, 240, 310, 45));

        txtPassword = new JPasswordField();
        loginContainer.add(txtPassword, new AbsoluteConstraints(350, 300, 310, 45));

        btnMasuk = new JButton(I18nService.get("login.btn.login"));
        btnMasuk.addActionListener(e -> prosesLogin());
        loginContainer.add(btnMasuk, new AbsoluteConstraints(350, 380, 310, 50));

        btnKeDaftar = new JButton(I18nService.get("login.btn.register"));
        btnKeDaftar.addActionListener(e -> {
            new FormDaftar().setVisible(true);
            this.dispose();
        });
        loginContainer.add(btnKeDaftar, new AbsoluteConstraints(350, 440, 310, -1));

        mainPanel.add(loginContainer);
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
        txtUsername.putClientProperty("JTextField.placeholderText", I18nService.get("login.username"));
        
        txtPassword.setBackground(inputBg);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        txtPassword.putClientProperty("JTextField.placeholderText", I18nService.get("login.password"));

        btnMasuk.setBackground(ThemeManager.NAVY);
        btnMasuk.setForeground(ThemeManager.WHITE);
        btnMasuk.setFont(ThemeManager.FONT_BOLD_14);
        btnMasuk.setBorderPainted(false);
        btnMasuk.setFocusPainted(false);
        btnMasuk.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnKeDaftar.setForeground(Color.WHITE);
        btnKeDaftar.setContentAreaFilled(false);
        btnKeDaftar.setBorderPainted(false);
        btnKeDaftar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
