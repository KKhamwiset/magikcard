package stages;

public class StageData {
    private int rows;
    private int cols;
    private String monsterImagePath;
    private int monsterHP;
    private int monsterATK;
    private int monsterDEF;
    private int monsterREGEN;
    private String backgroundPath;
    private String musicPath;
    
    public StageData(int rows, int cols, String monsterImagePath, int monsterHP, 
                     int monsterATK, int monsterDEF,int monsterRegen, String backgroundPath, String musicPath) {
        this.rows = rows;
        this.cols = cols;
        this.monsterImagePath = monsterImagePath;
        this.monsterHP = monsterHP;
        this.monsterATK = monsterATK;
        this.monsterDEF = monsterDEF;
        this.monsterREGEN = monsterRegen;
        this.backgroundPath = backgroundPath;
        this.musicPath = musicPath;
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public String getMonsterImagePath() { return monsterImagePath; }
    public int getMonsterHP() { return monsterHP; }
    public int getMonsterATK() { return monsterATK; }
    public int getMonsterDEF() { return monsterDEF; }
    public int getMonsterREGEN() {return monsterREGEN;}
    public String getBackgroundPath() { return backgroundPath; }
    public String getMusicPath() { return musicPath; }
}
