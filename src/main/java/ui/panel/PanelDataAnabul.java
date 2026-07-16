package ui.panel;

import ui.style.ThemeManager;
import dao.KucingDAO;
import model.Kucing;
import util.I18nService;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

public final class PanelDataAnabul extends JPanel implements I18nService.I18nChangeListener {

    private JPanel cardContainer, pnlForm;
    private JScrollPane scrollPane;
    private JTextField txtSearch, txtId, txtNama, txtUmur, txtGambarPath; 
    private JComboBox<String> cbRas, cbKandang, cbStatus, cbKondisi;
    private JLabel lblTitle, lblLGambar; 
    private JButton btnUpdate, btnSave, btnBrowseGambar; 
    private JLabel lblLID, lblLNama, lblLUmur, lblLRas, lblLKandang, lblLStatus, lblLKondisi;
    private List<Kucing> allKucing;

    public PanelDataAnabul() {
        I18nService.registerListener(this);
        initLayout();
        applyTheme();
        loadData();
    }

    public void loadData() {
        KucingDAO dao = new KucingDAO();
        allKucing = dao.findAll();
        allKucing.sort((k1, k2) -> {
            try {
                return Integer.compare(Integer.parseInt(k1.getIdRfid()), Integer.parseInt(k2.getIdRfid()));
            } catch (NumberFormatException e) {
                return k1.getIdRfid().compareTo(k2.getIdRfid());
            }
        });
        renderCards(allKucing);
    }

    private void renderCards(List<Kucing> list) {
        cardContainer.removeAll();
        for (Kucing k : list) {
            cardContainer.add(createCard(k));
        }
        int remainder = list.size() % 3;
        int fillersNeeded = (remainder == 0) ? 0 : 3 - remainder;
        for (int i = 0; i < fillersNeeded; i++) {
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            cardContainer.add(filler);
        }
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    @Override
    public void onLanguageChanged() {
        lblTitle.setText(I18nService.get("data.title"));
        txtSearch.putClientProperty("JTextField.placeholderText", I18nService.get("data.search"));
        btnSave.setText(I18nService.get("data.btn.refresh"));
        btnUpdate.setText(I18nService.get("data.btn.update"));
        lblLID.setText(I18nService.get("data.form.id"));
        lblLNama.setText(I18nService.get("data.form.name"));
        lblLUmur.setText(I18nService.get("data.form.age"));
        lblLRas.setText(I18nService.get("data.form.breed"));
        lblLKandang.setText(I18nService.get("data.form.cage"));
        lblLStatus.setText(I18nService.get("data.form.status"));
        lblLKondisi.setText(I18nService.get("data.form.condition"));
        lblLGambar.setText(I18nService.get("data.form.photo"));
        btnBrowseGambar.setText(I18nService.get("data.btn.browse"));
        
        cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{
            I18nService.get("data.status.fed"), 
            I18nService.get("data.status.notfed")
        }));
        cbKondisi.setModel(new DefaultComboBoxModel<>(new String[]{
            I18nService.get("data.condition.healthy"), 
            I18nService.get("data.condition.sick"), 
            I18nService.get("data.condition.deceased")
        }));
        
