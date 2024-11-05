package entities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import magikcard.GameScreen;
import magikcard.ImageComponent;

public class Monster extends EntitiesDetails implements MonsterAction {
    private Timer regenTimer;
    private Timer attackAnimationTimer;
    private ImageComponent monsterModel;
    private JPanel currentPanel;
    private JPanel damageIndicator;
    private boolean isAttacking = false;
    private  int ATTACK_DISTANCE = 100;
    private int originalX;
    private final int MONSTER_X = 825;
    private final int MONSTER_Y = 45;
    public Monster(String imagePath, int maxhp, int atk, int def, int regen, JPanel currentPanel, GameScreen game) {
        this.currentPanel = currentPanel;
        this.MAXHP = maxhp;
        this.HP = MAXHP;
        this.ATK = atk;
        this.DEF = def;
        this.REGEN = regen;
        this.X = MONSTER_X; 
        this.Y = MONSTER_Y;
        this.originalX = this.X;
        createVisual(imagePath, currentPanel);
        regenHP(game);
    }

    private void createVisual(String imagePath, JPanel panel) {
        if (monsterModel != null) {
            Container parent = monsterModel.getParent();
            if (parent != null) {
                panel.remove(parent);
            }
        }

        JPanel containerPanel = new JPanel(null);
        containerPanel.setPreferredSize(new Dimension(200, 175));
        containerPanel.setOpaque(false);
        monsterModel = new ImageComponent(
                imagePath,
                200,
                175,
                0,
                0
        );

        monsterModel.setBounds(0, 0, 200, 175);
        monsterModel.setVisible(true);


        containerPanel.add(monsterModel);
        containerPanel.setVisible(true);
        containerPanel.setBounds(X, Y, 200, 175);
        panel.add(containerPanel);
        monsterModel.revalidate();
        monsterModel.repaint();
        containerPanel.revalidate();
        containerPanel.repaint();
        panel.revalidate();
        panel.repaint();

        System.out.println("Monster visual created at: " + X + ", " + Y);
    }

    @Override
    public void Attack(Player player) {
        if (!isAttacking) {
            isAttacking = true;
            Container container = monsterModel.getParent();
            int startX = container.getX();

            attackAnimationTimer = new Timer(16, new ActionListener() {
                int steps = 0;
                boolean forward = true;
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (forward) {
                        container.setLocation(startX - (steps * 5), container.getY());
                        steps++;

                        if (steps >= 10) {
                            forward = false;
                            player.setHP(player.getHP() - ATK);
                            player.takingDamage();
                        }
                    } else {
                        container.setLocation(startX - ((20 - steps) * 5), container.getY());
                        steps++;

                        if (steps >= 20) {
                            container.setLocation(startX, container.getY());
                            isAttacking = false;
                            ((Timer) e.getSource()).stop();
                        }
                    }

                    monsterModel.repaint();
                    container.repaint();
                    currentPanel.revalidate();
                    currentPanel.repaint();
                }
            });

            attackAnimationTimer.start();
            System.out.println("Monster is attacking!");
        }
    }

    public void cleanup() {
        if (monsterModel != null) {
            Container parent = monsterModel.getParent();
            if (parent != null) {
                currentPanel.remove(parent);
                currentPanel.revalidate();
                currentPanel.repaint();
            }
        }
        stopRegeneration();
        if (attackAnimationTimer != null && attackAnimationTimer.isRunning()) {
            attackAnimationTimer.stop();
        }
    }

    @Override
    public void takingDamage() {
        System.out.println("Monster is taking damage!");

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

    private JPanel createDamageIndicator() {
        JPanel indicator = new JPanel();
        indicator.setOpaque(true);
        indicator.setBackground(new Color(255, 0, 0, 100));
        return indicator;
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
