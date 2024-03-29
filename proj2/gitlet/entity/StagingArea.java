package gitlet.entity;


import gitlet.exception.GitletException;
import gitlet.utils.Helper;
import gitlet.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Staging Area stores those files added but was not going to commit.
 * This class uses the technique called "singleton pattern",
 * which limits the number of the existing instances and the method to get that only one instance.
 *
 * @author jeffrey-zhang
 * @version 1.0
 */
public class StagingArea implements Serializable {

    /** A kv map stores pairs of File-Blob. */
    private static TreeMap<File, Blob> fileBlobTable;

    /** Name of the fileBlobTable after it serialized on the disk.
     * @see #save()  */
    private static final String serializedName = "StagingArea";

    /** The singleton instance of this class. */
    private static volatile StagingArea instance;


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
            synchronized (StagingArea.class) {
                if (instance == null) {
                    // initialize the instance
                    // read the fileBlobTable from the disk
                    File tableFile = Utils.join(Helper.REPO_DIR, serializedName);
                    if (!tableFile.exists()) {
                        instance = new StagingArea();
                        instance.save();
                    } else {
                        TreeMap<File, Blob> diskTable = Utils.readObject(tableFile, TreeMap.class);
                        instance = new StagingArea(diskTable);
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Create a blob for the given file, and store this file-blob entry in the table.
     * After storing, the table will be saved on the disk.
     *
     * @param filePath the relative path to the file to be stored.
     * @return true if the file was successfully stored, false otherwise.
     * @throws GitletException if the blob creation failed.
     */
    public boolean add(String filePath) throws GitletException {
        // TODO Maybe should check nested files in a directory, aka, add a "directory".
        filePath = filePath.replace("./", "");
        Blob blob = Blob.createBlob(filePath);
        // defensive check
        // TODO check the version of the file.
        if (blob != null) {
            File file = Helper.getFileByPath(filePath);
            fileBlobTable.put(file, blob);
            save();
            return true;
        }
        return false;
    }

    /**
     * Set corresponding file to be unstaged in staging area.
     *
     * @param filePath the relative path to the file that is supposed to be staged.
     * @return true if the file was successfully set to be unstaged, otherwise, false.
     */
    public boolean remove(String filePath) {
        filePath = filePath.replace("./", "");
        File blobFile = Utils.join(Helper.ROOT_DIR, filePath);
        if (!fileBlobTable.containsKey(blobFile)) {
            return false;
        }
        fileBlobTable.remove(blobFile);
        return true;
    }

    /** Get all files in the staging area. */
    public List<File> getStagedFiles() {
        return new ArrayList<>(fileBlobTable.keySet());
    }

    /** Moving contents in the fileBlob into another table. */
    public TreeMap<File, Blob> moveTable() {
        TreeMap<File, Blob> originalTable = fileBlobTable;
        fileBlobTable = new TreeMap<>();
        save();
        return originalTable;
    }

    /** Get the name of files which are stored in the staging area. */
    public ArrayList<String> getStagingFileName() {
        ArrayList<String> fileNames = new ArrayList<>(fileBlobTable.size());
        for (File file : fileBlobTable.keySet()) {
            fileNames.add(file.toString().replace(Helper.ROOT_DIR, ""));
        }
        return fileNames;
    }

    /** Save the member variable fileBlobTable to the disk. */
    private void save() {
        File tableFile = Utils.join(Helper.REPO_DIR, serializedName);
        Utils.writeObject(tableFile, fileBlobTable);
    }
}