        loadData();
        repaint();
    }

    private String trans(String val) {
        if (val == null) return "-";
        String lang = I18nService.getCurrentLocale().getLanguage();
        String v = val.trim().toLowerCase();
        if (lang.equals("en")) {
            return switch (v) {
                case "sudah makan" -> "Fed";
                case "belum makan" -> "Not Fed";
                case "sehat" -> "Healthy";
                case "sakit" -> "Sick";
                case "meninggal" -> "Deceased";
                case "persia" -> "Persian";
                case "anggora" -> "Anggora";
                case "kampung" -> "Domestic";
                default -> val;
            };
        } else if (lang.equals("es")) {
            return switch (v) {
                case "sudah makan" -> "Alimentado";
                case "belum makan" -> "No alimentado";
                case "sehat" -> "Sano";
                case "sakit" -> "Enfermo";
                case "meninggal" -> "Fallecido";
                case "persia" -> "Persa";
                case "anggora" -> "Angora";
                case "kampung" -> "Doméstico";
                default -> val;
            };
        }
        return val;
    }

    private String reverseTrans(String val) {
        if (val == null) return "-";
        String v = val.trim().toLowerCase();
        return switch (v) {
            case "fed", "alimentado" -> "Sudah makan";
            case "not fed", "no alimentado" -> "Belum makan";
            case "healthy", "sano" -> "Sehat";
            case "sick", "enfermo" -> "Sakit";
            case "deceased", "fallecido" -> "Meninggal";
            default -> val;
        };
    }

    private void fillForm(Kucing k) {
        txtId.setText(k.getIdRfid());
        txtNama.setText(k.getNama());
        txtUmur.setText(String.valueOf(k.getUmur()));
        cbRas.setSelectedItem(k.getRas());
        cbKandang.setSelectedItem(k.getKandang());
        cbStatus.setSelectedItem(trans(k.getStatusMakan()));
        cbKondisi.setSelectedItem(trans(k.getKondisiKesehatan()));
        txtGambarPath.setText(k.getGambarPath()); 
    }

    private void clearForm() {
        txtId.setText("");
        txtNama.setText("");
        txtUmur.setText("");
        txtGambarPath.setText(""); 
        cbRas.setSelectedIndex(0);
        cbKandang.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        cbKondisi.setSelectedIndex(0);
    }

    private void executeAction(boolean isInsert) {
        try {
            Kucing k = new Kucing(
                txtId.getText(), txtNama.getText(), cbRas.getSelectedItem().toString(),
                Integer.parseInt(txtUmur.getText()), cbKandang.getSelectedItem().toString(),
                reverseTrans(cbStatus.getSelectedItem().toString()),
                reverseTrans(cbKondisi.getSelectedItem().toString()),
                txtGambarPath.getText() 
            );
            KucingDAO dao = new KucingDAO();
            if (isInsert) dao.insert(k);
            else dao.update(k);
            clearForm();
            String msg = isInsert ? I18nService.get("data.msg.add.success") : I18nService.get("data.msg.update.success");
            JOptionPane.showMessageDialog(this, msg);
            loadData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, I18nService.get("data.msg.error"));
        }
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());
        lblTitle = new JLabel(I18nService.get("data.title"));
        add(lblTitle, new AbsoluteConstraints(20, 15, -1, -1));
        
        pnlForm = new JPanel(new AbsoluteLayout());
        pnlForm.setBackground(ThemeManager.NAVY);
        add(pnlForm, new AbsoluteConstraints(20, 55, 960, 195));
        
        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", I18nService.get("data.search"));
        txtSearch.setBorder(new EmptyBorder(0, 10, 0, 40));
        pnlForm.add(txtSearch, new AbsoluteConstraints(20, 15, 520, 40));
        
        JLabel lblSearchIcon = new JLabel(FontIcon.of(Feather.SEARCH, 18, Color.GRAY));
        pnlForm.add(lblSearchIcon, new AbsoluteConstraints(505, 25, -1, -1));
        
        btnSave = new JButton(I18nService.get("data.btn.refresh"));
        btnSave.addActionListener(e -> { clearForm(); loadData(); });
        pnlForm.add(btnSave, new AbsoluteConstraints(560, 15, 160, 40));
        
        btnUpdate = new JButton(I18nService.get("data.btn.update"));
        btnUpdate.addActionListener(e -> executeAction(false));
        pnlForm.add(btnUpdate, new AbsoluteConstraints(740, 15, 200, 40));
        
        lblLID = createFormLabel(I18nService.get("data.form.id"));
        pnlForm.add(lblLID, new AbsoluteConstraints(20, 65, -1, -1));
        txtId = new JTextField();
        pnlForm.add(txtId, new AbsoluteConstraints(20, 85, 215, 38));
        
        lblLRas = createFormLabel(I18nService.get("data.form.breed"));
        pnlForm.add(lblLRas, new AbsoluteConstraints(20, 130, -1, -1));
        cbRas = new JComboBox<>(new String[]{"Persia", "Anggora", "Siam", "Kampung", "British"});
        pnlForm.add(cbRas, new AbsoluteConstraints(20, 150, 215, 35));
        
        lblLNama = createFormLabel(I18nService.get("data.form.name"));
        pnlForm.add(lblLNama, new AbsoluteConstraints(255, 65, -1, -1));
        txtNama = new JTextField();
        pnlForm.add(txtNama, new AbsoluteConstraints(255, 85, 215, 38));
        
        lblLKandang = createFormLabel(I18nService.get("data.form.cage"));
        pnlForm.add(lblLKandang, new AbsoluteConstraints(255, 130, -1, -1));
        cbKandang = new JComboBox<>(new String[]{"A1", "A2", "B1", "B2", "B3", "C1"});
        pnlForm.add(cbKandang, new AbsoluteConstraints(255, 150, 215, 35));
        
        lblLUmur = createFormLabel(I18nService.get("data.form.age"));
        pnlForm.add(lblLUmur, new AbsoluteConstraints(490, 65, -1, -1));
        txtUmur = new JTextField();
        pnlForm.add(txtUmur, new AbsoluteConstraints(490, 85, 215, 38));
        
        lblLStatus = createFormLabel(I18nService.get("data.form.status"));
        pnlForm.add(lblLStatus, new AbsoluteConstraints(490, 130, -1, -1));
        cbStatus = new JComboBox<>(new String[]{I18nService.get("data.status.fed"), I18nService.get("data.status.notfed")});
        pnlForm.add(cbStatus, new AbsoluteConstraints(490, 150, 215, 35));
        
        lblLKondisi = createFormLabel(I18nService.get("data.form.condition"));
        pnlForm.add(lblLKondisi, new AbsoluteConstraints(725, 65, -1, -1));
        cbKondisi = new JComboBox<>(new String[]{I18nService.get("data.condition.healthy"), I18nService.get("data.condition.sick"), I18nService.get("data.condition.deceased")});
        pnlForm.add(cbKondisi, new AbsoluteConstraints(725, 85, 215, 35));
        
        lblLGambar = createFormLabel(I18nService.get("data.form.photo"));
        pnlForm.add(lblLGambar, new AbsoluteConstraints(725, 130, -1, -1));
        
        txtGambarPath = new JTextField();
        txtGambarPath.setEditable(false);
        pnlForm.add(txtGambarPath, new AbsoluteConstraints(725, 150, 145, 35));
        
        btnBrowseGambar = new JButton(I18nService.get("data.btn.browse"));
        btnBrowseGambar.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                txtGambarPath.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        pnlForm.add(btnBrowseGambar, new AbsoluteConstraints(875, 150, 65, 35));
        
        cardContainer = new JPanel(new GridLayout(0, 3, 25, 25));
        cardContainer.setOpaque(false);
        cardContainer.setBorder(new EmptyBorder(30, 25, 30, 25));
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardContainer, BorderLayout.NORTH);
        
        scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, new AbsoluteConstraints(0, 250, 1000, 450));
        
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String q = txtSearch.getText().toLowerCase();
                renderCards(allKucing.stream().filter(k -> {
                    String name = k.getNama().toLowerCase();
                    String id = k.getIdRfid().toLowerCase();
                    String breed = k.getRas().toLowerCase();
                    String breedEn = trans(k.getRas()).toLowerCase();
                    String cage = k.getKandang().toLowerCase();
                    String status = k.getStatusMakan().toLowerCase();
                    String statusEn = trans(k.getStatusMakan()).toLowerCase();
                    String cond = k.getKondisiKesehatan().toLowerCase();
                    String condEn = trans(k.getKondisiKesehatan()).toLowerCase();
                    return name.contains(q) || id.contains(q) || breed.contains(q) || breedEn.contains(q) ||
                           cage.contains(q) || status.contains(q) || statusEn.contains(q) ||
                           cond.contains(q) || condEn.contains(q);
                }).collect(Collectors.toList()));
            }
        });
    }

    private String fmt(String val) {
        return (val == null || val.equalsIgnoreCase("null") || val.isEmpty()) ? "-" : val;
    }

    private JPanel createCard(Kucing k) {
        JPanel card = new JPanel(new AbsoluteLayout());
        card.setPreferredSize(new Dimension(300, 240)); 
        card.setBackground(ThemeManager.NAVY);

        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 100), 1));
        
        String imgPath = (k.getGambarPath() != null && !k.getGambarPath().isEmpty()) ? k.getGambarPath() : "";
        try {
            if (!imgPath.isEmpty()) {
                ImageIcon icon = new ImageIcon(imgPath);
                Image scaledImg = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(scaledImg));
            } else {
                lblFoto.setText("No Image");
                lblFoto.setForeground(Color.LIGHT_GRAY);
            }
        } catch (Exception e) {
            lblFoto.setText("Error");
            lblFoto.setForeground(Color.LIGHT_GRAY);
        }
        card.add(lblFoto, new AbsoluteConstraints(15, 20, 80, 100));

        int y = 20;
        String[][] data = {
            {I18nService.get("data.card.id"), fmt(k.getIdRfid())},
            {I18nService.get("data.card.name"), fmt(k.getNama())},
            {I18nService.get("data.card.breed"), trans(k.getRas())},
            {I18nService.get("data.card.age"), k.getUmur() + I18nService.get("data.card.unit.month")},
            {I18nService.get("data.card.cage"), fmt(k.getKandang())}
        };
        
        for (String[] row : data) {
            JLabel lblKey = new JLabel(row[0]);
            lblKey.setForeground(new Color(200, 200, 200));
            lblKey.setFont(new Font("Segoe UI", Font.BOLD, 12));
            card.add(lblKey, new AbsoluteConstraints(110, y, 55, -1));
            
            JLabel lblVal = new JLabel(": " + row[1]);
            lblVal.setForeground(Color.WHITE);
            lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            card.add(lblVal, new AbsoluteConstraints(165, y, 120, -1));
            y += 20;
        }
        
        JButton btnEdit = new JButton(I18nService.get("data.card.btn.edit"));
        JButton btnDelete = new JButton(I18nService.get("data.card.btn.delete"));
        styleCardBtn(btnEdit, ThemeManager.STAT_YELLOW, Color.BLACK);
        styleCardBtn(btnDelete, ThemeManager.STAT_RED, Color.WHITE);
        
        btnEdit.addActionListener(e -> fillForm(k));
        btnDelete.addActionListener(e -> {
            String msg = I18nService.get("data.msg.confirm.delete") + k.getNama() + "?";
            String title = I18nService.get("data.msg.confirm.title");
            int conf = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) { 
                new KucingDAO().delete(k.getIdRfid()); 
                loadData(); 
            }
        });
        
        card.add(btnEdit, new AbsoluteConstraints(25, 190, 125, 32));
        card.add(btnDelete, new AbsoluteConstraints(160, 190, 125, 32));
        return card;
    }

    private JLabel createFormLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return l;
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        styleBtn(btnUpdate, ThemeManager.STAT_RED, Color.WHITE);
        styleBtn(btnSave, ThemeManager.STAT_GREEN, Color.WHITE);
    }

    private void styleBtn(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleCardBtn(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
