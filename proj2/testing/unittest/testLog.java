package testing.unittest;

import gitlet.entity.Commit;
import gitlet.utils.Log;
import org.junit.Test;

public class testLog {

    @Test
    public void testLogSingle() {
        String path = "commits/b2/eaf1af628ac51458e1c5c1dd88152bfffe0095";
        Commit commit = Commit.load(path);
        System.out.println(Log.getLog(commit));
    }

    @Test
    public void testPrintLogStack() {
        String path = "commits/b2/eaf1af628ac51458e1c5c1dd88152bfffe0095";
        Commit commit = Commit.load(path);
        Log.printLogStack(commit);
    }
}
