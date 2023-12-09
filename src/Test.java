import java.io.*;

public class Test {
    public void setDiceValues(Dice dice, int value1, int value2) {
        dice.setDice1(value1);
        dice.setDice2(value2);
        System.out.println("Dices: " + dice.getDice1() + ", " + dice.getDice2());
    }

    public void executeCommandsFromFile(Dice dice, String fileName) {
        try {
            File file = new File("data/" + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Executing command: " + line);
                String command = line.toUpperCase();
                switch (command) {
                    case "ROLL":
                        dice.roll();
                        dice.displayDices();
                        break;
                    case "QUIT":
                        System.out.println("Exiting game.");
                        System.exit(0);
                        break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
        }
    }

}
