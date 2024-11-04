package entities;

import javax.swing.JPanel;
import javax.swing.Timer;
import magikcard.GameScreen;
import magikcard.ImageComponent;

public class Monster extends EntitiesDetails implements MonsterAction {
    
    public Monster(String imagePath, int maxhp, int atk, int def, int regen, JPanel currentPanel,GameScreen game) {
        this.MAXHP = maxhp;
        this.HP = MAXHP;   
        this.ATK = atk;
        this.DEF = def;
        this.REGEN = regen; 
        ImageComponent monsterModel = new ImageComponent(imagePath, 200, 175,( currentPanel.getWidth() * 2 ) + 150, (currentPanel.getHeight() / 2) + 20);
        currentPanel.add(monsterModel);
        regenHP(game);  
    }

    @Override
    public void setHP(int newHP) {
        this.HP = newHP;
        System.out.println("Monster HP : " + newHP);
    }

    @Override
    public void setATK(int newATK) {
        this.ATK = newATK;
        System.out.println("Monster ATK : " + newATK);
    }

    @Override
    public void setDEF(int newDEF) {
        this.DEF = newDEF;
        System.out.println("Monster DEF : " + newDEF);
    }

    @Override
    public void setREGEN(int newREGEN) {
        this.REGEN = newREGEN;
        System.out.println("Monster REGEN : " + newREGEN);
    }

    @Override
        public void Attack(Player player) {
            System.out.println("Monster is attacking!");
            player.setHP(player.getHP() - this.ATK); 
        }

    @Override
        public void takingDamage() {
            System.out.println("Monster is taking heavy damage but still standing strong!");
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
