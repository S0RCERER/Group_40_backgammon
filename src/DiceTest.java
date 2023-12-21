import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    private Dice dice;

    @BeforeEach
    void setUp() {
        dice = new Dice();
    }

    @Test
    void testRoll() {
        dice.roll();
        assertTrue(dice.getDice1() >= 1 && dice.getDice1() <= 6, "Dice1 should be between 1 and 6 after roll");
        assertTrue(dice.getDice2() >= 1 && dice.getDice2() <= 6, "Dice2 should be between 1 and 6 after roll");
    }

    @Test
    void testSetAndGetDice1() {
        dice.setDice1(4);
        assertEquals(4, dice.getDice1(), "getDice1 should return the value set by setDice1");
    }

    @Test
    void testSetAndGetDice2() {
        dice.setDice2(5);
        assertEquals(5, dice.getDice2(), "getDice2 should return the value set by setDice2");
    }

    @Test
    void testSetDice1WithBoundaryValues() {
        dice.setDice1(1);
        assertEquals(1, dice.getDice1(), "getDice1 should return 1 when set to 1");

        dice.setDice1(6);
        assertEquals(6, dice.getDice1(), "getDice1 should return 6 when set to 6");
    }

    @Test
    void testSetDice2WithBoundaryValues() {
        dice.setDice2(1);
        assertEquals(1, dice.getDice2(), "getDice2 should return 1 when set to 1");

        dice.setDice2(6);
        assertEquals(6, dice.getDice2(), "getDice2 should return 6 when set to 6");
    }

    @Test
    void testRollDiceMultipleTimes() {
        for (int i = 0; i < 10; i++) {
            dice.roll();
            assertTrue(dice.getDice1() >= 1 && dice.getDice1() <= 6, "Dice1 should always be between 1 and 6");
            assertTrue(dice.getDice2() >= 1 && dice.getDice2() <= 6, "Dice2 should always be between 1 and 6");
        }
    }

    @Test
    void testDiceValuesNotEqualAfterRoll() {
        boolean diceEqual = true;
        for (int i = 0; i < 10; i++) {
            dice.roll();
            if (dice.getDice1() != dice.getDice2()) {
                diceEqual = false;
                break;
            }
        }
        assertFalse(diceEqual, "It is unlikely that dice1 and dice2 are equal after multiple rolls");
    }

    @Test
    void testSetDice1WithInvalidValues() {
        // Test setting dice1 with values outside the valid range
        dice.setDice1(-1);
        assertTrue(dice.getDice1() < 1 || dice.getDice1() > 6, "Setting dice1 to -1 should result in an invalid value");

        dice.setDice1(7);
        assertTrue(dice.getDice1() < 1 || dice.getDice1() > 6, "Setting dice1 to 7 should result in an invalid value");
    }

    @Test
    void testSetDice2WithInvalidValues() {
        // Test setting dice2 with values outside the valid range
        dice.setDice2(-1);
        assertTrue(dice.getDice2() < 1 || dice.getDice2() > 6, "Setting dice2 to -1 should result in an invalid value");

        dice.setDice2(7);
        assertTrue(dice.getDice2() < 1 || dice.getDice2() > 6, "Setting dice2 to 7 should result in an invalid value");
    }

    @Test
    void testConsistencyOfRepeatedRolls() {
        // Test that repeated rolls produce different results
        int sameResultCount = 0;
        int previousDice1 = dice.getDice1();
        int previousDice2 = dice.getDice2();

        for (int i = 0; i < 5; i++) {
            dice.roll();
            if (dice.getDice1() == previousDice1 && dice.getDice2() == previousDice2) {
                sameResultCount++;
            }
            previousDice1 = dice.getDice1();
            previousDice2 = dice.getDice2();
        }

        assertTrue(sameResultCount < 5, "It is unlikely that all rolls will produce the same result consecutively");
    }

    @Test
    void testDiceEqualityAfterSeveralRolls() {
        // Test that dice are not always equal after several rolls
        int equalCount = 0;

        for (int i = 0; i < 20; i++) {
            dice.roll();
            if (dice.getDice1() == dice.getDice2()) {
                equalCount++;
            }
        }

        assertTrue(equalCount < 20, "It is unlikely that dice1 and dice2 are always equal after several rolls");
    }


}
