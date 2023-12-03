package testing.unittest;


import gitlet.Blob;
import gitlet.Helper;
import org.junit.Test;
import java.io.IOException;


public class testBlob {

    @Test
    public void testNormalAdd1() throws Helper.FileDoesNotExistException {
        Blob blob = Blob.createBlob("testRepo/Test.java");
        Helper.printBlobContent(blob);
    }

    @Test
    public void testNormalAdd2() throws Helper.FileDoesNotExistException {
        Blob blob = Blob.createBlob("./testRepo/Test.java");
        Helper.printBlobContent(blob);
    }

    @Test
    public void testErrorAdd1() {
        try {
            Blob blob = Blob.createBlob("testRepo/Tast.java");
            Helper.printBlobContent(blob);
        } catch (Helper.FileDoesNotExistException e) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }
}
