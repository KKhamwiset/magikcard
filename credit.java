import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndCredits extends JPanel {
    private String[] credits = {
        "Directed by: John Doe",
        "Produced by: Jane Smith",
        "Starring: Actor A",
        "Starring: Actor B",
        "Music by: Composer C",
        "Edited by: Editor D",
        "Produced by: Production Company",
        "Special Thanks to: Everyone"
    };

    private int currentY = 600; // Start off-screen
    private Timer timer;

    public EndCredits() {
        // Set up the timer to scroll the text
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentY -= 1; // Scroll up
                if (currentY < -credits.length * 30) {
                    currentY = 600; // Reset to start position
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight()); // Background

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // Draw each credit line
        for (int i = 0; i < credits.length; i++) {
            g.drawString(credits[i], 50, currentY + (i * 30));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("End Credits");
        EndCredits creditsPanel = new EndCredits();
        frame.add(creditsPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
