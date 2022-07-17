package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    // Inner nested Node class
    private static class Node<T> {
        private T _item;
        private Node<T> _next;
        private Node<T> _prev;

        Node(T item, Node<T> prev, Node<T> next) {
            _item = item;
            _prev = prev;
            _next = next;
        }
    }

    // Iterator class
    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> currentNode;

        /**
         * Constructor of LinkedListDequeIterator generator.
         */
        LinkedListDequeIterator() {
            currentNode = _sentinel;
        }

        /**
         * To judge whether a LinkedListDeque is entirely visited.
         *
         * @return TRUE if a LinkedListDeque hasn't been entirely visited, otherwise, FALSE.
         */
        @Override
        public boolean hasNext() {
            return currentNode._next != _sentinel;
        }

        /**
         * Get the next item.
         *
         * @return The next item.
         */
        @Override
        public T next() {
            currentNode = currentNode._next;
            return currentNode._item;
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
     * Add an item to the front of a particular LinkedListDeque.
     *
     * @param item: The item which is supposed to be added as first one in LinkedListDeque.
     */
    @Override
    public void addFirst(T item) {
        Node<T> firstAdded = new Node<>(item, _sentinel, _sentinel._next);
        _sentinel._next._prev = firstAdded;
        _sentinel._next = firstAdded;
        _size += 1;
    }

    /**
     * Add an item to the back of a particular LinkedListDeque.
     *
     * @param item: The item which is supposed to be added as last in LinkedListDeque.
     */
    @Override
    public void addLast(T item) {
        Node<T> lastAdded = new Node<>(item, _sentinel._prev, _sentinel);
        _sentinel._prev._next = lastAdded;
        _sentinel._prev = lastAdded;
        _size += 1;
    }

    /**
     * Give size of a LinkedListDeque.
     *
     * @return Size of a LinkedListDeque.
     */
    @Override
    public int size() {
        return _size;
    }

    /**
     * Remove the first item of the LinkedListDeque.
     *
     * @return The removed item value if there exists any item, otherwise, null.
     */
    @Override
    public T removeFirst() {
        if (_size == 0) {
            return null;
        }
        T firstItem = _sentinel._next._item;
        _sentinel._next = _sentinel._next._next;
        _sentinel._next._prev = _sentinel;
        _size -= 1;
        return firstItem;
    }

    /**
     * Remove the last item of the LinkedListDeque.
     *
     * @return The removed item value if there exists any item, otherwise, null.
     */
    @Override
    public T removeLast() {
        if (_size == 0) {
            return null;
        }
        T lastItem = _sentinel._prev._item;
        _sentinel._prev = _sentinel._prev._prev;
        _sentinel._prev._next = _sentinel;
        _size -= 1;
        return lastItem;
    }

    /**
     * Get corresponding item based on its index in LinkedListDeque.
     *
     * @param index: Location of the item you want to get, beginning with 0.
     * @return Corresponding item value if there exists such an index, otherwise, null.
     */
    @Override
    public T get(int index) {
        Node<T> currentNode = _sentinel;
        // according to where indexed item located to decided iterate from head or tail.
        if (index < _size / 2) {
            for (int i = -1; i < index; i++) {   // i = -1 because node begins with sentinel.
                currentNode = currentNode._next;
            }
        } else {
            for (int i = 0; i < _size - index; i++) {
                currentNode = currentNode._prev;
            }
        }
        // if index is illegal, then the method would return _sentinel._item, which is null.
        return currentNode._item;
    }

    /**
     * Get corresponding item based on its index in LinkedListDeque but recursively.
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
     * @param index:        Location of the item you want to get, beginning with 0.
     * @param currentNode: To memorized which current node is.
     * @return Corresponding Node with specified index.
     */
    private Node<T> getRecursive(int index, Node<T> currentNode, final String direction) {
        if (index == 0) {
            return currentNode;
        }
        // Decide direction
        if (direction.equals("FORWARD")) {
            return getRecursive(index - 1, currentNode._next, direction);
        } else if (direction.equals("BACKWARD")) {
            return getRecursive(index - 1, currentNode._prev, direction);
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
     * To generate an iterator for this LinkedListDeque.
     *
     * @return Iterator of the LinkedListDeque.
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /**
     * To judge whether a given object is equal to this LinkedListDeque.
     *
     * @param o: The given object
     * @return TRUE if o is LinkedListDeque which contains same items in same order to this.
     */
    @Override
    public boolean equals(Object o) {
        // Do previous checks
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> another = (Deque<T>) o;
        if (_size != another.size()) {
            return false;
        }
        // Check items
        Node<T> myCurrentNode = _sentinel._next;
        for (int i = 0; i < another.size(); i++) {
            T item = another.get(i);
            if (!item.equals(myCurrentNode._item)) {
                return false;
            }
            myCurrentNode = myCurrentNode._next;
        }
        return true;
    }
}
