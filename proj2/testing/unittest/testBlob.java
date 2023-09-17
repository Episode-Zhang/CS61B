package testing.unittest;
import gitlet.Blob;
import org.junit.Test;
import java.io.IOException;


public class testBlob {

    @Test
    public void testNormalAdd1() throws IOException {
        Blob blob = new Blob("testRepo/Test.java");
        String path = blob.save();
        Blob.catContent(path);
    }

    @Test
    public void testNormalAdd2() throws IOException {
        Blob blob = new Blob("./testRepo/Test.java");
        String path = blob.save();
        Blob.catContent(path);
    }

    @Test
    public void testErrorAdd1() throws IOException {
        Blob blob = new Blob("testRepo/Tast.java");
        String path = blob.save();
        Blob.catContent(path);
    }
}
