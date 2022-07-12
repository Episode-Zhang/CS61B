package deque;

public interface Deque<T> {
    // Adds an item of type T to the *front* of the deque. Item won't be null
    void addFirst(T item);

    // Adds an item of type T to the *back* of the deque. Item won't be null
    void addLast(T item);

    // Return true iff deque is empty
    boolean isEmpty();

    // Return size of deque
    int size();

    // Print items which are separated by a space. Add newline when all items printed.
    void printDeque();

    // Removes and returns the item at the front of the deque. Return null iff no such item exists.
    T removeFirst();

    // Removes and returns the item at the back of the deque. Return null iff no such item exists.
    T removeLast();

    // Get corresponding item. Return null if it doesn't exist.
    T get(int index);
}
