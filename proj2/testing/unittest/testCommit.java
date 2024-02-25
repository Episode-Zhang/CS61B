package testing.unittest;

import gitlet.entity.Commit;
import gitlet.utils.Helper;
import gitlet.entity.StagingArea;
import org.junit.Test;

public class testCommit {

    @Test
    public void testCommitInitialization() {
        Commit commit = Commit.init();
        System.out.println(commit);
    }

    @Test
    public void testCreateCommit() throws Helper.FileDoesNotExistException {
        StagingArea stagingArea = StagingArea.getInstance();
        stagingArea.add("./testRepo/Test.java");
        stagingArea.add("./testRepo/hi.txt");
        Commit ancestor = Commit.init();
        Commit commit = Commit.createCommit(ancestor, "test commit", stagingArea);
        System.out.println(commit);
    }
}
