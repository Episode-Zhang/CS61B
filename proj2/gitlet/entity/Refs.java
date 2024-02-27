package gitlet.entity;

import gitlet.pattern.Publisher;
import gitlet.pattern.Subscriber;
import gitlet.utils.Helper;
import gitlet.utils.MemCache;
import gitlet.utils.MessageType;
import gitlet.utils.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class stores those pointers and references of the gitlet, e.g. HEAD.
 *
 * @author jeffrey zhang
 * @version 1.0
 */
public class Refs implements Subscriber {

    private static volatile Refs instance;

    /** Directory to store HEAD and branches on the disk, relative to .gitlet. */
    private static final String POINTER_DIR = "refs";

    /** HEAD pointer. */
    private Commit HEAD;

    /** BRANCH pointers. */
    private Branch branch;

    /** Branch class abstracts the branch structure in the gitlet project. */
    public static class Branch implements Serializable {
        /** Stores current branch name in the gitlet. */
        private String currentBranchName;

        /** All branches in the gitlet. */
        private HashMap<String, Commit> branches;

        public Branch(String name, Commit commit) {
            currentBranchName = name;
            branches = new HashMap<String, Commit>() {{
                put(currentBranchName, commit);
            }};
        }

        public String getCurrentBranchName() {
            return currentBranchName;
        }

        public void setCurrentBranchName(String name) {
            currentBranchName = name;
        }

        public void updateCurrentBranch(Commit commit) {
            branches.put(currentBranchName, commit);
        }

        public Commit getCurrentBranch() {
            return branches.get(currentBranchName);
        }
    }

    private Refs() {
        // load HEAD and BRANCH from the disk if it exists
        HEAD = readHEAD();
        branch = readBRANCH();
    }

    public static Refs getInstance() {
        if (instance == null) {
            synchronized (Refs.class) {
                if (instance == null) {
                    instance = new Refs();
                }
            }
        }
        return instance;
    }

    /** Get the HEAD pointer. */
    public Commit getHEAD() {
        return Refs.getInstance().HEAD;
    }

    /** Get the BRANCH object. */
    public Branch getBRANCH() {
        return Refs.getInstance().branch;
    }

    @Override
    public void subscribeTo(Publisher publisher) {
        publisher.attach(this);
    }

    @Override
    public void updateSelf(MessageType message) {
        switch (message) {
            case INIT: {
                // create master branch
                Commit latestCommit = (Commit) MemCache.getInstance().get("latestCommit");
                branch = new Branch("master", latestCommit);
                // write branch to the disk
                writeBRANCH();
                break;
            }
            case COMMIT: {
                // retrieve the latest commit from MemCache and update the HEAD pointer
                Commit latestCommit = (Commit) MemCache.getInstance().get("latestCommit");
                HEAD = latestCommit;
                // write HEAD to the disk
                writeHEAD();
                // update branch
                branch.updateCurrentBranch(latestCommit);
                writeBRANCH();
                break;
            }
            default:
                break;
        }
    }

    /** Read HEAD from the disk. */
    private Commit readHEAD() {
        final File HEAD_FILE = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, POINTER_DIR, "HEAD");
        if (HEAD_FILE.exists()) {
            return Utils.readObject(HEAD_FILE, Commit.class);
        }
        return null;
    }

    /** Write HEAD to the disk. */
    private void writeHEAD() {
        File dest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, POINTER_DIR);
        if (!dest.exists()) {
            dest.mkdir();
        }
        File headFile = Utils.join(dest, "HEAD");
        Utils.writeObject(headFile, HEAD);
    }

    /** Read BRANCH from the disk. */
    private Branch readBRANCH() {
        final File BRANCH_FILE = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, POINTER_DIR, "branch");
        if (BRANCH_FILE.exists()) {
            return Utils.readObject(BRANCH_FILE, Branch.class);
        }
        return null;
    }

    /** Write BRANCH to the disk. */
    private void writeBRANCH() {
        File dest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, POINTER_DIR);
        if (!dest.exists()) {
            dest.mkdir();
        }
        File branchFile = Utils.join(dest, "branch");
        Utils.writeObject(branchFile, branch);
    }
}
