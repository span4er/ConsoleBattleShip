package span4er.prod.battleship;

public class Cell {
    private int x;
    private int y;
    private CellType cellType;
//    private boolean isFired = false;
//    private boolean isHit = false;
    private boolean isShip;
    Ship ship = null;

    public Cell(CellType cellType){
        this.cellType = cellType;
        isShip = false;
    }

    public Cell(CellType cellType, int x, int y){
        this.cellType = cellType;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isShip() {
        return isShip;
    }

    public void setShip(Ship ship){
        this.ship = ship;
        this.isShip = true;
    }

    public CellType getCellType(){
        return cellType;
    }

    public void setCellType(CellType cellType){
        this.cellType = cellType;
    }

    public void fire(){

    }
    public int hit(){
        if(cellType.name().equals(CellType.HIT_SHIP.name())||
                cellType.name().equals(CellType.HIT_BLANK.name())||
                cellType.name().equals(CellType.DESTROYED_SHIP.name())){
            return 0;
        }
        if(!isShip){
            setCellType(CellType.HIT_BLANK);
            return -1;
        }else{
            setCellType(CellType.HIT_SHIP);
            if(ship.checkShipDestroyed()) return 2;
            return 1;
        }
    }
}
