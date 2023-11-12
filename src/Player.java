public class Player {
    private String name;
    private String piece;
    private int pip;
    public Player(String name, String piece) {
        this.name = name;
        this.piece = piece;
        this.pip = 156;
    }

    public String getName() {
        return name;
    }

    public String getPiece() {
        return piece;
    }

    public int getPip() {
        return pip;
    }

    public void setPip(int newPip) {
        this.pip = newPip;
    }
}
