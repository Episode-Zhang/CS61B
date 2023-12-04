package testing.unittest;


import gitlet.StagingArea;
import gitlet.Helper;
import org.junit.Test;

public class testStagingArea {

    @Test
    public void testNormalAdd1() throws Helper.FileDoesNotExistException {
        final StagingArea stagingArea = StagingArea.getInstance();
        final String filePath = "testRepo/Test.java";
        stagingArea.add(filePath);
    }

    @Test
    public void testNormalAdd2() throws Helper.FileDoesNotExistException {
        final StagingArea stagingArea = StagingArea.getInstance();
        final String filePath = "./testRepo/Test.java";
        stagingArea.add(filePath);
    }
}
