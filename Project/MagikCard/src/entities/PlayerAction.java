package entities;
import magikcard.GameScreen;

public interface PlayerAction {

    void setHP(int newHP);
    void setATK(int newATK);
    void setDEF(int newDEF);
    void setREGEN(int newREGEN);
    void Attack(Monster monster);
    void takingDamage();
    void regenHP(GameScreen screen);
    void increaseStats();
}
