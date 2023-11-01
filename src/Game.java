import java.util.Scanner;
public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Dice dice;
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the user name of player O: ");
        String playerOName = scanner.nextLine();
        System.out.print("Please enter the user name of player X: ");
        String playerXName = scanner.nextLine();
        player1 = new Player(playerOName);
        player2 = new Player(playerXName);
        dice = new Dice();
        board = new Board();
        Player temp;
        if(dice.rollToStart(player1, player2)){
            temp = player1;
        }
        else{
            temp = player2;
        }

        while (!isGameOver()) {
            board.displayBoard();
            System.out.println(temp.getName() + "'s turn.");
            System.out.println("Enter command:\nRoll\nQuit");
            String command = scanner.nextLine().toUpperCase();
            if (command.equalsIgnoreCase("QUIT")) {
                break;
            } else if (command.equalsIgnoreCase("ROLL")) {
                dice.roll();
                System.out.println("Dices: " + dice.getDice1() + ", " + dice.getDice2());

            } else {

            }
            switchPlayers(temp,player1,player2);
        }

    }

    private boolean isGameOver() {
        if(board.getBoard()[0] == 15 || board.getBoard()[25] == 15){
            return true;
        }
        else{
            return false;
        }
    }

    private void switchPlayers(Player temp, Player player1, Player player2){
        if(temp.getName() != player1.getName()){
            temp = player1;
        }
        else{
            temp = player2;
        }
    }
}
