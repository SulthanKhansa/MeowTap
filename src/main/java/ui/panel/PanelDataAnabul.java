package ui.panel;

import ui.style.ThemeManager;
import ui.dialog.DialogAnabul;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class PanelDataAnabul extends JPanel {

    private JTable table;
    private JScrollPane scrollPane;
    private JTextField txtSearch;
    private JButton btnTambah, btnEdit, btnHapus;

    public PanelDataAnabul() {
        initLayout();
        applyTheme();
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
        Object[][] data = {
            {"1", "Riski", "Anggora", "2 thn", "B2", "Sudah Makan"},
            {"2", "Meow", "Persia", "1 thn", "A1", "Belum Makan"}
        };
        
        table = new JTable(new DefaultTableModel(data, cols));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new AbsoluteConstraints(20, 140, 950, 500));
    }

    private void showDialog(DialogAnabul.Mode mode) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        new DialogAnabul(parent, mode).setVisible(true);
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        
        btnTambah.setBackground(ThemeManager.STAT_GREEN);
        btnTambah.setForeground(Color.WHITE);
        
        btnEdit.setBackground(ThemeManager.STAT_YELLOW);
        btnEdit.setForeground(Color.BLACK);
        
        btnHapus.setBackground(ThemeManager.STAT_RED);
        btnHapus.setForeground(Color.WHITE);
        
        // Styling Buttons
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
