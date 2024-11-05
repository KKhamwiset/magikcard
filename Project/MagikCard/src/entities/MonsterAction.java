package entities;

import magikcard.GameScreen;

public interface MonsterAction {

    void setHP(int newHP);
    void setATK(int newATK);
    void setDEF(int newDEF);
    void setREGEN(int newREGEN);
    void Attack(Player player);
    void takingDamage();
    void regenHP(GameScreen screen);
}
