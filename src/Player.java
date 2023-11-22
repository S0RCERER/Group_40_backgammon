public class Player {
    private String name;
    private String checker;
    private int pip;
    private boolean finalPhase;
    public Player(String name, String checker) {
        this.name = name;
        this.checker = checker;
        this.pip = 167;
    }

    public String getName() {
        return name;
    }

    public String getChecker() {
        return checker;
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
        //如果还有失去的棋子，每个棋子计入一圈pip
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
        //查看是否所有棋子都在家，来判断游戏是否进入“bearing off”的阶段
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
            //顺时针方向查找，当找到第一个棋子return
            for (int i = 1; i < 25; i++){
                if (board.getBoard(i) < 0){
                    return 25 - i;
                }
            }
        }
        else{
            //逆时针查找
            for (int i = 25; i > 0; i--){
                if(board.getBoard(i) > 0){
                    return i;
                }
            }
        }
        return -1;
    }

}
