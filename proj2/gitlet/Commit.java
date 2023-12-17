package gitlet;


import java.io.File;
import java.io.Serializable;
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
public class Commit implements Serializable {

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

    /** Directory to save the commit, using relative path. */
    private File dest;


    /** Constructor of the Commit, only used in the public method createCommit.
     * @see #createCommit(Commit, String, StagingArea) */
    private Commit(String id, Commit child, ArrayList<Commit> parent,
                   String timestamp, String message, TreeMap<File, Blob> table, File dest) {
        this.id = id;
        this.child = child;
        this.parent = parent;
        this.timestamp = timestamp;
        this.message = message;
        this.fileBlobTable = table;
        this.dest = dest;
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
        // create parent of the commit instance
        ArrayList<Commit> parents = new ArrayList<>();
        parents.add(parent);
        // create timestamp of the commit instance
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timestamp = formatter.format(new Date());
        // create file-blob map for this commit
        TreeMap<File, Blob> fileBlobTable = stagingArea.moveTable();
        // create id for this commit
        StringBuilder blobIds = new StringBuilder();
        for (Blob blob : fileBlobTable.values()) {
            blobIds.append(blob.getId());
        }
        String commitId = Utils.sha1(blobIds.toString());
        // create saving destination for this commit
        int dirBound = 2;
        String commitDir = "commits/" + commitId.substring(0, dirBound);
        String commitName = commitId.substring(dirBound);
        File commitDest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, commitDir);
        // create a new commit
        Commit commit = new Commit(commitId, null, parents, timestamp, message, fileBlobTable, commitDest);
        commit.save(commitName);
        return commit;
    }

    /** Using the commit message, timestamp, files to represent a commit object. */
    @Override
    public String toString() {
        ArrayList<String> files = new ArrayList<>(fileBlobTable.keySet().size());
        for (File file : fileBlobTable.keySet()) {
            files.add(file.toString());
        }
        return String.format("commit message: %s\n" +
                "commit time: %s\n" +
                "commit files: %s\n",
                message, timestamp, String.join(", ", files)
        );
    }

    /**
     * save(or serialize, in precise) the commit object to the disk.
     *
     * @param path the path to save the commit, using relative path.
     * @return the saved path of the commit object. */
    private String save(String path) {
        if (!dest.exists()) {
            dest.mkdirs();
        }
        final File commit = Utils.join(dest, path);
        Utils.writeObject(commit, this);
        final String blobPath = commit.toString().replace(Helper.ROOT_DIR, "");
        return blobPath;
    }
}
