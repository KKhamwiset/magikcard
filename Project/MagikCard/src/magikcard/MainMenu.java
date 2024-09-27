package magikcard;

import javax.swing.*;
import java.awt.*;



public class MainMenu extends BackgroundPanel {
    private BackgroundMusic bgMusic;
    
    public MainMenu(Game game) {
        
        super("..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg");
        bgMusic = new BackgroundMusic();
        playSong();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalGlue());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        JLabel titleLabel = new JLabel();
        Font titleFont = FontLoader.loadFont("..\\Fonts\\2005_iannnnnGMM.ttf", 110f);
        titleLabel.setText("MagikCard");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.setOpaque(false);
        ImageComponent logo = new ImageComponent("..\\Assets\\Image\\Icon\\logo.png", 250, 300);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(150, 0)));
        titlePanel.add(logo);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(345, 0)));

        
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
        bgMusic.playMusic("..\\Music\\Revived Witch - Main Theme ( Soundtrack Music Video Game ).wav");
        bgMusic.setVolume(0.95f);
    }
    protected void stopSong(){
        bgMusic.stopMusic();
    }
    
}
