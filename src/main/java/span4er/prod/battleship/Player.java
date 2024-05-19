package span4er.prod.battleship;

public class Player {
    private String username;
    private BattleShipBoard board;

    public Player(String username, BattleShipBoard board){
        this.username = username;
        this.board = board;
    }

    public String getUsername(){
        return username;
    }
    public BattleShipBoard getBoard(){
        return board;
    }
}
