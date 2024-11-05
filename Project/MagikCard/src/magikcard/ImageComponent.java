package magikcard;

import javax.swing.JPanel;
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
        this(imagePath, null);
    }

    public ImageComponent(String imagePath, JPanel parentPanel) {
        this(imagePath, parentPanel.getWidth(), parentPanel.getHeight());
    }

    public ImageComponent(String imagePath, int width, int height) {
        this(imagePath, width, height, 0, 0);
    }

    public ImageComponent(String imagePath, int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.xCoor = x;
        this.yCoor = y;
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(String imagePath, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics g = scaledImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();

            this.image = scaledImage;
            this.width = width;
            this.height = height;

            this.setPreferredSize(new Dimension(width, height));
            this.revalidate();
            this.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(this.image, this.xCoor, this.yCoor, this.width, this.height, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}
