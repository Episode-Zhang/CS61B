package randomizedtest;

import org.junit.Test;
import static org.junit.Assert.*;


public class testThreeAddThreeRemove {
    @Test
    public void testAddThreeIntegers() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();

        expected.addLast(4);    actual.addLast(4);
        expected.addLast(5);    actual.addLast(5);
        expected.addLast(6);    actual.addLast(6);

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testRemoveOneInteger() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();

        expected.addLast(4);    actual.addLast(4);
        expected.addLast(5);    actual.addLast(5);
        expected.addLast(6);    actual.addLast(6);
        expected.removeLast();    actual.removeLast();

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testRemoveTwoIntegers() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();

        expected.addLast(4);    actual.addLast(4);
        expected.addLast(5);    actual.addLast(5);
        expected.addLast(6);    actual.addLast(6);
        expected.removeLast();    actual.removeLast();
        expected.removeLast();    actual.removeLast();

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
