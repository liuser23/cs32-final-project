package edu.brown.cs.student.main.BloomFilter.SimilarityMetrics;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;

import java.util.BitSet;

/**
 * The SimilarXNOR class implements the SimilarityMetric interface. Given two Bloom Filters it will
 * return a similarity score based on the XNOR operation, done by negating the reuslt of an XOR
 * operation.
 */
public class SimilarXNOR implements SimilarityMetric {
  /**
   * Implements XNOR by doing XOR operation between Bloom Filters and flipping all bits in
   * the resulting BitSet.
   * @param b1 first bloom filter
   * @param b2 second bloom filter
   * @return integer representing similarity score (number of bits set to 1)
   */
  @Override
  public int similarityScore(BloomFilter b1, BloomFilter b2) {
    BitSet b1Set = b1.getBitSetCopy();
    BitSet b2Set = b2.getBitSetCopy();
    // XOR operation modifies bits in b1
    b1Set.xor(b2Set);
    // Negate XOR result to get XNOR comparison, note setsize is not built-in length() or size()
    b1Set.flip(0, b1.getSetsize());
    return b1Set.cardinality();
  }
}
