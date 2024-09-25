package magikcard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MainMenu extends BackgroundPanel {

    private Game game;
    private BufferedImage backgroundImage;
    private BackgroundMusic bgMusic;
    public MainMenu(Game game) {
        super("..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg");
        this.game = game;
        bgMusic = new BackgroundMusic();
        playSong();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalGlue());
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        JLabel titleLabel = new JLabel();
        titlePanel.add(Box.createHorizontalStrut(150));
        titleLabel.setText("Magikcard");
        titleLabel.setFont(new Font("2005_iannnnnGMM", Font.PLAIN, 100));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setOpaque(false);
        ImageComponent logo = new ImageComponent("..\\Assets\\Image\\Icon\\logo.png", 250, 300);
        titlePanel.add(logo);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(400));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        ImageButton startButton = new ImageButton("..\\Assets\\Button\\ButtonMenu\\play.png", e -> {
            game.switchToGameScreen();
            stopSong();
        });
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        ImageButton exitButton = new ImageButton("..\\Assets\\Button\\ButtonMenu\\exit.png", e -> {
            System.exit(0);
        });
        buttonPanel.add(exitButton);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.add(titlePanel);
        this.add(buttonPanel);
        this.setVerticalSpace(2);
    }
    
    protected void setVerticalSpace(int n) {
        for (int i = 0; i < n; i++) {
            this.add(Box.createVerticalGlue());
        }
    }
    protected void playSong(){
        bgMusic.playMusic("..\\Music\\Revived Witch - Main Theme ( Soundtrack Music Video Game ) - Peaceful Sounds Extended 10 Minutes.wav");
    }
    protected void stopSong(){
        bgMusic.stopMusic();
    }
    
}
