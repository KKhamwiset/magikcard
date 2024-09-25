package magikcard;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageComponent extends JComponent {
    private BufferedImage image;
    private int width;
    private int height;
    private int xCoor = 0;
    private int yCoor = 0;
    public ImageComponent(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageComponent(String imagePath, int width, int height) {
        this.width = width;
        this.height = height;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ImageComponent(String imagePath, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.xCoor = x;
        this.yCoor = y;
        try {
            image = ImageIO.read(new File(imagePath));
//            setBounds(x, y, width, height); // Set bounds for the component
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, this.xCoor, this.yCoor, width, height, this);
        }
    }
     @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}
