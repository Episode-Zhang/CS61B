package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  private static void randomized(AListNoResizing<Integer> L, BuggyAList<Integer> buggy_L) {
      final int MAX_N = 5000;
      for (int i = 0; i < MAX_N; i++) {
          int operation_num = StdRandom.uniform(0, 3);
          if (operation_num == 0) {
              // addLast
              int rand_val = StdRandom.uniform(0, 100);
              L.addLast(rand_val);
              buggy_L.addLast(rand_val);
          } else if (operation_num == 1 && L.size() > 0) {
              // remove last
              L.removeLast();
              buggy_L.removeLast();
          } else if (operation_num == 2) {
              // size
              int size = L.size();
              int buggy_size = buggy_L.size();
          }
      }
  }

    @Test
    public void randomTest1() {
        AListNoResizing<Integer> expected = new AListNoResizing<>();
        BuggyAList<Integer> actual = new BuggyAList<>();
        randomized(expected, actual);

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
