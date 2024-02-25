package testing.unittest;

import gitlet.entity.Commit;
import org.junit.Test;

public class testPointer {

    @Test
    public void testReadPointer() {
        String file = "HEAD";
        Commit commit = Commit.load(file);
        System.out.println(commit);
    }
}
