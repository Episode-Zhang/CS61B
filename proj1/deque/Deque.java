package deque;

import java.util.Iterator;


public interface Deque<T> {
    public void addFirst(T item);   // O(1) for LinkedList
    public void addLast(T item);    // O(1) for LinkedList
    public boolean isEmpty();       // O(1) for both
    public int size();              // O(1) for both
    public void printDeque();       // print each item separated by a space, print a new line when finished
    public T removeFirst();         // O(1) for LinkedList
    public T removeLast();          // O(1) for LinkedList
    public T get(int index);        // O(n) for LinkedList
}
