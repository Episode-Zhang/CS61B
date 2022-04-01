package deque;

import java.util.Iterator;

public class LinkedListDeque<T>/* implements Deque<T>*/ {
    // nested node class
    private static class Node<T> {
        private T _item;
        private Node<T> _prev;
        private Node<T> _next;
        public Node(T item, Node<T> prev, Node<T> next) {
            _item = item;
            _prev = prev;
            _next = next;
        }
    }
    // attributes
    private Node<T> _sentinel;
    private int _size;
    // methods
    public LinkedListDeque() {
        _sentinel = new Node<>(null, null, null);
        _sentinel._prev = _sentinel;
        _sentinel._next = _sentinel;
        _size = 0;
    }

    public void addFirst(T item) {  // O(1)
        _sentinel._next = new Node<>(item, _sentinel, _sentinel._next);
        // After insertion, the original first one arranged from
        // "_sentinel._next" to "_sentinel._next._next" as _sentinel._next to be inserted.
        _sentinel._next._next._prev = _sentinel._next;
        _size += 1;
    }

    public void addLast(T item) {   // O(1)
        _sentinel._prev = new Node<>(item, _sentinel._prev, _sentinel);
        _sentinel._prev._prev._next = _sentinel._prev;
        _size += 1;
    }

    public boolean isEmpty() {      // O(1)
        return _size == 0;
    }

    public int size() {             // O(1)
        return _size;
    }

    public void printDeque() {      // print each item separated by a space, print a new line when finished
        Node<T> cur_node = _sentinel._next;
        String content = "";
        while (cur_node != _sentinel) {
            content += cur_node._item + " ";
            cur_node = cur_node._next;
        }
        System.out.println(content + '\n');
    }
    public T removeFirst() {        // O(1)
        Node<T> first_node = _sentinel._next;
        first_node._next._prev = first_node._prev;
        _sentinel._next = first_node._next;
        _size -= 1;
        return first_node._item;
    }

    public T removeLast() {         // O(1)
        Node<T> last_node = _sentinel._prev;
        last_node._prev._next = last_node._next;
        _sentinel._prev = last_node._prev;
        // up to here, all reference pointing to last node have been removed
        _size -= 1;
        return last_node._item;
    }

    public T get(int index) {
        Node<T> cur_node = _sentinel._next;
        for (int i = 0; i < index; i++) {
            cur_node = cur_node._next;
        }
        return cur_node._item;
    }
    //public Iterator<T> iterator();  // O(n)
    //public boolean equals(Object obj); // equal if o is deque and contain same items with same order

    public static void main(String[] args) {
        LinkedListDeque<String> l = new LinkedListDeque<>();
        l.addFirst("Hello");
        l.addLast("world");
        l.addFirst("program");
        l.addFirst("first");
        l.addFirst("my");
        l.addLast("Hello");
        l.addLast("Java");
        l.removeFirst();    // my
        l.removeLast();     // java
        l.removeLast();     // Hello
        l.removeFirst();    // first
        l.removeFirst();    // program
        l.printDeque();
    }
}
