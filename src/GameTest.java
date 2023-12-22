import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        game = new Game();
        player1 = new Player("Alice", "o");
        player2 = new Player("Bob", "x");

        Field player1Field = Game.class.getDeclaredField("player1");
        Field player2Field = Game.class.getDeclaredField("player2");
        player1Field.setAccessible(true);
        player2Field.setAccessible(true);
        player1Field.set(game, player1);
        player2Field.set(game, player2);
    }

    @Test
    void testOfferDouble() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Method offerDoubleMethod = Game.class.getDeclaredMethod("offerDouble", Player.class);
        offerDoubleMethod.setAccessible(true);
        offerDoubleMethod.invoke(game, player1);

        Field doublingValueField = Game.class.getDeclaredField("doublingValue");
        doublingValueField.setAccessible(true);
        assertEquals(2, doublingValueField.getInt(game), "Doubling value should be 2 after player1 offers to double");

        Field doublingOwnerField = Game.class.getDeclaredField("doublingOwner");
        doublingOwnerField.setAccessible(true);
        assertEquals(player1, doublingOwnerField.get(game), "Player1 should be the owner of the doubling cube after offering to double");
    }

    @Test
    void testCanPlayerDouble() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method canPlayerDoubleMethod = Game.class.getDeclaredMethod("canPlayerDouble", Player.class);
        canPlayerDoubleMethod.setAccessible(true);
        boolean canDouble = (boolean) canPlayerDoubleMethod.invoke(game, player1);
        assertTrue(canDouble, "Player1 should be able to double at the beginning of the game");
    }


    @Test
    void testGameRunningState() throws NoSuchFieldException, IllegalAccessException {
        Field gameRunningField = Game.class.getDeclaredField("gameRunning");
        gameRunningField.setAccessible(true);
        assertFalse(gameRunningField.getBoolean(game), "Game should not be running initially");
    }


    @Test
    void testGameDurationSetting() throws NoSuchFieldException, IllegalAccessException {
        Field gameDurationField = Game.class.getDeclaredField("gameDuration");
        gameDurationField.setAccessible(true);
        gameDurationField.setLong(game, 10000); // Set game duration to 10,000 milliseconds

        assertEquals(10000, gameDurationField.getLong(game), "Game duration should be correctly set");
    }

    @Test
    void testInitialGameSettings() {
        assertNotNull(game, "Game object should be initialized");
        assertEquals("Alice", player1.getName(), "Player1 should be initialized with the name Alice");
        assertEquals("Bob", player2.getName(), "Player2 should be initialized with the name Bob");
    }

    @Test
    void testInitialPlayerScores() {
        assertEquals(0, player1.getScore(), "Initial score of player1 should be 0");
        assertEquals(0, player2.getScore(), "Initial score of player2 should be 0");
    }

    @Test
    void testPlayerPieceType() {
        assertEquals("o", player1.getChecker(), "Player1 should have checker type 'o'");
        assertEquals("x", player2.getChecker(), "Player2 should have checker type 'x'");
    }
}
