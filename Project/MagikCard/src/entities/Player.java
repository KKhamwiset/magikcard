/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;
import magikcard.ImageComponent;
import javax.swing.*;

/**
 *
 * @author KKhamvised
 */
public class Player extends EntitiesDetails implements PlayerAction {
    public Player(String ImagePath,JPanel currentPanel){
        ImageComponent playerModel = new ImageComponent(ImagePath, 125, 175, 150, (currentPanel.getHeight() / 2) + 20);
        this.HP = 1000;
        this.DEF = 100;
        this.ATK = 100;
        this.REGEN = 5;
        this.X = 150;
        this.Y = (currentPanel.getHeight() / 2) + 20;
        currentPanel.add(playerModel);
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
    public void increaseStats(){
        this.setHP(this.HP + 1000);
        this.setATK(this.ATK + 100);
        this.setDEF(this.DEF + 20);
        this.setREGEN(this.REGEN + 10);
    }
}
