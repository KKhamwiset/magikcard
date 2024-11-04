package entities;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import magikcard.GameScreen;
import magikcard.ImageComponent;

public class Monster extends EntitiesDetails implements MonsterAction {
    private Timer regenTimer;
    private ImageComponent monsterModel;
    private JPanel currentPanel;
    private JPanel damageIndicator; 
    public Monster(String imagePath, int maxhp, int atk, int def, int regen, JPanel currentPanelD, GameScreen game) {
        this.currentPanel = currentPanelD;
        this.MAXHP = maxhp;
        this.HP = MAXHP;   
        this.ATK = atk;
        this.DEF = def;
        this.REGEN = regen; 
        createVisual(imagePath, currentPanel);
        regenHP(game);
    }
    
    private void createVisual(String imagePath, JPanel panel) {
        if (monsterModel != null) {
            panel.remove(monsterModel);
        }

        monsterModel = new ImageComponent(imagePath, 200, 175, 0, 0);
        panel.add(monsterModel);
        monsterModel.setVisible(true);
        
        System.out.println("Monster visual created with dimensions:");
        System.out.println("Width: " + monsterModel.getWidth());
        System.out.println("Height: " + monsterModel.getHeight());
        
        panel.revalidate();
        panel.repaint();
    }

    public void cleanup() {
        if (monsterModel != null && currentPanel != null) {
            currentPanel.remove(monsterModel);
            currentPanel.revalidate();
            currentPanel.repaint();
        }
        stopRegeneration();
    }

    @Override
    public void setHP(int newHP) {
        this.HP = newHP;
        System.out.println("Set Monster HP : " + newHP);
    }

    @Override
    public void setATK(int newATK) {
        this.ATK = newATK;
        System.out.println("Set Monster ATK : " + newATK);
    }

    @Override
    public void setDEF(int newDEF) {
        this.DEF = newDEF;
        System.out.println("Set Monster DEF : " + newDEF);
    }

    @Override
    public void setREGEN(int newREGEN) {
        this.REGEN = newREGEN;
        System.out.println("Set Monster REGEN : " + newREGEN);
    }

    @Override
        public void Attack(Player player) {
            System.out.println("Monster is attacking!");
            player.takingDamage();
            player.setHP(player.getHP() - this.ATK); 
        }
    private JPanel createDamageIndicator() {
        JPanel indicator = new JPanel();
        indicator.setOpaque(true);
        indicator.setBackground(new Color(255, 0, 0, 100));
        return indicator;
    }
    @Override
    public void takingDamage() {
        System.out.println("Player is taking damage!");

        if (damageIndicator == null) {
            damageIndicator = createDamageIndicator();
            monsterModel.add(damageIndicator);
        }

        damageIndicator.setBounds(0, 0, monsterModel.getWidth(), monsterModel.getHeight());
        damageIndicator.setVisible(true);

        monsterModel.revalidate();
        monsterModel.repaint();
        Timer damageTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                damageIndicator.setVisible(false);
                ((Timer) e.getSource()).stop();
            }
        });
        damageTimer.setRepeats(false);
        damageTimer.start();
    }
    public void stopRegeneration() {
        if (regenTimer != null && regenTimer.isRunning()) {
            regenTimer.stop();
            System.out.println("Stopped monster regeneration");
        }
    }
    
    @Override
    public void regenHP(GameScreen screen) {
        stopRegeneration();
        
        regenTimer = new Timer(5000, e -> {
            if (this.HP <= 0 || screen.gameResult != null) {
                stopRegeneration();
                return;
            }
            
            int normalRegen = this.REGEN;
            int overflowRegen = this.MAXHP - this.HP;
            int regenValue = normalRegen > overflowRegen ? overflowRegen : normalRegen;
            
            if (this.HP + regenValue <= this.MAXHP) {
                setHP(this.HP + regenValue);
            }
        });
        regenTimer.start();
        System.out.println("Started monster regeneration timer");
    }
    

    public static class NormalMonster extends Monster {
        public NormalMonster(String imagePath, JPanel currentPanel,GameScreen game,int hp,int atk,int def,int regen) {
            super(imagePath, hp, atk, def, regen, currentPanel,game);
        }
    }

    public static class BossMonster extends Monster{
        public BossMonster(String imagePath, JPanel currentPanel,GameScreen game,int hp,int atk,int def,int regen) {
            super(imagePath, hp, atk, def, regen, currentPanel,game);
        }
    }
}
