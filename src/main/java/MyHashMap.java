import java.util.*;
import java.util.stream.Collectors;

/**
 * Custom {@code HashMap} implementation.
 */
public class MyHashMap<K, V> {

    /**
     * The default initial capacity of {@code MyHashMap}.
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * The load factor.
     */
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * The number of key-value pairs stored in this map.
     */
    private int size;

    /**
     * The array of buckets in which key-value pairs (nodes) are stored.
     */
    private Node<K, V>[] table;

    /**
     * Constructs an empty {@code MyHashMap} with default initial capacity.
     */
    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    /**
     * Constructs an empty {@code MyHashMap} with specified initial capacity.
     *
     * @param initialCapacity the initial capacity
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    @SuppressWarnings({"unchecked"})
    public MyHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        } else if (initialCapacity < DEFAULT_CAPACITY) {
            initialCapacity = DEFAULT_CAPACITY;
        }

        table = (Node<K, V>[]) new Node[initialCapacity];
    }

    /**
     * The node class used for storing key-value pairs in {@code MyHashMap}.
     */
    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final String toString() {
            return key + " - " + value;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;

            return o instanceof Map.Entry<?, ?> entry
                    && key.equals(entry.getKey())
                    && value.equals(entry.getValue());
        }
    }

    /**
     * Returns the number of key-value pairs in this {@code MyHashMap}
     *
     * @return Returns the number of key-value pairs
     */
    public int size() {
        return size;
    }

    /**
     * Creates a new key-value pair in this map or changes value if key-value pair with same key already exists.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @throws IllegalArgumentException if key or value is null
     */
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Illegal key or value: null");
        }

        // check if this map needs to be resized
        if (checkIfResizeNeeded()) {
            resize();
        }

        //put key-value pair in this map
        putInTable(table, key, value);
    }

    /**
     * Removes the key-value pair associated with this key if present.
     *
     * @param key key whose key-value pair is to be removed from the {@code MyHashMap}
     * @throws IllegalArgumentException if key is null
     */
    public void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key: null");
        }

        //remove key-value pair from this map
        removeNode(key);
    }

    /**
     * Returns the value with which the specified key is associated.
     * Returns {@code null} if there is no such key in this map.
     *
     * @param key key to be used for search of a key-value pair
     * @return Returns the value with which specified key is associated or null if there is no such key.
     * @throws IllegalArgumentException if key is null
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key: null");
        }

        // get key-value pair with this key
        Node<K, V> currentNode = getNode(key);

        return currentNode != null ? currentNode.value : null;
    }

    /**
     * Returns {@code true} if this {@code MyHashMap} contains key-value pair with this key.
     *
     * @param key key to be used for search of a key-value pair
     * @return Returns {@code true} if map contains key-value pair with this key or {@code false} otherwise.
     * @throws IllegalArgumentException if the key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key: null");
        }

        // get key-value pair with this key
        Node<K, V> currentNode = getNode(key);

        return currentNode != null;
    }

    /**
     * Returns {@code true} if this {@code MyHashMap} contains any key-value pairs with this value.
     *
     * @param value value to be used for search of a key-value pairs
     * @return Returns {@code true} if map contains any key-value pairs with this value or {@code false} otherwise.
     * @throws IllegalArgumentException if the value is null
     */
    public boolean containsValue(V value) {
        if (value == null) {
            throw new IllegalArgumentException("Illegal value: null");
        }

        // cycle through every bucket in bucket array
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];

            // if there is a key-value pair
            while (currentNode != null) {
                // check the value in the key-value pair
                // return true if the value equals to the specified value
                if (Objects.equals(value, currentNode.value)) {
                    return true;
                }

                // check next key-value pair
                currentNode = currentNode.next;
            }
        }

        return false;
    }

    /**
     * Clears {@code MyHashMap}. Removes all key-value pairs from this map.
     */
    public void clear() {
        if (size > 0) {
            size = 0;

            // cycle through every bucket and delete every key-value pair
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
        }
    }

    /**
     * Returns an unmodifiable {@code Set} that contains all key-value pairs of this {@code MyHashMap}.
     *
     * @return a set that contains all key-value pairs of this map
     */
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>(size);

        // add all nodes from this map to entry set
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];

            // if current node is not null then add it to set
            // and set next node (actual next node or null if last node in bucket) as current node
            while (currentNode != null) {
                entrySet.add(currentNode);
                currentNode = currentNode.next;
            }
        }

        // return unmodifiable set
        entrySet = Collections.unmodifiableSet(entrySet);

        return entrySet;
    }

    /**
     * Returns an unmodifiable {@code Set} that contains all keys from key-value pairs of this {@code MyHashMap}.
     *
     * @return a set that contains all keys from key-value pairs of this map
     */
    public Set<K> keySet() {
        Set<Map.Entry<K, V>> entrySet = entrySet();

        // get keys from nodes in entry set and return unmodifiable set
        return entrySet.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns an unmodifiable {@code List} that contains all values from key-value pairs of this {@code MyHashMap}.
     *
     * @return a list that contains all values from key-value pairs of this map
     */
    public List<V> values() {
        Set<Map.Entry<K, V>> entrySet = entrySet();

        // get values from nodes in entry set and return unmodifiable list
        return entrySet.stream()
                .map(Map.Entry::getValue)
                .toList();
    }

    /**
     * Adds a new key-value pair in the specified table (bucket array) or changes the value in a key-value pair
     * if pair with this key already exists.
     *
     * @param currentTable bucket array in which the key-value pair will be added
     * @param key          key
     * @param value        value
     */
    private void putInTable(Node<K, V>[] currentTable, K key, V value) {
        // get the index of bucket in which this key-value pair will be or already is stored
        int indexOfBucket = getBucketIndex(key, currentTable.length);

        Node<K, V> currentNode = currentTable[indexOfBucket];

        if (currentNode == null) {
            // if bucket is empty just add a new key-value pair
            currentTable[indexOfBucket] = new Node<>(key.hashCode(), key, value, null);
            size++;
        } else {
            // cycle through key-value pairs
            while (true) {
                if (currentNode.getKey().hashCode() == key.hashCode()
                        && currentNode.getKey().equals(key)) {
                    // if there is a key-value pair with this key already change the value in this pair
                    currentNode.setValue(value);
                    break;
                } else if (currentNode.next == null) {
                    // if there is no key-value pairs with this key in this bucket add new one
                    currentNode.next = new Node<>(key.hashCode(), key, value, null);
                    size++;
                    break;
                } else {
                    // if there is still key-value pairs in this bucket then check next one
                    currentNode = currentNode.next;
                }
            }
        }
    }

    /**
     * Removes a key-value pair from this {@code MyHashMap}.
     *
     * @param key key
     */
    private void removeNode(K key) {
        // get the index of bucket in which this key-value pair is stored
        int indexOfBucket = getBucketIndex(key, table.length);

        Node<K, V> currentNode = table[indexOfBucket];

        if (currentNode != null) {
            // if there is a key-value pair check its key
            if (currentNode.getKey().hashCode() == key.hashCode()
                    && currentNode.getKey().equals(key)) {
                // if keys are equal then remove this key-value pair
                table[indexOfBucket] = currentNode.next;
                size--;
                return;
            }

            // cycle through all key-value pairs in the bucket
            while (currentNode.next != null) {
                // if there is next key-value pair check its key
                if (currentNode.next.getKey().hashCode() == key.hashCode()
                        && currentNode.next.getKey().equals(key)) {
                    // if keys are equal then replace this key-value pair with its next pair (or null)
                    currentNode.next = currentNode.next.next;
                    size--;
                    return;
                } else {
                    // check next key-value pair
                    currentNode = currentNode.next;
                }
            }
        }
    }

    /**
     * Returns a key-value pair which contains this key from this {@code MyHashMap}
     * or {@code null} if there is no such key-value pair.
     *
     * @param key key
     * @return a key-value pair which contains this key or {@code null} if there is no such pairs
     */
    private Node<K, V> getNode(K key) {
        // get the index of bucket in which this key-value pair is stored
        int indexOfBucket = getBucketIndex(key, table.length);

        Node<K, V> currentNode = table[indexOfBucket];

        if (currentNode != null) {
            // cycle through all key-value pairs in this bucket
            while (true) {
                if (currentNode.getKey().hashCode() == key.hashCode()
                        && currentNode.getKey().equals(key)) {
                    // if we found a key-value pair with this key then return it
                    return currentNode;
                } else if (currentNode.next == null) {
                    // if there is no more key-value pairs in this bucket then return null
                    break;
                } else {
                    // check next key-value pair
                    currentNode = currentNode.next;
                }
            }
        }

        return null;
    }

    /**
     * Checks if this {@code MyHashMap} needs more capacity.
     *
     * @return {@code true} if resize needed or {@code false} otherwise
     */
    private boolean checkIfResizeNeeded() {
        return ((float) size / table.length) > LOAD_FACTOR;
    }

    /**
     * Increases capacity of this {@code MyHashMap}.
     */
    @SuppressWarnings({"unchecked"})
    private void resize() {
        // create a new bucket array with 2x size of the old one
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
        // clear the size count
        size = 0;

        // cycle through all buckets of the current bucket array
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];

            // put all key-value pairs from this bucket in the new bucket array
            while (currentNode != null) {
                putInTable(newTable, currentNode.getKey(), currentNode.getValue());
                currentNode = currentNode.next;
            }
        }

        // replace the old bucket array with the new one
        table = newTable;
    }

    private int getBucketIndex(K key, int tableLength) {
        // hash function used to calculate the index of bucket
        // where key value pair will be placed or searched for
        return key.hashCode() & (tableLength - 1);
    }
}
