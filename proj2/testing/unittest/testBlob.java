package testing.unittest;


import gitlet.entity.Blob;
import gitlet.exception.GitletException;
import gitlet.utils.Helper;
import org.junit.Test;

public class testBlob {

    @Test
    public void testSaveWithoutCwdMark() throws GitletException {
        Blob blob = Blob.createBlob("testRepo/Test.java");
        // should print file contents
        Helper.printBlobContent(blob);
    }

    @Test
    public void testSaveWithCwdMark() throws GitletException {
        Blob blob = Blob.createBlob("./testRepo/Test.java");
        // should print file contents
        Helper.printBlobContent(blob);
    }

    @Test(expected = GitletException.class)
    public void testSaveWithNonExistingFile() throws GitletException {
        Blob blob = Blob.createBlob("testRepo/Tast.java");
        // should raise an exception
        Helper.printBlobContent(blob);
    }

    @Test
    public void testReadWithoutCwdMark() {
        // should print file contents
        Helper.printBlobContent(".gitlet/blobs/18/3ba7a5395fb4dd0a1062fb04164577d7654b73");
    }

    @Test
    public void testReadWithCwdMark() {
        // should print file contents
        Helper.printBlobContent("./.gitlet/blobs/18/3ba7a5395fb4dd0a1062fb04164577d7654b73");
    }
}
