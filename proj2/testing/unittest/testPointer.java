package testing.unittest;

import gitlet.entity.Commit;
import gitlet.entity.Pointer;
import org.junit.Test;

public class testPointer {

    @Test
    public void testReadPointer() {
        System.out.println(Pointer.getHEAD());
    }
}
