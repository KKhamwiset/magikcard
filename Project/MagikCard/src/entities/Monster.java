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
    private final int MONSTER_X = 725;
    private final int MONSTER_Y = 45;
    private GameScreen game;

    public Monster(String imagePath, int maxhp, int atk, int def, int regen, JPanel currentPanel, GameScreen game) {
        this.currentPanel = currentPanel;
        this.MAXHP = maxhp;
        this.HP = MAXHP;
        this.ATK = atk;
        this.DEF = def;
        this.REGEN = regen;
        this.X = MONSTER_X;
        this.Y = MONSTER_Y;
        this.game = game;
        createVisual(imagePath, currentPanel);
        regenHP(game);
    }

    private void createVisual(String imagePath, JPanel panel) {
        if (monsterModel != null) {
            panel.remove(monsterModel);
        }

        monsterModel = new ImageComponent(
                imagePath,
                200,
                175,
                0,
                0
        );

        monsterModel.setBounds(MONSTER_X, MONSTER_Y, 200, 175);
        monsterModel.setVisible(true);
        monsterModel.revalidate();
        monsterModel.repaint();
        panel.add(monsterModel);
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void Attack(Player player) {
        if (isAttacking) {
            if (attackAnimationTimer != null) {
                attackAnimationTimer.stop();
            }
            monsterModel.setBounds(MONSTER_X, MONSTER_Y, monsterModel.getWidth(), monsterModel.getHeight());
        }

        isAttacking = true;
        int startX = monsterModel.getX();
        int targetX = player.getX() + 100; 

        attackAnimationTimer = new Timer(12, new ActionListener() {
            int steps = 0;
            boolean forward = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (forward) {
                    monsterModel.setBounds(
                            startX - (steps * 16),
                            monsterModel.getY(),
                            monsterModel.getWidth(),
                            monsterModel.getHeight()
                    );
                    steps++;

                    if (monsterModel.getX() <= targetX) {
                        forward = false;
                        player.setHP(player.getHP() - atkDamage(player));
                        player.takingDamage();
                        steps = 0;
                    }
                } else {
                    monsterModel.setBounds(
                            startX - ((startX - targetX) - steps * 16),
                            monsterModel.getY(),
                            monsterModel.getWidth(),
                            monsterModel.getHeight()
                    );
                    steps++;

                    if (monsterModel.getX() >= startX) {
                        monsterModel.setBounds(startX, monsterModel.getY(),
                                monsterModel.getWidth(), monsterModel.getHeight());
                        isAttacking = false;
                        ((Timer) e.getSource()).stop();
                    }
                }

                monsterModel.revalidate();
                monsterModel.repaint();
                currentPanel.revalidate();
                currentPanel.repaint();
            }
        });
        attackAnimationTimer.start();
    }

    public void cleanup() {
        if (monsterModel != null) {
            currentPanel.remove(monsterModel);
            currentPanel.revalidate();
            currentPanel.repaint();
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

    public int atkDamage(Player player) {
        int atkDamage = ATK - ((int) (player.getDEF() * 0.20));
        if (atkDamage < (int) (player.getMAXHP() * 0.02)) {
            atkDamage = (int) (player.getMAXHP() * 0.02);
        }
        return atkDamage;
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

        public NormalMonster(String imagePath, JPanel currentPanel, GameScreen game, int hp, int atk, int def, int regen) {
            super(imagePath, hp, atk, def, regen, currentPanel, game);
        }
    }

    public static class BossMonster extends Monster {

        private Timer continuousDamageTimer;

        public BossMonster(String imagePath, JPanel currentPanel, GameScreen game, int hp, int atk, int def, int regen) {
            super(imagePath, hp, atk, def, regen, currentPanel, game);
            if (game.stageManager.getCurrentStage().isFinalStage()) {
                startContinuousDamage(game);
            }
        }

        private void startContinuousDamage(GameScreen gameScreen) {
            continuousDamageTimer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Player player = gameScreen.context.getPlayer();
                    Monster monster = gameScreen.context.getCurrentMonster();
                    if (player.getHP() > -1) {
                        player.setHP(player.getHP() - ((int) (player.getMAXHP() * 0.045)));
                        player.takingDamage();
                    }
                    if (monster.getHP() <= 0) {
                        ((Timer) e.getSource()).stop();
                    }
                }
            });
            continuousDamageTimer.start();
        }

        @Override
        public void cleanup() {
            super.cleanup();
            if (continuousDamageTimer != null && continuousDamageTimer.isRunning()) {
                continuousDamageTimer.stop();
            }
        }

        @Override
        public int atkDamage(Player player) {
            return ATK;
        }
    }
}
