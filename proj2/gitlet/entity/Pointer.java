package gitlet.entity;

import gitlet.pattern.Publisher;
import gitlet.pattern.Subscriber;
import gitlet.utils.Helper;
import gitlet.utils.MemCache;
import gitlet.utils.MessageType;
import gitlet.utils.Utils;

import java.io.File;

/**
 * This class stores those pointers and references of the gitlet, e.g. HEAD.
 *
 * @author jeffrey zhang
 * @version 1.0
 */
public class Pointer implements Subscriber {

    private static volatile Pointer instance;

    private Commit HEAD;

    private Pointer() {
        // load HEAD from the disk if it exists
        final File HEAD_FILE = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, "HEAD");
        if (HEAD_FILE.exists()) {
            HEAD = Utils.readObject(HEAD_FILE, Commit.class);
        }
    }

    public static Pointer getInstance() {
        if (instance == null) {
            synchronized (Pointer.class) {
                if (instance == null) {
                    instance = new Pointer();
                }
            }
        }
        return instance;
    }

    /** Get the HEAD Pointer. */
    public static Commit getHEAD() {
        return Pointer.getInstance().HEAD;
    }

    @Override
    public void subscribeTo(Publisher publisher) {
        publisher.attach(this);
    }

    @Override
    public void updateSelf(MessageType message) {
        switch (message) {
            case COMMIT:
                // retrieve the latest commit from MemCache and update the HEAD pointer
                Commit latestCommit = (Commit) MemCache.getInstance().get("latestCommit");
                HEAD = latestCommit;
                // write HEAD to the disk
                File dest = Utils.join(Helper.ROOT_DIR, Helper.REPO_DIR, "HEAD");
                Utils.writeObject(dest, HEAD);
                break;
            default:
                break;
        }
    }
}
