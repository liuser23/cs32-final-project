package edu.brown.cs.student.main.BloomFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements a map from Strings to BloomFilters by imposing the constraint that
 * all BloomFilters within the map have the same false positive rate and size. BloomFilterMaps
 * may be created from scratch or an existing Map. The default false positive rate is 0.1.
 *
 */
public class BloomFilterMap {

  /**
   * The global default false positive rate of the bloom filter.
   */
  private static final double DEFAULT_FALSE_POSITIVE_RATE = 0.1;

  /**
   * The false positive rate of this particular instance of BloomFilterMap, may or may not equal
   * DEFAULT_FALSE_POSITIVE_RATE.
   */
  private final double falsePositiveRate;

  /**
   * The internal map from String to BloomFilter.
   */
  private final Map<String, BloomFilter> bloomFilters;

  /**
   * Constructs a new BloomFilterMap with the default false positive rate (0.1) and empty
   * internal hashmap.
   */
  public BloomFilterMap() {
    this(DEFAULT_FALSE_POSITIVE_RATE);
  }

  /**
   * Constructs a new BloomFilterMap with a custom false positive rate and empty internal
   * hashmap.
   * @param falsePositiveRate the custom false positive rate, between 0 and 1.
   */
  public BloomFilterMap(double falsePositiveRate) {
    this(falsePositiveRate, new HashMap<>());
  }

  /**
   * Constructs a new BloomFilterMap with a custom false positive rate and existing Map of
   * bloom filters; these bloom filters are not verified to be of the same size and false
   * positive rate yet, so care should be taken when calling this manually.
   * @param falsePositiveRate the custom false positive rate to use, in the range 0-1.
   * @param bloomFilters The existing map of bloom filters. A copy will be made of this map.
   */
  public BloomFilterMap(double falsePositiveRate, Map<String, BloomFilter> bloomFilters) {
    if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
      throw new IllegalArgumentException("False positive rate must be between zero and one.");
    }
    if (bloomFilters == null) {
      throw new IllegalArgumentException("Cannot pass null map to BloomFilterMap constructor.");
    }
    this.falsePositiveRate = falsePositiveRate;
    this.bloomFilters = new HashMap<>(bloomFilters);
  }

  /**
   * From a key and a List of data, insert all the data into a new BloomFilter and add it into
   * this BloomFilterMap with the specified Key.
   * @param key the key to associate with the new bloom filter.
   * @param data a string list of data to insert into the new bloom filter.
   * @param filterSize the max number of elements to be inserted into the bloom filter.
   * @return this BloomFilterMap itself after modification, for chaining purposes.
   */
  public BloomFilterMap addBloomFilter(String key, List<String> data, int filterSize) {
    // create a new BloomFilter with the desired parameters, insert everything, put it into the map
    BloomFilter bf = new BloomFilter(this.falsePositiveRate, filterSize);
    data.forEach(bf::insertElement);
    this.bloomFilters.put(key, bf);
    return this;
  }

  /**
   * Returns true if this BloomFilterMap contains a bloom filter entry associated with the
   * specified key, false otherwise.
   * @param key the key of which to check membership
   * @return true if the key is in the map, false otherwise.
   */
  public boolean containsKey(String key) {
    return this.bloomFilters.containsKey(key);
  }

  /**
   * Get the bloom filter with the specified key, or null if the specified key is not in the
   * BloomFilterMap.
   * @param key the key of the desired Bloom Filter.
   * @return the Bloom Filter corresponding to the given key, or null if there is no such key in
   *         the BloomFilterMap.
   */
  public BloomFilter get(String key) {
    return this.bloomFilters.get(key);
  }

  /**
   * Get a safely mutable copy of this BloomFilterMap. The return value of this function can safely
   * be filtered/modified without affecting the original BloomFilterMap.
   * @return a copy of the internal HashMap used by this BloomFilterMap.
   */
  public Map<String, BloomFilter> copyMap() {
    return new HashMap<>(this.bloomFilters);
  }

  /**
   * Return a string representation of this BloomFilterMap, for demonstration purposes.
   * @return a pretty-printed representation of this BloomFilterMap.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, BloomFilter> entry : bloomFilters.entrySet()) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
    }
    return sb.toString();
  }
}
