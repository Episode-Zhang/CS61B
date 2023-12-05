package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.TreeMap;

/**
 * Staging Area stores those files added but was not going to commit.
 * This class uses the technique called "singleton pattern",
 * which limits the number of the existing instances and the method to get that only one instance.
 *
 * @author jeffery-zhang
 * @version 1.0
 */
public class StagingArea implements Serializable {

    /** A kv map stores pairs of File-Blob. */
    private static TreeMap<File, Blob> fileBlobTable;

    /** Name of the fileBlobTable after it serialized on the disk.
     * @see #save()  */
    private static final String serializedName = "StagingArea";

    /** The singleton instance of this class. */
    private static StagingArea instance;


    /** Constructor of the Blob, only used in the public method getInstance.
     * @see #getInstance() */
    private StagingArea() {
        fileBlobTable = new TreeMap<>();
    }

    /**
     * Constructor of the Blob, only used in the public method getInstance.
     * @see #getInstance()
     *
     * @param table A TreeMap of File-Blob entries, will be assigned to the fileBlobTable.
     */
    private StagingArea(TreeMap<File, Blob> table) {
        fileBlobTable = table;
    }

    /** The only one method to get the unique StagingArea instance, which associates with the table file on disk. */
    public static StagingArea getInstance() {
        if (instance == null) {
            // initialize the instance
            // read the fileBlobTable from the disk
            File tableFile = Utils.join(Helper.REPO_DIR, serializedName);
            if (!tableFile.exists()) {
                instance = new StagingArea();
            } else {
                TreeMap<File, Blob> diskTable = Utils.readObject(tableFile, TreeMap.class);
                instance = new StagingArea(diskTable);
            }
            return instance;
        }
        return instance;
    }

    /**
     * Create a blob for the given file, and store this file-blob entry in the table.
     * After storing, the table will be saved on the disk.
     *
     * @param filePath the relative path to the file to be stored.
     * @return true if the file was successfully stored, false otherwise.
     * @throws Helper.FileDoesNotExistException if the blob creation failed.
     */
    public boolean add(String filePath) throws Helper.FileDoesNotExistException {
        // TODO Maybe should check nested files in a directory, aka, add a "directory".
        Blob blob = Blob.createBlob(filePath);
        // defensive check
        if (blob != null) {
            File file = Helper.getFileByPath(filePath);
            fileBlobTable.put(file, blob);
            save();
            return true;
        }
        return false;
    }

    /** Get the File-Blob mapping table. */
    public TreeMap<File, Blob> getTable() {
        return fileBlobTable;
    }

    /** Clear the File-Blob mapping table (in move semantics, it doesn't do a real clear). */
    public void clearTable() {
        fileBlobTable = new TreeMap<>();
    }

    /** Save the member variable fileBlobTable to the disk. */
    public void save() {
        File tableFile = Utils.join(Helper.REPO_DIR, serializedName);
        Utils.writeObject(tableFile, fileBlobTable);
    }
}
