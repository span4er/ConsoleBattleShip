package span4er.prod.battleship;

import java.util.Random;
import java.util.Scanner;

public class Game {
    static final int SHIPS_TO_DESTROY = 5;
    static final int SHIPS_TO_PLANT_EACH_BOARD = 5;
    static Scanner input = new Scanner(System.in);
    static boolean isEnd = false;
    static Player playerOne;
    static Player playerTwo;
    static BattleShipBoard playerOneBoard = new BattleShipBoard();
    static BattleShipBoard playerTwoBoard = new BattleShipBoard();

    public static void startGame(){
        Random rand = new Random();
        String firstUsername;
        String secondUsername;
        int first = rand.nextInt(2);
        System.out.println("Начало матча в морской бой");
        System.out.println("Введите имя для игрока 1");
        firstUsername = input.next();
        System.out.println("Введите имя для игрока 2");
        secondUsername = input.next();
        if(first == 0) {
            playerOne = new Player(firstUsername,playerOneBoard);
            playerTwo = new Player(secondUsername,playerTwoBoard);
        }else{
            playerOne = new Player(secondUsername,playerOneBoard);
            playerTwo = new Player(firstUsername,playerTwoBoard);
        }
        System.out.printf("Первым будет ходить игрок %s%nИгрок %s отвернись!%n", playerOne.getUsername(), playerTwo.getUsername());
        buildBoard(playerOneBoard);
        System.out.printf("Тепрь игрок %s расставляет корабли%n", playerTwo.getUsername());
        buildBoard(playerTwoBoard);
        System.out.printf("Начинеает игрок%s%nИгрок %s отвернись!%n", playerOne.getUsername(), playerTwo.getUsername());
        mainLoop();
    }

    public static void buildBoard(BattleShipBoard board){
        int countShips = SHIPS_TO_PLANT_EACH_BOARD;
        while(countShips > 0){
            try {
                BattleShipBoard.printBoardForFriend(board);
                System.out.printf("Осталось %d четырёх палубных, %d трёх палубных, %d двух палубных, %d одно палубных%n",
                        board.getCountFourDeck(), board.getCountThreeDeck(), board.getCountTwoDeck(), board.getCountOneDeck());
                System.out.println("Введите координаты корабля (формат: x,y;x,y;...");
                String[] bufCells = input.next().split(";");
                Cell[] placement = new Cell[bufCells.length];
                for (int i = 0; i < bufCells.length; i++) {
                    int x = Integer.parseInt(bufCells[i].split(",")[0]) - 1;
                    int y = Integer.parseInt(bufCells[i].split(",")[1]) - 1;
                    if(x < 0 || x >9 || y < 0 || y > 9)
                        throw new ShipException("Неверные координаты корабля!");
                    placement[i] = new Cell(CellType.SHIP, x, y);
                }
                if (!board.tryAddShip(new Ship(placement))) {
                    System.out.println("Корабль не добавился !");
                    continue;
                }
                countShips--;
            }
            catch (ShipException e){
                System.out.println(e.getMessage());
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Проверьте координаты корабля");
            }
        }
        BattleShipBoard.printBoardForFriend(board);
        System.out.println("Нажмите Enter для продолжения");
//        input.nextLine();
//        System.out.print("\033с");
//        System.out.flush();
    }

    public static void playerFase(Player playerTerm, Player playerNotTerm){
        boolean isTermEnd = false;
        while(!isTermEnd){
            BattleShipBoard.printBoards(playerTerm.getBoard(), playerNotTerm.getBoard());
            System.out.println("Введите координаты для выстрела...(формат: x,y)");
            String[] bufCells = input.next().split(",");
            if(bufCells.length > 2){
                System.out.println("Неверные координаты выстрела!Введите ещё раз");
            }
            int x = Integer.parseInt(bufCells[0]);
            int y = Integer.parseInt(bufCells[1]);
            isTermEnd = !playerNotTerm.getBoard().tryFireBoard(x,y);
            checkGameEnd();
        }
    }

    public static void checkGameEnd(){
        if(playerOneBoard.getDestroyedShips() == SHIPS_TO_DESTROY){
            System.out.printf("Победил игрок %s, игрок %s удачи в следующий раз",playerTwo.getUsername(),playerOne.getUsername());
            isEnd = true;
            exit();
        }
        else if(playerTwoBoard.getDestroyedShips() == SHIPS_TO_DESTROY){
            System.out.printf("Победил игрок %s, игрок %s удачи в следующий раз",playerOne.getUsername(),playerTwo.getUsername());
            isEnd = true;
            exit();
        }
    }

    public static void mainLoop(){
        while(!isEnd){
            System.out.printf("Ход %s%n",playerOne.getUsername());
            playerFase(playerOne,playerTwo);
            System.out.printf("Ход %s%n",playerTwo.getUsername());
            playerFase(playerTwo,playerOne);
        }
    }

    public static void exit(){
        System.out.println("Игровые поля:");
        System.out.println(playerOne.getUsername());
        BattleShipBoard.printBoardForFriend(playerOneBoard);
        System.out.println(playerTwo.getUsername());
        BattleShipBoard.printBoardForFriend(playerTwoBoard);
        System.exit(0);
    }
}
