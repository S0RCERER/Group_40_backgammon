import java.util.Random;

public class Dice {
    private int dice1;
    private int dice2;
    private Random random;

    public Dice() {
        random = new Random();
    }

    public void roll() {
        dice1 = random.nextInt(6) + 1;
        dice2 = random.nextInt(6) + 1;
    }

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public boolean rollToStart(Player player1, Player player2){
        roll();
        boolean flag = true;
        if(getDice1() > getDice2()){
            System.out.println("Dice1: " + getDice1() + " > Dice2: " + getDice2());
            System.out.println(player1.getName() + " goes first");
            return true;
        }
        else{
            if(getDice1() < getDice2()){
                System.out.println("Dice1: " + getDice1() + " < Dice2: " + getDice2());
                System.out.println(player2.getName() + " goes first");
                return false;
            }
            else{
                System.out.println("Dice1: " + getDice1() + " = Dice2: " + getDice2());
                System.out.println("Rolling again");
                rollToStart(player1,player2);
            }
        }
        return flag;
    }
}
