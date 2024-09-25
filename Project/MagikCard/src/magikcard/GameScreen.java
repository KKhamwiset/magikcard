package magikcard;

import entities.Card;
import entities.Player;
import entities.Monster;
import entities.GameContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

class topFrame extends BackgroundPanel {

    public topFrame() {
        super("..\\Assets\\Background\\2306.w063.n005.146B.p1.146.jpg");
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1200, 350));
    }
}

class bottomRightFrame extends BackgroundPanel {

    public bottomRightFrame() {
        super("..\\Assets\\Background\\cardBord.png");
        this.setPreferredSize(new Dimension(600, 450));
    }
}

class bottomLeftFrame extends BackgroundPanel {

    public bottomLeftFrame() {
        super("..\\Assets\\Background\\7966815.jpg");
        this.setPreferredSize(new Dimension(600, 450));
    }
}

class bottomFrame extends BackgroundPanel {

    public bottomFrame() {
        super("..\\Assets\\Background\\mainBG.png");
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }
}

public class GameScreen extends JPanel {
    private int matchCount = 0;
    private Game game;
    private GameState currentState;
    private JPanel cardPanel;
    private BackgroundPanel statusPanel;
    private BackgroundMusic bgMusic;
    private Player character;
    private Monster enemies;
    public int rows = 3;
    public int cols = 6;
    public ArrayList<Card> flippedCards = new ArrayList<>();
    public ArrayList<ImageButton> flippedButtons = new ArrayList<>();
    private JLabel atkLabel;
    private JLabel hpLabel;
    private JLabel defLabel;
    private JLabel regenLabel;
    public GameScreen(Game game) {
        this.game = game;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        bgMusic = new BackgroundMusic();
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
        topFrame gameRender = new topFrame();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        ImageButton backButton = new ImageButton("..\\Assets\\Button\\back-removebg-preview.png", e -> {
            game.switchToMainMenu(this);
            resetCardUI();
            stopSong();
        }, 60, 60);
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        gameRender.add(buttonPanel, BorderLayout.NORTH);
        BackgroundPanel bottomPanel = new bottomFrame();
        BackgroundPanel gamePlay = new bottomRightFrame();
        statusPanel = new bottomLeftFrame();
        cardPanel = new JPanel();
        setupFightingUI(gameRender);
        setupGameUI(gamePlay);
        setupStatusUI(statusPanel,character);
        gamePlay.add(Box.createHorizontalGlue());
        bottomPanel.add(statusPanel);
        bottomPanel.add(gamePlay);
        mainFrame.add(gameRender);
        mainFrame.add(bottomPanel);
        this.add(mainFrame);
        currentState = GameState.START;
    }
    private void setupFightingUI(JPanel topPanel) {
       JPanel fightingPanel = new JPanel();
       fightingPanel.setLayout(new BoxLayout(fightingPanel, BoxLayout.X_AXIS));
       character = new Player("..\\Assets\\Entities\\player.png", fightingPanel);
       fightingPanel.add(Box.createHorizontalGlue());
       enemies = new Monster.NormalMonster("..\\Assets\\Entities\\monster.png", fightingPanel);
       fightingPanel.setOpaque(false);
       topPanel.add(fightingPanel);
   }
    private JPanel createPanelWithIconAndLabel(ImageComponent icon, JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.add(icon);
        panel.add(label);
        return panel;
    }
    private void setLabelStyle(JLabel label) {
        label.setFont(new Font("2005_iannnnnGMM", Font.PLAIN, 30));
        label.setForeground(Color.WHITE);
    }
    private void setupStatusUI(JPanel status,Player player) {
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
        int IconWidth = 30;
        int IconHeight = 30;
        ImageComponent ATK = new ImageComponent("..\\Assets\\Image\\Icon\\sword_9742884.png", IconWidth, IconHeight);
        ImageComponent HP = new ImageComponent("..\\Assets\\Image\\Icon\\healthcare_16433864.png", IconWidth, IconHeight);
        ImageComponent DEF = new ImageComponent("..\\Assets\\Image\\Icon\\shield_1825342.png", IconWidth, IconHeight);
        ImageComponent REGEN = new ImageComponent("..\\Assets\\Image\\Icon\\healthcare_9224842.png", IconWidth, IconHeight);
        atkLabel = new JLabel("ATK: " + player.getATK());
        hpLabel = new JLabel("HP: " +  player.getHP());
        defLabel = new JLabel("DEF: " + player.getDEF() );
        regenLabel = new JLabel("REGEN: " + player.getREGEN());
        setLabelStyle(atkLabel);
        setLabelStyle(hpLabel);
        setLabelStyle(defLabel);
        setLabelStyle(regenLabel);
        regenLabel.setForeground(Color.WHITE);
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconPanel.add(createPanelWithIconAndLabel(HP, hpLabel));
        iconPanel.add(createPanelWithIconAndLabel(ATK, atkLabel));
        iconPanel.add(createPanelWithIconAndLabel(DEF, defLabel));
        iconPanel.add(createPanelWithIconAndLabel(REGEN, regenLabel));
        iconPanel.setOpaque(false);
        status.add(Box.createVerticalGlue());
        status.add(iconPanel);
        status.add(Box.createVerticalGlue());
        status.revalidate();
        status.repaint();
    }
    private void setupGameUI(JPanel gamePlay) {
        int cardWidth = 75;
        int cardHeight = 95;
        cardPanel.setLayout(new GridLayout(rows, cols, 10, 10));
        String folderPath = "..\\Assets\\Card";
        Card cards_Set = new Card(folderPath, cardWidth, cardHeight,this);
        cards_Set.shuffleCards();
        ArrayList<Card> shuffledCard = cards_Set.getShuffledCards();
        for (Card card : shuffledCard) {
                String backCardPath = card.getBackCardImagePath(folderPath);
                ImageButton cardIcon = new ImageButton(backCardPath, null, cardWidth, cardHeight);
                GameContext context = new GameContext(character, enemies, this);
                cardIcon.addActionListener(e -> card.handleCardClick(cardIcon, cardWidth, cardHeight, flippedCards, flippedButtons,context));
                cardPanel.add(cardIcon);
            }
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(75, 40, 40, 50));
        if (currentState != GameState.START) {
            gamePlay.setOpaque(false);
            gamePlay.add(Box.createHorizontalGlue());
            gamePlay.add(cardPanel);
        } 
        else {
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }
    public void resetCardUI() {
        matchCount = 0;
        flippedCards.clear();
        flippedButtons.clear();
        cardPanel.removeAll();
        setupGameUI(cardPanel);
    }
    public int getcurrentMatch(){
        return matchCount;
    }
    public void setMatch(int n){
        this.matchCount = n;
    }
    public int maximumCard(){
        return rows * cols;
    }
    public void updateStatusLabels(Player player) {
        atkLabel.setText("ATK: " + player.getATK());
        hpLabel.setText("HP: " + player.getHP());
        defLabel.setText("DEF: " + player.getDEF());
        regenLabel.setText("REGEN: " + player.getREGEN());
    }
    public void repaintstatusPanel(){
        statusPanel.revalidate();
        statusPanel.repaint();
    }
    protected void playSong(){
        bgMusic.playMusic("..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav");
        bgMusic.setVolume(0.825f);
    }
    protected void stopSong(){
        bgMusic.stopMusic();
    }
}
