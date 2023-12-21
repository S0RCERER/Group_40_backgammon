import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    private Direction direction;

    @BeforeEach
    void setUp() {
        direction = new Direction(5, 10);
    }

    @Test
    void testGetStart() {
        assertEquals(5, direction.getStart(), "getStart should return the start value set in the constructor");
    }

    @Test
    void testGetDestination() {
        assertEquals(10, direction.getDestination(), "getDestination should return the destination value set in the constructor");
    }

    @Test
    void testDirectionWithSameStartAndDestination() {
        Direction sameDirection = new Direction(8, 8);
        assertEquals(8, sameDirection.getStart(), "Start should be 8 when start and destination are same");
        assertEquals(8, sameDirection.getDestination(), "Destination should be 8 when start and destination are same");
    }

    @Test
    void testDirectionWithNegativeValues() {
        Direction negativeDirection = new Direction(-3, -5);
        assertEquals(-3, negativeDirection.getStart(), "Start should be -3 when negative values are used");
        assertEquals(-5, negativeDirection.getDestination(), "Destination should be -5 when negative values are used");
    }

    @Test
    void testDirectionWithZeroValues() {
        Direction zeroDirection = new Direction(0, 0);
        assertEquals(0, zeroDirection.getStart(), "Start should be 0 when zero is used");
        assertEquals(0, zeroDirection.getDestination(), "Destination should be 0 when zero is used");
    }

}
