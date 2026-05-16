package ui.panel;

import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import ui.style.ThemeManager;
import model.Admin;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelInformasiAkun extends JPanel {

    private final Admin admin;
    private JLabel lblTitle, lblAdmin, lblUsernameInfo, lblNamaInfo;
    private JPanel cardUsername, cardNama, pnlAdmin;
    private boolean isEnglish = false;

    public PanelInformasiAkun(Admin admin) {
        this.admin = admin;
        initLayout();
        applyTheme();
    }

    // --- Logic & Actions ---

    public void setLanguage(boolean eng) {
        this.isEnglish = eng;
        if (isEnglish) {
            lblTitle.setText("Account Information");
            lblUsernameInfo.setText("Username : " + admin.getUsername());
            lblNamaInfo.setText("Full Name : " + admin.getNamaLengkap());
        } else {
            lblTitle.setText("Informasi Akun");
            lblUsernameInfo.setText("Username : " + admin.getUsername());
            lblNamaInfo.setText("Nama Lengkap : " + admin.getNamaLengkap());
        }
        repaint();
    }

    // --- UI & Theme ---

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        // Panel admin atas (Header)
        pnlAdmin = new JPanel(new BorderLayout());
        lblAdmin = new JLabel(admin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);
        add(pnlAdmin, new AbsoluteConstraints(600, 20, 240, 55));

        // Judul Halaman
        lblTitle = new JLabel("Informasi Akun");
        add(lblTitle, new AbsoluteConstraints(380, 120, -1, -1));

        // Card Username
        cardUsername = new JPanel(new AbsoluteLayout());
        lblUsernameInfo = new JLabel("Username : " + admin.getUsername());
        cardUsername.add(lblUsernameInfo, new AbsoluteConstraints(20, 20, 450, 30));
        add(cardUsername, new AbsoluteConstraints(250, 210, 500, 70));

        // Card Nama
        cardNama = new JPanel(new AbsoluteLayout());
        lblNamaInfo = new JLabel("Nama Lengkap : " + admin.getNamaLengkap());
        cardNama.add(lblNamaInfo, new AbsoluteConstraints(20, 20, 450, 30));
        add(cardNama, new AbsoluteConstraints(250, 320, 500, 70));
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);

        pnlAdmin.setBackground(ThemeManager.NAVY);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        lblTitle.setFont(ThemeManager.FONT_WELCOME); // Gunakan font standar dashboard
        lblTitle.setForeground(ThemeManager.WHITE);

        // Styling Cards
        styleCard(cardUsername, lblUsernameInfo);
        styleCard(cardNama, lblNamaInfo);
    }

    private void styleCard(JPanel card, JLabel label) {
        card.setBackground(ThemeManager.DARK_BLUE); // Ubah ke Navy/Dark agar selaras dengan dashboard
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
    }
}