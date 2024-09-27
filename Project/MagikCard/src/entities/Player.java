package entities;

import magikcard.ImageComponent;
import javax.swing.*;
import magikcard.GameScreen;

public class Player extends EntitiesDetails implements PlayerAction {
    public Player(String ImagePath,JPanel currentPanel,GameScreen game) {
        ImageComponent playerModel = new ImageComponent(ImagePath, 125, 175, 150, (currentPanel.getHeight() / 2) + 20);
        this.MAXHP = 1000;
        this.HP = MAXHP;
        this.DEF = 100;
        this.ATK = 100;
        this.REGEN = 5;
        this.X = 150;
        this.Y = (currentPanel.getHeight() / 2) + 20;
        currentPanel.add(playerModel);
        regenHP(game);
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
        System.out.println("Player is attacking!");
    }

    @Override
    public void takingDamage() {
        System.out.println("Player is taking damage!");
    }
    
    @Override
    public void regenHP(GameScreen screen){
        Timer regenHP = new Timer(5000, e ->{
            int normalRegen = this.REGEN;
            int overflowRegen = this.MAXHP - this.HP;
            int regenValue = normalRegen > overflowRegen ? overflowRegen : normalRegen;
            if (this.HP <= 0 ||  screen.gameResult != null) {
                ((Timer) e.getSource()).stop();
            }
            else if (this.HP + regenValue <= this.MAXHP){
                setHP(this.HP + regenValue);
            }
        });
        regenHP.start();
    }
    
    @Override
    public void increaseStats(){
        this.setHP(this.HP + 1000);
        this.setATK(this.ATK + 100);
        this.setDEF(this.DEF + 20);
        this.setREGEN(this.REGEN + 10);
    }
}
