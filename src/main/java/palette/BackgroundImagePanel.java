package palette;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundImagePanel extends JPanel {

    private Image backgroundImage;

    public BackgroundImagePanel() {
        setOpaque(false);
    }

    public void setBackground(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            this.backgroundImage = icon.getImage();
        } catch (Exception e) {
            System.err.println("BackgroundImagePanel: Gambar tidak ditemukan - " + imagePath);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
