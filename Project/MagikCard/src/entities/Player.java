package entities;

import magikcard.ImageComponent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import magikcard.GameScreen;

public class Player extends EntitiesDetails implements PlayerAction {

    private ImageComponent playerModel;
    private JPanel currentPanel;
    private JPanel damageIndicator;
    private Timer attackAnimationTimer;
    public boolean isAttacking = false;
    private static final String PLAYER_IMAGE = "..\\Assets\\Entities\\player.png";
    private static final String PLAYER_IMAGE_ATTACK = "..\\Assets\\Entities\\player_attack.png";
    private final int PLAYER_X = 300;
    private final int PLAYER_Y = 50;
    private int baseX = 0;

    public Player(JPanel currentPanel, GameScreen game) {
        this.currentPanel = currentPanel;
        this.MAXHP = 1000;
        this.HP = MAXHP;
        this.DEF = 50;
        this.ATK = 100;
        this.REGEN = 5;
        this.X = PLAYER_X;
        this.Y = PLAYER_Y;
        System.out.println("Player Panel Height : " + currentPanel.getHeight());
        createVisual(currentPanel);
        regenHP(game);
    }

    public void createVisual(JPanel panel) {
        if (playerModel != null) {
            panel.remove(playerModel);
        }

        playerModel = new ImageComponent(
                PLAYER_IMAGE,
                125,
                175,
                0,
                0
        );

        playerModel.setBounds(PLAYER_X, PLAYER_Y, 125, 175);
        playerModel.setVisible(true);
        playerModel.revalidate();
        playerModel.repaint();
        panel.add(playerModel);
        panel.revalidate();
        panel.repaint();

    }

    public void recreateVisual(JPanel panel) {
        if (attackAnimationTimer != null && attackAnimationTimer.isRunning()) {
            attackAnimationTimer.stop();
        }
        createVisual(panel);
    }

    @Override
    public void Attack(Monster monster) {
        if (isAttacking) {
            if (attackAnimationTimer != null) {
                attackAnimationTimer.stop();
            }
            playerModel.setBounds(PLAYER_X, playerModel.getY(), playerModel.getWidth(), playerModel.getHeight());
            playerModel.setImage(PLAYER_IMAGE, 125, 175);
        }

        isAttacking = true;
        int startX = playerModel.getX();
        int targetX = monster.getX() - 100; 

        attackAnimationTimer = new Timer(12, new ActionListener() {
            int steps = 0;
            boolean forward = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (forward) {
                    playerModel.setBounds(
                            startX + (steps * 16),
                            playerModel.getY(),
                            playerModel.getWidth(),
                            playerModel.getHeight()
                    );
                    steps++;
                    if (playerModel.getX() >= targetX) {
                        forward = false;
                        playerModel.setImage(PLAYER_IMAGE_ATTACK, 125, 175);
                        monster.setHP(monster.getHP() - (ATK - ((int) (monster.getDEF() * 0.10))));
                        monster.takingDamage();
                        Timer attackImageTimer = new Timer(200, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playerModel.setImage(PLAYER_IMAGE, 125, 175);
                                ((Timer) e.getSource()).stop();
                            }
                        });
                        attackImageTimer.setRepeats(false);
                        attackImageTimer.start();
                        steps = 0;
                    }
                } else {
                    playerModel.setBounds(
                            startX + ((targetX - startX) - steps * 16),
                            playerModel.getY(),
                            playerModel.getWidth(),
                            playerModel.getHeight()
                    );
                    steps++;

                    if (playerModel.getX() <= startX) {
                        playerModel.setBounds(startX, playerModel.getY(),
                                playerModel.getWidth(), playerModel.getHeight());
                        isAttacking = false;
                        ((Timer) e.getSource()).stop();
                    }
                }

                playerModel.revalidate();
                playerModel.repaint();
                currentPanel.revalidate();
                currentPanel.repaint();
            }
        });
        attackAnimationTimer.start();
    }

    @Override
    public void takingDamage() {
        System.out.println("Player is taking damage!");

        if (damageIndicator == null) {
            damageIndicator = createDamageIndicator();
            playerModel.add(damageIndicator);
        }

        damageIndicator.setBounds(0, 0, playerModel.getWidth(), playerModel.getHeight());
        damageIndicator.setVisible(true);

        playerModel.revalidate();
        playerModel.repaint();
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
    public void regenHP(GameScreen screen) {
        Timer regenHP = new Timer(5000, e -> {
            int normalRegen = this.REGEN;
            int overflowRegen = this.MAXHP - this.HP;
            int regenValue = Math.min(normalRegen, overflowRegen);
            if (this.HP <= 0 || screen.gameResult != null) {
                ((Timer) e.getSource()).stop();
            } else if (this.HP + regenValue <= this.MAXHP) {
                setHP(this.HP + regenValue);
            }
        });
        regenHP.start();
    }

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
    public void increaseStats() {
        int oldMaxHP = this.MAXHP;
        this.MAXHP = this.MAXHP + ((int) (this.MAXHP * 0.15));
        if (oldMaxHP == this.HP) {
            this.setHP(this.MAXHP);
        }
        this.setHP(Math.min((this.HP + (int) (this.MAXHP * 0.07)), this.MAXHP));
        this.setATK(this.ATK + ((int) (this.ATK * 0.10)));
        this.setDEF(this.DEF + ((int) (this.DEF * 0.10)));
        this.setREGEN(Math.max(1, this.REGEN + ((int) (Math.sqrt(this.ATK + this.DEF) * 0.5))));
    }
}
