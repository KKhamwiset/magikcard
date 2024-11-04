package entities;

import magikcard.GameScreen;

public class GameContext {
    private Player player;
    private Monster currentMonster;
    private GameScreen currentGame;

    public GameContext(Player player, Monster currentMonster, GameScreen currentGame) {
        this.player = player;
        this.currentMonster = currentMonster;
        this.currentGame = currentGame;
    }
    public void setNewMonster(Monster newMonster){
        this.currentMonster = newMonster;
                
    }
    public Player getPlayer() {
        return player;
    }

    public Monster getCurrentMonster() {
        return currentMonster;
    }

    public GameScreen getCurrentGame() {
        return currentGame;
    }
}
