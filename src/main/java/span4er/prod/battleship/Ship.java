package span4er.prod.battleship;

public class Ship {
    private final int countDecks;
    private boolean isDead;
    private boolean isHorizontal;
    private final Cell[] placement;

    public Ship(Cell[] placement){
        this.countDecks = placement.length;
        this.placement = placement;
        this.isDead = false;
        for(Cell cell:placement){
            cell.setShip(this);
        }
        if(placement.length > 1) {
            if (Math.abs(placement[0].getX() - placement[1].getX()) == 1) isHorizontal = false;
            else if (Math.abs(placement[0].getY() - placement[1].getY()) == 1) isHorizontal = true;
        }
        else {
            isHorizontal = true;
        }
    }

    public int getCountDecks(){
        return countDecks;
    }

    public Cell[] getPlacement(){
        return placement;
    }
    public boolean isHorizontal(){
        return isHorizontal;
    }

    public void sortPlacement(){
        if(placement.length > 1) {
            for (int i = 0; i < placement.length; i++) {
                int pos = i;
                Cell min = placement[i];
                for (int j = i + 1; j < placement.length; j++) {
                    if (placement[j].getX() < min.getX() || placement[j].getY() < min.getY()) {
                        pos = j;
                        min = placement[j];
                    }
                }
                placement[pos] = placement[i];
                placement[i] = min;
            }
        }
    }
    public boolean checkShipDestroyed(){
        for(Cell cell:placement){
            if(cell.getCellType().name().equals(CellType.SHIP.name())){
                return false;
            }
        }
        isDead = true;
        for(Cell cell:placement){
            cell.setCellType(CellType.DESTROYED_SHIP);
        }
        return true;
    }
}
