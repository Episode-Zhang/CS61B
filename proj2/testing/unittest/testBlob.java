package testing.unittest;


import gitlet.entity.Blob;
import gitlet.exception.GitletException;
import gitlet.utils.Helper;
import gitlet.utils.Utils;
import org.junit.Test;

import java.io.File;

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
        Helper.printBlobContent(".gitlet/blobs/85/9c7220e246737ebb8b79bd7160e61a41ad5abf");
    }

    @Test
    public void testReadWithCwdMark() {
        // should print file contents
        Helper.printBlobContent("./.gitlet/blobs/85/9c7220e246737ebb8b79bd7160e61a41ad5abf");
    }

    @Test
    public void testRestoreToFile() {
        File blobFile = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR,
                "/blobs/85/9c7220e246737ebb8b79bd7160e61a41ad5abf");
        Blob blob = Utils.readObject(blobFile, Blob.class);
        blob.restoreToFile();
    }
}
