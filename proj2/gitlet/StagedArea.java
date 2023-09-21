package gitlet;

import java.io.File;
import java.util.TreeMap;

/**
 * Staged area stores added files with their blob between each two commits.
 * @author jeffery-zhang
 */
public class StagedArea {

    private TreeMap<File, Blob> tree;

    private static StagedArea instance = null;

    private StagedArea() {}

    public static StagedArea getInstance() {
        if (instance == null) {
            instance = new StagedArea();
        }
        return instance;
    }
}
