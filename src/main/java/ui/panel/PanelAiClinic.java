package ui.panel;

import ui.style.ThemeManager;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class PanelAiClinic extends JPanel {

    private JTextArea txtChat;
    private JTextField txtInput;
    private JButton btnSend;
    private JScrollPane scrollChat;
    private JLabel lblTitle;

    public PanelAiClinic() {
        initLayout();
        applyTheme();
    }

    // Logic & Actions

    private void sendMessage() {
        String msg = txtInput.getText();
        if (!msg.isEmpty()) {
            txtChat.append("User: " + msg + "\n");
            txtInput.setText("");
            txtChat.append("AI: Sedang memproses pertanyaan Anda...\n\n");
        }
    }

    // UI & Theme

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel("MeowTap AI Clinic");
        add(lblTitle, new AbsoluteConstraints(30, 30, -1, -1));

        txtChat = new JTextArea();
        txtChat.setEditable(false);
        txtChat.setLineWrap(true);
        txtChat.setWrapStyleWord(true);
        
        scrollChat = new JScrollPane(txtChat);
        add(scrollChat, new AbsoluteConstraints(30, 80, 940, 480));

        txtInput = new JTextField();
        txtInput.addActionListener(e -> sendMessage());
        add(txtInput, new AbsoluteConstraints(30, 580, 800, 50));

        btnSend = new JButton("Kirim");
        btnSend.addActionListener(e -> sendMessage());
        add(btnSend, new AbsoluteConstraints(850, 580, 120, 50));
    }

    private void applyTheme() {
        setBackground(ThemeManager.LAVENDER);
        
        lblTitle.setFont(ThemeManager.FONT_WELCOME);
        lblTitle.setForeground(ThemeManager.WHITE);

        txtChat.setBackground(ThemeManager.DARK_BLUE);
        txtChat.setForeground(Color.WHITE);
        txtChat.setFont(ThemeManager.FONT_PLAIN_12);
        txtChat.setMargin(new Insets(15, 15, 15, 15));
        
        scrollChat.setBorder(null);

        txtInput.setBackground(new Color(217, 217, 217));
        txtInput.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        btnSend.setBackground(ThemeManager.NAVY);
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(ThemeManager.FONT_BOLD_14);
        btnSend.setBorderPainted(false);
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
