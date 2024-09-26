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
    private BackgroundPanel statusPanel;
    private BackgroundMusic bgMusic;
    private Player character;
    private Monster enemies;
    private JLabel atkLabel;
    private JLabel hpLabel;
    private JLabel defLabel;
    private JLabel regenLabel;
    private JPanel overlayPanel;
    private JPanel cardPanel;
    private JProgressBar playerHealthBar;
    private JProgressBar monsterHealthBar;
    private JLayeredPane layeredPane;
    public GameState gameResult = null;
    public GameState currentState;
    public GameContext context;
    public int rows = 3;
    public int cols = 6;
    public ArrayList<Card> flippedCards = new ArrayList<>();
    public ArrayList<ImageButton> flippedButtons = new ArrayList<>();

    public GameScreen(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        bgMusic = new BackgroundMusic();


        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 800));
        

        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
        mainFrame.setBounds(0, 0, 1200, 800);
        

        topFrame gameRender = new topFrame();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ImageButton backButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\back.png", e -> {
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
        setupStatusUI(statusPanel, character);
        context = new GameContext(character, enemies, this);
        
        bottomPanel.add(statusPanel);
        bottomPanel.add(gamePlay);
        mainFrame.add(gameRender);
        mainFrame.add(bottomPanel);
        
 
        layeredPane.add(mainFrame, JLayeredPane.DEFAULT_LAYER);
        createOverlayPanel();
        
        Timer HealthBarTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHealthBars();
                if (context.getCurrentMonster().getHP() <= 0 || enemies.getHP() <= 0) {
                    ((Timer) e.getSource()).stop(); 
                }
            }
        });
        HealthBarTimer.start();
        
        Timer StatusPanelTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStatusLabels(character);
                if (context.getCurrentMonster().getHP() <= 0 || enemies.getHP() <= 0) {
                    ((Timer) e.getSource()).stop(); 
                }
            }
        });
        StatusPanelTimer.start();
        
        this.add(layeredPane, BorderLayout.CENTER);
        currentState = GameState.START;
    }

    private void createOverlayPanel() {
        overlayPanel = new JPanel(null);
        overlayPanel.setVisible(false);
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, 1200, 800);

        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setOpaque(true);
        innerPanel.setBackground(new Color(255, 255, 255, 100));
        innerPanel.setBounds(0, 0, 1200, 800);


        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 48));
        messageLabel.setForeground(Color.BLACK);

      
        ImageButton backButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\back.png", e -> {
            game.switchToMainMenu(this);
            resetCardUI();
            stopSong();
        }, 60, 60);
        ImageButton restartButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\restart.png", e -> {
            game.restartGameScreen(this);
            stopSong();
        }, 60, 60);
        
        GridBagConstraints Label = new GridBagConstraints();
        Label.gridx = 0;
        Label.gridy = 0;
        Label.insets = new Insets(0, 0, 20, 0);
        Label.anchor = GridBagConstraints.CENTER;
        Label.gridwidth = 2;
        innerPanel.add(messageLabel, Label);
        
        GridBagConstraints Button1 = new GridBagConstraints();
        Button1.gridx = 0;
        Button1.gridy = 1;
        Button1.anchor = GridBagConstraints.WEST;
        
        GridBagConstraints Button2 = new GridBagConstraints();
        Button2.gridx = 1;
        Button2.gridy = 1;
        Button2.anchor = GridBagConstraints.EAST;

        
        innerPanel.add(backButton, Button1);
        innerPanel.add(restartButton, Button2);
        
        overlayPanel.add(innerPanel);
        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {}
        });

        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);
    }



    public void showOverlay(boolean win) {
  
        JPanel innerPanel = (JPanel) overlayPanel.getComponent(0); 
        JLabel messageLabel = (JLabel) innerPanel.getComponent(0);
        messageLabel.setForeground(Color.WHITE);
        if (win) {
            innerPanel.setBackground(new Color(0, 255, 0, 90)); 
            messageLabel.setText("You Win!");
        } else {
            innerPanel.setBackground(new Color(255, 0, 0, 90));
            messageLabel.setText("You Lose!");
        }

        overlayPanel.setVisible(true);
    }
    public void hideOverlay() {
        overlayPanel.setVisible(false);
    }

    public void drawScreen(JPanel parentPanel) {
        showOverlay(gameResult == GameState.WIN);
    }

    private void setupFightingUI(JPanel topPanel) {
        JPanel fightingPanel = new JPanel();
        fightingPanel.setLayout(new BoxLayout(fightingPanel, BoxLayout.X_AXIS));
        fightingPanel.setOpaque(false);

        character = new Player("..\\Assets\\Entities\\player.png", fightingPanel);
        enemies = new Monster.NormalMonster("..\\Assets\\Entities\\monster.png", fightingPanel);


        playerHealthBar = new JProgressBar(0, character.getHP());
        playerHealthBar.setValue(character.getHP());
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setForeground(Color.RED); 
        playerHealthBar.setBackground(Color.DARK_GRAY); 

        JPanel playerHealthPanel = new JPanel();
        playerHealthPanel.setLayout(new BorderLayout());
        playerHealthPanel.setOpaque(false);
        playerHealthPanel.add(playerHealthBar, BorderLayout.SOUTH);
        topPanel.add(playerHealthPanel, BorderLayout.WEST);

        monsterHealthBar = new JProgressBar(0, enemies.getHP());
        monsterHealthBar.setValue(enemies.getHP());
        monsterHealthBar.setStringPainted(true);
        monsterHealthBar.setForeground(Color.RED);
        monsterHealthBar.setBackground(Color.DARK_GRAY);

        JPanel monsterHealthPanel = new JPanel();
        monsterHealthPanel.setLayout(new BorderLayout());
        monsterHealthPanel.setOpaque(false);
        monsterHealthPanel.add(monsterHealthBar, BorderLayout.SOUTH);
        topPanel.add(monsterHealthPanel, BorderLayout.EAST);

        fightingPanel.add(Box.createHorizontalGlue());
        fightingPanel.add(Box.createHorizontalGlue());

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
        hpLabel.setText("HP: " + (player.getHP() < 0 ? 0 : player.getHP()));
        defLabel.setText("DEF: " + player.getDEF());
        regenLabel.setText("REGEN: " + player.getREGEN());
        repaintstatusPanel();
    }
    public void updateHealthBars() {
        playerHealthBar.setValue(character.getHP());
        monsterHealthBar.setValue(enemies.getHP());
        playerHealthBar.repaint();
        monsterHealthBar.repaint();
    }
    public void repaintstatusPanel(){
        statusPanel.revalidate();
        statusPanel.repaint();
    }
    
    protected void playSong(){
        bgMusic.playMusic("..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav");
        bgMusic.setVolume(0.9f);
    }
    
    protected void stopSong(){
        bgMusic.stopMusic();
    }
    
}
