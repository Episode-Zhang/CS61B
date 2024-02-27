package testing.unittest;

import gitlet.entity.Refs;
import org.junit.Test;

public class testRefs {

    @Test
    public void testReadHEAD() {
        System.out.println(Refs.getInstance().getHEAD());
    }

    @Test
    public void testReadBranch() {
        System.out.println(Refs.getInstance().getBRANCH().getCurrentBranchName());
        System.out.println(Refs.getInstance().getBRANCH().getCurrentBranch());
    }
}
