import java.util.Arrays;

public class Board {
    private int[] board;
    // count the number of lost checkers
    private int lostO;
    private int lostX;
    // initialize the board, o is positive, x is negative
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
        lostO = 0;
        lostX = 0;
    }

    public void displayBoard() {
        System.out.println("13--+---+---+---+---18 BAR 19--+---+---+---+---24 OFF");
        displayPips(board);
        System.out.println("12--+---+---+---+---07 BAR 06--+---+---+---+---01 OFF");
    }

    private void displayPips(int[] board) {
        String output = "";
        // find the max row number, loop each row
        for(int j =0; j < Math.max(findMaxAbsoluteValue(Arrays.copyOfRange(board,13 ,26)),lostX); j++){
            // loop each column
            for (int i = 13; i <= 25; i++){
                //if the checker is o
                if(board[i] > 0){
                    if (j < Math.abs(board[i])){
                        output +="o ";
                    }
                    else{
                        output +="  ";
                    }
                }
                else{
                    //if the checker is empty
                    if(board[i] == 0){
                        if(j == 0 && i != 25){
                            output += "|   ";
                        }
                        else{
                            output += "    ";
                        }
                    }
                    //if the checker is x
                    else{
                        if (j < Math.abs(board[i])){
                            output +="x ";
                        }
                        else{
                            output +="  ";
                        }
                    }
                }
                //keep the space between each checker
                if (Math.abs(board[i]) > 9){
                    output += " ";
                }
                else{
                    if(Math.abs(board[i]) < 10 && board[i] != 0){
                        output += "  ";
                    }
                }
                //consider the bar
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
                    //consider the off
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
                //if the checker is o
                if(board[i] > 0){
                    //if the checker is empty
                    if (j >= Math.max(findMaxAbsoluteValue(Arrays.copyOfRange(board,0 ,13)),lostO) - Math.abs(board[i])){
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
        if(player.getChecker() == "o"){
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
        if(player.getChecker() == "o"){
            return lostO;
        }
        else{
            return lostX;
        }
    }
}