package edu.brown.cs.student.main.UserFilters;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;
import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

public class UserFilters {
  private final BloomFilter artistBF;
  private final BloomFilter songBF;
  private final BloomFilter genreBF;
  private final SimilarityMetric[] metrics;
  private final double[] weights;


  public UserFilters(BloomFilter artistBF, BloomFilter songBF, BloomFilter genreBF,
                     SimilarityMetric[] metrics, double[] bfWeights) {
    this.artistBF = artistBF;
    this.songBF = songBF;
    this.genreBF = genreBF;
    this.metrics = metrics;
    this.weights = bfWeights;
  }

  public BloomFilter getArtistBF() {
    return artistBF;
  }

  public BloomFilter getSongBF() {
    return songBF;
  }

  public BloomFilter getGenreBF() {
    return genreBF;
  }

  public SimilarityMetric[] getMetrics() {
    return metrics;
  }

  public double[] getWeights() {
    return weights;
  }

  @Override
  public String toString() {
    return "ArtistBF: " + this.artistBF.toString() + "\n" +
        "SongBF: " + this.songBF.toString() + "\n" +
        "genreBF: " + this.genreBF.toString() + "\n";
  }
}
