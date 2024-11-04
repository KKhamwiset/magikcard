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
    private static final String PLAYER_IMAGE = "..\\Assets\\Entities\\player.png";

    public Player(JPanel currentPanel, GameScreen game) {
        this.currentPanel = currentPanel;
        this.MAXHP = 1000;
        this.HP = MAXHP;
        this.DEF = 100;
        this.ATK = 10;
        this.REGEN = 5;
        this.X = 150;
        this.Y = (currentPanel.getHeight() / 2) + 20;

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
        monster.takingDamage();
        System.out.println("Player is attacking!");
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
    public void increaseStats() {
        this.setHP(this.HP + 1000);
        this.setATK(this.ATK + 100);
        this.setDEF(this.DEF + 20);
        this.setREGEN(this.REGEN + 10);
    }
}
