package magikcard;


import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageButton extends JButton {
    private Card associatedCard;
    public ImageButton(String imagePath, ActionListener listener) {
        try {
            Image image = ImageIO.read(new File(imagePath));
            ImageIcon icon = new ImageIcon(image);
            this.setIcon(icon);
            this.setBorderPainted(false);
            this.setContentAreaFilled(false);
            this.setFocusPainted(false);
            this.addActionListener(listener);
            this.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ImageButton(String imagePath, ActionListener listener, int width, int height) {
        try {
            Image image = ImageIO.read(new File(imagePath));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
        this.setIcon(scaledIcon);
            this.setPreferredSize(new Dimension(width, height));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.addActionListener(listener);
    }
    public void setImage(String imagePath,int width,int height) {
        try {
            Image image = ImageIO.read(new File(imagePath));
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            this.setIcon(scaledIcon);
            this.setPreferredSize(new Dimension(width, height));
            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
