package edu.brown.cs.student.main.BloomFilter.SimilarityMetrics;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;

import java.util.BitSet;
/**
 * The SimilarAND class implements the SimilarityMetric interface. Given two Bloom Filters it will
 * return a similarity score based on the AND operation.
 */
public class SimilarAND implements SimilarityMetric {
  /**
   * Implements AND by doing BitSet AND method between Bloom Filters.
   * @param b1 first bloom filter
   * @param b2 second bloom filter
   * @return integer representing similarity score (number of bits set to 1)
   */
  @Override
  public int similarityScore(BloomFilter b1, BloomFilter b2) {
    BitSet b1Set = b1.getBitSetCopy();
    BitSet b2Set = b2.getBitSetCopy();
    // AND operation modifies bits in b1
    b1Set.and(b2Set);
    // Counts number of true bits in b1
    return b1Set.cardinality();
  }
}

