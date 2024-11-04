package entities;
import magikcard.ImageComponent;
import javax.swing.*;
import magikcard.GameScreen;

public class Player extends EntitiesDetails implements PlayerAction {
    private ImageComponent playerModel; 
    private JPanel currentPanel;
    private static final String PLAYER_IMAGE = "..\\Assets\\Entities\\player.png";
    
    public Player(String ImagePath, JPanel currentPanel, GameScreen game) {
        this.currentPanel = currentPanel;
        this.MAXHP = 1000;
        this.HP = MAXHP;
        this.DEF = 100;
        this.ATK = 100;
        this.REGEN = 5;
        this.X = 150;
        this.Y = (currentPanel.getHeight() / 2) + 20;
        
        SwingUtilities.invokeLater(() -> {
            createVisual(currentPanel);
        });
        regenHP(game);
    }

    public void createVisual(JPanel panel) {
        if (playerModel != null) {
            panel.remove(playerModel);
        }

        playerModel = new ImageComponent(
            PLAYER_IMAGE,
            125,    // width
            175,    // height
            0,      // Let layout handle x position
            0       // Let layout handle y position
        );
        
        panel.add(playerModel);
        playerModel.setVisible(true);
        panel.revalidate();
        panel.repaint();
        
        System.out.println("Player visual created");
    }
    
    public void recreateVisual(JPanel panel) {
        this.currentPanel = panel;
        if (playerModel != null) {
            currentPanel.remove(playerModel);
        }
        createVisual(panel);
    }
    
    // Rest of the methods remain the same...
    @Override
    public void setHP(int newHP) {
        this.HP = newHP;
        System.out.println("Setting HP to: " + newHP);
    }
    
    @Override
    public void setATK(int newATK) {
        this.ATK = newATK;
        System.out.println("Setting ATK to: " + newATK);
    }
    
    @Override
    public void setDEF(int newDEF) {
        this.DEF = newDEF;
        System.out.println("Setting DEF to: " + newDEF);
    }
    
    @Override
    public void setREGEN(int newREGEN) {
        this.REGEN = newREGEN;
        System.out.println("Setting REGEN to: " + newREGEN);
    }
    
    @Override
    public void Attack(Monster monster) {
        monster.setHP(monster.getHP() - this.ATK); 
        System.out.println("Player is attacking!");
    }
    
    @Override
    public void takingDamage() {
        System.out.println("Player is taking damage!");
    }
    
    @Override
    public void regenHP(GameScreen screen) {
        Timer regenHP = new Timer(5000, e -> {
            int normalRegen = this.REGEN;
            int overflowRegen = this.MAXHP - this.HP;
            int regenValue = normalRegen > overflowRegen ? overflowRegen : normalRegen;
            if (this.HP <= 0 || screen.gameResult != null) {
                ((Timer) e.getSource()).stop();
            }
            else if (this.HP + regenValue <= this.MAXHP) {
                setHP(this.HP + regenValue);
            }
        });
        regenHP.start();
    }
    
    @Override
    public void increaseStats() {
        this.setHP(this.HP + 1000);
        this.setATK(this.ATK + 100);
        this.setDEF(this.DEF + 20);
        this.setREGEN(this.REGEN + 10);
    }
}