package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    // Inner nested Node class
    private static class Node<T> {
        public T _item;
        public Node<T> _next;
        public Node<T> _prev;

        public Node(T item, Node<T> prev, Node<T> next) {
            _item = item;
            _prev = prev;
            _next = next;
        }
    }

    // Iterator class
    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> current_node;

        /**
         * Constructor of LinkedListDequeIterator generator.
         */
        public LinkedListDequeIterator() {
            current_node = _sentinel;
        }

        /**
         * To judge whether a LinkedListDeque is entirely visited.
         */
        @Override
        public boolean hasNext() {
            return current_node._next != _sentinel;
        }

        /**
         * Get the next item.
         *
         * @return The next item.
         */
        @Override
        public T next() {
            current_node = current_node._next;
            return current_node._item;
        }
    }

    // instance variables
    private Node<T> _sentinel;
    private int _size;

    /**
     * Constructor of LinkedListDeque.
     */
    public LinkedListDeque() {
        _size = 0;
        _sentinel = new Node<>(null, null, null);
        // circular sentinel, make it a loop
        _sentinel._next = _sentinel;
        _sentinel._prev = _sentinel;
    }

    /**
     * Add an item to the front of a particular linkedlistdeque.
     *
     * @param item: The item which is supposed to be added as first in linkedlistdeque.
     */
    @Override
    public void addFirst(T item) {
        Node<T> first_added = new Node<>(item, _sentinel, _sentinel._next);
        _sentinel._next._prev = first_added;
        _sentinel._next = first_added;
        _size += 1;
    }

    /**
     * Add an item to the back of a particular linkedlistdeque.
     *
     * @param item: The item which is supposed to be added as last in linkedlistdeque.
     */
    @Override
    public void addLast(T item) {
        Node<T> last_added = new Node<>(item, _sentinel._prev, _sentinel);
        _sentinel._prev._next = last_added;
        _sentinel._prev = last_added;
        _size += 1;
    }

    /**
     * To judge whether a linkedlistdeque has at least single item.
     *
     * @return TRUE if there is at least one item, otherwise FALSE.
     */
    @Override
    public boolean isEmpty() {
        return _size == 0;
    }

    /**
     * Give size of a linkedlistdeque.
     *
     * @return Size of a linkedlistdeque.
     */
    @Override
    public int size() {
        return _size;
    }

    /**
     * Remove the first item of the linkedlistdeque.
     *
     * @return The removed item value if there exists any item, otherwise, null.
     */
    @Override
    public T removeFirst() {
        if (_size == 0) {
            return null;
        }
        T first_item = _sentinel._next._item;
        _sentinel._next = _sentinel._next._next;
        _sentinel._next._prev = _sentinel;
        _size -= 1;
        return first_item;
    }

    /**
     * Remove the last item of the linkedlistdeque.
     *
     * @return The removed item value if there exists any item, otherwise, null.
     */
    @Override
    public T removeLast() {
        if (_size == 0) {
            return null;
        }
        T last_item = _sentinel._prev._item;
        _sentinel._prev = _sentinel._prev._prev;
        _sentinel._prev._next = _sentinel;
        _size -= 1;
        return last_item;
    }

    /**
     * Get corresponding item based on its index in linkedlistdeque.
     *
     * @param index: Location of the item you want to get, beginning with 0.
     * @return Corresponding item value if there exists such an index, otherwise, null.
     */
    @Override
    public T get(int index) {
        Node<T> current_node = _sentinel;
        // according to where indexed item located to decided iterate from head or tail.
        if (index < _size / 2) {
            for (int i = -1; i < index; i++) {   // i = -1 because node begins with sentinel.
                current_node = current_node._next;
            }
        } else {
            for (int i = 0; i < _size - index; i++) {
                current_node = current_node._prev;
            }
        }
        // if index is illegal, then the method would return _sentinel._item, which is null.
        return current_node._item;
    }

    /**
     * Get corresponding item based on its index in linkedlistdeque but recursively.
     *
     * @param index: Location of the item you want to get, beginning with 0.
     * @return Corresponding item value if there exists such an index, otherwise, null.
     */
    public T getRecursive(int index) {
        // null cases
        if (index < 0 || index >= _size) {
            return null;
        }
        Node<T> target = null;
        // Choose direction based on position of index
        if (index < _size / 2) {
            target = getRecursive(index, _sentinel._next, "FORWARD");
            return target._item;
        } else {
            target = getRecursive(_size - index - 1, _sentinel._prev, "BACKWARD");
            return target._item;
        }
    }

    /**
     * Handle method of public getRecursive version with current node memorized.
     *
     * @param index: Location of the item you want to get, beginning with 0.
     * @param current_node: To memorized which current node is.
     * @return Corresponding Node with specified index.
     */
    private Node<T> getRecursive(int index, Node<T> current_node, final String direction) {
        if (index == 0) {
            return current_node;
        }
        // Decide direction
        if (direction.equals("FORWARD")) {
            return getRecursive(index - 1, current_node._next, direction);
        } else if (direction.equals("BACKWARD")) {
            return getRecursive(index - 1, current_node._prev, direction);
        } else {
            throw new IllegalArgumentException(
                    "The argument \"direction\" must be selected from either FORWARD of BACKWARD ");
        }
    }

    /**
     * Print all items separated with a space to console, ended with newline.
     */
    @Override
    public void printDeque() {
        StringBuilder contents = new StringBuilder();
        for (T item : this) {
            contents.append(item.toString()).append(" ");
        }
        contents.deleteCharAt(contents.length() - 1);
        contents.append('\n');
        System.out.println(contents);
    }

    /**
     * To generate an iterator for this linkedlistdeque.
     *
     * @return Iterator of the linkedlistdeque.
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * To judge whether a given object(especially a linkedlistdeque) is equal to this linkedlistdeque.
     * @param o: The given object
     * @return   TRUE if o is linkedlistdeque which contains same items in same order.
     */
    @Override
    public boolean equals(Object o) {
        // Do previous checks
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<T> another = (LinkedListDeque<T>) o;
        if (_size != another.size()) {
            return false;
        }
        // Check items
        Node<T> my_current_node = _sentinel._next;
        for (T item : another) {
            if (!item.equals(my_current_node._item)) {
                return false;
            }
            my_current_node = my_current_node._next;
        }
        return true;
    }

}
