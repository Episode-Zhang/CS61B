package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    // Iterator class
    private class ArrayDequeIterator implements Iterator<T> {
        private int _nowPos;
        private int _steps;

        /**
         * Constructor of ArrayDequeIterator generator.
         */
        ArrayDequeIterator() {
            _nowPos = Math.floorMod((phead() + 1), _capacity);
            _steps = 0;
        }

        /**
         * To judge whether an ArrayDeque is entirely visited.
         *
         * @return TRUE if an ArrayDeque hasn't been entirely visited, otherwise, FALSE.
         */
        @Override
        public boolean hasNext() {
            // (_nowPos != ptail()) && _size > 0
            return _steps < _size;
        }

        /**
         * Get the next item.
         *
         * @return The next item.
         */
        @Override
        public T next() {
            T value = _items[_nowPos];
            _nowPos = Math.floorMod(_nowPos + 1, _capacity);
            _steps += 1;
            return value;
        }
    }

    // instance variables
    private T[] _items;
    private int _size;
    private int _capacity;
    private int _phead;
    private int _ptail;
    private static final double USAGE_PERCENT = 0.25;
    private static final int RESIZE_FACTOR = 2;
    private static final int MIN_CAPACITY = 8;

    /**
     * Constructor of ArrayDeque.
     */
    public ArrayDeque() {
        /* In the below I will give some properties of my ADeque with "**" mark represents to
         * emphasis.
         *
         * 1. The deque will be implemented in *circular topology* on array, which means once
         *    the array is *not null*, *addFirst* method will *loop back* to array, result
         *    in adding new items from back of the array to its front. eg, adding [1, 2, 3, 4]
         *    with addFirst method, the items stores like: [1, 4, 3, 2].
         *
         * 2. The method addLast just insert items in natural order, aka, adding [1, 2, 3, 4]
         *    with addLast will shape the array as [1, 2, 3, 4].
         *
         * 3. Variable _PHEAD always points to the *head* position of the array while _PTAIL
         *    always points to the *tail* position of the array.
         *
         * 4. Define that the *head* position refers to the position of item that *next addFirst*
         *    operation will insert in. Similarly, the *tail* position refers to the position
         *    of item that *next addLast* operation will insert in.
         *
         * 5. Each addFirst operation accounts for _PHEAD minus 1, symmetrically, each addLast
         *    operation accounts for _PTAIL plus 1.
         *
         * 6. At the beginning when array is of empty size, _PHEAD equals to _CAPACITY and
         *    _PTAIL equals to 0. Adding item to a null array is *considered both* addFirst
         *    and addLast operations, therefore, _PHEAD will minus 1 as well as _PTAIL will
         *    plus 1, and this is the *only occasion* of add-operation such that one operation
         *    accounts for two pointers changed.
         *
         * 7. The situation of remove operation can be inferred symmetrically.
         *
         * 8. One thing should be notice is that the purpose of setting _PHEAD and _PTAIL is to
         *    simplify our work on recording add/remove-operations, however, they can rarely be
         *    used directly. Consider a simple case:
         *      At the beginning we have _PHEAD equals to _CAPACITY and _PTAIL equals to 0, and
         *      then we do addLast(1), addLast(2). Now we have array like [1, 2, x, x, x, x, x, x,],
         *      "x" representing null. At this moment we have _PHEAD = 7 and _PTAIL = 2. Next,
         *      we do removeFirst() twice, 1 would be removed first with 2 coming second. But
         *      after remove operations, _PHEAD will equal to 9 as well as _PTAIL equals to 1(The
         *      last remove regarded both removeFirst and remove Last). Obviously, _PHEAD is neither
         *      within the bound since _CAPACITY equals to 8 nor can we locate the address of item
         *      1 and 2 through value of _PHEAD.
         *    Here we can see that the add/remove-operations don't promise to async with the true
         *    index to be inserted of the array. Actually the occasion of what is mentioned is just
         *    one possible case of all. What we can indeed use is the value of two pointers
         *    *modulo* _CAPACITY. It's easy to check the validation as you go back to case above.
         *
         * 9. According to existing analysis, we can easily drive to such an inequality about
         *    _PHEAD and _PTAIL:
         *      0 <= _PTAIL - 1 <= _PHEAD + 1
         *
         * 10. If we get that _PTAIL ≡ _PHEAD(mod _CAPACITY), then there must be array is of full
         *    size or empty.
         */
        _capacity = MIN_CAPACITY;  // default capacity
        _size = 0;
        _items = (T[]) new Object[_capacity];
        _phead = _capacity;
        _ptail = 0;
    }

    /**
     * Manage the position while calling method attached with "First" in array.
     *
     * @return The next position to be inserted for addFirst.
     */
    private int phead() {
        return Math.floorMod(_phead, _capacity);
    }

    /**
     * Manage the position while calling method attached with "Last" in array.
     *
     * @return The next position to be inserted for addLast.
     */
    private int ptail() {
        return Math.floorMod(_ptail, _capacity);
    }

    /**
     * To adjust capacity of this array dynamically according to actual size.
     */
    private void resize(final int newCapacity) {
        T[] newItem = (T[]) new Object[newCapacity];
        // copied items in natural order from head to tail.
        int i = 0;
        for (T item : this) {
            newItem[i++] = item;
        }
        // reset phead and ptail in NEWITEM.
        _phead = newCapacity - 1;
        _ptail = _size;
        // reset other instance variables
        _items = newItem;
        _capacity = newCapacity;
    }

    /**
     * Add an item to the front of the ArrayDeque.
     *
     * @param item: The item which is supposed to be added as first one in ArrayDeque.
     */
    @Override
    public void addFirst(T item) {
        // check capacity
        if (_size >= _capacity) {
            resize(_capacity * RESIZE_FACTOR);
        }
        _items[phead()] = item;
        _phead -= 1;
        // Deal with special case size == 0
        // In an empty array, the first location is equivalent to the last location.
        if (_size == 0) {
            _ptail += 1;
        }
        _size += 1;
    }

    /**
     * Add an item to the back of the ArrayDeque.
     *
     * @param item: The item which is supposed to be added as last one in ArrayDeque.
     */
    @Override
    public void addLast(T item) {
        // check capacity
        if (_size >= _capacity) {
            resize(_capacity * RESIZE_FACTOR);
        }
        _items[ptail()] = item;
        _ptail += 1;
        // Deal with special case size == 0
        // In an empty array, the first location is equivalent to the last location.
        if (_size == 0) {
            _phead -= 1;
        }
        _size += 1;
    }

    /**
     * Remove the first item of the ArrayDeque.
     *
     * @return The removed item value if there exists any item, otherwise, null.
     */
    @Override
    public T removeFirst() {
        if (_size == 0) {
            return null;
        }
        // check capacity
        if (_size <= _capacity * USAGE_PERCENT && _size > MIN_CAPACITY) {
            resize(_capacity / RESIZE_FACTOR);
        }
        int removedPosition = Math.floorMod(phead() + 1, _capacity);
        T value = _items[removedPosition];
        _items[removedPosition] = null;
        _phead += 1;
        // The only item both to be the first item and the last item.
        if (_size == 1) {
            _ptail -= 1;
        }
        _size -= 1;
        return value;
    }

    /**
     * Remove the last item of the ArrayDeque.
     *
     * @return The removed item value if there exists any item, otherwise, null.
     */
    @Override
    public T removeLast() {
        if (_size == 0) {
            return null;
        }
        // check capacity
        if (_size <= _capacity * USAGE_PERCENT && _size > MIN_CAPACITY) {
            resize(_capacity / RESIZE_FACTOR);
        }
        int removedPosition = Math.floorMod(ptail() - 1, _capacity);
        T value = _items[removedPosition];
        _items[removedPosition] = null;
        _ptail -= 1;
        // The only item both to be the first item and the last item.
        if (_size == 1) {
            _phead += 1;
        }
        _size -= 1;
        return value;
    }

    /**
     * To judge whether an ArrayDeque has at least single item.
     *
     * @return TRUE if there is at least one item, otherwise FALSE.
     *//*
    @Override
    public boolean isEmpty() {
        return _size == 0;
    }*/

    /**
     * Give size of an ArrayDeque.
     *
     * @return Size of an ArrayDeque.
     */
    @Override
    public int size() {
        return _size;
    }

    /**
     * Get corresponding item based on its index in ArrayDeque.
     *
     * @param index: Location of the item you want to get, beginning with 0.
     * @return Corresponding item value if there exists such an index, otherwise, null.
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= _size) {
            return null;
        }
        int offsetIndex = Math.floorMod(phead() + 1 + index, _capacity);
        return _items[offsetIndex];
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
     * To generate an iterator for this ArrayDeque.
     *
     * @return Iterator of the ArrayDeque.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /**
     * To judge whether a given object(especially a ArrayDeque) is equal to this ArrayDeque.
     *
     * @param obj: The given object
     * @return TRUE if o is ArrayDeque which contains same items in same order to this.
     */
    @Override
    public boolean equals(Object obj) {
        // Do previous checks
        if (!(obj instanceof Deque)) {
            return false;
        }
        Deque<T> another = (Deque<T>) obj;
        if (_size != another.size()) {
            return false;
        }
        // check items
        int myPos = Math.floorMod(phead() + 1, _capacity);
        Iterator<T> anotherIter = another.iterator();
        while (anotherIter.hasNext()) {
            T item = anotherIter.next();
            if (!item.equals(_items[myPos])) {
                return false;
            }
            myPos = Math.floorMod(myPos + 1, _capacity);
        }
        return true;
    }
}
