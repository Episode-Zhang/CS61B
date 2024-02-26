package gitlet.entity;


import gitlet.exception.GitletException;
import gitlet.utils.Helper;
import gitlet.utils.Utils;

import java.io.File;
import java.io.Serializable;

/**
 * A blob represents the state of a file.
 * Each blob has its unique sha1 hash id, and content of corresponding file.
 *
 * @author jeffrey-zhang
 * @version 1.0 2023-12-03
 */
public class Blob implements Serializable {

    /** Id of this blob, should be sha1 hash of file content. */
    private String id;

    /** Content of corresponding file.  */
    private String content;

    /** Source directory of the file stored by this blob. */
    private File src;

    /** Directory to save the blob, using relative path. */
    private File dest;

    /** Root Directory to put all blobs in .gitlet. */
    private static final String BLOB_ROOTDIR = "/blobs/";

    /** Grabbing first two digits in a blob's sha1 code as name
     *  of the directory in .gitlet/blobs/ to save each blob. */
    private static final int DIR_BOUND = 2;


    /** Constructor of the Blob, only used in the public method createBlob.
     * @see #createBlob(String) */
    private Blob(String id, String content, File src, File dest) {
        this.id = id;
        this.content = content;
        this.src = src;
        this.dest = dest;
    }

    /**
     * Factory method, which creates a new blob from the given file.
     *
     * @param filePath The path of the given file, using relative path.
     * @return The blob object corresponding to the given file.
     * @throws GitletException if the file does not exist.
     */
    public static Blob createBlob(String filePath) throws GitletException {
        // check if the repository has been initialized
        if (!Helper.repoInitialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        // repository has been initialized, generate corresponding blob.
        File blobFile = Helper.getFileByPath(filePath);
        // members
        String fileContent = Utils.readContentsAsString(blobFile);
        String sha1Hash = Utils.sha1(fileContent);
        // use the first 2 bytes of the sha1 hash as the blob saving directory
        // and the last 38 bytes as the blob's file name on disk.
        final String blobDir = BLOB_ROOTDIR + sha1Hash.substring(0, DIR_BOUND);
        final String blobName = sha1Hash.substring(DIR_BOUND);
        File blobDest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, blobDir);
        // As long as the blob created, it will be saved to the disk.
        Blob blob = new Blob(sha1Hash, fileContent, blobFile, blobDest);
        blob.save(blobName);
        return blob;
    }

    /** Using the content of file to represent a blob. */
    @Override
    public String toString() {
        return content;
    }

    /** Get the id of this Blob. */
    public String getId() {
        return id;
    }

    /** Restore the content of the blob to file. */
    public void restoreToFile() {
        Utils.writeContents(src, content);
    }

    /**
     * save(or serialize, in precise) the blob object to the disk.
     *
     * @param path the path to save the blob, using relative path.
     * @return the saved path of the blob object.
     */
    private String save(String path) {
        // check if the destination directory exists, if not, create.
        if (!dest.exists()) {
            dest.mkdirs();
        }
        final File blob = Utils.join(dest, path);
        Utils.writeObject(blob, this);
        final String blobPath = blob.toString().replace(Helper.ROOT_DIR, "");
        return blobPath;
    }
}
