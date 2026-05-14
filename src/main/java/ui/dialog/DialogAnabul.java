package ui.dialog;

import ui.style.ThemeManager;
import dao.KucingDAO;
import model.Kucing;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class DialogAnabul extends JDialog {

    public enum Mode { TAMBAH, EDIT, HAPUS }
    
    private JTextField txtId, txtNama, txtUmur;
    private JComboBox<String> cbRas, cbKandang, cbStatus, cbKondisi;
    private JButton btnAction;
    private JLabel lblTitle;
    private final Mode currentMode;
    private boolean isEnglish = false;

    public DialogAnabul(Frame parent, Mode mode) {
        super(parent, true);
        this.currentMode = mode;
        initLayout();
        applyTheme();
    }

    // --- Logic & Actions ---

    public void prepareData(Kucing k) {
        txtId.setText(k.getIdRfid());
        txtId.setEditable(false);
        txtNama.setText(k.getNama());
        txtUmur.setText(String.valueOf(k.getUmur()));
        cbRas.setSelectedItem(k.getRas());
        cbKandang.setSelectedItem(k.getKandang());
        cbStatus.setSelectedItem(k.getStatusMakan());
        cbKondisi.setSelectedItem(k.getKondisiKesehatan());
        
        if (currentMode == Mode.HAPUS) {
            txtNama.setEditable(false);
            txtUmur.setEditable(false);
            cbRas.setEnabled(false);
            cbKandang.setEnabled(false);
            cbStatus.setEnabled(false);
            cbKondisi.setEnabled(false);
        }
    }

    private void executeAction() {
        KucingDAO dao = new KucingDAO();
        Kucing k = new Kucing(
            txtId.getText(),
            txtNama.getText(),
            cbRas.getSelectedItem().toString(),
            Integer.parseInt(txtUmur.getText()),
            cbKandang.getSelectedItem().toString(),
            cbStatus.getSelectedItem().toString(),
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

    private String trans(String val) {
        if (!isEnglish) return val;
        switch (val.toLowerCase()) {
            case "sudah makan": return "Fed";
            case "belum makan": return "Not Fed";
            case "sehat": return "Healthy";
            case "sakit": return "Sick";
            case "meninggal": return "Deceased";
            default: return val;
        }
    }

    // --- UI & Theme ---

    public void setLanguage(boolean eng) {
        this.isEnglish = eng;
        if (isEnglish) {
            String m = currentMode == Mode.TAMBAH ? "ADD" : (currentMode == Mode.EDIT ? "EDIT" : "DELETE");
            lblTitle.setText(m + " ANABUL DATA");
            updateLabel(1, "Name");
            updateLabel(2, "Breed");
            updateLabel(3, "Age (month)");
            updateLabel(4, "Cage");
            updateLabel(5, "Feeding Status");
            updateLabel(6, "Condition");
            btnAction.setText(m);
            
            cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Fed", "Not Fed"}));
            cbKondisi.setModel(new DefaultComboBoxModel<>(new String[]{"Healthy", "Sick", "Deceased"}));
        } else {
            String m = currentMode == Mode.TAMBAH ? "TAMBAH" : (currentMode == Mode.EDIT ? "EDIT" : "HAPUS");
            lblTitle.setText(m + " DATA ANABUL");
            updateLabel(1, "Nama");
            updateLabel(2, "Ras");
            updateLabel(3, "Umur (bulan)");
            updateLabel(4, "Kandang");
            updateLabel(5, "Status Makan");
            updateLabel(6, "Kondisi");
            btnAction.setText(m);
            
            cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Sudah Makan", "Belum Makan"}));
            cbKondisi.setModel(new DefaultComboBoxModel<>(new String[]{"Sehat", "Sakit", "Meninggal"}));
        }
    }

    private void updateLabel(int index, String newText) {
        int labelCounter = 0;
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JLabel && c != lblTitle) {
                labelCounter++;
                if (labelCounter == index + 1) { // Skip ID RFID label
                    ((JLabel) c).setText(newText + "  :");
                }
            }
        }
    }

    private void initLayout() {
        setSize(500, 570);
        setLocationRelativeTo(getOwner());
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel(currentMode.name() + " DATA ANABUL", SwingConstants.CENTER);
        add(lblTitle, new AbsoluteConstraints(0, 20, 500, -1));

        addLabelInput("ID RFID", 80, txtId = new JTextField());
        addLabelInput("Nama", 130, txtNama = new JTextField());
        addLabelCombo("Ras", 180, cbRas = new JComboBox<>(new String[]{"Persia", "Anggora", "Kampung", "Siam"}));
        addLabelInput("Umur (bulan)", 230, txtUmur = new JTextField());
        addLabelCombo("Kandang", 280, cbKandang = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "C1"}));
        addLabelCombo("Status Makan", 330, cbStatus = new JComboBox<>(new String[]{"Sudah Makan", "Belum Makan"}));
        addLabelCombo("Kondisi", 380, cbKondisi = new JComboBox<>(new String[]{"Sehat", "Sakit", "Meninggal"}));

        btnAction = new JButton(currentMode == Mode.TAMBAH ? "TAMBAH" : (currentMode == Mode.EDIT ? "EDIT" : "HAPUS"));
        btnAction.addActionListener(e -> executeAction());
        add(btnAction, new AbsoluteConstraints(330, 460, 120, 45));
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
        
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAction.setBorderPainted(false);
    }
}
