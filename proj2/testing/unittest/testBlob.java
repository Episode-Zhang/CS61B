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
        Helper.printBlobContent(".gitlet/blobs/32/8a0927acbd3cc158bad216da5d734c52ed9565");
    }

    @Test
    public void testReadWithCwdMark() {
        // should print file contents
        Helper.printBlobContent("./.gitlet/blobs/32/8a0927acbd3cc158bad216da5d734c52ed9565");
    }
}
