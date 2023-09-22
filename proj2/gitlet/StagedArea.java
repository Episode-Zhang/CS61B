package gitlet;

import java.io.File;
import java.util.HashMap;

/**
 * Staged area stores added files with their blob between each two commits.
 * @author jeffery-zhang
 */
public class StagedArea {

    /** Maintains a file-name-to-blob map.  */
    private HashMap<File, Blob> fileStorage;

    /** This class uses singleton pattern. */
    private static StagedArea instance = null;

    /** Constructor. */
    private StagedArea() {}

    /** The initialization of the unique instance will be delayed to the first time it is used. */
    public static StagedArea getInstance() {
        if (instance == null) {
            instance = new StagedArea();
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
}
