import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // ... 其他测试用例 ...

    @Test
    void testRemoveLostO() {
        // Test removing a lost O piece
        board.addLostO();
        board.removeLost(new Player("PlayerName", "o")); // 假设 "PlayerName" 是玩家的名字
        assertEquals(0, board.getLostO(), "After removing, lostO should be 0");
    }

    @Test
    void testRemoveLostX() {
        // Test removing a lost X piece
        board.addLostX();
        board.removeLost(new Player("PlayerName", "x")); // 假设 "PlayerName" 是玩家的名字
        assertEquals(0, board.getLostX(), "After removing, lostX should be 0");
    }

    @Test
    void testMultipleAdditionsAndRemovals() {
        // Test multiple additions and removals of pieces
        for (int i = 0; i < 3; i++) {
            board.addLostO();
            board.addLostX();
        }
        assertEquals(3, board.getLostO(), "After adding 3 times, lostO should be 3");
        assertEquals(3, board.getLostX(), "After adding 3 times, lostX should be 3");
        for (int i = 0; i < 3; i++) {
            board.removeLost(new Player("PlayerName", "o"));
            board.removeLost(new Player("PlayerName", "x"));
        }
        assertEquals(0, board.getLostO(), "After removing 3 times, lostO should be 0");
        assertEquals(0, board.getLostX(), "After removing 3 times, lostX should be 0");
    }

    void testInvalidBoardIndex() {
        // Test behavior when accessing an invalid board index
        assertThrows(IndexOutOfBoundsException.class, () -> board.getBoard(-1), "Accessing a negative index should throw IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> board.getBoard(26), "Accessing an index beyond board size should throw IndexOutOfBoundsException");
    }

    @Test
    void testUpdateBoardIndex() {
        // Test updating a board index with different values
        board.setBoard(10, 4);
        assertEquals(4, board.getBoard(10), "Board at position 10 should be updated to 4");
        board.setBoard(10, -2);
        assertEquals(-2, board.getBoard(10), "Board at position 10 should be updated to -2");
    }

    @Test
    void testLostCounters() {
        // Test the lost counters for both players
        board.addLostO();
        board.addLostX();
        board.addLostO();
        assertEquals(2, board.getLostO(), "LostO counter should be 2");
        assertEquals(1, board.getLostX(), "LostX counter should be 1");
        board.removeLost(new Player("PlayerName", "o"));
        board.removeLost(new Player("PlayerName", "x"));
        assertEquals(1, board.getLostO(), "LostO counter should be 1 after removal");
        assertEquals(0, board.getLostX(), "LostX counter should be 0 after removal");
    }

    @Test
    void testBoardValuesAtSpecificPositions() {
        // Test the initial values at specific board positions
        assertEquals(0, board.getBoard(0), "Initially, position 0 should be 0");
        assertEquals(-5, board.getBoard(19), "Initially, position 19 should have -5");
        assertEquals(5, board.getBoard(6), "Initially, position 6 should have 5");
        // Add more checks for specific positions as needed
    }

    @Test
    void testBoardValueSetting() {
        // Test setting board values at various positions
        board.setBoard(3, 2);
        assertEquals(2, board.getBoard(3), "Position 3 should be set to 2");
        board.setBoard(15, -3);
        assertEquals(-3, board.getBoard(15), "Position 15 should be set to -3");
    }

    @Test
    void testBoardValueReset() {
        // Test resetting board values at various positions
        board.setBoard(3, 2);
        board.setBoard(3, 0);
        assertEquals(0, board.getBoard(3), "Position 3 should be reset to 0");
        board.setBoard(15, -3);
        board.setBoard(15, 0);
        assertEquals(0, board.getBoard(15), "Position 15 should be reset to 0");
    }

    @Test
    void testIncrementAndDecrementBoardValues() {
        // Test incrementing and decrementing board values
        board.setBoard(20, 1);
        board.setBoard(20, board.getBoard(20) + 1);
        assertEquals(2, board.getBoard(20), "Position 20 should be incremented to 2");
        board.setBoard(20, board.getBoard(20) - 1);
        assertEquals(1, board.getBoard(20), "Position 20 should be decremented to 1");
    }

}
