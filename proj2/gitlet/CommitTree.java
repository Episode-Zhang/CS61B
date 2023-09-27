package gitlet;


import java.io.File;
import java.util.Date;
import java.util.HashMap;


/**
 * Organize all commits into a tree structure.
 *
 * @author jeffery-zhang
 */
public class CommitTree {
    /** Root of the Commit Tree. */
    private CommitNode root;

    /** The instance of the CommitTree should be unique. */
    private static CommitTree instance;

    /** The initialization will be delayed in its first being requested time. */
    private CommitTree() {
        this.root = new CommitNode("initial commit", new HashMap<>(), null);
    }

    /** Get the unique instance. */
    public static CommitTree getInstance() {
        if (instance == null) {
            // initialize
            instance = new CommitTree();
        }
        return instance;
    }

}
