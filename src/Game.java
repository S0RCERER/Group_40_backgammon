import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The {@code Game} class represents a backgammon game session. It manages the game's state,
 * including the board layout, player turns, and the doubling cube mechanics. 
 * This class handles the game flow, including starting and stopping the game, managing player moves,
 * handling dice rolls, and keeping track of the game duration and scores. 
 * It offers methods to offer and accept doubling stakes, validate moves, and determine the game outcome.
 */
public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player temp;
    private Dice dice;
    private List<Direction> moves = new ArrayList<>();
    private long gameStartTime;
    private long gameDuration;

    private Test test;

    private Thread gameThread;

    private int doublingValue;
    private Player doublingOwner;


    private int winningScore;
    private volatile boolean gameRunning; // control the game running in the thread
    private Thread timeCheckThread; // check the time in the thread

    // private Scanner scanner;

    public Game() {
        // Existing initialization code...
        doublingValue = 1; // Initialize doubling cube value
        doublingOwner = null; // Initially, no one owns the doubling cube
    }

    
    /**
 * Offers to double the stakes of the game. This method increases the doubling value 
 * if the specified player is allowed to double. It sets the doubling cube's owner to the 
 * player who made the offer. If the player is not allowed to double, a message is displayed.
 *
 * @param player The player attempting to double the stakes.
 */
    public void offerDouble(Player player) {
        if (canPlayerDouble(player)) {
            doublingValue *= 2;
            doublingOwner = player;
            System.out.println(player.getName() + " has offered to double. The stake is now: " + doublingValue);
            // Further logic to allow the other player to accept or refuse
        } else {
            System.out.println(player.getName() + " cannot double at this time.");
        }
    }

    // Check if a player can double
    /**
 * Determines if the specified player is allowed to double the stakes at the current game state.
 * This method checks the game rules and conditions under which a player can offer to double the stakes.
 *
 * @param player The player whose eligibility to double is being checked.
 * @return {@code true} if the player can double, {@code false} otherwise.
 */
    private boolean canPlayerDouble(Player player) {

        return true;

    }

    // Display the doubling cube
    public void displayDoublingCube() {
        if (doublingOwner != null) {
            System.out.println("Doubling cube: " + doublingValue + " (Owned by " + doublingOwner.getName() + ")");
        } else {
            System.out.println("Doubling cube is neutral: " + doublingValue);
        }
    }

    /**
 * Initializes and starts a new game. This method sets up the players, the board, and the dice.
 * It prompts the user for game settings such as player names and match duration. 
 * The game starts with the roll to determine which player goes first.
 */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        gameRunning = true; 

        System.out.print("Please enter the user name of player o: ");
        String playerOName = scanner.nextLine();
        System.out.print("Please enter the user name of player x: ");
        String playerXName = scanner.nextLine();
        String select;

        while (true) {

            System.out.print("Enter the socre to win: ");

            winningScore = scanner.nextInt();
            scanner.nextLine();
            if (winningScore < 5) {
                System.out.println("Invalid choice.");
            } else {
                
                break;
            }

        }

        while (true) {
            System.out.println("Please select the length of the match: " +
                    "\nA. 10 minutes" +
                    "\nB. 30 minutes" +
                    "\nC. No limit");
            select = scanner.nextLine();
            // select = scanner2.nextLine();
            if ((select.equalsIgnoreCase("A") || select.equalsIgnoreCase("B")) || select.equalsIgnoreCase("C")) {

                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }

        if (select.equalsIgnoreCase("A")) {
             gameDuration = 10 * 60 * 1000;
            //gameDuration = 3000;// test 3 seconds
        } else if (select.equalsIgnoreCase("B")) {
            gameDuration = 30 * 60 * 1000;
        } else {
            gameDuration = -1;
        }

        player1 = new Player(playerOName, "o");
        player2 = new Player(playerXName, "x");

        goGame();
    }


    /**
 * Prepares and starts the main game loop. This method initializes game components such as the board, dice, and players.
 * It determines the starting player based on a dice roll and sets up the necessary threads for the game and time monitoring.
 * The game state is reset before starting, including setting the doubling cube value to its initial state.
 * This method should be called to start a new game or restart the game with the current settings.
 */
    public void goGame() {

        doublingValue = 1;
        dice = new Dice();
        board = new Board();
        test = new Test();
        if (dice.rollToStart(player1, player2)) {
            temp = player1;
        } else {
            temp = player2;
        }
        board.displayBoard();


        // if (gameThread != null) {
        //     stopGame();

        // }

if(gameThread==null){
        gameRunning = true;

        gameThread = new Thread(this::runGame, "GameThread-" + System.currentTimeMillis());

        gameThread.start();

        timeCheckThread = new Thread(this::checkGameTime, "TimeCheckThread-" + System.currentTimeMillis());

        timeCheckThread.start();
}

    }

    private boolean isGameOver() {
        if (board.getBoard(0) == 15 || board.getBoard(25) == -15) {

            return true;
        } else {
            return false;
        }
    }

    private void displayCommands() {
        System.out.println("All commands:\n1. Roll\n2. Quit\n3. Hint\n4. Move");
    }

    private void displayMoves(Player player, ArrayList<Integer> dices, Board board) {

        // sort the dices in reverse order, so that when the dice is larger than the furthest checker, it can be handled
        Collections.sort(dices, Collections.reverseOrder());
        System.out.print(player.getChecker() + " to play " + dices.get(0));
        for (int i = 1; i < dices.size(); i++) {
            System.out.print("-" + dices.get(i));
        }
        System.out.print("\n");
        
        // add count, add serial number for all possible moves through for loop
        int count = 0;
        int boundary;
        
        // if double, no longer loop through each dice
        int show = dices.size();
        if (dices.size() > 1) {
            if (dices.get(0) == dices.get(1)) {
                show = 1;
            }
        }
       
        // if the player does not have lost checker
        if (board.getLost(player) == 0) {
            if (player.getChecker() == "x") {

// if player is in the final phase, the dice is larger than the furthest checker, then the furthest checker can be moved into off
// if the largest dice is farther than the furthest checker, 
//then the largest dice can move the furthest checker into off, and other dices move according to the rules

                if (player.isFinalPhase(board)) {
                    
                    // read the range to OFF
                    boundary = 26;
                    
                    // the largest dice is farther than the furthest checker
                    if (player.findFurthestChecker(board) < dices.get(0)) {
                        dices.set(0, player.findFurthestChecker(board));
                    }
                } else {
                    boundary = 25;
                }
                
                // loop through all dices
                for (int i = 0; i < show; i++) {
                    
                    // loop through all the checkers
                    for (int j = 0; j < boundary; j++) {
                       
                        // if the checker is x, and if the checker is still on the board after moving
                        if (board.getBoard(j) < 0 && j + dices.get(i) < boundary) {
                            
                            // if the destination is empty, the player's checker exists, or the enemy's single checker
                            if (board.getBoard(j + dices.get(i)) < 2) {
                                String destinationText = "";
                                if (j + dices.get(i) == 25) {
                                    destinationText = "OFF";
                                } else {
                                    destinationText = Integer.toString(j + dices.get(i));
                                }
                                System.out.println((char) (65 + count) + ") Play " + j + "-" + destinationText);
                                moves.add(new Direction(j, j + dices.get(i)));
                                count++;
                            }
                        }
                    }
                }
            } else {
                
                // if checker is o
                if (player.isFinalPhase(board)) {
                    boundary = 0;
                    
                    // the largest dice is farther than the furthest checker
                    if (player.findFurthestChecker(board) < dices.get(0)) {
                        dices.set(0, player.findFurthestChecker(board));
                    }
                } else {
                    boundary = 1;
                }
                for (int i = 0; i < show; i++) {
                    for (int j = 25; j >= boundary; j--) {
                        if (board.getBoard(j) > 0 && j - dices.get(i) >= boundary) {
                            if (board.getBoard(j - dices.get(i)) > -2) {
                                String destinationText = "";
                                if (j - dices.get(i) == 0) {
                                    destinationText = "OFF";
                                } else {
                                    destinationText = Integer.toString(j - dices.get(i));
                                }
                                System.out.println((char) (65 + count) + ") Play " + j + "-" + destinationText);
                                moves.add(new Direction(j, j - dices.get(i)));
                                count++;
                            }
                        }
                    }
                }
            }
        }
       
        // if the player has lost checker
        else {
            
            // force to move the lost checker
            if (player.getChecker() == "x") {
                for (int i = 0; i < show; i++) {
                    if (board.getBoard(dices.get(i)) < 2) {
                        System.out.println((char) (65 + count) + ") Play bar - " + dices.get(i));
                        moves.add(new Direction(-1, dices.get(i)));
                        count++;
                    }
                }
            } else {
                for (int i = 0; i < show; i++) {
                    if (board.getBoard(25 - dices.get(i)) > -2) {
                        System.out.println((char) (65 + count) + ") Play bar - " + (25 - dices.get(i)));
                        moves.add(new Direction(-1, 25 - dices.get(i)));
                        count++;
                    }
                }
            }
        }
        System.out.print(">>");
    }

    public void moveChecker(Board board, int start, int destination, Player player) {
        if (player.getChecker() == "x") {
            if (board.getBoard(destination) == 1) {
                board.addLostO();
                board.setBoard(destination, board.getBoard(destination) - 1);
            }
            if (start == -1) {
                board.removeLost(player);
            } else {
                board.setBoard(start, board.getBoard(start) + 1);
            }
            board.setBoard(destination, board.getBoard(destination) - 1);
        } else {
            if (board.getBoard(destination) == -1) {
                board.addLostX();
                board.setBoard(destination, board.getBoard(destination) + 1);
            }
            if (start == -1) {
                board.removeLost(player);
            } else {
                board.setBoard(start, board.getBoard(start) - 1);
            }
            board.setBoard(destination, board.getBoard(destination) + 1);
        }
    }

    public ArrayList<Integer> removeInteger(ArrayList<Integer> arrayList, int start, int destination, Player player) {
        if (start == -1) {
            if (player.getChecker() == "x") {
                start = 0;
            } else {
                start = 25;
            }
        }
        int object = start - destination;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) == Math.abs(object)) {
                arrayList.remove(i);
                break;
            }
        }
        return arrayList;
    }

    public Matcher validateDice(String command) {
        String dicePattern = "^DICE\\s+(\\d+)\\s+(\\d+)$";
        Pattern diceRegex = Pattern.compile(dicePattern);
        Matcher diceMatcher = diceRegex.matcher(command);
        return diceMatcher;
    }

    public Matcher validateFile(String command) {
        String filePattern = "^(?i)test\\s+(\\S+\\.txt)$";
        Pattern fileRegex = Pattern.compile(filePattern);
        Matcher fileMatcher = fileRegex.matcher(command);
        return fileMatcher;
    }

    private void checkGameTime() {
        while (gameRunning) {
            if (gameDuration != -1 && isTimeUp()) {
                System.out.println("Time's up! Game over.");
                // System.exit(0);

                // stopGame();
                gameRunning = false;

                promptNextMatch();
                break;
            }

            try {
                Thread.sleep(1000); // check every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // reset interrupt status
                break;
                // e.printStackTrace();
            }

        }
    }

    public void stopGame() {
        // System.out.println("Stopping game...");
        gameRunning = false; 

        // try {
        //     Thread.sleep(100); 
        // } catch (InterruptedException e) {
        //     gameThread.interrupt(); 
        //     timeCheckThread.interrupt(); 
        // }

        if(gameThread != null) {
            try {
                //gameThread.join();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            }
        }

        gameRunning = true; 
    }

    private boolean isTimeUp() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - gameStartTime;
        return elapsedTime >= gameDuration;
    }

