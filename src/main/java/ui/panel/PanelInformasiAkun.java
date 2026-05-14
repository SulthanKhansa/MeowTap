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

    private JLabel lblTitle;
    private JPanel cardUsername, cardNama, pnlAdmin;
    private JLabel lblAdmin;

    public PanelInformasiAkun(Admin admin) {

        setLayout(new AbsoluteLayout());
        setBackground(ThemeManager.LAVENDER);

        // Panel admin atas
        pnlAdmin = new JPanel(new BorderLayout());
        pnlAdmin.setBackground(ThemeManager.NAVY);

        lblAdmin = new JLabel(admin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);

        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);

        add(pnlAdmin, new AbsoluteConstraints(600, 20, 240, 55));

        // Judul
        lblTitle = new JLabel("Informasi Akun");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        add(lblTitle, new AbsoluteConstraints(420, 120, -1, -1));

        // Card Username
        cardUsername = new JPanel();
        cardUsername.setLayout(new AbsoluteLayout());
        cardUsername.setBackground(new Color(230,230,230));

        add(cardUsername, new AbsoluteConstraints(320, 210, 500, 70));

        JLabel lblUsername = new JLabel(
            "Username : " + admin.getUsername()
        );

        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblUsername.setForeground(Color.BLACK);

        cardUsername.add(
            lblUsername,
            new AbsoluteConstraints(20, 20, 450, 30)
        );

        // Card Nama
        cardNama = new JPanel();
        cardNama.setLayout(new AbsoluteLayout());
        cardNama.setBackground(new Color(230,230,230));

        add(cardNama, new AbsoluteConstraints(320, 320, 500, 70));

        JLabel lblNama = new JLabel(
            "Nama Lengkap : " + admin.getNamaLengkap()
        );

        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNama.setForeground(Color.BLACK);

        cardNama.add(
            lblNama,
            new AbsoluteConstraints(20, 20, 450, 30)
        );
    }
}