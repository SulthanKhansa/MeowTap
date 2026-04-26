package ui.panel;

import ui.style.ThemeManager;
import ui.dialog.DialogAnabul;
import dao.KucingDAO;
import model.Kucing;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class PanelDataAnabul extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JTextField txtSearch;
    private JButton btnTambah, btnEdit, btnHapus;

    public PanelDataAnabul() {
        initLayout();
        applyTheme();
        loadData(); // Load data saat panel dibuka
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        JLabel lblTitle = new JLabel("Direktori Anabul");
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        add(lblTitle, new AbsoluteConstraints(20, 20, -1, -1));

        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Cari nama atau ID RFID...");
        add(txtSearch, new AbsoluteConstraints(20, 80, 400, 40));

        btnTambah = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        
        btnTambah.addActionListener(e -> showDialog(DialogAnabul.Mode.TAMBAH));
        btnEdit.addActionListener(e -> showDialog(DialogAnabul.Mode.EDIT));
        btnHapus.addActionListener(e -> showDialog(DialogAnabul.Mode.HAPUS));

        add(btnTambah, new AbsoluteConstraints(440, 80, 100, 40));
        add(btnEdit, new AbsoluteConstraints(550, 80, 100, 40));
        add(btnHapus, new AbsoluteConstraints(660, 80, 100, 40));

        // Inisialisasi Tabel
        String[] cols = {"ID RFID", "Nama", "Ras", "Umur", "Kandang", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        add(scrollPane, new AbsoluteConstraints(20, 140, 950, 500));
    }

    public void loadData() {
        tableModel.setRowCount(0); // Kosongkan tabel
        KucingDAO dao = new KucingDAO();
        List<Kucing> list = dao.findAll();
        
        for (Kucing k : list) {
            tableModel.addRow(new Object[]{
                k.getIdRfid(),
                k.getNama(),
                k.getRas(),
                k.getUmur() + " bln",
                k.getKandang(),
                k.getStatusKesehatan()
            });
        }
    }

    private void showDialog(DialogAnabul.Mode mode) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogAnabul dialog = new DialogAnabul(parent, mode);
        
        // Jika mode EDIT, isi data terpilih
        if (mode != DialogAnabul.Mode.TAMBAH) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel terlebih dahulu!");
                return;
            }
            String id = table.getValueAt(row, 0).toString();
            Kucing k = new KucingDAO().findById(id);
            dialog.prepareData(k);
        }
        
        dialog.setVisible(true);
        loadData(); // Refresh tabel setelah dialog ditutup
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        btnTambah.setBackground(ThemeManager.STAT_GREEN);
        btnTambah.setForeground(Color.WHITE);
        btnEdit.setBackground(ThemeManager.STAT_YELLOW);
        btnEdit.setForeground(Color.BLACK);
        btnHapus.setBackground(ThemeManager.STAT_RED);
        btnHapus.setForeground(Color.WHITE);
        styleBtn(btnTambah);
        styleBtn(btnEdit);
        styleBtn(btnHapus);
    }

    private void styleBtn(JButton b) {
        b.setFont(ThemeManager.FONT_BOLD_14);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
