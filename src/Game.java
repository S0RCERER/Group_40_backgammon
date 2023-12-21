import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //private int totalRounds;
    private int currentRound;
    private int winningScore;
    private volatile boolean gameRunning; // 控制游戏线程运行的标志
    private Thread timeCheckThread; // 检查游戏时间的线程



    public Game() {
        // Existing initialization code...
        doublingValue = 1; // Initialize doubling cube value
        doublingOwner = null; // Initially, no one owns the doubling cube
    }
    // Method to handle a player offering to double
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



    public void start() {
        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);

        gameRunning = true; // 初始化时将游戏运行标志设为 true


        System.out.print("Please enter the user name of player o: ");
        String playerOName = scanner.nextLine();
        System.out.print("Please enter the user name of player x: ");
        String playerXName = scanner.nextLine();
        String select;

        while(true){

            System.out.print("Enter the score to win: ");

            winningScore = scanner.nextInt();
            if(winningScore<5){
                System.out.println("Invalid choice.");
            }else{
                currentRound = 1;

                break;
            }

        }

        while(true){
            System.out.println("Please select the length of the match: " +
                    "\nA. 10 minutes" +
                    "\nB. 30 minutes" +
                    "\nC. No limit");
            select = scanner2.nextLine();
            if ((select.equalsIgnoreCase("A" ) || select.equalsIgnoreCase("B" )) || select.equalsIgnoreCase("C" )){


                break;
            }
            else{
                System.out.println("Invalid choice.");
            }
        }

        if (select.equalsIgnoreCase("A")) {
            gameDuration = 10 * 60 * 1000;
            //gameDuration = 3000;//测试3秒
        } else if (select.equalsIgnoreCase("B")) {
            gameDuration = 30 * 60 * 1000;
        } else {
            gameDuration = -1;
        }

        player1 = new Player(playerOName, "o");
        player2 = new Player(playerXName, "x");

        goGame();
    }

    public void goGame(){

        doublingValue=1;
        dice = new Dice();
        board = new Board();
        test = new Test();
        if (dice.rollToStart(player1, player2)){
            temp = player1;
        }
        else{
            temp = player2;
        }
        board.displayBoard();

        gameStartTime = System.currentTimeMillis();
        if (gameThread == null) {
            gameThread = new Thread(this::runGame);
            gameThread.start();
        }
        if (timeCheckThread == null) {
            timeCheckThread = new Thread(this::checkGameTime);

            timeCheckThread.start();
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
        System.out.println("All commands:\n1. Roll\n2. Quit\n3. Hint\n4. Move");
    }

    private void displayMoves(Player player, ArrayList<Integer> dices, Board board){
        //对骰子进行逆排序，方便后续当骰子点数大于最远棋子的处理
        Collections.sort(dices, Collections.reverseOrder());
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
                //如果玩家处在最后阶段，骰子大于最远的棋子，那么可以将最远的棋子移入off
                //最大的骰子如果比最远的棋子远，那么最大的骰子可以将最远的棋子移入off，其他骰子按照规则移动
                if (player.isFinalPhase(board)){
                    //读取范围到OFF
                    boundary = 26;
                    //最大的骰子设为最远距离数
                    if(player.findFurthestChecker(board) < dices.get(0)){
                        dices.set(0,player.findFurthestChecker(board));
                    }
                }
                else{
                    boundary = 25;
                }
                //遍历所有的骰子
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
                    //最大的骰子设为最远距离数
                    if(player.findFurthestChecker(board) < dices.get(0)){
                        dices.set(0,player.findFurthestChecker(board));
                    }
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

    public Matcher validateDice(String command){
        String dicePattern = "^DICE\\s+(\\d+)\\s+(\\d+)$";
        Pattern diceRegex = Pattern.compile(dicePattern);
        Matcher diceMatcher = diceRegex.matcher(command);
        return diceMatcher;
    }

    public Matcher validateFile(String command){
        String filePattern = "^(?i)test\\s+(\\S+\\.txt)$";
        Pattern fileRegex = Pattern.compile(filePattern);
        Matcher fileMatcher = fileRegex.matcher(command);
        return fileMatcher;
    }

    private void checkGameTime() {
        while (gameRunning) {
            if (gameDuration != -1 && isTimeUp()) {
                System.out.println("Time's up! Game over.");
                System.exit(0);
                break;
            }

            try {
                Thread.sleep(1000); // 每秒检查一次游戏时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100); // 每秒检查一次
            } catch (InterruptedException e) {
                // 线程被中断时的处理
                break;
            }
        }
    }
    public void stopGame() {
        gameRunning = false; // 设置游戏运行标志为 false
        if (gameThread != null) {
            gameThread.interrupt(); // 尝试中断游戏线程
        }

    }

    private boolean isTimeUp() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - gameStartTime;
        return elapsedTime >= gameDuration;
    }

    private void runGame() {
        Scanner scanner = new Scanner(System.in);

        while (!isGameOver() && gameRunning) {
            System.out.println(temp.getName() + "'s turn:");
            System.out.println("Pip:" + temp.getPip(board) + ", Time elapsed: " + formattedTime(System.currentTimeMillis() - gameStartTime));
            boolean quit = false;
            while(true){

                // 在玩家回合开始前检查是否有加倍提议
                if (doublingValue > 1 && temp != doublingOwner) {
                    System.out.println("Double has been offered. Do you accept? (yes/no)");
                    String response = scanner.nextLine().toLowerCase();
                    if (response.equalsIgnoreCase("no")) {
                        refuseDouble(temp);
                        break;
                    } else {
                        acceptDouble(temp);
                        // 如果接受加倍，允许当前玩家再次提出加倍
                        System.out.print("Do you want to double? (yes/no): ");
                        response = scanner.nextLine();
                        if (response.equalsIgnoreCase("yes")) {
                            offerDouble(temp);
                        }
                    }
                }

                System.out.print("Enter command:");
                String command = scanner.nextLine();
                String upperCaseCommand = command.toUpperCase();

                Matcher diceMatcher = validateDice(upperCaseCommand);
                Matcher fileMatcher = validateFile(command);


                if (diceMatcher.find()) {
                    int firstInt = Integer.parseInt(diceMatcher.group(1));
                    int secondInt = Integer.parseInt(diceMatcher.group(2));
                    test.setDiceValues(dice,firstInt,secondInt);
                } else if (fileMatcher.find()) {
                    test.executeCommandsFromFile(dice, fileMatcher.group(1));
                }
                else if (upperCaseCommand.equalsIgnoreCase("QUIT")) {
                    System.out.println("Exiting game.");
                    quit = true;
                    break;
                } else if (upperCaseCommand.equalsIgnoreCase("ROLL")) {
                    dice.roll();
                    dice.displayDices();
                } else if (upperCaseCommand.equalsIgnoreCase("HELP")){
                    displayCommands();
                } else if (upperCaseCommand.equalsIgnoreCase("PIP")){
                    System.out.println("Pip: " +temp.getPip(board));
                }  else if (upperCaseCommand.equalsIgnoreCase("DOUBLE")){

                    offerDouble(temp);
                    // Logic for the other player to accept or refuse the double
                }


                else if (upperCaseCommand.equalsIgnoreCase("HINT")){
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
                            //如果没有合法移动，强制跳过
                            if (moves.size() == 0){
                                System.out.print("No valid moves available. Skipping your turn\n");
                                dices.clear();
                                break;
                            }

                            String choice = scanner.nextLine().toUpperCase();
                            System.out.println(choice);
                            if (choice.equalsIgnoreCase("QUIT")){
                                System.out.println("Exiting game.");
                                quit = true;
                                break;
                            }
                            else{
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
                        }
                        if(quit){
                            break;
                        }
                        else{
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
                                if (isGameOver()){
                                    break;
                                }
                            }
                        }
                    }
                    break;
                } else{
                    System.out.println("Invalid command.");
                }
            }
            if(quit){
                break;
            }
            if (temp.getName() != player1.getName()){
                temp = player1;
            }
            else{
                temp = player2;
            }
            // 当一场游戏结束
            updateScores();  // 更新积分


        }
    }

    private void updateScores() {
        if (board.getBoard(0) == 15) {
            player1.addScore(doublingValue); // 玩家1赢得这局游戏
            // 提示是否开始下一场比赛
            promptNextMatch();
        } else if (board.getBoard(25) == 15) {
            player2.addScore(doublingValue); // 玩家2赢得这局游戏
            // 提示是否开始下一场比赛
            promptNextMatch();
        }
        // 检查是否有玩家赢得了锦标赛
    }

    public void matchControl(){
        if (player1.getScore() >= winningScore || player2.getScore() >= winningScore) {
            announceWinner();
        }
    }

    private void promptNextMatch() {
        System.out.print("Start next match? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            // 重置游戏状态，开始新的一局
            resetGame();

        }
    }
    private void resetGame() {
        // 重置游戏状态，准备新的一局
        // 例如，重置棋盘、骰子等
        // 可能需要重新初始化玩家或其他游戏元素
        stopGame(); // 停止当前运行的游戏线程
        // 重置游戏状态...
        gameRunning = true; // 重置游戏运行标志
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
        System.out.println("A new game start");
        updateScores();

        resetGame();
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
    }
}