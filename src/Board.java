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
        for (int i = 13; i <= 25; i++){
            if(board[i] > 0){
                output +="O"+board[i];
            }
            else{
                if(board[i] == 0){
                    output += "|   ";
                }
                else{
                    output += "X"+Math.abs(board[i]);
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
        }
        System.out.println(output);
        System.out.println();
        output = "";
        for (int i = 12; i >= 0; i--){
            if(board[i] > 0){
                output +="O"+board[i];
            }
            else{
                if(board[i] == 0){
                    output += "|   ";
                }
                else{
                    output += "X"+Math.abs(board[i]);
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
        }
        System.out.println(output);
    }

    public int[] getBoard() {
        return board;
    }
}
