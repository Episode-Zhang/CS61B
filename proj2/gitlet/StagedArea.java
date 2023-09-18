package gitlet;

import java.io.File;
import java.util.TreeMap;

/**
 * Staged area stores added files with their blob between each two commits.
 * @author jeffery-zhang
 */
public class StagedArea {
    // TODO: Should use skeleton mode.
    TreeMap<File, Blob> tree;

    public StagedArea() {
        
    }
}
