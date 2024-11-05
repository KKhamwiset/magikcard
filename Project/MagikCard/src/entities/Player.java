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
    private boolean isAttacking = false;
    private static final String PLAYER_IMAGE = "..\\Assets\\Entities\\player.png";
    private static final String PLAYER_IMAGE_ATTACK = "..\\Assets\\Entities\\player_attack.png";
    private static final int ATTACK_DISTANCE = 70;
    private final int PLAYER_X = 150;
    private final int PLAYER_Y = 50;
    private int baseX = 0;

    public Player(JPanel currentPanel, GameScreen game) {
        this.currentPanel = currentPanel;
        this.MAXHP = 1000;
        this.HP = MAXHP;
        this.DEF = 100;
        this.ATK = 50;
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

        JPanel containerPanel = new JPanel(null);
        containerPanel.setPreferredSize(new Dimension(125, 175));
        containerPanel.setMinimumSize(new Dimension(125, 175));
        containerPanel.setMaximumSize(new Dimension(125, 175));
        containerPanel.setSize(125, 175);
        containerPanel.setOpaque(true);


        playerModel = new ImageComponent(
                PLAYER_IMAGE,
                125,
                175,
                0,
                0
        );

        playerModel.setBounds(0, 0, 125, 175);
        playerModel.setVisible(true);

        containerPanel.add(playerModel);
        containerPanel.setVisible(true);
        containerPanel.setOpaque(false);
        panel.add(containerPanel);

        containerPanel.setBounds(X, Y, 125, 175);

        playerModel.revalidate();
        playerModel.repaint();
        containerPanel.revalidate();
        containerPanel.repaint();
        panel.revalidate();
        panel.repaint();

        baseX = containerPanel.getX();

    }

    public void recreateVisual(JPanel panel) {
        if (attackAnimationTimer != null && attackAnimationTimer.isRunning()) {
            attackAnimationTimer.stop();
        }
        this.currentPanel = panel;
        if (playerModel != null) {
            Container parent = playerModel.getParent();
            if (parent != null) {
                currentPanel.remove(parent);
            }
        }
        createVisual(panel);
    }

@Override
public void Attack(Monster monster) {
    if (!isAttacking) {
        isAttacking = true;
        Container container = playerModel.getParent();
        Rectangle originalBounds = container.getBounds();
        int startX = originalBounds.x;
        attackAnimationTimer = new Timer(16, new ActionListener() {
            int steps = 0;
            boolean forward = true;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (forward) {
                    container.setBounds(
                            startX + (steps * 5),
                            originalBounds.y,
                            originalBounds.width,
                            originalBounds.height
                    );
                    steps++;
                    if (steps >= 20) {
                        forward = false;
                        playerModel.setImage(PLAYER_IMAGE_ATTACK, 125, 175);
                        monster.setHP(monster.getHP() - ATK);
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
                    container.setBounds(
                            startX + ((20 - steps) * 5),
                            originalBounds.y,
                            originalBounds.width,
                            originalBounds.height
                    );
                    steps++;

                    if (steps >= 20) {
                        container.setBounds(originalBounds);
                        isAttacking = false;
                        ((Timer) e.getSource()).stop();
                    }
                }
                container.setPreferredSize(new Dimension(125, 175));
                container.revalidate();
                container.repaint();
                currentPanel.revalidate();
                currentPanel.repaint();

                System.out.println("Container position: " + container.getX() + ", " + container.getY());
            }
        });

        attackAnimationTimer.start();
    }
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
        this.setHP(this.HP + 1000);
        this.setATK(this.ATK + 100);
        this.setDEF(this.DEF + 20);
        this.setREGEN(this.REGEN + 10);
    }
}
