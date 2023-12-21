public class Player {
    private String name;
    private String checker;
    private int pip;

    private int score;

    public Player(String name, String checker) {
        this.name = name;
        this.checker = checker;
        this.pip = 167;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public String getChecker() {
        return checker;
    }
    public void addScore(int points) {
        this.score += points;
    }

    // getter and setter for score
    public int getScore() {
        return this.score;
    }
    public int getPip(Board board) {
        int count = 0;
        for(int i = 1; i < 25; i++) {
            if (board.getBoard(i) != 0){
                if (getChecker() == "x" && board.getBoard(i) < 0) {
                    count += (25 - i) * Math.abs(board.getBoard(i));
                }
                if (getChecker() == "o" && board.getBoard(i) > 0){
                    count += i * board.getBoard(i);
                }
            }
        }

        // if still have lost checker, each checker counts as 25 pips
        if (getChecker() == "x"){
            count += board.getLostX() * 25;
        }
        else{
            count += board.getLostO() * 25;
        }
        setPip(count);
        return pip;
    }

    public void setPip(int newPip) {
        this.pip = newPip;
    }

    public boolean isFinalPhase(Board board){

        // check if all checkers are in home board
        if (getChecker() == "x" && board.getLostX() == 0){
            for (int i = 1; i < 19; i++){
                if (board.getBoard(i) < 0){
                    return false;
                }
            }
        }
        else{
            if (getChecker() == "o" && board.getLostO() == 0){
                for (int i = 25; i > 6; i--){
                    if(board.getBoard(i) > 0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int findFurthestChecker(Board board){
        if (getChecker() == "x"){

            // check if all checkers are in home board
            for (int i = 1; i < 25; i++){
                if (board.getBoard(i) < 0){
                    return 25 - i;
                }
            }
        }
        else{

            // check if all checkers are in home board
            for (int i = 25; i > 0; i--){
                if(board.getBoard(i) > 0){
                    return i;
                }
            }
        }
        return -1;
    }

}