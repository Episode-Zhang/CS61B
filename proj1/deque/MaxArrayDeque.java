package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    // instance variable
    private Comparator<T> _compatator;

    /**
     * Consturctor of MaxArrayDeque
     *
     * @param c: Identify the comparator and pass it to a MaxArrayDeque.
     */
    public MaxArrayDeque(Comparator<T> c) {
        super();
        _compatator = c;
    }

    /**
     * To get the maximum of this ArrayDeque based on comparator given in constructor.
     *
     * @return The max item of this ArrayDeque.
     */
    public T max() {
        T maxItem = this.get(0);
        for (T item : this) {
            if (_compatator.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }

    /**
     * To get the maximum of this ArrayDeque based on user-defined comparator.
     *
     * @param c: The comparator to identity the bigger one while doing comparison.
     * @return The max item of this ArrayDeque.
     */
    public T max(Comparator<T> c) {
        T maxItem = this.get(0);
        for (T item : this) {
            if (c.compare(item, maxItem) > 0) {
                maxItem = item;
            }
        }
        return maxItem;
    }
}
