package bstmap;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class BSTMap<K extends Comparable<? super K>, V> implements Map61B<K, V> {

    /** The data structure of node of binary search tree. */
    private static class BSTNode<K extends Comparable<? super K>, V> {
        /** Store K-V pair in this node */
        public K key;
        public V value;
        /** Store references to children in this node. */
        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        /** Default Constructor. */
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            // Children will set to null in default.
        }
    }

    /** Iterator class of BSTMap. It iterates keys of a BSTMap. */
    private class BSTMapIterator implements Iterator<K> {

        /** Store all keys in this BSTMap. */
        private K[] keys;

        private int iterPos;

        /** Record the next insertion place of key for array keys. Only changed
         *  after calling private method visitInOrder. */
        private int lastKeyPos = 0;

        public BSTMapIterator() {
            // All keys will be previously stored when iterator initialized.
            keys = (K[]) new Comparable[size];
            visitInOrder(root, keys);
            iterPos = 0;
        }

        @Override
        public boolean hasNext() {
            return iterPos < keys.length;
        }

        @Override
        public K next() {
            K nextItem = keys[iterPos];
            iterPos += 1;
            return nextItem;
        }

        /**
         * Visit nodes of this BST in order and put them into container.
         *
         * @param node The given node specifies a subtree that will be traversed.
         * @param container The given container is a array that will gather all keys in order.
         */
        private void visitInOrder(final BSTNode<K, V> node, K[] container) {
            if (node.left != null) {
                visitInOrder(node.left, container);
            }
            container[lastKeyPos++] = node.key;
            if (node.right != null) {
                visitInOrder(node.right, container);
            }
        }
    }

    private BSTNode<K, V> root;
    private int size;

    /** Default constructor. */
    public BSTMap() { this.size = 0; }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() { return this.size; }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return search(this.root, key) != null;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key. */
    public V get(K key) {
        BSTNode<K, V> target = search(this.root, key);
        return target == null ? null : target.value;
    }

    /** Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        this.root = insert(this.root, key, value);
        this.size += 1;
    }

    /** Removes the mapping for the specified key from this map if present.
     *  Not required for Lab 7. If you don't implement this, throw an
     *  UnsupportedOperationException. */
    public V remove(K key) {
        BSTNode<K, V> target = search(this.root, key);
        if (target != null) {
            this.root = delete(this.root, key);
            this.size -= 1;
        }
        return target == null ? null : target.value;
    }

    /** Removes the entry for the specified key only if it is currently mapped to
      * the specified value. Not required for Lab 7. If you don't implement this,
      * throw an UnsupportedOperationException.*/
    public V remove(K key, V value) {
        BSTNode<K, V> target = search(this.root, key);
        if (target != null && target.value == value) {
            this.root = delete(this.root, key);
            this.size -= 1;
            return target.value;
        }
        return null;
    }

    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    /** Returns a Set view of the keys contained in this map. Not required for Lab 7.
      * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (K key : this) {
            keys.add(key);
        }
        return keys;
    }

    /** Print all data stored in this BSTMap in order of increasing key. */
    public void printInOrder() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        String[] stringOfItems = new String[this.size];
        int index = 0;
        for (K key : this) {
            V value = get(key);
            stringOfItems[index++] = String.format("{%s: %s}", key.toString(), value.toString());
        }
        return "[" + String.join(", ", stringOfItems) + "]";
    }

    /**
     * To search the node specified by the given key.
     *
     * @param startNode The start place where a search will begin.
     * @param key To identify the search target to be the node containing the given key.
     * @return The node containing the given key if search hits, otherwise, null.
     */
    private BSTNode<K, V> search(final BSTNode<K, V> startNode, final K key) {
        if (startNode == null) {
            return null;
        }
        if (key.compareTo(startNode.key) < 0) {
            return search(startNode.left, key);
        } else if (key.compareTo(startNode.key) > 0) {
            return search(startNode.right, key);
        } else {
            return startNode;
        }
    }

    /**
     * To insert a node containing the given K-V pair to the subtree rooted at the
     * given startNode.
     *
     * @param startNode Determine the subtree where an insertion begins.
     * @param key The key of K-V pair that will be inserted.
     * @param value The value of K-V pair that will be inserted.
     * @return The node after inserting the node containing given K-V pair.
     */
    private BSTNode<K, V> insert(BSTNode<K, V> startNode, final K key, final V value) {
        if (startNode == null) {
            return new BSTNode<>(key, value);
        }
        if (key.compareTo(startNode.key) < 0) {
            startNode.left = insert(startNode.left, key, value);
        } else if (key.compareTo(startNode.key) > 0){
            startNode.right = insert(startNode.right, key, value);
        } else {
            startNode.value = value;
        }
        return startNode;
    }

    /**
     * To delete a node containing the given K-V pair to the subtree rooted at the
     * given startNode.
     *
     * @param startNode Determine the subtree where a deletion begins.
     * @param key The key of K-V pair that will be deleted.
     * @return The node after deleting the node containing given K-V pair.
     */
    private BSTNode<K, V> delete(BSTNode<K, V> startNode, final K key) {
        if (startNode == null) {
            return null;
        }
        if (key.compareTo(startNode.key) < 0) {
            startNode.left = delete(startNode.left, key);
        } else if (key.compareTo(startNode.key) > 0) {
            startNode.right = delete(startNode.right, key);
        } else {
            if (startNode.left == null) {
                startNode = startNode.right;
            } else if (startNode.right == null) {
                startNode = startNode.left;
            } else {
                BSTNode<K, V> successor = getSuccessor(startNode);
                successor.left = startNode.left;
                successor.right = startNode.right;
                startNode = successor;
            }
        }
        return startNode;
    }

    /** To get successor node of the given node. */
    private BSTNode<K, V> getSuccessor(final BSTNode<K, V> node) {
        BSTNode<K, V> successor = min(node.right);
        node.right = deleteMin(node.right);
        return successor;
    }

    /** To get the minimum-key containing node in the subtree rooted at the given node. */
    private BSTNode<K, V> min(final BSTNode<K, V> node) {
        BSTNode<K, V> loopNode = node;
        while (loopNode.left != null) {
            loopNode = loopNode.left;
        }
        return loopNode;
    }

    /** To delete the minimum-key containing node in the subtree rooted at the given node. */
    private BSTNode<K, V> deleteMin(BSTNode<K, V> node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return node;
    }
}
