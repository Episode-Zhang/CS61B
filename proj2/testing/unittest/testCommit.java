package testing.unittest;

import gitlet.entity.Commit;
import gitlet.exception.GitletException;
import gitlet.entity.StagingArea;
import org.junit.Test;

public class testCommit {

    @Test
    public void testCommitInitialization() {
        Commit commit = Commit.init();
        System.out.println(commit);
    }

    @Test
    public void testCreateCommit() throws GitletException {
        StagingArea stagingArea = StagingArea.getInstance();
        stagingArea.add("./testRepo/Test.java");
        stagingArea.add("./testRepo/hi.txt");
        Commit ancestor = Commit.init();
        ancestor.createChildCommit("test commit");
        System.out.println(ancestor.getChild());
    }
}
