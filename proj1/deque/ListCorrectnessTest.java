package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class ListCorrectnessTest {
    //---------------Give Normal-behavior-based test cases---------------

    /** Test whether a single addFirst() can be success among different type */
    @Test
    public void testSingleAddFirst() {
        Integer expected_int = StdRandom.uniform(-100, 100);
        Double expected_double = StdRandom.uniform(-3.14159265, 3.14159265);
        String expected_str = "BullyBully";
        Boolean expected_bool = true;

        LinkedListDeque<Integer> l_int = new LinkedListDeque<>();
        l_int.addFirst(expected_int);
        LinkedListDeque<Double> l_double = new LinkedListDeque<>();
        l_double.addFirst(expected_double);
        LinkedListDeque<String> l_str = new LinkedListDeque<>();
        l_str.addFirst(expected_str);
        LinkedListDeque<Boolean> l_bool = new LinkedListDeque<>();
        l_bool.addFirst(expected_bool);

        assertEquals(expected_int, l_int.get(0));
        assertEquals(expected_double, l_double.get(0));
        assertEquals(expected_str, l_str.get(0));
        assertEquals(expected_bool, l_bool.get(0));
    }


    /** Test whether addFirst() can give valid and correct result under large input */
    @Test
    public void testLargeAddFirstValidity() {
        final int size = 200000;
        int[] expected = new int[size];
        LinkedListDeque<Integer> l = new LinkedListDeque<>();

        for (int i = 0; i < size; i++) {
            int val = StdRandom.uniform(-10000000, 10000000);
            l.addFirst(val);
            expected[size - 1 - i] = val;
        }

        for (int i = 0; i < size; i++) {
            assertEquals(expected[i], l.get(i).intValue());
        }
    }


    /** Test correctness of LinkedList based on a series of random calls on add/remove method,
     *  compared to Java built-in ArrayList.
     *  Assume that user won't do any remove method on empty lists
     */
    @Test
    public void testRandomCallCombination() {
        LinkedListDeque<Integer> actual = new LinkedListDeque<>();
        ArrayList<Integer> expected = new ArrayList<>();
        final int times = 100000000;
        // randomize behavior
        for(int i = 0; i < times; i++) {
            int state = StdRandom.uniform(0, 4);
            int val = StdRandom.uniform(-1000, 1000);
            switch (state) {
                case 0: // addFirst
                    expected.add(0, val);
                    actual.addFirst(val);
                    break;
                case 1: // addLast
                    expected.add(expected.size(), val);
                    actual.addLast(val);
                    break;
                case 2: // removeFirst
                    if (!expected.isEmpty() && !actual.isEmpty()) {
                        expected.remove(0);
                        actual.removeFirst();
                    }
                    break;
                case 3: // removeLast
                    if (!expected.isEmpty() && !actual.isEmpty()) {
                        expected.remove(expected.size() - 1);
                        actual.removeLast();
                    }
                    break;
                default:
                    break;
            }
        }
        // check size
        assertEquals(expected.size(), actual.size());
        // check value both using get method and getRecursive method
        for (int i = 0; i < expected.size(); i++) {
            if (StdRandom.uniform(0, 1) == 0) {
                assertEquals(expected.get(i), actual.get(i));
            }
            else {
                assertEquals(expected.get(i), actual.getRecursive(i));
            }
        }
    }
    //-------------------------------------------------------------------

    //---------------Give Weird behavior based test cases---------------

    /** Assume that user would use remove method on an empty list
     *  We defined a rule that call remove method on any empty list yields NULL, and won't change
     *  states of LinkedList.
     */
    @Test
    public void testRemoveOnEmptyList() {
        LinkedListDeque<String> l = new LinkedListDeque<>();
        assertNull(l.removeFirst());
        assertNull(l.removeLast());
        assertEquals(0, l.size());
    }


    /** Test correctness of LinkedList based on a series of random calls on add/remove method,
     *  compared to Java built-in ArrayList.
     *  Assume that user probably do remove method on empty lists
     */
    @Test
    public void testWeirdRandomCallCombination() {
        LinkedListDeque<Integer> actual = new LinkedListDeque<>();
        ArrayList<Integer> expected = new ArrayList<>();
        final int times = 100000000;
        // randomize behavior
        for(int i = 0; i < times; i++) {
            int state = StdRandom.uniform(0, 4);
            int val = StdRandom.uniform(-1000, 1000);
            switch (state) {
                case 0: // addFirst
                    expected.add(0, val);
                    actual.addFirst(val);
                    break;
                case 1: // addLast
                    expected.add(expected.size(), val);
                    actual.addLast(val);
                    break;
                case 2: // removeFirst
                    actual.removeFirst();
                    if (!expected.isEmpty()) {
                        expected.remove(0);
                    }
                    break;
                case 3: // removeLast
                    actual.removeLast();
                    if (!expected.isEmpty()) {
                        expected.remove(expected.size() - 1);
                    }
                    break;
                default:
                    break;
            }
        }
        // check size
        assertEquals(expected.size(), actual.size());
        // check value both using get method and getRecursive method
        for (int i = 0; i < expected.size(); i++) {
            if (StdRandom.uniform(0, 1) == 0) {
                assertEquals(expected.get(i), actual.get(i));
            }
            else {
                assertEquals(expected.get(i), actual.getRecursive(i));
            }
        }
    }
}
