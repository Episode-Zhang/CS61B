package gitlet;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.text.SimpleDateFormat;

/**
 * A commit represents a snapshot for those file in staging area,
 * which will be stored and record by linked structure.
 *
 * @author jeffery-zhang
 * @version 1.0
 */
public class Commit {

    /** Id of this blob, should be sha1 hash based on all blobs' id. */
    private String id;

    /** Child commit of this commit. */
    private Commit child;

    /** Parent commit of this commit, might be multiple. */
    private ArrayList<Commit> parent;

    /** Timestamp when this commit generates. */
    private String timestamp;

    /** Commit message. */
    private String message;

    /** Mapping for File to Blob. */
    private TreeMap<File, Blob> fileBlobTable;


    /** Constructor of the Commit, only used in the public method createCommit.
     * @see #createCommit(Commit, String, StagingArea) */
    private Commit(String id, Commit child, ArrayList<Commit> parent,
                   String timestamp, String message, TreeMap<File, Blob> table) {
        this.id = id;
        this.child = child;
        this.parent = parent;
        this.timestamp = timestamp;
        this.message = message;
        this.fileBlobTable = table;
    }

    /**
     * Factory method, which creates a new Commit.
     *
     * @param parent the parent commit of this commit.
     * @param message the commit message.
     * @param stagingArea the file-blob map for this commit.
     * @return a Commit instance.
     */
    public Commit createCommit(Commit parent, String message, StagingArea stagingArea) {
        // create id of the commit instance
        StringBuilder blobIds = new StringBuilder();
        for (Blob blob : stagingArea.getTable().values()) {
            blobIds.append(blob.getId());
        }
        String commitId = Utils.sha1(blobIds.toString());
        // create parent of the commit instance
        ArrayList<Commit> parents = new ArrayList<>();
        parents.add(parent);
        // create timestamp of the commit instance
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timestamp = formatter.format(new Date());
        // create file-blob map for this commit
        TreeMap<File, Blob> fileBlobTable = stagingArea.getTable();
        // create id for this commit
        Commit commit = new Commit(commitId, null, parents, timestamp, message, fileBlobTable);
        commit.save();
        return commit;
    }



    private void addChild(Commit childCommit) {
        child = childCommit;
    }

    private void addParent(Commit parentCommit) {
        parent.add(parentCommit);
    }


    private void save() {
        // TODO
    }




}
