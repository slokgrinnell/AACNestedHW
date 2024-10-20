package edu.grinnell.csc207.util;

/**
 * KVPair
 * Represents a simple key-value pair. This structure is used to store mappings between keys and values.
 */
public class KVPair<K, V> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The key.
   */
  K key;

  /**
   * The value.
   */
  V val;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

    /**
     * 
     * Creates an empty key-value pair.
     */
    KVPair() {
        this(null, null);  // Initialize key and value to null
    }  // end of default constructor

    /**
     * Constructor
     * Initializes the key-value pair with a key and a value.
     * @param key the key
     * @param value the value
     */
    KVPair(K key, V value) {
        this.key = key;  // Set the key
        this.val = value;  // Set the value
    }  // end of constructor

  // +------------------+--------------------------------------------
  // | Standard methods |
  // +------------------+

    /**
     * Creates a copy of the current key-value pair.
     * @return a new KVPair with the same key and value
     */
    public KVPair<K, V> clone() {
        return new KVPair<>(this.key, this.val);  // Create a copy of the key-value pair
    }  // end of clone method

    /**
     * Returns a string representation of the key-value pair.
     * @return the string representation
     */
    public String toString() {
        return "{ " + this.key.toString() + " : " + this.val.toString() + " }";  // Format the key-value pair as a string
    }  // end of toString method

  // +----------------+----------------------------------------------
  // | Getter methods |
  // +----------------+

    /**
     * Returns the key of the pair.
     * @return the key
     */
    public K getKey() {
        return this.key;  // Return the key
    }  // end of getKey method

    /**
     * Returns the value of the pair.
     * @return the value
     */
    public V getValue() {
        return this.val;  // Return the value
    }  // end of getValue method
}  // end of KVPair class
