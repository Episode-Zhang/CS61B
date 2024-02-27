package testing.unittest;

import gitlet.entity.Commit;
import gitlet.entity.Refs;
import gitlet.exception.GitletException;
import gitlet.entity.StagingArea;
import gitlet.utils.Helper;
import gitlet.utils.Utils;
import org.junit.Test;

import java.io.File;

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

    @Test
    public void testReadCommit() {
        String filePath = "commits/d6/4aa51b7e5668a1f5401785c72d60bd177cb865";
        Commit commit = Commit.load(filePath);
        System.out.println(commit);
    }

    @Test
    public void testCheckoutFileInHEAD() {
        Commit HEAD = Refs.getInstance().getHEAD();
        HEAD.checkout("/testRepo/Test.java");
        System.out.println(StagingArea.getInstance().getStagedFiles());
    }
}
