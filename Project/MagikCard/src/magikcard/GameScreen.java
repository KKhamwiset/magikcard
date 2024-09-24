package magikcard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

class topFrame extends BackgroundPanel {

    public topFrame() {
        super("..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg");
    }
}

class bottomRightFrame extends BackgroundPanel {

    public bottomRightFrame() {
        super("..\\Assets\\Background\\cardBord.png");
    }
}

class bottomLeftFrame extends BackgroundPanel {

    public bottomLeftFrame() {
        super("..\\Assets\\fall-wood-background-design\\7966815.jpg");
    }
}

class bottomFrame extends BackgroundPanel {

    public bottomFrame() {
        super("..\\Assets\\Background\\mainBG.png");
    }
}

public class GameScreen extends JPanel {
    public int matchedPair = 0;
    private Game game;
    private JPanel cardPanel;
    private JPanel statusPanel;
    private BackgroundMusic bgMusic;
    public ArrayList<Card> flippedCards = new ArrayList<>();
    public ArrayList<ImageButton> flippedButtons = new ArrayList<>();
    public GameScreen(Game game) {
        this.game = game;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        bgMusic = new BackgroundMusic();
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
        topFrame gameRender = new topFrame();
        gameRender.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ImageButton backButton = new ImageButton("..\\Assets\\Button\\back-removebg-preview.png", e -> {
            game.switchToMainMenu(this);
            resetGame();
            stopSong();
        }, 60, 60);
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        gameRender.add(buttonPanel, BorderLayout.NORTH);
        BackgroundPanel bottomPanel = new bottomFrame();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        BackgroundPanel gamePlay = new bottomRightFrame();
        gamePlay.setLayout(new BoxLayout(gamePlay, BoxLayout.X_AXIS));
        cardPanel = new JPanel();
        BackgroundPanel statusPanel = new bottomLeftFrame();
        statusPanel.setLayout(new GridLayout(4, 1, 0, 0));
        cardPanel.setOpaque(false);
        gamePlay.setOpaque(false);
        gameRender.setPreferredSize(new Dimension(1200, 350));
        gamePlay.setPreferredSize(new Dimension(600, 450));
        statusPanel.setPreferredSize(new Dimension(600, 450));
        setupGameUI(gamePlay);
        gamePlay.add(Box.createHorizontalGlue());
        gamePlay.add(cardPanel);
        bottomPanel.add(statusPanel);
        bottomPanel.add(gamePlay);
        mainFrame.add(gameRender);
        mainFrame.add(bottomPanel);
        this.add(mainFrame);
    }
 
    private void setupGameUI(JPanel gamePlay) {
        int cardWidth = 70;
        int cardHeight = 95;
        int rows = 3;
        int cols = 6;
        cardPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        String folderPath = "..\\Assets\\card";
        Card cards_Set = new Card(folderPath, cardWidth, cardHeight);
        cards_Set.shuffleCards();
        ArrayList<Card> shuffledCard = cards_Set.getShuffledCards();
        for (Card card : shuffledCard) {
            String backCardPath = card.getBackCardImagePath(folderPath);
            ImageButton cardIcon = new ImageButton(backCardPath, null, cardWidth, cardHeight);
            cardIcon.addActionListener(e -> card.handleCardClick(cardIcon, cardWidth, cardHeight, flippedCards, flippedButtons));
            cardPanel.add(cardIcon);
        }
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(45, 10, 25, 55));
        gamePlay.add(Box.createHorizontalGlue());
        gamePlay.add(cardPanel);
    }
     private void resetGame() {
        matchedPair = 0;
        flippedCards.clear();
        flippedButtons.clear();
        cardPanel.removeAll();
        JPanel gamePlay = (JPanel) cardPanel.getParent();
        setupGameUI(gamePlay);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    protected void playSong(){
        bgMusic.playMusic("..\\Music\\Revived Witch - Battle Dazzling.wav");
        bgMusic.setVolume(0.9f);
    }
    protected void stopSong(){
        bgMusic.stopMusic();
    }
}
