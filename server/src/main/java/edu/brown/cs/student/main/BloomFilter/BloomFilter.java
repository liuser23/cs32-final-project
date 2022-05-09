package edu.brown.cs.student.main.BloomFilter;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.BitSet;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

/**
 * The BloomFilter class stores a single BloomFilter which is represented using a BitSet object.
 * Elements can be inserted or queried from the BloomFilter and the underlying BitSets can also
 * be used to generate recommendations by comparing similarity scores between them. It hashes
 * elements into the filter according to a preset hashing scheme (provided by helper code).
 */
public class BloomFilter {
  private static final int RADIX = 16;
  private static final int HEX_CONSTANT1 = 0xFF;
  private static final int HEX_CONSTANT2 = 0x0F;
  private static final MessageDigest HASH_FUNCTION;
  // Static Initializer for Hash Function
  static {
    MessageDigest tempValue = null;
    try {
      tempValue = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      System.out.println("ERROR: invalid hash-function");
    }
    HASH_FUNCTION = tempValue;
  }
  // Character Set for Hashing (Encodes String into Byte Array)
  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private final BitSet bitSet;
  private final int hashCount;
  private final int setsize;
  /**
   * Constructor for Bloom Filter based on given parameters r and n. The parameters r and n
   * represent the desired false positive rate and max number of elements to insert into bloom
   * filter. The arguments are used to calculate the number of hashes(k) and size of the bitSet(m).
   * @param r double (0, 1) representing rate of false positives
   * @param n non-negative integer representing max number of elements inserted into Bloom Filter
   * @throws IllegalArgumentException when r is not between (0,1), n is negative, or result of
   * calculating bit-set-size leads to invalid result (i.e. size 0 bitset)
   */
  public BloomFilter(double r, int n) throws IllegalArgumentException {
    // Check for valid r and n values
    if (r <= 0 || r >= 1 || n <= 0) {
      throw new IllegalArgumentException("value of <r> and <n> not valid, <r> "
          + "should be rate from (0, 1), <n> should be non-negative integer ");
    }
    // Note: log_2(r) = ln(r)/ln(2)
    this.hashCount = (int) Math.ceil(-1 * (Math.log(r) / Math.log(2)));
    this.setsize = (int) Math.ceil((hashCount * n) / Math.log(2));
    // Double-Check for Potential Edge-Cases
    if (hashCount <= 0 || setsize <= 0) {
      throw new IllegalArgumentException("values of <r> and <n> lead to "
          + "invalid hashCount or bit-set size");
    }
    this.bitSet = new BitSet(setsize);
  }
  /**
   * Given a String representing an element, inserts the element into the Bloom Filter. This
   * modifies the underlying bitSet, setting certain bits to true.
   * @param element String, presents element we want to insert into Bloom Filter
   */
  public void insertElement(String element) {
    byte[] b = element.getBytes(CHARSET);
    BigInteger[] hashValue = createHashes(b, hashCount);
    for (BigInteger hash: hashValue) {
      // Converting hash-code into a valid index value
      int index = hash.mod(BigInteger.valueOf(setsize)).intValue();
      bitSet.set(index);
    }
  }
  /**
   * Given a String representing an element to query for, returns true if the element might
   * be in the bloom filter (positive or false positive), and false if the element is definitely not
   * in the bloom filter.
   * @param element String, represent element we are checking for in Bloom Filter
   * @return true for positive/false positive, false for not present
   */
  public boolean mightContainElement(String element) {
    byte[] b = element.getBytes(CHARSET);
    BigInteger[] hashValue = createHashes(b, hashCount);
    for (BigInteger hash: hashValue) {
      // Converting hash-code into a valid index value
      int index = hash.mod(BigInteger.valueOf(setsize)).intValue();
      if (!bitSet.get(index)) {
        return false;
      }
    }
    return true;
  }
  /**
   * Returns hashCount k of BloomFilter calculated via given equation.
   * @return int representing number of locations element will be hashed to
   */
  public int getHashCount() {
    return this.hashCount;
  }
  /**
   * Returns setsize of bitSet that is contained by BloomFilter. This differs from bitSet.length()
   * (location of the highest bit) and bitSet.size(), size of bitSet in memory.
   * @return int representing calculated size of bitSet
   */
  public int getSetsize() {
    return this.setsize;
  }
  /**
   * Returns a copy of the bitSet that is the same as underlying BitSet. This BitSet is used
   * for comparisons in similarity metric, since sometimes similarity metric methods change
   * the underlying bitSet. Prevents original bitSet from changing.
   * @return copy of BitSet
   */
  public BitSet getBitSetCopy() {
    return (BitSet) bitSet.clone();
  }
  /**
   * Prints the bitset as a binary string (string consisting of 1's and 0's).
   * Source : https://stackoverflow.com/questions/34748006/how-to-convert-bitset-to-binary-string-effectively
   * @return String representation of the bitset
   */
  @Override
  public String toString() {
    int bitCount = setsize;
    final StringBuilder buffer = new StringBuilder(bitCount);
    IntStream.range(0, bitCount).mapToObj(i -> bitSet.get(i) ? '1' : '0').forEach(buffer::append);
    return buffer.toString();
  }
  /**
   * Generates hashes based on the contents of an array of bytes, converts the result into
   * BigIntegers, and stores them in an array. The hash function is called until the required number
   * of BigIntegers are produced.
   * For each call to the hash function a salt is prepended to the data. The salt is increased by 1
   * for each call.
   *
   * @param data input data.
   * @param numHashes number of hashes/BigIntegers to produce.
   * @return array of BigInteger hashes
   */
  public static BigInteger[] createHashes(byte[] data, int numHashes) {
    BigInteger[] result = new BigInteger[numHashes];

    int k = 0;
    BigInteger salt = BigInteger.valueOf(0);
    while (k < numHashes) {
      HASH_FUNCTION.update(salt.toByteArray());
      salt = salt.add(BigInteger.valueOf(1));
      byte[] hash = HASH_FUNCTION.digest(data);
      HASH_FUNCTION.reset();

      // convert hash byte array to hex string, then to BigInteger
      String hexHash = bytesToHex(hash);
      result[k] = new BigInteger(hexHash, RADIX);
      k++;
    }
    return result;
  }
  /**
   * Converts a byte array to a hex string.
   * Source: https://stackoverflow.com/a/9855338
   *
   * @param bytes the byte array to convert
   * @return the hex string
   */
  private static String bytesToHex(byte[] bytes) {
    byte[] hexArray = "0123456789ABCDEF".getBytes();
    byte[] hexChars = new byte[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & HEX_CONSTANT1;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & HEX_CONSTANT2];
    }
    return new String(hexChars, CHARSET);
  }
}

