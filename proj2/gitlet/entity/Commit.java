package gitlet.entity;


import gitlet.exception.GitletException;
import gitlet.pattern.Publisher;
import gitlet.pattern.Subscriber;
import gitlet.utils.Helper;
import gitlet.utils.MemCache;
import gitlet.utils.MessageType;
import gitlet.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * A commit represents a snapshot for those file in staging area,
 * which will be stored and record by linked structure.
 *
 * @author jeffrey-zhang
 * @version 1.0
 */
public class Commit implements Serializable, Publisher {

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

    /** Root Directory to put all commits in .gitlet. */
    public static final String COMMIT_ROOTDIR = "/commits/";

    /** Grabbing first two digits in a commit's sha1 code as name
     *  of the directory in .gitlet/commits/ to save each commit. */
    private static final int DIR_BOUND = 2;

    /** Date parsing pattern. */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Constructor of the Commit, only used in the factory method createFirstCommit and createNonFirstCommit.
     * @see #createFirstCommit()
     * @see #createNonFirstCommit(Commit, String)
     */
    private Commit(String id, Commit child, ArrayList<Commit> parent,
                   String timestamp, String message, TreeMap<File, Blob> table, File dest) {
        this.id = id;
        this.child = child;
        this.parent = parent;
        this.timestamp = timestamp;
        this.message = message;
        this.fileBlobTable = table;
        this.dest = dest;
        // add subscribers
        this.subscribers.add(Refs.getInstance());
    }

    /** Get the child commit of this commit. */
    public Commit getChild() {
        return this.child;
    }

    /** Get the sha1 hash id of this commit. */
    public String getId() {
        return this.id;
    }

    /** Get the timestamp string of this commit. */
    public String getTimestamp() {
        return this.timestamp;
    }

    /** Get the message of this commit. */
    public String getMessage() {
        return this.message;
    }

    /** Get the parent commits of this commit. */
    public ArrayList<Commit> getParent() {
        return this.parent;
    }

    /** Factory method to create an initial commit object. */
    public static Commit init() {
        return createFirstCommit();
    }

    /** Create a child commit linked to this commit. */
    public Commit createChildCommit(String message) {
        return createNonFirstCommit(this, message);
    }

    /**
     * Read commit from disk.
     *
     * @param relativePath the relative path to the commit to be read, given the bash path is .gitlet.
     * @return the corresponding commit object.
     */
    public static Commit load(String relativePath) {
        File commitFile = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, relativePath);
        Commit commit = Utils.readObject(commitFile, Commit.class);
        return commit;
    }

    /**
     * Retrieve file from this commit and put it back into the working directory.
     * Set the checked-out file to be unstaged.
     *
     * @param relativeFilePath the name of the file to be put back into the working directory.
     */
    public void checkout(String relativeFilePath) {
        File file = Utils.join(Helper.ROOT_DIR, relativeFilePath);
        if (!fileBlobTable.containsKey(file)) {
            throw new GitletException("File does not exist in that commit.");
        }
        Blob blob = fileBlobTable.get(file);
        blob.restoreToFile();
        StagingArea.getInstance().remove(relativeFilePath);
    }

    /** Using the commit message, timestamp, files to represent a commit object. */
    @Override
    public String toString() {
        ArrayList<String> files = new ArrayList<>();
        if (fileBlobTable != null) {
            for (File file : fileBlobTable.keySet()) {
                files.add(file.toString().replace(Helper.ROOT_DIR, ""));
            }
        }
        return String.format("commit message: %s\n" +
                "commit time: %s\n" +
                "commit files: %s\n",
                message, timestamp, String.join(", ", files)
        );
    }

    /** Factory method. To create the initial commit. */
    private static Commit createFirstCommit() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        // id
        final String INIT_ID = "-1";
        String sha1_id = Utils.sha1(INIT_ID);
        // timestamp
        final String INIT_TIMEZONE = "UTC";
        formatter.setTimeZone(TimeZone.getTimeZone(INIT_TIMEZONE));
        String timestamp = formatter.format(new Date(0));
        // dest
        String commitName = "root";
        File commitDest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, COMMIT_ROOTDIR);
        // create init commit
        Commit commit = new Commit(sha1_id, null, null,
                timestamp, "initial commit", null, commitDest);
        commit.save(commitName);
        // put this commit into MemCache
        MemCache.getInstance().set("latestCommit", commit);
        // notify all subscribers that a commit has been created
        commit.notifySubscribers(MessageType.INIT);
        commit.notifySubscribers(MessageType.COMMIT);
        return commit;
    }

    /** Factory method. To create a non-initial commit. */
    private static Commit createNonFirstCommit(Commit parent, String message) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        StagingArea stagingArea = StagingArea.getInstance();
        // parent
        ArrayList<Commit> parents = new ArrayList<>();
        parents.add(parent);
        // timestamp
        String timestamp = formatter.format(new Date());
        // file-blob table
        TreeMap<File, Blob> fileBlobTable = stagingArea.moveTable();
        // id
        StringBuilder blobIds = new StringBuilder();
        for (Blob blob : fileBlobTable.values()) {
            blobIds.append(blob.getId());
        }
        String commitId = Utils.sha1(blobIds.toString());
        // saving destination
        String commitDir = COMMIT_ROOTDIR + commitId.substring(0, DIR_BOUND);
        String commitName = commitId.substring(DIR_BOUND);
        File commitDest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, commitDir);
        // create a new commit
        Commit commit = new Commit(commitId, null, parents, timestamp, message, fileBlobTable, commitDest);
        parent.setChild(commit);
        commit.save(commitName);
        // put this commit into MemCache
        MemCache.getInstance().set("latestCommit", commit);
        // notify all subscribers that a commit has been created
        commit.notifySubscribers(MessageType.COMMIT);
        return commit;
    }

    /** Set child commit to this corresponding to the given commit. */
    private void setChild(Commit childCommit) {
        child = childCommit;
    }

    /**
     * save(or serialize, in precise) the commit object to the disk.
     *
     * @param path the path to save the commit, using relative path.
     * @return the saved path of the commit object.
     */
    private String save(String path) {
        if (!dest.exists()) {
            dest.mkdirs();
        }
        final File commit = Utils.join(dest, path);
        Utils.writeObject(commit, this);
        final String commitPath = commit.toString().replace(Helper.ROOT_DIR, "");
        return commitPath;
    }

    /** Subscribers to Commit class. */
    private transient List<Subscriber> subscribers = new ArrayList<>();

    @Override
    public void attach(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void detach(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(MessageType message) {
        for (Subscriber subscriber : subscribers) {
            subscriber.updateSelf(message);
        }
    }
}
