package testing.unittest;


import gitlet.entity.StagingArea;
import gitlet.utils.Helper;
import org.junit.Test;

public class testStagingArea {

    @Test
    public void testAddWithoutCwdMark() throws Helper.FileDoesNotExistException {
        StagingArea stagingArea = StagingArea.getInstance();
        String filePath = "testRepo/Test.java";
        stagingArea.add(filePath);
    }

    @Test
    public void testAddWithCwdMark() throws Helper.FileDoesNotExistException {
        StagingArea stagingArea = StagingArea.getInstance();
        String filePath = "./testRepo/Test.java";
        stagingArea.add(filePath);
    }

    @Test
    public void testReadStagingFiles() {
        StagingArea stagingArea = StagingArea.getInstance();
        // should print name of files in staging area.
        System.out.println(stagingArea.getStagingFileName());
    }

    @Test
    public void testAddMultipleFiles() throws Helper.FileDoesNotExistException {
        StagingArea stagingArea = StagingArea.getInstance();
        String filePath1 = "./testRepo/Test.java";
        String filePath2 = "./testRepo/hi.txt";
        stagingArea.add(filePath1);
        stagingArea.add(filePath2);
    }

    @Test(expected = Helper.FileDoesNotExistException.class)
    public void testAddNonExistingFile() throws Helper.FileDoesNotExistException {
        StagingArea stagingArea = StagingArea.getInstance();
        String filePath = "./testRepo/Tast.java";
        stagingArea.add(filePath);
    }
}
