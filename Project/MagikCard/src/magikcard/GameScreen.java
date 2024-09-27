package magikcard;

import entities.Card;
import entities.Player;
import entities.Monster;
import entities.GameContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    private Timer updateTimer;
    private Font MainFont = FontLoader.loadFont("..\\Fonts\\2005_iannnnnGMM.ttf", 30f);
    private Font OverlayFont = FontLoader.loadFont("..\\Fonts\\ZFTERMIN_.ttf", 64f);
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
    

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 800));
    
        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
    
        BackgroundPanel gameRender = new topFrame();
        gameRender.setLayout(new BoxLayout(gameRender, BoxLayout.Y_AXIS));
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

        mainFrame.setBounds(0, 0, 1200, 800);
        layeredPane.add(mainFrame, JLayeredPane.DEFAULT_LAYER);
        

        createOverlayPanel();
        overlayPanel.setBounds(0, 0, 1200, 800);
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane, BorderLayout.CENTER);
        HPBarCheck();
        currentState = GameState.START;
    }
    
    private void createOverlayPanel() {
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (this.getBackground().getAlpha() > 0) {
                    g.setColor(this.getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        overlayPanel.setBounds(0, 0, 1200, 800);
        overlayPanel.setOpaque(false);
        overlayPanel.setVisible(false);
        overlayPanel.setBackground(new Color(0, 0, 0, 0));
        overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));
    
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
    
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel messageLabel2 = new JLabel("", SwingConstants.CENTER);
        messageLabel2.setAlignmentX(Component.CENTER_ALIGNMENT); 

        labelPanel.add(messageLabel);
        labelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        labelPanel.add(messageLabel2);
        
        labelPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 
    

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
        ImageButton backButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\back.png", e -> {
            game.switchToMainMenu(this);
            resetCardUI();
            stopSong();
        }, 60, 60);
    
        ImageButton restartButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\restart.png", e -> {
            game.restartGameScreen(this);
            stopSong();
        }, 60, 60);
    
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        buttonPanel.add(restartButton);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        overlayPanel.add(Box.createVerticalGlue());
        overlayPanel.add(Box.createVerticalGlue());
        overlayPanel.add(labelPanel);
        overlayPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        overlayPanel.add(buttonPanel);
        overlayPanel.add(Box.createVerticalGlue());
    
        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume(); 
            }
        });
    }
    
        public void showOverlay(boolean win) {
            JPanel outerJLabel = (JPanel) overlayPanel.getComponent(2);
            JLabel messageLabel = (JLabel) outerJLabel.getComponent(0);
            JLabel messageLabel2 = (JLabel) outerJLabel.getComponent(2);
        
    
            messageLabel.setFont(OverlayFont);
            messageLabel.setForeground(Color.WHITE);
            messageLabel2.setFont(OverlayFont);
            messageLabel2.setForeground(Color.WHITE);
        
            if (win) {
                overlayPanel.setBackground(new Color(10, 255, 10, 60));
                messageLabel.setText("You Win!!!!");
                messageLabel2.setText("Congratulations!");
            } else {
                overlayPanel.setBackground(new Color(255, 10, 10, 60));
                messageLabel.setText("You Lose!");
                messageLabel2.setText("Better luck next time!");
            }
        
            overlayPanel.setVisible(true);
            overlayPanel.revalidate();
            overlayPanel.repaint();
        }
    
    public void hideOverlay() {
        overlayPanel.setVisible(false);
    }

    public void drawScreen() {
        showOverlay(gameResult.equals(GameState.WIN));
    }

    private void setupFightingUI(JPanel topPanel) {
        JPanel fightingPanel = new JPanel();
        fightingPanel.setLayout(new BoxLayout(fightingPanel, BoxLayout.X_AXIS));
        fightingPanel.setOpaque(false);
        character = new Player("..\\Assets\\Entities\\player.png", fightingPanel,this);
        enemies = new Monster.NormalMonster("..\\Assets\\Entities\\monster.png", fightingPanel,this);
        topPanel.add(fightingPanel, BorderLayout.CENTER);
        

        JPanel  healthBarPanel = new JPanel();
        healthBarPanel.setOpaque(false);
        healthBarPanel.setLayout(new BoxLayout(healthBarPanel, BoxLayout.X_AXIS));
        playerHealthBar = new JProgressBar(0, character.getHP());
        playerHealthBar.setValue(character.getHP());
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setForeground(Color.RED);
        playerHealthBar.setBackground(Color.DARK_GRAY);
        playerHealthBar.setPreferredSize(new Dimension(0,40));

        JPanel playerHealthPanel = new JPanel();
        playerHealthPanel.setLayout(new BorderLayout());
        playerHealthPanel.setOpaque(false);
        playerHealthPanel.add(playerHealthBar, BorderLayout.SOUTH);
        healthBarPanel.add(playerHealthPanel, BorderLayout.WEST);

        monsterHealthBar = new JProgressBar(0, enemies.getHP());
        monsterHealthBar.setValue(enemies.getHP());
        monsterHealthBar.setStringPainted(true);
        monsterHealthBar.setForeground(Color.RED);
        monsterHealthBar.setBackground(Color.DARK_GRAY);
        monsterHealthBar.setPreferredSize(new Dimension(0,40));

        JPanel monsterHealthPanel = new JPanel();
        monsterHealthPanel.setLayout(new BorderLayout());
        monsterHealthPanel.setOpaque(false);
        monsterHealthPanel.add(monsterHealthBar, BorderLayout.SOUTH);

        healthBarPanel.add(monsterHealthPanel, BorderLayout.EAST);
        topPanel.add(healthBarPanel);
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
        label.setFont(MainFont);
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
        cardPanel.revalidate();
        cardPanel.repaint();
        overlayPanel.revalidate();
        overlayPanel.repaint();
    }
    
    public void HPBarCheck(){
        updateTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    updateHealthBars();
                    updateStatusLabels(character);
                    HPCheck(); 
                    if (enemies.getHP() <= 0 || character.getHP() <= 0) { 
                        ((Timer) e.getSource()).stop();
                        return;
                    }
            }
        });
        updateTimer.start();
    }
    public void HPCheck() {
        Timer hpCheckTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean playerAlive = character.getHP() > 0;
                boolean monsterAlive = enemies.getHP() > 0;

                if (!playerAlive && gameResult != GameState.LOSE) {
                    gameResult = GameState.LOSE;
                    drawScreen();
                    ((Timer) e.getSource()).stop(); 
                } 
                else if (!monsterAlive && gameResult != GameState.WIN) {
                    gameResult = GameState.WIN;
                    drawScreen();
                    ((Timer) e.getSource()).stop(); 
                }
            }
        });
        hpCheckTimer.start();
    }


    public void updateStatusLabels(Player player) {
        atkLabel.setText("ATK: " + player.getATK());
        hpLabel.setText("HP: " + (player.getHP() < 0 ? 0 : player.getHP()));
        defLabel.setText("DEF: " + player.getDEF());
        regenLabel.setText("REGEN: " + player.getREGEN());
    }
    public void updateHealthBars() {
        playerHealthBar.setValue(character.getHP());
        monsterHealthBar.setValue(enemies.getHP());
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
    
    protected void playSong(){
        bgMusic.playMusic("..\\Music\\Revived Witch OST  - 16 -  Seth Tsui - Frog Chevalier.wav");
        bgMusic.setVolume(0.9f);
    }
    
    protected void stopSong(){
        bgMusic.stopMusic();
    }
    
    
}
