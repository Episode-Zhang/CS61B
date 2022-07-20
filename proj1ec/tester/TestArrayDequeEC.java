package tester;

import org.junit.Test;
import static org.junit.Assert.*;
import student.StudentArrayDeque;
import edu.princeton.cs.introcs.StdRandom;

public class TestArrayDequeEC {
    private static final int initCapacity = 8;

    @Test
    public void testResizeWithRemoveTwice() {
        StudentArrayDeque<Integer> studentADeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> trueADeque = new ArrayDequeSolution<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < initCapacity; i++) {
            int randint = StdRandom.uniform(0, initCapacity);
            studentADeque.addFirst(randint);
            trueADeque.addFirst(randint);
            sb.append("addFirst(" + randint + ")\n");
        }
        // first remove runs healthily
        sb.append("removeLast()\n");
        assertEquals(sb.toString(), trueADeque.removeLast(), studentADeque.removeLast());
        sb.append("removeLast()");
        assertEquals(sb.toString(), trueADeque.removeLast(), studentADeque.removeLast());
    }
}
