package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;
import java.util.NoSuchElementException;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @author Slok Rajbhandari
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> implements Iterable<KVPair<K,V>> {

  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (memory allocated to the array).
   */
  int capacity;


  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

    // +--------------+------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * 
     * Creates a new, empty associative array.
     */
    @SuppressWarnings({ "unchecked" })
    public AssociativeArray() {
        this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);  // Initialize pairs array
        this.capacity = DEFAULT_CAPACITY;  // Set default capacity
        this.size = 0;  // Initialize size to zero
    }  // end of constructor

    // +------------------+--------------------------------------------
    // | Standard Methods |
    // +------------------+

    /**
     * Creates a copy of this AssociativeArray.
     * @return a copy of the AssociativeArray
     */
    public AssociativeArray<K, V> clone() {
        AssociativeArray<K, V> copy = new AssociativeArray<>();  // Create a new associative array
        for (int i = 0; i < this.size; i++) {
            if (this.size > copy.capacity) {
                copy.expand();
            }
            copy.pairs[i] = new KVPair<>(this.pairs[i].key, this.pairs[i].val);
        }
        copy.size = this.size;
        return copy;
    }  // end of clone method

    /**
     * Converts the array to a string representation.
     * @return the string representation of the array
     */
    public String toString() {
        if (this.size == 0) {
            return "{}";  // Return empty braces if the array is empty
        }
        String output = String.format("{ %s: %s", this.pairs[0].key, this.pairs[0].val);  // Initialize output
        for (int i = 1; i < this.size; i++) {
            output += String.format(", %s: %s", this.pairs[i].key, this.pairs[i].val);  // Append each key-value pair
        }
        output += " }";  // Close the braces
        return output;  // Return the final string
    }  // end of toString method

    // +----------------+----------------------------------------------
    // | Public Methods |
    // +----------------+

    /**
     * Sets the value associated with a key. Future calls to get(key) will return the new value.
     * @param key the key
     * @param value the value to be associated with the key
     */
    public void set(K key, V value) throws NullKeyException {
        if (key == null) {
            throw new NullKeyException();  // Handle null keys
        }
        int index;
        try {
            index = this.find(key);  // Find the key in the array
            this.pairs[index].val = value;  // Update the value if key is found
        } catch (KeyNotFoundException e) {
            if (this.size >= this.capacity) {
                this.expand();  // Expand the array if full
                capacity = capacity * 2;  // Double the capacity
            }
            index = this.size;  // Set the index to the next available slot
            this.pairs[index] = new KVPair<>(key, value);  // Add new key-value pair
            this.size++;  // Increment the size
        }
    }  // end of set method

    /**
     * Retrieves the value associated with a key.
     * @param key the key
     * @return the value associated with the key
     * @throws KeyNotFoundException if the key is not found
     */
    public V get(K key) throws KeyNotFoundException {
        if (key == null) {
            throw new KeyNotFoundException();  // Handle null keys
        }
        return this.pairs[this.find(key)].val;
    }  // end of get method

    /**
     * Determines if the array contains a given key.
     * @param key the key to check
     * @return true if the key exists, false otherwise
     */
    public boolean hasKey(K key) {
        try {
            this.find(key);  // Try to find the key
            return true;  // Key exists
        } catch (KeyNotFoundException e) {
            return false;  // Key does not exist
        }
    }  // end of hasKey method

    /**
     * Removes the key-value pair associated with a key.
     * @param key the key to remove
     */
    public void remove(K key) {
        try {
            int index = this.find(key);  // Find the key
            this.pairs[index] = this.pairs[this.size - 1];  // Replace the key-value pair with the last pair
            this.pairs[this.size] = new KVPair<>();  // Clear the last slot
            this.size--;
        } catch (KeyNotFoundException e) {
            return;  // Key not found, do nothing
        }
    }  // end of remove method

    /**
     * Returns the number of key-value pairs in the array.
     * @return the size of the array
     */
    public int size() {
        return this.size;  // Return the size of the array
    }  // end of size method

    // +-----------------+---------------------------------------------
    // | Private Methods |
    // +-----------------+

    /**
     * Expands the underlying array to accommodate more key-value pairs.
     */
    void expand() {
        this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
    }  // end of expand method

    /**
     * Finds the index of the first entry in `pairs` that contains the specified key.
     * @param key the key to find
     * @return the index of the key
     * @throws KeyNotFoundException if the key is not found
     */
    public int find(K key) throws KeyNotFoundException {
        for (int i = 0; i < this.size; i++) {
            if (this.pairs[i].key == null) {
                throw new KeyNotFoundException();  // Handle null keys
            }
            if (this.pairs[i].key.equals(key)) {
                return i;  // Return the index if the key is found
            }
        }
        throw new KeyNotFoundException();  // Throw an exception if the key is not found
    }  // end of find method

    /**
     * Implements iterator for AssociativeArray using an anonymous class. 
     */
    @Override
    public java.util.Iterator<KVPair<K, V>> iterator() {
        return new java.util.Iterator<KVPair<K,V>>() {
            int index = 0;  // Index for iteration

            public boolean hasNext() {
                return (this.index < AssociativeArray.this.size);  // Check if there are more elements
            } // hasNext()

            public KVPair<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();  // Handle case where no more elements
                }
                return AssociativeArray.this.pairs[this.index++];  // Return the next element
            } // next()
        };  // end Iterator (already was tehre)
    }  // end
}  // end of AssociativeArray class
