package entities;

public abstract class EntitiesDetails {
    protected int HP;
    protected int DEF;
    protected int ATK;
    protected int REGEN;
    protected int X;
    protected int Y;
    public int getHP() { return this.HP; }
    public int getDEF() { return this.DEF; }
    public int getATK() { return this.ATK; }
    public int getREGEN() { return this.REGEN; }
    public abstract void setHP(int newHP);
    public abstract void setATK(int newATK);
    public abstract void setDEF(int newDEF);
    public abstract void setREGEN(int newREGEN);
    public abstract void regenHP();
}
