package deque;

import java.util.Iterator;


public interface Deque<T> {
    public void addFirst(T item);
    public void addLast(T item);
    public boolean isEmpty();
    public int size();
    public void printDeque();   // print each item separated by a space, print a new line when finished
    public T removeFirst();
    public T removeLast();
    public T get(int index);
    public Iterator<T> iterator();
    public boolean equals(Object obj); // equal if o is deque and contain same items with same order
}
