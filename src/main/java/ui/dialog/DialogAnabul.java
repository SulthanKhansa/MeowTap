package ui.dialog;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class DialogAnabul extends JDialog {

    public enum Mode { TAMBAH, EDIT, HAPUS }
    
    private JTextField txtId, txtNama, txtUmur;
    private JComboBox<String> cbRas, cbKandang, cbStatus;
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
        setSize(500, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel(currentMode.name() + " DATA ANABUL", SwingConstants.CENTER);
        add(lblTitle, new AbsoluteConstraints(0, 20, 500, -1));

        // Rows
        addLabelInput("ID RFID", 80, txtId = new JTextField());
        addLabelInput("Nama", 130, txtNama = new JTextField());
        addLabelCombo("Ras", 180, cbRas = new JComboBox<>(new String[]{"Persia", "Anggora", "Kampung"}));
        addLabelInput("Umur", 230, txtUmur = new JTextField());
        addLabelCombo("Kandang", 280, cbKandang = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2"}));
        addLabelCombo("Status", 330, cbStatus = new JComboBox<>(new String[]{"Sehat", "Sakit", "Sudah Makan"}));

        btnAction = new JButton(currentMode == Mode.TAMBAH ? "TAMBAH" : (currentMode == Mode.EDIT ? "EDIT" : "HAPUS"));
        add(btnAction, new AbsoluteConstraints(330, 400, 120, 40));
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
