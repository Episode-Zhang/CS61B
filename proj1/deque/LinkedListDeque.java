package deque;

import java.util.Iterator;


public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
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

    private class LinkedListDequeIterator implements Iterator<T> {
        private int pos;
        public LinkedListDequeIterator() {
            pos = 0;
        }
        @Override
        public boolean hasNext() {
            return pos < _size;
        }
        @Override
        public T next() {
            T return_item = null;
            // optimize the speed on getting last node
            if (pos == _size - 1) {
                return_item = _sentinel._prev._item;
            }
            else {
                Node<T> cur_node = _sentinel._next;
                for (int i = 0; i < pos; i++) {
                    cur_node = cur_node._next;
                }
                return_item = cur_node._item;
            }
            pos += 1;
            return return_item;
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

    @Override
    public void addFirst(T item) {  // O(1)
        _sentinel._next = new Node<>(item, _sentinel, _sentinel._next);
        // After insertion, the original first one arranged from
        // "_sentinel._next" to "_sentinel._next._next" as _sentinel._next to be inserted.
        _sentinel._next._next._prev = _sentinel._next;
        _size += 1;
    }

    @Override
    public void addLast(T item) {   // O(1)
        _sentinel._prev = new Node<>(item, _sentinel._prev, _sentinel);
        _sentinel._prev._prev._next = _sentinel._prev;
        _size += 1;
    }

    @Override
    public boolean isEmpty() {      // O(1)
        return _size == 0;
    }

    @Override
    public int size() {             // O(1)
        return _size;
    }

    @Override
    public void printDeque() {      // print each item separated by a space, print a new line when finished
        Node<T> cur_node = _sentinel._next;
        String content = "";
        while (cur_node != _sentinel) {
            content += cur_node._item + " ";
            cur_node = cur_node._next;
        }
        System.out.println(content + '\n');
    }

    @Override
    public T removeFirst() {        // O(1)
        if (_size == 0) {
            return null;
        }
        Node<T> first_node = _sentinel._next;
        first_node._next._prev = first_node._prev;
        _sentinel._next = first_node._next;
        _size -= 1;
        return first_node._item;
    }

    @Override
    public T removeLast() {         // O(1)
        if (_size == 0) {
            return null;
        }
        Node<T> last_node = _sentinel._prev;
        last_node._prev._next = last_node._next;
        _sentinel._prev = last_node._prev;
        // up to here, all reference pointing to last node have been removed
        _size -= 1;
        return last_node._item;
    }

    @Override
    public T get(int index) {
        Node<T> cur_node = _sentinel._next;
        for (int i = 0; i < index; i++) {
            cur_node = cur_node._next;
        }
        return cur_node._item;
    }

    public T getRecursive(int index) {
        return getRecursive(index, _sentinel._next);
    }

    private T getRecursive(int index, Node<T> node) {
        if (index == 0) {
            return node._item;
        }
        return getRecursive(index - 1, node._next);
    }

    @Override
    public Iterator<T> iterator() { // O(n)
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object obj) { // equal if o is deque and contain same items with same order
        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof LinkedListDeque) || _size != ((LinkedListDeque<T>) obj).size()) {
            return false;
        }
        else {
            Iterator<T> my_iter = iterator();
            Iterator<T> obj_iter = ((LinkedListDeque<T>) obj).iterator();
            while (my_iter.hasNext() && obj_iter.hasNext()) {
                T my_value = my_iter.next();
                T obj_value = obj_iter.next();
                if (!my_value.equals(obj_value)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString() {
        String[] content = new String[_size];
        int i = 0;
        for (T ele : this ) {
            content[i++] = ele.toString();
        }
        return "[" + String.join(", ", content) + "]";
    }

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
        //l.printDeque();
        System.out.println(l);
    }
}
