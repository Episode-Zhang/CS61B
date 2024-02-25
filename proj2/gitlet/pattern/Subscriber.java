package gitlet.pattern;

import gitlet.utils.MessageType;

/**
 * Abstract subscriber class for Observer Pattern.
 *
 * @author jeffrey zhang
 * @version 1.0
 */
public interface Subscriber {

    /** Subscribe to a specific publisher. */
    void subscribeTo(Publisher publisher);

    /** Update self by given message. */
    void updateSelf(MessageType message);
}
