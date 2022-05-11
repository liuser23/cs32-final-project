package edu.brown.cs.student.main.UserFilters;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;
import edu.brown.cs.student.main.FriendRec.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements a map from Strings to BloomFilters by imposing the constraint that
 * all BloomFilters within the map have the same false positive rate and size. BloomFilterMaps
 * may be created from scratch or an existing Map. The default false positive rate is 0.1.
 *
 */
public class UserFiltersMap {

  /**
   * The global default false positive rate of the bloom filter.
   */
  private static final double DEFAULT_FALSE_POSITIVE_RATE = 0.1;

  /**
   * The false positive rate of this particular instance of UserFiltersMap, may or may not equal
   * DEFAULT_FALSE_POSITIVE_RATE.
   */
  private final double falsePositiveRate;

  /**
   * The internal map from String to BloomFilter.
   */
  private final Map<String, UserFilters> userFilters;

  /**
   * Constructs a new UserFiltersMap with the default false positive rate (0.1) and empty
   * internal hashmap.
   */
  public UserFiltersMap() {
    this(DEFAULT_FALSE_POSITIVE_RATE);
  }

  /**
   * Constructs a new UserFiltersMap with a custom false positive rate and empty internal
   * hashmap.
   * @param falsePositiveRate the custom false positive rate, between 0 and 1.
   */
  public UserFiltersMap(double falsePositiveRate) {
    this(falsePositiveRate, new HashMap<>());
  }

  /**
   * Constructs a new UserFiltersMap with a custom false positive rate and existing Map of
   * bloom filters; these bloom filters are not verified to be of the same size and false
   * positive rate yet, so care should be taken when calling this manually.
   * @param falsePositiveRate the custom false positive rate to use, in the range 0-1.
   * @param userFilters The existing map of bloom filters. A copy will be made of this map.
   */
  public UserFiltersMap(double falsePositiveRate, Map<String, UserFilters> userFilters) {
    if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
      throw new IllegalArgumentException("False positive rate must be between zero and one.");
    }
    if (userFilters == null) {
      throw new IllegalArgumentException("Cannot pass null map to UserFiltersMap constructor.");
    }
    this.falsePositiveRate = falsePositiveRate;
    this.userFilters = new HashMap<>(userFilters);
  }

  /**
   * From a key and a List of data, insert all the data into a new BloomFilter and add it into
   * this UserFiltersMap with the specified Key.
   * @param key the key to associate with the new bloom filter.
   * @param data a string list of data to insert into the new bloom filter.
   * @param filterSizes the max number of elements to be inserted into each bloom filter
   */
  public void addUserFilter(String key, UserInfo data, int[] filterSizes) {
    // create a new BloomFilter with the desired parameters, insert everything, put it into the map
    BloomFilter artistBF = new BloomFilter(this.falsePositiveRate, filterSizes[0]);
    data.getArtistData().forEach(artistBF::insertElement);

    BloomFilter songBF = new BloomFilter(this.falsePositiveRate, filterSizes[1]);
    data.getSongData().forEach(songBF::insertElement);

    BloomFilter genreBF = new BloomFilter(this.falsePositiveRate, filterSizes[2]);
    data.getGenreData().forEach(genreBF::insertElement);

    this.userFilters.put(key, new UserFilters(artistBF, songBF, genreBF, data.getMetrics(), data.getWeights()));
  }

  /**
   * Returns true if this UserFiltersMap contains a bloom filter entry associated with the
   * specified key, false otherwise.
   * @param key the key of which to check membership
   * @return true if the key is in the map, false otherwise.
   */
  public boolean containsKey(String key) {
    return this.userFilters.containsKey(key);
  }

  /**
   * Get the bloom filter with the specified key, or null if the specified key is not in the
   * UserFiltersMap.
   * @param key the key of the desired Bloom Filter.
   * @return the Bloom Filter corresponding to the given key, or null if there is no such key in
   *         the UserFiltersMap.
   */
  public UserFilters get(String key) {
    return this.userFilters.get(key);
  }

  /**
   * Get a safely mutable copy of this UserFiltersMap. The return value of this function can safely
   * be filtered/modified without affecting the original UserFiltersMap.
   * @return a copy of the internal HashMap used by this UserFiltersMap.
   */
  public Map<String, UserFilters> copyMap() {
    return new HashMap<>(this.userFilters);
  }

  /**
   * Return a string representation of this UserFiltersMap, for demonstration purposes.
   * @return a pretty-printed representation of this UserFiltersMap.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, UserFilters> entry : userFilters.entrySet()) {
      sb.append(entry.getKey()).append("\n ===============================\n ")
          .append(entry.getValue().toString()).append("\n");
    }
    return sb.toString();
  }
}
