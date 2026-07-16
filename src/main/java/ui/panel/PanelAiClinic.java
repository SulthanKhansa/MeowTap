package ui.panel;

import ui.style.ThemeManager;
import util.I18nService;
import java.awt.*;
import javax.swing.*;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class PanelAiClinic extends JPanel implements I18nService.I18nChangeListener {

    private JTextArea txtChat;
    private JTextField txtInput;
    private JButton btnSend;
    private JScrollPane scrollChat;
    private JLabel lblTitle;

    public PanelAiClinic() {
        I18nService.registerListener(this);
        initLayout();
        applyTheme();
        onLanguageChanged();
    }

    @Override
    public void onLanguageChanged() {
        lblTitle.setText(I18nService.get("ai.title"));
        btnSend.setText(I18nService.get("ai.btn.send"));
        txtInput.putClientProperty("JTextField.placeholderText", I18nService.get("ai.placeholder"));
    }

    private void sendMessage() {
        String msg = txtInput.getText();
        if (!msg.isEmpty()) {
            txtChat.append("User: " + msg + "\n");
            txtInput.setText("");
            txtChat.append("AI:...\n\n");
        }
    }

    private void initLayout() {
        setLayout(new AbsoluteLayout());

        lblTitle = new JLabel(I18nService.get("ai.title"));
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

        btnSend = new JButton(I18nService.get("ai.btn.send"));
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
