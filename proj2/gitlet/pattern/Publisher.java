package gitlet.pattern;

import gitlet.utils.MessageType;

import java.util.List;
import java.util.ArrayList;

/**
 * Abstract publisher class for Observer Pattern.
 *
 * @author jeffrey zhang
 * @version 1.0
 */
public interface Publisher {
    /** Add subscriber to this publisher. */
    void attach(Subscriber subscriber);

    /** Remove subscriber to this publisher. */
    void detach(Subscriber subscriber);

    /** Notify all subscriber by specific message. */
    void notifySubscribers(MessageType message);
}
