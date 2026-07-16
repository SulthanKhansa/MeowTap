package ui.panel;

import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import ui.style.ThemeManager;
import model.Admin;
import util.I18nService;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.feather.Feather;

public class PanelInformasiAkun extends JPanel implements I18nService.I18nChangeListener {

    private final Admin admin;
    private JLabel lblTitle, lblAdmin, lblUsernameInfo, lblNamaInfo;
    private JPanel cardUsername, cardNama, pnlAdmin;

    public PanelInformasiAkun(Admin admin) {
        this.admin = admin;
        I18nService.registerListener(this);
        initLayout();
        applyTheme();
        onLanguageChanged();
    }

    @Override
    public void onLanguageChanged() {
        lblTitle.setText(I18nService.get("account.title"));
        lblUsernameInfo.setText(I18nService.get("account.username") + admin.getUsername());
        lblNamaInfo.setText(I18nService.get("account.fullname") + admin.getNamaLengkap());
        repaint();
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        pnlAdmin = new JPanel(new BorderLayout());
        lblAdmin = new JLabel(admin.getNamaLengkap(), SwingConstants.CENTER);
        lblAdmin.setIcon(FontIcon.of(Feather.USER, 20, Color.WHITE));
        lblAdmin.setIconTextGap(10);
        pnlAdmin.add(lblAdmin, BorderLayout.CENTER);
        add(pnlAdmin, new AbsoluteConstraints(600, 20, 240, 55));

        lblTitle = new JLabel(I18nService.get("account.title"));
        add(lblTitle, new AbsoluteConstraints(380, 120, -1, -1));

        cardUsername = new JPanel(new AbsoluteLayout());
        lblUsernameInfo = new JLabel(I18nService.get("account.username") + admin.getUsername());
        cardUsername.add(lblUsernameInfo, new AbsoluteConstraints(20, 20, 450, 30));
        add(cardUsername, new AbsoluteConstraints(250, 210, 500, 70));

        cardNama = new JPanel(new AbsoluteLayout());
        lblNamaInfo = new JLabel(I18nService.get("account.fullname") + admin.getNamaLengkap());
        cardNama.add(lblNamaInfo, new AbsoluteConstraints(20, 20, 450, 30));
        add(cardNama, new AbsoluteConstraints(250, 320, 500, 70));
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);

        pnlAdmin.setBackground(ThemeManager.NAVY);
        lblAdmin.setForeground(Color.WHITE);
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);

        styleCard(cardUsername, lblUsernameInfo);
        styleCard(cardNama, lblNamaInfo);
    }

    private void styleCard(JPanel card, JLabel label) {
        card.setBackground(ThemeManager.DARK_BLUE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
    }
}
