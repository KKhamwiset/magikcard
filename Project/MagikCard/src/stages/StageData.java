package stages;

public class StageData {
    private final int rows;
    private final int cols;
    private final String monsterImagePath;
    private final int monsterHP;
    private final int monsterATK;
    private final int monsterDEF;
    private final String backgroundPath;
    private final String musicPath;
    
    public StageData(int rows, int cols, String monsterImagePath, int monsterHP, 
                     int monsterATK, int monsterDEF, String backgroundPath, String musicPath) {
        this.rows = rows;
        this.cols = cols;
        this.monsterImagePath = monsterImagePath;
        this.monsterHP = monsterHP;
        this.monsterATK = monsterATK;
        this.monsterDEF = monsterDEF;
        this.backgroundPath = backgroundPath;
        this.musicPath = musicPath;
    }
    
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public String getMonsterImagePath() { return monsterImagePath; }
    public int getMonsterHP() { return monsterHP; }
    public int getMonsterATK() { return monsterATK; }
    public int getMonsterDEF() { return monsterDEF; }
    public String getBackgroundPath() { return backgroundPath; }
    public String getMusicPath() { return musicPath; }
}
