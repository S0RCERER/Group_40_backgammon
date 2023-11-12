public class Player {
    private String name;
    private String checker;
    private int pip;
    private boolean finalPhase;
    public Player(String name, String checker) {
        this.name = name;
        this.checker = checker;
        this.pip = 156;
    }

    public String getName() {
        return name;
    }

    public String getChecker() {
        return checker;
    }

    public int getPip() {
        return pip;
    }

    public void setPip(int newPip) {
        this.pip = newPip;
    }

    public boolean isFinalPhase(Board board){
        if (getChecker() == "x"){
            for (int i = 1; i < 19; i++){
                if (board.getBoard(i) < 0){
                    return false;
                }
            }
        }
        else{
            for (int i = 25; i > 6; i--){
                if(board.getBoard(i) > 0){
                    return false;
                }
            }
        }
        return true;
    }

    public int findFurthestChecker(Board board){
        if (getChecker() == "x"){
            for (int i = 1; i < 25; i++){
                if (board.getBoard(i) < 0){
                    return i;
                }
            }
        }
        else{
            for (int i = 25; i > 0; i--){
                if(board.getBoard(i) > 0){
                    return i;
                }
            }
        }
        return -1;
    }

}
