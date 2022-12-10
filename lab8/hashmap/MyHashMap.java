package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Episode Zhang
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] _buckets;
    // You should probably define some more!

    /** Number of K-V pairs stored in this HashMap. */
    private int _size;

    /** Number of buckets in this HashMap. */
    private int _capacity;

    /** Max load of this HashMap. */
    private final double _max_load;

    /** Constructors */
    public MyHashMap() {
        // set default capacity and load factor
        _capacity = 16;
        _max_load = 0.75;
        _size = 0;
        _buckets = createTable(_capacity);
    }

    /** Constructors with a given initial capacity. */
    public MyHashMap(int initialSize) {
        // Assuming that initialSize is legal. Usually it should be examined.
        _capacity = initialSize;
        _max_load = 0.75;
        _size = 0;
        _buckets = createTable(_capacity);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        // Assuming that initialSize & maxLoad are legal.
        _capacity = initialSize;
        _max_load = maxLoad;
        _size = 0;
        _buckets = createTable(_capacity);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        // Use AList in default
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        // Uniformed entrance of initializing a HashMap-buckets structure.
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return _size;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        // reset the capacity to its default value
        _capacity = 16;
        // empty other instance variables
        _size = 0;
        _buckets = createTable(_capacity);
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return find(_buckets, key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return find(_buckets, key);
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        // Do volume check ahead of insertion.
        if (isOversized()) {
            // Each time expanded, double the current _capacity.
            resize(_capacity * 2);
        }
        if (insert(_buckets, key, value)) {
            _size += 1;
        }
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        V value = delete(_buckets, key);
        if (value != null) {
            _size -= 1;
        }
        return value;
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        if (delete(_buckets, key, value) != null) {
            _size -= 1;
            return value;
        }
        return null;
    }

    /** Iterator class of MyHashMap. */
    private class HashMapIterator implements Iterator<K> {

        /** All keys in current HashMap. */
        private K[] keys = (K[]) keySet().toArray();

        /** Position of current wizard. */
        private int pos = 0;

        @Override
        public boolean hasNext() {
            return pos < keys.length;
        }

        @Override
        public K next() {
            return keys[pos++];
        }
    }

    /** Iterator method of this HashMap. */
    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }


    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Collection<Node> bucket : _buckets) {
            if (bucket != null) {
                for (Node node : bucket) {
                    keys.add(node.key);
                }
            }
        }
        return keys;
    }

    /** Hash a given key and return its bucket-index. */
    private int hash(K key) {
        // use simple modulo operation.
        return Math.floorMod(key.hashCode(), _capacity);
    }

    /** Hash a given key by a given modulo number. */
    private int hash(K key, int modulo) {
        return Math.floorMod(key.hashCode(), modulo);
    }

    /** Find record in a given table by a given key. If it doesn't exist, return null.*/
    private V find(Collection<Node>[] buckets, K key) {
        int key_hash = hash(key, buckets.length);
        Collection<Node> bucket = buckets[key_hash];
        // Iterate only when bucket isn't null
        if (bucket != null) {
            for (Node node : bucket) {
                if (key.equals(node.key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    /** Check if the size of a bucket exceeds the max load. */
    private boolean isOversized() {
        double load_factor = (double) _size / _capacity;
        return load_factor > _max_load;
    }

    /** Resize the table by given capacity. */
    private void resize(int newCapacity) {
        Collection<Node>[] newBuckets = createTable(newCapacity);
        moveRecords(_buckets, newBuckets);
        // update instance variables
        _capacity = newCapacity;
        _buckets = newBuckets;
    }

    /** Move all K-V pairs from table origin to table destination. */
    private void moveRecords(Collection<Node>[] origin, Collection<Node>[] destination) {
        // For each bucket in origin table
        for (Collection<Node> bucket : origin) {
            if (bucket != null) {
                // for each record in origin's bucket
                for (Node node : bucket) {
                    int capacity = destination.length;
                    int destination_key_hash = hash(node.key, capacity);
                    // destination's corresponding bucket is null, create bucket
                    if (destination[destination_key_hash] == null) {
                        destination[destination_key_hash] = createBucket();
                    }
                    // moving to a new table, one can directly add without checking duplicates
                    destination[destination_key_hash].add(node);
                }
            }
        }
    }

    /**
     * Solely Insert a K-V pair into a table (leaving the size check alone).
     * Return true if it indeed did an insertion, or, false if it just updated the
     * previously inserted K-V pair.
     */
    private boolean insert(Collection<Node>[] buckets, K key, V value) {
        // Promise that the load factor of buckets is within the max load.
        Node newNode = createNode(key, value);
        int key_hash = hash(key);
        // handle the null bucket
        if (buckets[key_hash] == null) {
            buckets[key_hash] = createBucket();
            buckets[key_hash].add(newNode);
        } else {
            // K-V record already exists, update it and return.
            for (Node node : buckets[key_hash]) {
                if (key.equals(node.key)) {
                    node.value = value;
                    return false;
                }
            }
            // new record, just insert it.
            buckets[key_hash].add(newNode);
        }
        return true;
    }

    /** Delete a record specialized by given key in the given buckets. */
    private V delete(Collection<Node>[] buckets, K key) {
        int key_hash = hash(key);
        Collection<Node> bucket = buckets[key_hash];
        // Check the bucket further only when it is not empty.
        if (bucket != null) {
            for (Node node : bucket) {
                if (key.equals(node.key)) {
                    V value = node.value;
                    bucket.remove(node);
                    return value;
                }
            }
        }
        return null;
    }

    /** Delete a record specialized by given key and value in the given buckets. */
    private V delete(Collection<Node>[] buckets, K key, V value) {
        int key_hash = hash(key);
        Collection<Node> bucket = buckets[key_hash];
        // Check the bucket further only when it is not empty.
        if (bucket != null) {
            for (Node node : bucket) {
                if (key.equals(node.key) && value.equals(node.value)) {
                    bucket.remove(node);
                    return value;
                }
            }
        }
        return null;
    }

}
