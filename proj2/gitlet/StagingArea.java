package gitlet;

import java.io.File;
import java.util.HashMap;

/**
 * Staged area stores added files with their blob between each two commits.
 * @author jeffery-zhang
 */
public class StagingArea {

    /** Maintains a file-name-to-blob map.  */
    private HashMap<File, Blob> fileStorage;

    /** This class uses singleton pattern. */
    private static StagingArea instance = null;

    /** Constructor, only available inside the StagingArea class. */
    private StagingArea() {
        this.fileStorage = new HashMap<File, Blob>();
    }

    /** The initialization of the unique instance will be delayed to the first time it is used. */
    public static StagingArea getInstance() {
        if (instance == null) {
            instance = new StagingArea();
        }
        return instance;
    }

    /**
     * Add a file to the staged area.
     *
     * @param filename The name of the file to be added.
     * @param blob The blob corresponding to the given filename.
     */
    public void add(String filename, Blob blob) {
        instance.add(filename, blob);
    }

    /**
     * Get the current staging area's file storage, using move semantics.
     *
     * @return The current staging area's file storage.
     */
    public HashMap<File, Blob> getFileStorage() {
        HashMap<File, Blob> storage = this.fileStorage;
        this.fileStorage = new HashMap<>();
        return storage;
    }
}
