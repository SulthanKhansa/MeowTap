package ui.panel;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class PanelAiClinic extends JPanel {

    private JTextArea chatArea;
    private JTextField txtInput;
    private JButton btnKirim;

    public PanelAiClinic() {
        initLayout();
        applyTheme();
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        JLabel lblTitle = new JLabel("AI Clinic");
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);
        add(lblTitle, new AbsoluteConstraints(20, 20, -1, -1));

        JLabel lblSub = new JLabel("Konsultasi gejala awal anabul menggunakan Gemini AI");
        lblSub.setForeground(ThemeManager.WHITE);
        add(lblSub, new AbsoluteConstraints(20, 55, -1, -1));

        // Chat Bubble Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setText("System AI:\nHalo, Admin Tuko. Saya adalah asisten medis virtual MeowTap...");
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        JScrollPane scroll = new JScrollPane(chatArea);
        add(scroll, new AbsoluteConstraints(20, 100, 950, 450));

        // Input Area Row
        txtInput = new JTextField();
        txtInput.putClientProperty("JTextField.placeholderText", "Ketik gejala atau pertanyaan di sini...");
        add(txtInput, new AbsoluteConstraints(20, 570, 800, 45));

        btnKirim = new JButton("Kirim");
        add(btnKirim, new AbsoluteConstraints(840, 570, 130, 45));
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        
        chatArea.setBackground(new Color(230, 230, 230));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnKirim.setBackground(new Color(200, 27, 121)); // Pink AI Figma
        btnKirim.setForeground(Color.WHITE);
        btnKirim.setFont(ThemeManager.FONT_BOLD_14);
    }
}
