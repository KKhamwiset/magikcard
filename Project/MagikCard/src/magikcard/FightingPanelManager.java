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
    private JPanel healthBarPanel;

    public FightingPanelManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        initializePanels();
    }

    private void initializePanels() {
        fightingPanel = new JPanel(null) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1200, 280);
            }
        };
        fightingPanel.setOpaque(false);
        character = new Player(fightingPanel, gameScreen);
        StageData initialStage = gameScreen.stageManager.getCurrentStage();
        createMonsterForStage(initialStage);
        createAndSetupHealthBars();
    }

    private void createAndSetupHealthBars() {
        healthBarPanel = new JPanel();
        healthBarPanel.setLayout(new BoxLayout(healthBarPanel, BoxLayout.X_AXIS));
        healthBarPanel.setOpaque(false);

        playerHealthBar = new JProgressBar(0, character.getHP());
        playerHealthBar.setValue(character.getHP());
        setupHealthBar(playerHealthBar);

        monsterHealthBar = new JProgressBar(0, enemies.getHP());
        monsterHealthBar.setValue(enemies.getHP());
        setupHealthBar(monsterHealthBar);

        JPanel playerBarContainer = createHealthBarContainer(playerHealthBar);
        JPanel monsterBarContainer = createHealthBarContainer(monsterHealthBar);

        healthBarPanel.add(Box.createHorizontalStrut(50));
        healthBarPanel.add(playerBarContainer);
        healthBarPanel.add(Box.createHorizontalGlue());
        healthBarPanel.add(monsterBarContainer);
        healthBarPanel.add(Box.createHorizontalStrut(50));

        healthBarPanel.setBounds(0, 220, 1200, 40);
        fightingPanel.add(healthBarPanel);
    }

    private void setupHealthBar(JProgressBar healthBar) {
        healthBar.setStringPainted(true);
        healthBar.setString(healthBar.getValue() + "/" + healthBar.getMaximum());
        healthBar.setForeground(new Color(220, 20, 20));
        healthBar.setBackground(new Color(40, 40, 40));
        healthBar.setPreferredSize(new Dimension(400, 30));
        healthBar.setMinimumSize(new Dimension(400, 30));
        healthBar.setMaximumSize(new Dimension(400, 30));
        healthBar.setBorderPainted(true);
        healthBar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        healthBar.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private JPanel createHealthBarContainer(JProgressBar healthBar) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.add(healthBar, BorderLayout.CENTER);
        return container;
    }

    public void updateForNewStage(StageData stageData) {
        if (fightingPanel != null && gameScreen.currentState != null) {
            if (enemies != null) {
                enemies.cleanup();
            }

            createMonsterForStage(stageData);

            monsterHealthBar.setMaximum(enemies.getMAXHP());
            monsterHealthBar.setValue(enemies.getHP());
            updateHealthBars();

            fightingPanel.revalidate();
            fightingPanel.repaint();
        }
    }

    private void createMonsterForStage(StageData stageData) {
        boolean isLastStage = !gameScreen.stageManager.hasNextStage();
        if (isLastStage) {
            enemies = new Monster.BossMonster(
                    stageData.getMonsterImagePath(),
                    fightingPanel,
                    gameScreen,
                    stageData.getMonsterHP(),
                    stageData.getMonsterATK(),
                    stageData.getMonsterDEF(),
                    stageData.getMonsterREGEN()
            );
        } else {
            enemies = new Monster.NormalMonster(
                    stageData.getMonsterImagePath(),
                    fightingPanel,
                    gameScreen,
                    stageData.getMonsterHP(),
                    stageData.getMonsterATK(),
                    stageData.getMonsterDEF(),
                    stageData.getMonsterREGEN()
            );
        }
    }

    public void updateHealthBars() {
        if (playerHealthBar != null && monsterHealthBar != null) {
            int playerHP = Math.max(0, character.getHP());
            playerHealthBar.setMaximum(character.getMAXHP());
            playerHealthBar.setValue(playerHP);
            playerHealthBar.setString(playerHP + "/" + character.getMAXHP());

            int monsterHP = Math.max(0, enemies.getHP());
            monsterHealthBar.setValue(monsterHP);
            monsterHealthBar.setString(monsterHP + "/" + enemies.getMAXHP());

            healthBarPanel.setVisible(true);
            healthBarPanel.revalidate();
            healthBarPanel.repaint();
        }
    }

    public Player getCharacter() {
        return character;
    }

    public Monster getEnemies() {
        return enemies;
    }

    public JPanel getMainContainer() {
        return fightingPanel;
    }

    public JProgressBar getPlayerHealthBar() {
        return playerHealthBar;
    }

    public JProgressBar getMonsterHealthBar() {
        return monsterHealthBar;
    }
}
