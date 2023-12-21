import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player playerX;
    private Player playerO;
    private Board board;

    @BeforeEach
    void setUp() {
        playerX = new Player("Alice", "x");
        playerO = new Player("Bob", "o");
        board = new Board(); // 假设 Board 类已经存在并初始化了棋盘
    }

    @Test
    void testGetName() {
        assertEquals("Alice", playerX.getName(), "Player name should be Alice");
        assertEquals("Bob", playerO.getName(), "Player name should be Bob");
    }

    @Test
    void testGetChecker() {
        assertEquals("x", playerX.getChecker(), "PlayerX's checker type should be x");
        assertEquals("o", playerO.getChecker(), "PlayerO's checker type should be o");
    }

    @Test
    void testAddAndGetScore() {
        playerX.addScore(10);
        assertEquals(10, playerX.getScore(), "PlayerX's score should be 10 after adding 10 points");

        playerO.addScore(5);
        assertEquals(5, playerO.getScore(), "PlayerO's score should be 5 after adding 5 points");
    }

    @Test
    void testPipCalculation() {
        // 初始化棋盘以使测试有意义
        // ... 初始化代码 ...

        int pipX = playerX.getPip(board);
        int pipO = playerO.getPip(board);

        assertTrue(pipX > 0, "PlayerX's pip count should be positive");
        assertTrue(pipO > 0, "PlayerO's pip count should be positive");
    }

    @Test
    void testIsFinalPhase() {
        // 初始化棋盘以使测试有意义
        // ... 初始化代码 ...

        boolean finalPhaseX = playerX.isFinalPhase(board);
        boolean finalPhaseO = playerO.isFinalPhase(board);

        assertFalse(finalPhaseX, "PlayerX should not be in final phase initially");
        assertFalse(finalPhaseO, "PlayerO should not be in final phase initially");
    }

    @Test
    void testFindFurthestChecker() {
        // 初始化棋盘以使测试有意义
        // ... 初始化代码 ...

        int furthestX = playerX.findFurthestChecker(board);
        int furthestO = playerO.findFurthestChecker(board);

        assertTrue(furthestX >= 0, "PlayerX's furthest checker should be found");
        assertTrue(furthestO >= 0, "PlayerO's furthest checker should be found");
    }

    @Test
    void testScoreIncrement() {
        int initialScore = playerX.getScore();
        playerX.addScore(10);
        assertEquals(initialScore + 10, playerX.getScore(), "Score should correctly increment");
    }

    @Test
    void testPlayerNameAndCheckerType() {
        assertEquals("Alice", playerX.getName(), "Player name should match");
        assertEquals("x", playerX.getChecker(), "Checker type should match");
    }

    
}
