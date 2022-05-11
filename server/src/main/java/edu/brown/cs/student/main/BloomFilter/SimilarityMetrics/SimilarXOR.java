package edu.brown.cs.student.main.BloomFilter.SimilarityMetrics;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;

import java.util.BitSet;

// inverse of xnor
public class SimilarXOR implements SimilarityMetric{
  @Override
  public int similarityScore(BloomFilter b1, BloomFilter b2) {
    BitSet b1Set = b1.getBitSetCopy();
    BitSet b2Set = b2.getBitSetCopy();
    b1Set.xor(b2Set);
    return b1Set.cardinality();
  }
}
