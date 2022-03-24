package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrime1() {
        IntList input = IntList.of(1);
        boolean changed = IntListExercises.squarePrimes(input);
        assertEquals("1", input.toString());
        assertFalse(changed);
    }

    @Test
    public void testSquarePrime2() {
        IntList input = IntList.of(2);
        boolean changed = IntListExercises.squarePrimes(input);
        assertEquals("4", input.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrime3() {
        IntList input = IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        boolean changed = IntListExercises.squarePrimes(input);
        assertEquals("1 -> 4 -> 9 -> 4 -> 25 -> 6 -> 49 -> 8 -> 9 -> 10 -> 121", input.toString());
        assertTrue(changed);
    }
}
