package ui.dialog;

import ui.style.ThemeManager;
import dao.KucingDAO;
import model.Kucing;
import util.EncryptionUtils;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class DialogAnabul extends JDialog {

    public enum Mode { TAMBAH, EDIT, HAPUS }

    private JTextField txtId, txtNama, txtUmur;
    private JComboBox<String> cbRas, cbKandang, cbStatus, cbStatusMakan, cbKondisi;
    private JButton btnAction;
    private JLabel lblTitle;
    private Mode currentMode;

    public DialogAnabul(Frame parent, Mode mode) {
        super(parent, true);
        this.currentMode = mode;
        initLayout();
        applyTheme();
    }

    private void initLayout() {
        setSize(500, 580);
        setLocationRelativeTo(getOwner());
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel(I18nService.get("app.dialog." + (currentMode == Mode.TAMBAH ? "add" : currentMode == Mode.EDIT ? "edit" : "delete")), SwingConstants.CENTER);
        add(lblTitle, new AbsoluteConstraints(0, 20, 500, -1));

        addLabelInput(I18nService.get("app.dialog.idrfid"), 70, txtId = new JTextField());
        addLabelInput(I18nService.get("app.dialog.name"), 120, txtNama = new JTextField());
        addLabelCombo(I18nService.get("app.dialog.breed"), 170, cbRas = new JComboBox<>(new String[]{"Persia", "Anggora", "Kampung", "Siam"}));
        addLabelInput(I18nService.get("app.dialog.age"), 220, txtUmur = new JTextField());
        addLabelCombo(I18nService.get("app.dialog.cage"), 270, cbKandang = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "C1"}));
        addLabelCombo(I18nService.get("app.dialog.feedstatus"), 320, cbStatusMakan = new JComboBox<>(new String[]{"Sudah Makan", "Belum Makan"}));
        addLabelCombo(I18nService.get("app.dialog.health"), 370, cbKondisi = new JComboBox<>(new String[]{"Sehat", "Sakit"}));
        addLabelCombo(I18nService.get("app.dialog.status"), 420, cbStatus = new JComboBox<>(new String[]{"Sehat", "Sakit", "Sudah Makan", "Belum Makan"}));

        btnAction = new JButton(I18nService.get("app.dialog.btn" + (currentMode == Mode.TAMBAH ? "add" : currentMode == Mode.EDIT ? "edit" : "delete")));
        btnAction.addActionListener(e -> executeAction());
        add(btnAction, new AbsoluteConstraints(330, 480, 120, 45));
    }

    public void prepareData(Kucing k) {
        txtId.setText(k.getIdRfid());
        txtId.setEditable(false);
        txtNama.setText(k.getNama());
        txtUmur.setText(String.valueOf(k.getUmur()));
        cbRas.setSelectedItem(k.getRas());
        cbKandang.setSelectedItem(k.getKandang());
        cbStatusMakan.setSelectedItem(k.getStatusMakan() != null ? k.getStatusMakan() : I18nService.get("app.dialog.feedstatus.fed"));
        cbKondisi.setSelectedItem(k.getKondisiKesehatan() != null ? k.getKondisiKesehatan() : I18nService.get("app.dialog.health.sehat"));
        cbStatus.setSelectedItem(k.getStatusKesehatan());

        if (currentMode == Mode.HAPUS) {
            txtNama.setEditable(false);
            txtUmur.setEditable(false);
            cbRas.setEnabled(false);
            cbKandang.setEnabled(false);
            cbStatusMakan.setEnabled(false);
            cbKondisi.setEnabled(false);
            cbStatus.setEnabled(false);
        }
    }

    private void executeAction() {
        KucingDAO dao = new KucingDAO();
        String idRfid = txtId.getText();
        String encryptedId = EncryptionUtils.encrypt(idRfid);
        if (encryptedId != null) {
            System.out.println("DialogAnabul: ID RFID terenkripsi = " + encryptedId);
        }

        Kucing k = new Kucing(
            idRfid,
            txtNama.getText(),
            cbRas.getSelectedItem().toString(),
            Integer.parseInt(txtUmur.getText()),
            cbKandang.getSelectedItem().toString(),
            cbStatus.getSelectedItem().toString(),
            cbStatusMakan.getSelectedItem().toString(),
            cbKondisi.getSelectedItem().toString()
        );

        if (currentMode == Mode.TAMBAH) {
            dao.insert(k);
        } else if (currentMode == Mode.EDIT) {
            dao.update(k);
        } else {
            dao.delete(k.getIdRfid());
        }

        this.dispose();
    }

    private void addLabelInput(String label, int y, JTextField field) {
        JLabel l = new JLabel(label + "  :");
        l.setForeground(Color.WHITE);
        add(l, new AbsoluteConstraints(40, y, 100, 35));
        field.setBackground(new Color(217, 217, 217));
        add(field, new AbsoluteConstraints(150, y, 300, 35));
    }

    private void addLabelCombo(String label, int y, JComboBox combo) {
        JLabel l = new JLabel(label + "  :");
        l.setForeground(Color.WHITE);
        add(l, new AbsoluteConstraints(40, y, 100, 35));
        add(combo, new AbsoluteConstraints(150, y, 300, 35));
    }

    private void applyTheme() {
        getContentPane().setBackground(ThemeManager.NAVY);
        lblTitle.setFont(ThemeManager.FONT_BOLD_14);
        lblTitle.setForeground(ThemeManager.WHITE);

        if (currentMode == Mode.TAMBAH) {
            btnAction.setBackground(ThemeManager.WHITE);
            btnAction.setForeground(ThemeManager.NAVY);
        } else if (currentMode == Mode.EDIT) {
            btnAction.setBackground(ThemeManager.STAT_GREEN);
            btnAction.setForeground(Color.WHITE);
        } else {
            btnAction.setBackground(ThemeManager.STAT_RED);
            btnAction.setForeground(Color.WHITE);
        }
    }
}
