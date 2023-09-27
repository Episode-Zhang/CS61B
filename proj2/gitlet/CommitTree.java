package gitlet;

import java.util.Date;




/**
 * Represents a gitlet commit object.
 *
 * @author jeffery-zhang
 */
public class Commit {

    /** The message of this Commit. */
    private String message;

    /** Timestamp of this commit when created. */
    private Date timestamp;

    /** Parent of this commit. */
    private Commit parent;

    /**
     * Constructor of Commit Class.
     *
     * @param message The descriptive message of this Commit.
     * @param parent Parent of this Commit.
     */
    public Commit(String message, Commit parent) {
        // set timestamp to be current timestamp default
        this.timestamp = new Date();
        this.message = message;
        this.parent = parent;
    }


}
