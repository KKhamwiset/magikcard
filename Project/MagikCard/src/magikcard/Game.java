package magikcard;

import javax.swing.*;

public class Game {
    private JFrame frame;
    private MainMenu mainMenu;
    private GameScreen gameScreen;
    public Game() {
        frame = new JFrame("MagikCard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        mainMenu = new MainMenu(this);
        frame.add(mainMenu);
        frame.setVisible(true);
    }

    public void switchToGameScreen() {
        gameScreen = new GameScreen(this);
        frame.remove(mainMenu);
        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
    }
    public void restartGameScreen(JPanel currentScreen){
        gameScreen = new GameScreen(this);
        frame.remove(currentScreen);
        frame.add(gameScreen);
        frame.revalidate();
        frame.repaint();
    }
    public void switchToMainMenu(JPanel currentScreen) {
        frame.remove(currentScreen); 
        frame.add(mainMenu);
        mainMenu.playSong();
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new Game();
    }
}