/**
 * Executes the main game loop. This method is responsible for managing each turn of the game.
 * It controls the sequence of actions within a player's turn, including rolling dice, making moves,
 * and handling commands like quitting or offering doubles. The method checks for game-over conditions
 * and updates the game state accordingly. It also tracks and displays the elapsed time since the game started.
 * This method should be run in a separate thread to keep the game loop independent of other operations.
 */
    private void runGame() {

        gameStartTime = System.currentTimeMillis();
        Scanner scanner = new Scanner(System.in);
        // this.scanner = new Scanner(System.in);
        while (!isGameOver() && gameRunning) {
            System.out.println(temp.getName() + "'s turn:");
            
            // System.out.println("Current thread: " + Thread.currentThread().getName());

            System.out.println("Pip:" + temp.getPip(board) + ", Time elapsed: "
                    + formattedTime(System.currentTimeMillis() - gameStartTime));
            boolean quit = false;
            while (true) {

                
                // check if there is a double offer before the player's turn
                if (doublingValue > 1 && temp != doublingOwner) {
                    System.out.println("Double has been offered. Do you accept? (yes/no)");
                    String response = scanner.nextLine().toLowerCase();
                    if (response.equalsIgnoreCase("no")) {
                        refuseDouble(temp);
                        break;
                    } else if (response.equalsIgnoreCase("yes")) {
                        acceptDouble(temp);
                       
                        // if accept double, allow the current player to double again
                        System.out.print("Do you want to double? (yes/no): ");
                        response = scanner.nextLine();
                        if (response.equalsIgnoreCase("yes")) {
                            offerDouble(temp);
                        } else if (response.equalsIgnoreCase("no")) {
                            System.out.println(temp.getName() + " refused to double.");
                        } else {
                            System.out.println("Invalid command.");
                        }
                    } else {
                        System.out.println("Invalid command.");
                    }
                }

                System.out.print("Enter command:");
                String command = scanner.nextLine();
                if (!gameRunning) {
                    // stopGame();
                    try {
                        Thread.sleep(100); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); 

                        break; 
                    }

                    break;
                }

                String upperCaseCommand = command.toUpperCase();

                Matcher diceMatcher = validateDice(upperCaseCommand);
                Matcher fileMatcher = validateFile(command);

                if (diceMatcher.find()) {
                    int firstInt = Integer.parseInt(diceMatcher.group(1));
                    int secondInt = Integer.parseInt(diceMatcher.group(2));
                    test.setDiceValues(dice, firstInt, secondInt);
                } else if (fileMatcher.find()) {
                    test.executeCommandsFromFile(dice, fileMatcher.group(1));
                } else if (upperCaseCommand.equalsIgnoreCase("QUIT")) {
                    System.out.println("Exiting game.");
                    quit = true;
                    break;
                } else if (upperCaseCommand.equalsIgnoreCase("ROLL")) {
                    dice.roll();
                    dice.displayDices();
                } else if (upperCaseCommand.equalsIgnoreCase("HELP")) {
                    displayCommands();
                } else if (upperCaseCommand.equalsIgnoreCase("PIP")) {
                    System.out.println("Pip: " + temp.getPip(board));
                } else if (upperCaseCommand.equalsIgnoreCase("DOUBLE")) {

                    offerDouble(temp);
                    // Logic for the other player to accept or refuse the double
                }

                else if (upperCaseCommand.equalsIgnoreCase("HINT")) {
                   
                    // take out the number from dice and store it in arraylist
                    ArrayList<Integer> dices = new ArrayList<Integer>();
                    int dice1 = dice.getDice1();
                    int dice2 = dice.getDice2();
                    if (dice1 == dice2) {
                        
                        //when double happens
                        for (int i = 0; i < 4; i++) {
                            dices.add(dice1);
                        }
                    } else {
                        dices.add(dice1);
                        dices.add(dice2);
                    }
                    while (dices.size() > 0) {
                        
                        // loop, let the user use all the dices
                        int index = -1;
                        while (true) {
                           
                            // loop, display all possible moves, until read in a legal command
                            displayMoves(temp, dices, board);
                            
                            // if there is no legal move, force to skip
                            if (moves.size() == 0) {
                                System.out.print("No valid moves available. Skipping your turn\n");
                                dices.clear();
                                break;
                            }

                            String choice = scanner.nextLine().toUpperCase();
                            System.out.println(choice);
                            if (choice.equalsIgnoreCase("QUIT")) {
                                System.out.println("Exiting game.");
                                quit = true;
                                break;
                            } else {
                                if (choice.length() != 1) {
                                    System.out.println("Invalid command");
                                } else {
                                    index = (int) choice.charAt(0) - 65;
                                    if (index < moves.size() && index > -1) {
                                        break;
                                    } else {
                                        System.out.println("Invalid command");
                                    }
                                }
                            }
                        }
                        if (quit) {
                            break;
                        } else {
                            if (dices.size() != 0) {
                                
                                // get index, read start - destination Direction
                                Direction move = moves.get(index);
                                // System.out.println(move.getStart() + "-" +move.getDestination());
                                
                                // move the checker, and remove the used dice from dices
                                moveChecker(board, move.getStart(), move.getDestination(), temp);
                              
                                // remove the used choice from dices
                                moves.clear();
                                board.displayBoard();
                                removeInteger(dices, move.getStart(), move.getDestination(), temp);
                                if (isGameOver()) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                } else {
                    System.out.println("Invalid command.");
                }
            }
            if (quit) {
                break;
            }
            if (temp.getName() != player1.getName()) {
                temp = player1;
            } else {
                temp = player2;
            }
            // when the player's turn is over, clear the moves
            updateScores(); // Update scores after each turn
            
        }
    }

    private void updateScores() {

        int finalScore = 1;

        if (board.getBoard(0) == 15) {
            gameRunning = false;
            // double owner
            if (doublingOwner == player1) {

                finalScore = doublingValue * winScore(board, temp);
            } else {
                finalScore = winScore(board, temp);
            }
            player1.addScore(finalScore); // player1 wins the game
            
            // hint if start next match
            promptNextMatch();
        } else if (board.getBoard(25) == -15) {
            gameRunning = false;
            if (doublingOwner == player2) {
                finalScore = doublingValue * winScore(board, temp);
            } else {
                finalScore = winScore(board, temp);
            }
            player2.addScore(finalScore); // player2 wins the game
            
            promptNextMatch();
        }

        // check if any player wins the tournament
        matchControl();
    }

    public void matchControl() {
        if (player1.getScore() >= winningScore || player2.getScore() >= winningScore) {
            announceWinner();
        }
    }

    private void promptNextMatch() {
        while (true) {
            System.out.print("Start next match? (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                try {
                    Thread.sleep(100); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); 
                }
                // resetGame
                
                resetGame();
                break;

            } else if (scanner.nextLine().equalsIgnoreCase("no")) {
                System.out.println(temp.getName() + " Exiting game.");
                System.exit(0);
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    private void resetGame() {
        stopGame();
        // reset the game
        System.out.println("A new game start");
        System.out.println("score: " + player1.getScore() + " " + player2.getScore());
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
        
        this.goGame();
    }

    private String formattedTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void acceptDouble(Player player) {
        // Logic for accepting the double
        System.out.println(player.getName() + " has accepted the double.");
        // Update game state accordingly
    }

    public void refuseDouble(Player player) {
        // Logic for refusing the double
        System.out.println(player.getName() + " has refused the double.");

        int finalScore = 1;
        finalScore = doublingValue;
        // double owner
        if (doublingOwner == player1) {
            player1.addScore(finalScore);
        } else {
            player2.addScore(finalScore); // player2 wins the game
            
        }
        // check if any player wins the tournament
        matchControl();
        promptNextMatch();

    }

    private void announceWinner() {
        System.out.println("Final scores:");
        System.out.println("Player 1 score: " + player1.getScore());
        System.out.println("Player 2 score: " + player2.getScore());

        if (player1.getScore() > player2.getScore()) {
            System.out.println("Player 1 wins the tournament!");
        } else if (player2.getScore() > player1.getScore()) {
            System.out.println("Player 2 wins the tournament!");
        } else {
            System.out.println("The tournament ends in a draw!");
        }
        System.exit(0);
    }

    public int winScore(Board board, Player temp) {
        // playerO wins
        if (temp.getChecker() == "o") {
            
            // when the opponent gets a checker, single
            if (board.getBoard(25) < 0) {
                // System.out.println("Single");
                return 1;
            } else {
                
                // when the opponent has checker in the horizontal axis
                if (board.getLostX() > 0) {
                    // System.out.println("Gammon");
                    return 2;
                } else {
                    
                    // when the opponent has checker in the forbidden area
                    for (int i = 1; i < 7; i++) {
                        if (board.getBoard(i) < 0) {
                            // System.out.println("Gammon");
                            return 2;
                        }
                    }
                    // System.out.println("Backgammon");
                    return 3;
                }
            }
        }
        // playerX wins
        else {
            // when the opponent gets a checker, single
            if (board.getBoard(0) > 0) {
                // System.out.println("Single");
                return 1;
            } else {
                
                // when the opponent has checker in the horizontal axis
                if (board.getLostO() > 0) {
                    // System.out.println("Gammon");
                    return 2;
                } else {
                    
                    // when the opponent has checker in the forbidden area
                    for (int i = 19; i < 25; i++) {
                        if (board.getBoard(i) < 0) {
                            // System.out.println("Gammon");
                            return 2;
                        }
                    }
                    // System.out.println("Backgammon");
                    return 3;
                }
            }
        }
    }
}
