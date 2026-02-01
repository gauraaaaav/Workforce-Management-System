package ui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon("C:/Users/asus/Downloads/GauravWFMS_updated/GauravWFMS_updated/GauravWFMS/signup.jpg").getImage();
        } catch (Exception e) {
            System.out.println("Background image not found: " + e.getMessage());
        }
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
