import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CustomFontExample extends JFrame {
    
    public CustomFontExample() {
        try {
            // Load the custom font
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/MyCustomFont.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // Register the font
            ge.registerFont(customFont);
            
            // Use the font in a JLabel
            JLabel label = new JLabel("Hello, Custom Font!");
            label.setFont(customFont);
            add(label);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        setTitle("Custom Font Example");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomFontExample example = new CustomFontExample();
            example.setVisible(true);
        });
    }
}
