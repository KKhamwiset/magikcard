package magikcard;
import javax.swing.*;
import java.awt.*;
import entities.Player;
import entities.Monster;
import stages.*;

public class FightingPanelManager {
    private JPanel fightingPanel;
    private JProgressBar playerHealthBar;
    private JProgressBar monsterHealthBar;
    private Player character;
    private Monster enemies;
    private GameScreen gameScreen;
    private JPanel mainContainer;
    
    public FightingPanelManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        initializePanels();
    }
    
    private void initializePanels() {
        fightingPanel = new JPanel();
        fightingPanel.setLayout(new BoxLayout(fightingPanel, BoxLayout.X_AXIS));
        fightingPanel.setOpaque(false);
        
        character = new Player("..\\Assets\\Entities\\player.png", fightingPanel, gameScreen);
        enemies = new Monster.NormalMonster(
            "..\\Assets\\Entities\\monster.png", 
            fightingPanel, 
            gameScreen,
            10,  
            10,   
            5,   
            2     
        );
        
        JPanel healthBarPanel = createHealthBarPanel();
        mainContainer = new JPanel();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.add(fightingPanel, BorderLayout.CENTER);
        mainContainer.add(healthBarPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHealthBarPanel() {
        JPanel healthBarPanel = new JPanel();
        healthBarPanel.setOpaque(false);
        healthBarPanel.setLayout(new BoxLayout(healthBarPanel, BoxLayout.X_AXIS));
        
        playerHealthBar = new JProgressBar(0, character.getHP());
        setupHealthBar(playerHealthBar);
        JPanel playerPanel = createHealthBarContainer(playerHealthBar);
        healthBarPanel.add(playerPanel, BorderLayout.WEST);
        
        monsterHealthBar = new JProgressBar(0, enemies.getHP());
        setupHealthBar(monsterHealthBar);
        JPanel monsterPanel = createHealthBarContainer(monsterHealthBar);
        healthBarPanel.add(monsterPanel, BorderLayout.EAST);
        
        return healthBarPanel;
    }
    
    private void setupHealthBar(JProgressBar healthBar) {
        healthBar.setValue(healthBar.getMaximum());
        healthBar.setStringPainted(true);
        healthBar.setForeground(Color.RED);
        healthBar.setBackground(Color.DARK_GRAY);
        healthBar.setPreferredSize(new Dimension(0, 40));
    }
    
    private JPanel createHealthBarContainer(JProgressBar healthBar) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.add(healthBar, BorderLayout.SOUTH);
        return panel;
    }
    
    public void updateForNewStage(StageData stageData) {
        System.out.println("=== Starting Stage Update ===");
        if (fightingPanel != null && gameScreen.currentState != null) {
            if (enemies != null) {
                enemies.stopRegeneration();
            }
            fightingPanel.removeAll();
            character.recreateVisual(fightingPanel);
            System.out.println("Creating new monster with stats:");
            System.out.println("HP: " + stageData.getMonsterHP());
            System.out.println("ATK: " + stageData.getMonsterATK());
            System.out.println("DEF: " + stageData.getMonsterDEF());

            enemies = new Monster.NormalMonster(
                stageData.getMonsterImagePath(),
                fightingPanel,
                gameScreen,
                stageData.getMonsterHP(),
                stageData.getMonsterATK(),
                stageData.getMonsterDEF(),
                stageData.getMonsterREGEN()
            );

            monsterHealthBar.setMaximum(enemies.getHP());
            monsterHealthBar.setValue(enemies.getHP());
            fightingPanel.revalidate();
            fightingPanel.repaint();
        }
        System.out.println("=== Stage Update Complete ===");
    }

    public void updateHealthBars() {
        playerHealthBar.setValue(Math.max(0, character.getHP()));
        monsterHealthBar.setValue(Math.max(0, enemies.getHP()));
    }
    
    public Player getCharacter() { return character; }
    public Monster getEnemies() { return enemies; }
    public JPanel getMainContainer() { return mainContainer; }
    public JProgressBar getPlayerHealthBar() { return playerHealthBar; }
    public JProgressBar getMonsterHealthBar() { return monsterHealthBar; }
}