package span4er.prod.battleship;

import java.util.ArrayList;
import java.util.List;

public class BattleShipBoard {
    private Cell[][] sea = new Cell[10][10];
    private List<Ship> ships;
    private int destroyedShips = 0;

    public BattleShipBoard(Cell[][] sea){
        this.sea = sea;
    }

    public Cell[][] getSea() {
        return sea;
    }

    public BattleShipBoard(){
        ships = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            for(int j = 0;j < 10;j++){
                sea[i][j] = new Cell(CellType.BLANK,i,j);
            }
        }
    }

    public void addShip(Ship ship){
        ships.add(ship);
        for(Cell cell:ship.getPlacement()){
            sea[cell.getX()][cell.getY()] = cell;
        }
    }

    public int getDestroyedShips(){
        return destroyedShips;
    }

    public boolean tryAddShip(Ship ship){
        try{
            checkCountDecks(ship);
            checkCellsEmpty(ship);
            checkShipComplete(ship);
            checkOreol(ship);
            addShip(ship);
        }catch(ShipException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public static void checkCountDecks(Ship ship) throws ShipException{
        if(ship.getCountDecks() > 4 || ship.getCountDecks() < 1) {
            throw new ShipException("Невозможное кол-во палуб у корабля");
        }
    }

    public void checkCellsEmpty(Ship ship) throws ShipException{
        Cell[] cellsForShip = ship.getPlacement();
        for(Cell cell:cellsForShip){
           if(sea[cell.getX()][cell.getY()].isShip())
               throw new ShipException(String.format("Клетка(%d,%d) занята!",cell.getX(),cell.getY()));
        }
    }

    public static void checkShipComplete(Ship ship) throws ShipException{
        ship.sortPlacement();
        Cell[] placement = ship.getPlacement();
        for(int i = 1;i < placement.length;i++){
            if(ship.isHorizontal() && ((placement[i].getY() - placement[i - 1].getY() != 1) || placement[i].getX() != placement[i-1].getX()))
                throw new ShipException("Неверный координаты корабля");
            if(!ship.isHorizontal() && ((placement[i].getX() - placement[i - 1].getX() != 1) || placement[i].getY() != placement[i-1].getY()))
                throw new ShipException("Неверный координаты корабля");
        }
    }

    public void checkOreol(Ship ship) throws ShipException{
        Cell[] placement = ship.getPlacement();
        int firstCellX = placement[0].getX();
        int firstCellY = placement[0].getY();
        int lastCellX = placement[placement.length - 1].getX();
        int lastCellY = placement[placement.length - 1].getY();
        int decrease = 1;

        if(ship.isHorizontal()){
            if(firstCellY == 0) decrease = 0;
            for(int i = firstCellY - decrease;i <= lastCellY + 1 && i <= 9 && i >= 0; i++){
                if(firstCellX == 0){
                    if(sea[firstCellX + 1][i].isShip())
                        throw new ShipException("Ореол корабля касается существующего корабля!");
                }
                else if (firstCellX == 9){
                    if(sea[firstCellX - 1][i].isShip())
                        throw new ShipException("Ореол корабля касается существующего корабля!");
                }
                else {
                    if(sea[firstCellX - 1][i].isShip() || sea[firstCellX + 1][i].isShip())
                        throw new ShipException("Ореол корабля касается существующего корабля!");
                }
            }
            if (firstCellY > 0){
                if(sea[firstCellX][firstCellY - 1].isShip()) throw new ShipException("Ореол корабля касается существующего корабля!");
            }
            if(lastCellY != 9){
                if(sea[lastCellX][lastCellY + 1].isShip()) throw new ShipException("Ореол корабля касается существующего корабля!");
            }
        }else{
            if(firstCellX == 0) decrease = 0;
            for(int i = firstCellX - decrease;i <= lastCellX +1 && i <= 8 && i >= 0; i++){
                if(firstCellY == 0){
                    if(sea[i][firstCellY + 1].isShip())
                        throw new ShipException("Ореол корабля касается существующего корабля!");
                }
                else if (firstCellY == 9){
                    if(sea[i][firstCellY - 1].isShip())
                        throw new ShipException("Ореол корабля касается существующего корабля!");
                }
                else {
                    if(sea[i][firstCellY - 1].isShip() || sea[i][firstCellY + 1].isShip())
                        throw new ShipException("Ореол корабля касается существующего корабля!");
                }
            }
            if (firstCellX > 0){
                if(sea[firstCellX - 1][firstCellY].isShip()) throw new ShipException("Ореол корабля касается существующего корабля!");
            }
            if(lastCellX != 9){
                if(sea[lastCellX + 1][lastCellY].isShip()) throw new ShipException("Ореол корабля касается существующего корабля!");
            }
        }
    }

    public boolean tryFireBoard(int x, int y){
        int hitOutcome = sea[x-1][y-1].hit();
        switch(hitOutcome){
            case -1:{
                System.out.println("Мимо");
                break;
            }
            case 0:{
                System.out.println("Проверьте клетку!!");
                break;
            }
            case 1:{
                System.out.println("Попадание!");
                break;
            }
            case 2:{
                System.out.println("Утопил!");
                destroyedShips++;
                break;
            }
        }
        return hitOutcome >= 0;
    }

    public static void printBoardForFriend(BattleShipBoard board){
        System.out.println("Ваше поле:");
        int j = 1;
        for(Cell[] row: board.sea){
            System.out.printf("%2d",j);
            for(Cell cell:row){
                System.out.print(cell.getCellType().getCode());
            }
            System.out.println();
            j++;
        }
    }
    public static void printBoardForEnemy(BattleShipBoard board) {
        System.out.println("Поле противника:");
        for(Cell[] row: board.sea){
            for(Cell cell:row){
                if(cell.getCellType().name().equals("SHIP"))  System.out.print(CellType.BLANK.getCode());
                else
                   System.out.print(cell.getCellType().getCode());
            }
            System.out.println();
        }
    }

    public static void printBoards(BattleShipBoard ownBoard, BattleShipBoard enemyBoard){
        System.out.println("Поле противника:\t\tВаше поле");
        for(int i = 0;i < 10;i++){
            System.out.printf("%2d",i);
            for(int j = 0;j < 10;j++){
                if(enemyBoard.sea[i][j].getCellType().name().equals("SHIP"))  System.out.print(CellType.BLANK.getCode());
                else
                    System.out.print(enemyBoard.sea[i][j].getCellType().getCode());
            }
            System.out.print("\t");
            for(int j = 0;j < 10;j++){
                System.out.print(ownBoard.sea[i][j].getCellType().getCode());
            }
            System.out.println();
        }
    }
}
