package gitlet;


import java.io.File;
import java.util.Date;
import java.util.HashMap;

/** The node structure of commit. */
public class CommitNode {
    /** The message of this Commit. */
    private String message;

    /** Timestamp of this commit when created. */
    private Date timestamp;

    /** Stores the mapping relationship between files and its corresponding blobs. */
    private HashMap<File, Blob> fileToBlobMap;

    /** Parent of this commit. */
    private CommitTree parent;

    /**
     * Constructor of CommitNode Class.
     *
     * @param message The descriptive message of this CommitNode.
     * @param parent Parent node of this CommitNode.
     */
    public CommitNode(String message, HashMap<File, Blob> map, CommitTree parent) {
        this.fileToBlobMap = map;
        this.message = message;
        this.parent = parent;
        // set timestamp to be current timestamp default, except the root node
        this.timestamp = this.isRoot() ? new Date(0) : new Date();
    }

    /** Check if this node is the root node. */
    private boolean isRoot() {
        return this.parent == null;
    }
}
