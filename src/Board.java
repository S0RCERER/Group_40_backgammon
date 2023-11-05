public class Board {
    private int[] board;
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
    }

    public void displayBoard() {
        System.out.println("13--+---+---+---+---18 BAR 19--+---+---+---+---24 OFF");
        displayPips(board);
        System.out.println("12--+---+---+---+---07 BAR 06--+---+---+---+---01 OFF");
    }

    private void displayPips(int[] board) {
        String output = "";
        for(int j =0; j < findMaxAbsoluteValue(board); j++){
            for (int i = 13; i <= 25; i++){
                if(board[i] > 0){
                    if (j < Math.abs(board[i])){
                        output +="o ";
                    }
                    else{
                        output +=" ";
                    }
                }
                else{
                    if(board[i] == 0){
                        if(j == 0){
                            output += "|   ";
                        }
                        else{
                            output += "    ";
                        }
                    }
                    else{
                        if (j < Math.abs(board[i])){
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
                if (i == 18){
                    output += "   ";
                }
                else{
                    if(i == 25){
                        output += "\n";
                    }
                }
            }
        }
        System.out.println(output);
        output = "";
        for(int j =0; j < findMaxAbsoluteValue(board); j++) {
            for (int i = 12; i >= 0; i--){
                if(board[i] > 0){
                    if (j >= findMaxAbsoluteValue(board) - Math.abs(board[i])){
                        output +="o ";
                    }
                    else{
                        output +="  ";
                    }
                }
                else{
                    if(board[i] == 0){
                        if(j == findMaxAbsoluteValue(board) - 1){
                            output += "|   ";
                        }
                        else{
                            output += "    ";
                        }
                    }
                    else{
                        if (j >= findMaxAbsoluteValue(board) - Math.abs(board[i])){
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
                    output += "   ";
                }
                else{
                    if(i == 0 && j != findMaxAbsoluteValue(board) - 1){
                        output += "\n";
                    }
                }
            }
        }
        System.out.println(output);
    }

    public int[] getBoard() {
        return board;
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
}
