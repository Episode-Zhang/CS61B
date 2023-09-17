package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * The Blob Stores content of a specified file.
 * @author jeffery-zhang
 */
public class Blob implements Serializable {

    /** Name of this blob, represented by sha1 hash value of blob-corresponding file's content. */
    private String blobName;

    /** Content of this blob, storing series of bytes of corresponding file' content. */
    private String blobContent;

    /** Directory to save blob file(using relative path). */
    private File destination;

    /** Set root directory to be current working director. */
    private static final String ROOTDIR = System.getProperty("user.dir");

    /**
     * Constructor of Blob class. The constructor initialize the destination directory to save the
     * blob file by using ./gitlet/blob/{xx}/{yyyy...} while "xx" represents the first two characters
     * of blobName, "yyyy..." represents the remaining characters of blobName. In addition, you can
     * change the default destination directory by using method {@link #setDestination(String)}
     *
     * @param filePath The relative path of file to be added.
     */
    public Blob(String filePath) {
        // check if repository has initialized
        if (!Utils.join(ROOTDIR, ".gitlet").exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            // Allow creating .gitlet folder by "add" command in debugging mode
            if (!Utils.debuggingMode()) {
                System.exit(0);
            }
        }
        File file = getExisitingFile(filePath);
        // get the content of the origin file.
        String content = Utils.readContentsAsString(file);
        // set the name, content and destination of the blob
        this.blobName = Utils.sha1(content);
        this.blobContent = content;
        this.destination = Utils.join(ROOTDIR, ".gitlet", "blob", this.blobName.substring(0, 2));
    }

    /**
     * Set new destination to save the blob file.
     *
     * @param newDestination New destination, using relative path.
     * @return The relative path of saved blob.
     */
    public void setDestination(String newDestination) {
        this.destination = Utils.join(ROOTDIR, newDestination);
    }

    /** Serialize the blob instance. */
    public String save() throws IOException {
        if (!this.destination.exists()) {
            boolean success = this.destination.mkdirs();
            // Debugging mode
            if (Utils.debuggingMode() && !success) {
                System.out.println("Error: Failed to create destination directory: " + this.destination);
                System.exit(-1);
            }
        }
        File blobFile = Utils.join(this.destination.toString(), this.blobName.substring(2));
        Utils.writeObject(blobFile, this);
        return blobFile.toString().replace(ROOTDIR, "");
    }

    /** Log the content of this blob file into terminal. */
    public void catContent() {
        System.out.println(this.blobContent);
    }

    /**
     * Log the content of blob related to given path into terminal.
     *
     * @param blobPath The relative path of the blob to be checking.
     */
    public static void catContent(String blobPath) throws IOException {
        File blobFile = Utils.join(ROOTDIR, blobPath);
        if (!blobFile.exists()) {
            if (Utils.debuggingMode()) {
                throw new IOException("Blob file doesn't exist: " + blobFile);
            } else {
                System.exit(0);
            }
        }
        Blob blob = Utils.readObject(blobFile, Blob.class);
        System.out.println(blob.blobContent);
    }

    /**
     * Check if the given file path exists, exiting if there is no such file,
     * otherwise, return the File type of the given file path.
     *
     * @param filePath The file path to check, using relative paths.
     * @throws RuntimeException When given filePath is not exist in debugging mode.
     * @return The File type of the given file path.
     */
    private File getExisitingFile(String filePath) throws RuntimeException {
        final File file = Utils.join(ROOTDIR, filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            if (Utils.debuggingMode()) {
                throw new RuntimeException("File does not exist: " + filePath);
            }
            System.exit(0);
        }
        return file;
    }
}
