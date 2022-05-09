package edu.brown.cs.student.main.BloomFilter;

import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

import java.util.Comparator;
/**
 * The BFComparator implements the comparator interface and allows two Bloom Filters to be compared
 * based on similarity to a third Bloom Filter. In terms of finding the nearest neighbors,
 * we use the BFComparator to sort BLoomFilters based on their similarity to the student
 * with given ID (represented by mainFilter).
 */
public class BFComparator implements Comparator<BloomFilter> {
  private final BloomFilter mainFilter;
  private final SimilarityMetric similar;
  /**
   * The BFComparator constructor defines which is the mainFilter (point of reference for other
   * Bloom Filters) and also takes in a similarity metric. The similarity metric is used to
   * determine similarity scores and the relative ordering of filters.
   * @param mainFilter filter we want to compare other filters against
   * @param similar metric by which we determine how similar filters are
   */
  public BFComparator(BloomFilter mainFilter, SimilarityMetric similar) {
    this.mainFilter = mainFilter;
    this.similar = similar;
  }
  /**
   * Returns a positive integer if b1 is less similar than b2, zero if they are equally
   * similar, and negative integer if b1 is more similar than b2. When a list is sorted
   * according to this method, the least similar items are pushed towards the end of the list.
   * @param b1 first bloomFilter we are comparing
   * @param b2 second bloomFilter we compare against
   * @return integer representing relative ordering of two filters w.r.t. the main filter
   */
  @Override
  public int compare(BloomFilter b1, BloomFilter b2) {
    // Sort from greatest to least similarity score
    return similar.similarityScore(mainFilter, b2) - similar.similarityScore(mainFilter, b1);
  }
}