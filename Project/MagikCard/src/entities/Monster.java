package entities;

import javax.swing.JPanel;
import magikcard.ImageComponent;

public class Monster extends EntitiesDetails implements MonsterAction {
    
    public Monster(String imagePath, int hp, int atk, int def, int regen, JPanel currentPanel) {
        this.HP = hp;   
        this.ATK = atk;
        this.DEF = def;
        this.REGEN = regen; 
        ImageComponent monsterModel = new ImageComponent(imagePath, 200, 175,currentPanel.getWidth() * 2, (currentPanel.getHeight() / 2) + 20);
        currentPanel.add(monsterModel);
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
    
    public void Attack(Player player) {
        System.out.println("Monster is attacking!");
        player.setHP(player.getHP() - this.ATK); 
    }

    public void takingDamage() {
        System.out.println("Monster is taking damage!");
    }

    public static class NormalMonster extends Monster {
        public NormalMonster(String imagePath, JPanel currentPanel) {
            super(imagePath, 100, 20, currentPanel.getWidth() - 100, 5, currentPanel);
        }

        @Override
        public void Attack(Player player) {
            player.setHP(player.getHP() - this.ATK); 
            System.out.println("Normal monster is attacking!");
        }

        @Override
        public void takingDamage() {
            System.out.println("Normal monster is taking damage!");
        }
    }

    public static class BossMonster extends Monster {
        public BossMonster(String imagePath, JPanel currentPanel) {
            super(imagePath, 500, 50, 30, 10, currentPanel);
        }

        @Override
        public void Attack(Player player) {
            System.out.println("Boss monster is attacking!");
            player.setHP(player.getHP() - this.ATK); 
        }

        @Override
        public void takingDamage() {
            System.out.println("Boss monster is taking heavy damage but still standing strong!");
        }
    }
}
