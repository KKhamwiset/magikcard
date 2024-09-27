package entities;

import javax.swing.*;
import java.awt.*;

public class HealthBar extends JComponent {
    private int maxHP;
    private int currentHP;

    public HealthBar(int maxHP) {
        this.maxHP = maxHP;
        this.currentHP = maxHP; 
        setPreferredSize(new Dimension(400, 30)); 
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        double healthPercentage = (double) currentHP / maxHP;
        double playerHealthPercentage = Math.max(healthPercentage, 0);
        double enemyHealthPercentage = Math.max(1 - healthPercentage, 0); 

        g.setColor(Color.GREEN);
        int playerHealthWidth = (int) (width * playerHealthPercentage);
        g.fillRect(0, 0, playerHealthWidth, height);

        g.setColor(Color.RED);
        int enemyHealthWidth = (int) (width * enemyHealthPercentage);
        g.fillRect(width - enemyHealthWidth, 0, enemyHealthWidth, height);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height);

        String playerPercentageText = String.format("%.2f%%", playerHealthPercentage * 100);
        g.setColor(Color.WHITE);
        g.drawString(playerPercentageText, 5, height / 2 + 5);

        String enemyPercentageText = String.format("%.2f%%", enemyHealthPercentage * 100);
        g.drawString(enemyPercentageText, width - enemyPercentageText.length() * 7 - 5, height / 2 + 5); // Position the text
    }
}
