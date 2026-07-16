package ui.dialog;

import ui.style.ThemeManager;
import dao.KucingDAO;
import model.Kucing;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class DialogAnabul extends JDialog implements I18nService.I18nChangeListener {

    public enum Mode { TAMBAH, EDIT, HAPUS }
    
    private JTextField txtId, txtNama, txtUmur;
    private JComboBox<String> cbRas, cbKandang, cbStatus, cbKondisi;
    private JButton btnAction;
    private JLabel lblTitle;
    private final Mode currentMode;

    public DialogAnabul(Frame parent, Mode mode) {
        super(parent, true);
        this.currentMode = mode;
        I18nService.registerListener(this);
        initLayout();
        applyTheme();
        onLanguageChanged();
    }

    public void prepareData(Kucing k) {
        txtId.setText(k.getIdRfid());
        txtId.setEditable(false);
        txtNama.setText(k.getNama());
        txtUmur.setText(String.valueOf(k.getUmur()));
        cbRas.setSelectedItem(k.getRas());
        cbKandang.setSelectedItem(k.getKandang());
        cbStatus.setSelectedItem(trans(k.getStatusMakan()));
        cbKondisi.setSelectedItem(trans(k.getKondisiKesehatan()));
        
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
            reverseTrans(cbStatus.getSelectedItem().toString()),
            reverseTrans(cbKondisi.getSelectedItem().toString())
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
        if (val == null) return "-";
        String lang = I18nService.getCurrentLocale().getLanguage();
        String v = val.trim().toLowerCase();
        if (lang.equals("fr")) {
            return switch (v) {
                case "sudah makan" -> "Nourri";
                case "belum makan" -> "Non nourri";
                case "sehat" -> "En bonne santé";
                case "sakit" -> "Malade";
                case "meninggal" -> "Décédé";
                default -> val;
            };
        } else if (lang.equals("es")) {
            return switch (v) {
                case "sudah makan" -> "Alimentado";
                case "belum makan" -> "No alimentado";
                case "sehat" -> "Sano";
                case "sakit" -> "Enfermo";
                case "meninggal" -> "Fallecido";
                default -> val;
            };
        }
        return val;
    }

    private String reverseTrans(String val) {
        if (val == null) return "-";
        String v = val.trim().toLowerCase();
        return switch (v) {
            case "nourri", "alimentado" -> "Sudah makan";
            case "non nourri", "no alimentado" -> "Belum makan";
            case "en bonne santé", "sano" -> "Sehat";
            case "malade", "enfermo" -> "Sakit";
            case "décédé", "fallecido" -> "Meninggal";
            default -> val;
        };
    }

    @Override
    public void onLanguageChanged() {
        String modeKey = currentMode == Mode.TAMBAH ? "dialog.mode.add" : 
                         (currentMode == Mode.EDIT ? "dialog.mode.edit" : "dialog.mode.delete");
        lblTitle.setText(I18nService.get(modeKey) + I18nService.get("dialog.title.suffix"));
        
        updateLabel(1, I18nService.get("dialog.label.name"));
        updateLabel(2, I18nService.get("dialog.label.breed"));
        updateLabel(3, I18nService.get("dialog.label.age"));
        updateLabel(4, I18nService.get("dialog.label.cage"));
        updateLabel(5, I18nService.get("dialog.label.status"));
        updateLabel(6, I18nService.get("dialog.label.condition"));
        
        btnAction.setText(I18nService.get(modeKey));
        
        cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{
            I18nService.get("data.status.fed"), 
            I18nService.get("data.status.notfed")
        }));
        cbKondisi.setModel(new DefaultComboBoxModel<>(new String[]{
            I18nService.get("data.condition.healthy"), 
            I18nService.get("data.condition.sick"), 
            I18nService.get("data.condition.deceased")
        }));
    }

    private void updateLabel(int index, String newText) {
        int labelCounter = 0;
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JLabel && c != lblTitle) {
                labelCounter++;
                if (labelCounter == index + 1) {
                    ((JLabel) c).setText(newText + "  :");
                }
            }
        }
    }

    private void initLayout() {
        setSize(500, 570);
        setLocationRelativeTo(getOwner());
        setLayout(new AbsoluteLayout());

        String modeKey = currentMode == Mode.TAMBAH ? "dialog.mode.add" : 
                         (currentMode == Mode.EDIT ? "dialog.mode.edit" : "dialog.mode.delete");
        lblTitle = new JLabel(I18nService.get(modeKey) + I18nService.get("dialog.title.suffix"), SwingConstants.CENTER);
        add(lblTitle, new AbsoluteConstraints(0, 20, 500, -1));

        addLabelInput(I18nService.get("dialog.label.id"), 80, txtId = new JTextField());
        addLabelInput(I18nService.get("dialog.label.name"), 130, txtNama = new JTextField());
        addLabelCombo(I18nService.get("dialog.label.breed"), 180, cbRas = new JComboBox<>(new String[]{"Persia", "Anggora", "Kampung", "Siam"}));
        addLabelInput(I18nService.get("dialog.label.age"), 230, txtUmur = new JTextField());
        addLabelCombo(I18nService.get("dialog.label.cage"), 280, cbKandang = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "C1"}));
        addLabelCombo(I18nService.get("dialog.label.status"), 330, cbStatus = new JComboBox<>(new String[]{I18nService.get("data.status.fed"), I18nService.get("data.status.notfed")}));
        addLabelCombo(I18nService.get("dialog.label.condition"), 380, cbKondisi = new JComboBox<>(new String[]{I18nService.get("data.condition.healthy"), I18nService.get("data.condition.sick"), I18nService.get("data.condition.deceased")}));

        btnAction = new JButton(I18nService.get(modeKey));
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
