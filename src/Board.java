public class Board {
    private int[] board;
    //为失去的棋子计数
    private int lostO;
    private int lostX;
    //初始化棋盘，o为正数，x为负数
    public Board() {
        board = new int[26];
        for (int i = 0; i < 26 ;i++){
            board[i] = 0;
        }
        board[24] = 2;
        board[19] = -5;
        board[17] = -3;
        board[13] = 5;
        board[12] = -5;
        board[8] = 3;
        board[6] = 5;
        board[1] = -2;
        lostO = 2;
        lostX = 3;
    }

    public void displayBoard() {
        System.out.println("13--+---+---+---+---18 BAR 19--+---+---+---+---24 OFF");
        displayPips(board);
        System.out.println("12--+---+---+---+---07 BAR 06--+---+---+---+---01 OFF");
    }

    private void displayPips(int[] board) {
        String output = "";
        //找大最大的行数，循环每一行
        for(int j =0; j < Math.max(findMaxAbsoluteValue(board),lostX); j++){
            //循环每一列
            for (int i = 13; i <= 25; i++){
                //如果是o
                if(board[i] > 0){
                    if (j < Math.abs(board[i])){
                        output +="o ";
                    }
                    else{
                        output +="  ";
                    }
                }
                else{
                    //如果为空
                    if(board[i] == 0){
                        if(j == 0 && i != 25){
                            output += "|   ";
                        }
                        else{
                            output += "    ";
                        }
                    }
                    //如果是x
                    else{
                        if (j < Math.abs(board[i])){
                            output +="x ";
                        }
                        else{
                            output +="  ";
                        }
                    }
                }
                //检验是否是两位数字，保持对齐
                if (Math.abs(board[i]) > 9){
                    output += " ";
                }
                else{
                    if(Math.abs(board[i]) < 10 && board[i] != 0){
                        output += "  ";
                    }
                }
                //考虑被吃掉的棋子
                if (i == 18){
                    if(j < lostX){
                        output += "x  ";
                    }
                    else{
                        output += "   ";
                    }
                }
                else
                {
                    //考虑到达终点的棋子
                    if(i == 25){
                        output += "\n";
                    }
                }
            }
        }
        System.out.println(output);
        output = "";
        for(int j =0; j < Math.max(findMaxAbsoluteValue(board),lostO); j++) {
            for (int i = 12; i >= 0; i--){
                //如果棋子是o
                if(board[i] > 0){
                    //如果列数大于等于
                    if (j >= Math.max(findMaxAbsoluteValue(board),lostO) - Math.abs(board[i])){
                        output +="o ";
                    }
                    else{
                        output +="  ";
                    }
                }
                else{
                    if(board[i] == 0){
                        if(j == Math.max(findMaxAbsoluteValue(board),lostO) - 1 && i != 0){
                            output += "|   ";
                        }
                        else{
                            output += "    ";
                        }
                    }
                    else{
                        if (j >= Math.max(findMaxAbsoluteValue(board),lostO) - Math.abs(board[i])){
                            output +="x ";
                        }
                        else{
                            output +="  ";
                        }

                    }
                }
                if (Math.abs(board[i]) > 9){
                    output += " ";
                }
                else{
                    if(Math.abs(board[i]) < 10 && board[i] != 0){
                        output += "  ";
                    }
                }
                if (i == 7){
                    if(j >= findMaxAbsoluteValue(board) - lostO){
                        output += "o  ";
                    }
                    else{
                        output += "   ";
                    }
                }
                else{
                    if(i == 0 && j != Math.max(findMaxAbsoluteValue(board),lostO) - 1){
                        output += "\n";
                    }
                }
            }
        }
        System.out.println(output);
    }

    public int getBoard(int index) {
        return board[index];
    }

    public void setBoard(int index, int value){
        board[index] =value;
    }

    public static int findMaxAbsoluteValue(int[] board) {
        int maxAbsolute = Math.abs(board[0]);
        for (int i = 1; i < board.length; i++) {
            int absoluteValue = Math.abs(board[i]);
            if (absoluteValue > maxAbsolute) {
                maxAbsolute = absoluteValue;
            }
        }
        return maxAbsolute;
    }

    public void addLostO(){
        lostO++;
    }

    public void addLostX(){
        lostX++;
    }

    public void removeLost(Player player){
        if(player.getPiece() == "o"){
            lostO--;
        }
        else{
            lostX--;
        }
    }

    public int getLostX(){
        return lostX;
    }

    public int getLostO(){
        return lostO;
    }

    public int getLost(Player player){
        if(player.getPiece() == "o"){
            return lostO;
        }
        else{
            return lostX;
        }
    }
}
