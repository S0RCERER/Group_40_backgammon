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
        if (dice.rollToStart(player1, player2)){
            temp = player1;
        }
        else{
            temp = player2;
        }
        board.displayBoard();

        while (!isGameOver()) {
            System.out.println(temp.getName() + "'s turn:");
            System.out.println("Pip:" + temp.getPip(board));
            while(true){
                System.out.print("Enter command:");
                String command = scanner.nextLine().toUpperCase();
                if (command.equalsIgnoreCase("QUIT")) {
                    System.out.println(temp.getName() + " quit the game.");
                    break;
                } else if (command.equalsIgnoreCase("ROLL")) {
                    dice.roll();
                    System.out.println("Dices: " + dice.getDice1() + ", " + dice.getDice2());
                } else if (command.equalsIgnoreCase("HINT")){
                    displayCommands();
                } else if (command.equalsIgnoreCase("PIP")){
                    System.out.println("Pip: " +temp.getPip(board));
                }else if (command.equalsIgnoreCase("MOVE")){
                    //从dice中取出数目，存入arraylist
                    ArrayList<Integer> dices = new ArrayList<Integer>();
                    int dice1 = dice.getDice1();
                    int dice2 = dice.getDice2();
                    if (dice1 == dice2){
                        //当double的情况发生
                        for(int i = 0; i < 4; i++){
                            dices.add(dice1);
                        }
                    }
                    else{
                        dices.add(dice1);
                        dices.add(dice2);
                    }
                    while(dices.size() > 0 ){
                        //循环，让用户用掉所有的骰子
                        int index = -1;
                        while(true){
                            //循环，展示所有可能存在的移动，直到读入合法的命令
                            displayMoves(temp, dices, board);
                            if (moves.size() == 0){
                                System.out.print("No valid moves available. Skipping your turn\n");
                                dices.clear();
                                break;
                            }
                            String choice = scanner.nextLine().toUpperCase();
                            if (choice.length() != 1) {
                                System.out.println("Invalid command");
                            }
                            else{
                                index = (int)choice.charAt(0) - 65;
                                if (index < moves.size() && index > -1){
                                    break;
                                }
                                else{
                                    System.out.println("Invalid command");
                                }
                            }
                        }
                        if(dices.size() != 0){
                            //获取index，读取 起点 - 目的地 的Direction
                            Direction move = moves.get(index);
                            //System.out.println(move.getStart() + "-" +move.getDestination());
                            //移动棋子，并且从dices删除用过的骰子
                            moveChecker(board, move.getStart(), move.getDestination(), temp);
                            //删除原有的选项，下一次循环重新检测可能的移动
                            moves.clear();
                            board.displayBoard();
                            removeInteger(dices,move.getStart(), move.getDestination(), temp);
                        }
                    }
                    break;
                } else{
                    System.out.println("Invalid command.");
                }
            }
            if (temp.getName() != player1.getName()){
                temp = player1;
            }
            else{
                temp = player2;
            }
        }
    }

    private boolean isGameOver() {
        if (board.getBoard(0) == 15 || board.getBoard(25) == 15){
            return true;
        }
        else{
            return false;
        }
    }

    private void displayCommands(){
        System.out.println("All commands:\n1. Roll\n2. Quit\n3. Hint\n4.Move");
    }

    private void displayMoves(Player player, ArrayList<Integer> dices, Board board){
        System.out.print(player.getChecker() + " to play " + dices.get(0));
        for (int i = 1; i < dices.size(); i++){
            System.out.print("-" + dices.get(i));
        }
        System.out.print("\n");
        //添加count，通过for循环为所有可能的移动添加序号
        int count = 0;
        int boundary;
        //如果是double，不再循环各个骰子
        int show = dices.size();
        if (dices.size() > 1 ){
            if (dices.get(0) == dices.get(1)){
                show = 1;
            }
        }
        //如果该玩家不存在失去的棋子
        if (board.getLost(player) == 0){
            if (player.getChecker() == "x"){
                //遍历所有的骰子
                if (player.isFinalPhase(board)){
                    boundary = 26;
                }
                else{
                    boundary = 25;
                }
                for (int i = 0; i < show; i++){
                    //按照x行走顺序
                    for (int j = 0; j < boundary; j++){
                        //如果棋子是x，并且如果移动后还在棋盘内
                        if (board.getBoard(j) < 0 && j + dices.get(i) < boundary ){
                            //如果目标地点为空，己方棋子存在，或敌方单个棋子，
                            if (board.getBoard(j + dices.get(i)) < 2){
                                String destinationText = "";
                                if (j + dices.get(i) == 25) {
                                    destinationText = "OFF";
                                }
                                else{
                                    destinationText = Integer.toString(j + dices.get(i));
                                }
                                System.out.println((char)(65 + count) + ") Play " + j + "-" + destinationText);
                                moves.add(new Direction(j, j + dices.get(i)));
                                count++;
                            }
                        }
                    }
                }
            }
            else{
                //如果棋子是o
                if (player.isFinalPhase(board)){
                    boundary = 0;
                }
                else{
                    boundary = 1;
                }
                for (int i = 0; i < show; i++){
                    for (int j = 25; j >= boundary; j--){
                        if (board.getBoard(j) > 0 && j - dices.get(i) >= boundary ){
                            if (board.getBoard(j - dices.get(i)) > -2){
                                String destinationText = "";
                                if (j - dices.get(i) == 0) {
                                    destinationText = "OFF";
                                }
                                else{
                                    destinationText = Integer.toString(j - dices.get(i));
                                }
                                System.out.println((char)(65 + count) + ") Play " + j + "-" + destinationText);
                                moves.add(new Direction(j, j - dices.get(i)));
                                count++;
                            }
                        }
                    }
                }
            }

        }
        //如果玩家存在失去的棋子
        else{
            //强制移动失去的棋子
            if (player.getChecker() == "x"){
                for (int i = 0; i < show; i++){
                    if (board.getBoard(dices.get(i)) < 2 ){
                        System.out.println((char)(65 + count) + ") Play bar - " + dices.get(i));
                        moves.add(new Direction(-1, dices.get(i)));
                        count++;
                    }
                }
            }
            else{
                for (int i = 0; i < show; i++){
                    if (board.getBoard(25 - dices.get(i)) > -2){
                        System.out.println((char)(65 + count) + ") Play bar - " + (25 - dices.get(i)));
                        moves.add(new Direction(-1, 25 - dices.get(i)));
                        count++;
                    }
                }
            }
        }
        System.out.print(">>");
    }

    public void moveChecker(Board board, int start, int destination, Player player){
        if (player.getChecker() == "x"){
            if (board.getBoard(destination) == 1){
                board.addLostO();
                board.setBoard(destination, board.getBoard(destination) - 1);
            }
            if (start == -1){
                board.removeLost(player);
            }
            else{
                board.setBoard(start,board.getBoard(start) + 1);
            }
            board.setBoard(destination, board.getBoard(destination) - 1);
        }
        else{
            if (board.getBoard(destination) == -1){
                board.addLostX();
                board.setBoard(destination, board.getBoard(destination) + 1);
            }
            if (start == -1){
                board.removeLost(player);
            }
            else{
                board.setBoard(start,board.getBoard(start) - 1);
            }
            board.setBoard(destination, board.getBoard(destination) + 1);
        }
    }

    public ArrayList<Integer> removeInteger(ArrayList<Integer> arrayList, int start, int destination, Player player){
        if (start == -1){
            if(player.getChecker() == "x"){
                start = 0;
            }
            else{
                start = 25;
            }
        }
        int object = start -destination;
        for(int i = 0; i< arrayList.size(); i++){
            if (arrayList.get(i) == Math.abs(object)){
                arrayList.remove(i);
                break;
            }
        }
        return arrayList;
    }
}
