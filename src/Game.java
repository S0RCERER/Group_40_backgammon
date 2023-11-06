import java.util.*;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player temp;
    private Dice dice;
    private List<Direction> moves = new ArrayList<>();
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the user name of player o: ");
        String playerOName = scanner.nextLine();
        System.out.print("Please enter the user name of player x: ");
        String playerXName = scanner.nextLine();
        player1 = new Player(playerOName, "o");
        player2 = new Player(playerXName, "x");
        dice = new Dice();
        board = new Board();
        if(dice.rollToStart(player1, player2)){
            temp = player1;
        }
        else{
            temp = player2;
        }
        board.displayBoard();

        while (!isGameOver()) {
            System.out.println(temp.getName() + "'s turn.");
            while(true){
                System.out.print("Enter command:");
                String command = scanner.nextLine().toUpperCase();
                if (command.equalsIgnoreCase("QUIT")) {
                    break;
                } else if (command.equalsIgnoreCase("ROLL")) {
                    dice.roll();
                    System.out.println("Dices: " + dice.getDice1() + ", " + dice.getDice2());
                } else if (command.equalsIgnoreCase("HINT")){
                    displayCommands();
                } else if (command.equalsIgnoreCase("MOVE")){
                    int index;
                    while(true){
                        displayMoves(temp, dice, board);
                        String choice = scanner.nextLine().toUpperCase();
                        if(choice.length() != 1) {
                            System.out.println("Invalid command");
                        }
                        else{
                            index = (int)choice.charAt(0) - 65;
                            if(index < moves.size()){
                                break;
                            }
                            else{
                                System.out.println("Invalid command");
                            }
                        }
                    }
                    Direction move = moves.get(index);
                    //System.out.println(move.getStart() + "-" +move.getDestination());
                    movePiece(board, move.getStart(), move.getDestination());
                    board.displayBoard();
                    moves.clear();
                    break;
                } else{
                    System.out.println("Invalid command.");
                }
            }
            if(temp.getName() != player1.getName()){
                temp = player1;
            }
            else{
                temp = player2;
            }
        }
    }

    private boolean isGameOver() {
        if(board.getBoard(0) == 15 || board.getBoard(25) == 15){
            return true;
        }
        else{
            return false;
        }
    }

    private void displayCommands(){
        System.out.println("All commands:\n1. Roll\n2. Quit\n3. Hint");
    }

    private void displayMoves(Player player, Dice dice, Board board){
        System.out.println(player.getPiece() + " to play " + dice.getDice1() + "-" + dice.getDice2() + ". Select from:");
        int[] dices;
        int dice1 = dice.getDice1();
        int dice2 = dice.getDice2();
        if (dice1 == dice2) {
            dices = new int[1];
            dices[0] = dice1;
        } else {
            dices = new int[2];
            dices[0] = dice1;
            dices[1] = dice2;
        }
        int count = 0;
        if (player.getPiece() == "x"){
            for (int i = 0; i < dices.length; i++){
                for (int j = 0; j < 26; j++){
                    if (board.getBoard(j) < 0 && j + dices[i] < 26 ){
                        if (board.getBoard(j + dices[i]) < 2){
                            System.out.println((char)(65 + count) + ") Play " + j + "-" + (j + dices[i]));
                            moves.add(new Direction(j, j + dices[i]));
                            count++;
                        }
                    }
                }
            }
        }
        else{
            for (int i = 0; i < dices.length; i++){
                for (int j = 25; j >= 0; j--){
                    if (board.getBoard(j) > 0 && j - dices[i]  >= 0 ){
                        if (board.getBoard(j - dices[i]) > -2){
                            System.out.println((char)(65 + count) + ") Play " + j + "-" + (j - dices[i]));
                            moves.add(new Direction(j, j - dices[i]));
                            count++;
                        }
                    }
                }
            }
        }
        System.out.print(">>");
//
//        for (Direction move : moves) {
//            System.out.println("Start: " + move.getStart() + ", Destination: " + move.getDestination());
//        }
//
    }

    public void movePiece(Board board, int start, int destination){
        if (board.getBoard(start) < 0){
            board.setBoard(start,board.getBoard(start) + 1);
            board.setBoard(destination, board.getBoard(destination) - 1);
        }
        else{
            board.setBoard(start,board.getBoard(start) - 1);
            board.setBoard(destination, board.getBoard(destination) + 1);
        }
    }
}
