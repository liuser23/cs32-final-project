package edu.brown.cs.student.main.BloomFilter.SimilarityMetrics;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;

/**
 * The Similarity Metric interface defines a public method similarityScore by which
 * Bloom Filters can be compared in terms of similarity.
 */
public interface SimilarityMetric {
  /**
   * Returns similarity score between bloom filters.
   * @param b1 bloom filter 1
   * @param b2 bloom filter 2
   * @return integer representing similarity
   */
  int similarityScore(BloomFilter b1, BloomFilter b2);
}
