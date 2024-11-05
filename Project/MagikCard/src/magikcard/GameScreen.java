package magikcard;

import entities.Card;
import entities.Player;
import entities.Monster;
import entities.GameContext;
import stages.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class topFrame extends BackgroundPanel {

    public topFrame(StageData data) {
        super(data.getBackgroundPath());
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1201, 350));
    }

    public void changeBackground(String newImagePath) {
        updateBackground(newImagePath);
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
    public StageManager stageManager;
    private FightingPanelManager fightingManager;
    private BackgroundPanel statusPanel;
    private topFrame gameRender;
    private BackgroundMusic bgMusic;
    private JLabel atkLabel;
    private JLabel hpLabel;
    private JLabel defLabel;
    private JLabel regenLabel;
    private JPanel overlayPanel;
    private JPanel cardPanel;
    private Timer updateTimer;
    private Font MainFont = FontLoader.loadFont("..\\Fonts\\2005_iannnnnGMM.ttf", 30f);
    private Font OverlayFont = FontLoader.loadFont("..\\Fonts\\ZFTERMIN_.ttf", 64f);
    public GameState gameResult = null;
    public GameState currentState;
    public GameContext context;
    public int rows;
    public int cols;
    public ArrayList<Card> flippedCards = new ArrayList<>();
    public ArrayList<ImageButton> flippedButtons = new ArrayList<>();

    public GameScreen(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout());
        this.stageManager = new StageManager();
        this.fightingManager = new FightingPanelManager(this);
        this.bgMusic = new BackgroundMusic();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 800));

        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));

        BackgroundPanel bottomPanel = new bottomFrame();
        BackgroundPanel gamePlay = new bottomRightFrame();
        statusPanel = new bottomLeftFrame();
        cardPanel = new JPanel();

        setupGameUI(gamePlay);
        setupStatusUI(statusPanel);
        initializeStage(stageManager.getCurrentStage());
        initializeTopSection();
        context = new GameContext(fightingManager.getCharacter(), fightingManager.getEnemies(), this);

        bottomPanel.add(statusPanel);
        bottomPanel.add(gamePlay);
        mainFrame.add(gameRender);
        mainFrame.add(bottomPanel);

        mainFrame.setBounds(0, 0, 1200, 800);

        createOverlayPanel();
        overlayPanel.setBounds(0, 0, 1200, 800);
        layeredPane.add(mainFrame, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);
        this.add(layeredPane, BorderLayout.CENTER);

        currentState = GameState.START;
        HPBarCheck();
    }

    private void initializeTopSection() {
        gameRender = new topFrame(stageManager.getCurrentStage());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        ImageButton backButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\back.png",
                e -> {
                    bgMusic.stopMusic();
                    resetCardUI();
                    game.switchToMainMenu(this);
                }, 60, 60);
        buttonPanel.add(backButton);
        buttonPanel.setBounds(10, 10, 100, 70);
        gameRender.add(buttonPanel);
        JPanel fightingContainer = fightingManager.getMainContainer();
        fightingContainer.setBounds(
                0,
                70,
                1200,
                280
        );
        gameRender.add(fightingContainer);
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
            bgMusic.stopMusic();
            resetCardUI();
            game.switchToMainMenu(this);
        }, 60, 60);

        ImageButton restartButton = new ImageButton("..\\Assets\\Button\\ButtonOverLay\\restart.png", e -> {
            bgMusic.stopMusic();
            game.restartGameScreen(this);
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

    private void initializeStage(StageData stageData) {
        this.rows = stageData.getRows();
        this.cols = stageData.getCols();
        fightingManager.updateForNewStage(stageData);
        bgMusic.stopMusic();
        bgMusic.playMusic(stageData.getMusicPath());
    }

    private void setupGameUI(JPanel gamePlay) {
        int cardWidth = 75;
        int cardHeight = 95;
        this.rows = 3;
        int availableWidth = gamePlay.getWidth() - 80;
        cols = Math.max(4, availableWidth / (cardWidth + 10));

        cardPanel.setLayout(new GridLayout(rows, cols, 10, 10));
        String folderPath = "..\\Assets\\Card";
        Card cards_Set = new Card(folderPath, cardWidth, cardHeight, this);
        cards_Set.shuffleCards();

        for (Card card : cards_Set.getShuffledCards()) {
            String backCardPath = card.getBackCardImagePath(folderPath);
            ImageButton cardIcon = new ImageButton(backCardPath, null, cardWidth, cardHeight);
            cardIcon.addActionListener(e -> card.handleCardClick(cardIcon, cardWidth, cardHeight,
                    flippedCards, flippedButtons, context));
            cardPanel.add(cardIcon);
        }

        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(75, 40, 40, 50));

        if (currentState != GameState.START) {
            gamePlay.setOpaque(false);
            gamePlay.add(Box.createHorizontalGlue());
            gamePlay.add(cardPanel);
        } else {
            cardPanel.revalidate();
            cardPanel.repaint();
        }
    }

    private void setupStatusUI(JPanel status) {
        Player player = fightingManager.getCharacter();
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));

        int IconWidth = 30;
        int IconHeight = 30;
        ImageComponent ATK = new ImageComponent("..\\Assets\\Image\\Icon\\sword_9742884.png", IconWidth, IconHeight);
        ImageComponent HP = new ImageComponent("..\\Assets\\Image\\Icon\\healthcare_16433864.png", IconWidth, IconHeight);
        ImageComponent DEF = new ImageComponent("..\\Assets\\Image\\Icon\\shield_1825342.png", IconWidth, IconHeight);
        ImageComponent REGEN = new ImageComponent("..\\Assets\\Image\\Icon\\healthcare_9224842.png", IconWidth, IconHeight);

        atkLabel = new JLabel("ATK: " + player.getATK());
        hpLabel = new JLabel("HP: " + player.getHP());
        defLabel = new JLabel("DEF: " + player.getDEF());
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

    public void resetCardUI() {
        matchCount = 0;
        flippedCards.clear();
        flippedButtons.clear();
        cardPanel.removeAll();

        StageData currentStage = stageManager.getCurrentStage();
        this.rows = currentStage.getRows();
        this.cols = currentStage.getCols();

        setupGameUI(cardPanel);
        cardPanel.revalidate();
        cardPanel.repaint();
        overlayPanel.revalidate();
        overlayPanel.repaint();
    }

    public void HPBarCheck() {
        updateTimer = new Timer(100, e -> {
            fightingManager.updateHealthBars();
            updateStatusLabels();
            checkGameState();

            if (gameResult != null) {
                ((Timer) e.getSource()).stop();
            }
        });
        updateTimer.start();
    }

    public void checkGameState() {
        Player player = fightingManager.getCharacter();
        Monster monster = fightingManager.getEnemies();

        boolean playerAlive = player.getHP() > 0;
        boolean monsterAlive = monster.getHP() > 0;

        if (!playerAlive && gameResult != GameState.LOSE) {
            System.out.println("PLAYER DIED - GAME OVER");
            monster.stopRegeneration();
            gameResult = GameState.LOSE;
            drawScreen();
        } else if (!monsterAlive) {
            monster.stopRegeneration();
            if (stageManager.hasNextStage()) {
                System.out.println("MONSTER DIED - SHOWING DEATH ANIMATION");
                Timer deathTimer = new Timer(50, null);
                deathTimer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int currentHP = monster.getHP();
                        if (currentHP > -20) { 
                            monster.setHP(currentHP - 2);
                            fightingManager.updateHealthBars();
                        } else {
                            ((Timer) e.getSource()).stop();
                            StageData nextStage = stageManager.moveToNextStage();
                            setMatch(0);
                            gameRender.changeBackground(nextStage.getBackgroundPath());
                            initializeStage(nextStage);
                            cardPanel.removeAll();
                            setupGameUI(cardPanel);
                            cardPanel.revalidate();
                            cardPanel.repaint();

                            Monster newMonster = fightingManager.getEnemies();
                            context.setNewMonster(newMonster);
                        }
                    }
                });
                deathTimer.start();
            } else {
                System.out.println("ALL STAGES COMPLETE - VICTORY");
                gameResult = GameState.WIN;
                drawScreen();
            }
        }
    }

    private void updateStatusLabels() {
        Player player = fightingManager.getCharacter();
        atkLabel.setText("ATK: " + player.getATK());
        hpLabel.setText("HP: " + Math.max(0, player.getHP()));
        defLabel.setText("DEF: " + player.getDEF());
        regenLabel.setText("REGEN: " + player.getREGEN());
    }

    public int getcurrentMatch() {
        return matchCount;
    }

    public void setMatch(int n) {
        this.matchCount = n;
    }

    public int maximumCard() {
        StageData currentStage = stageManager.getCurrentStage();
        return currentStage.getRows() * currentStage.getCols();
    }

}
