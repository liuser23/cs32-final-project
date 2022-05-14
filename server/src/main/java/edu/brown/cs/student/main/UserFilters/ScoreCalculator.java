package edu.brown.cs.student.main.UserFilters;

import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

public class ScoreCalculator {
  private final UserFilters mainFilter;
  private final SimilarityMetric[] metrics;
  private final double[] weights;

  public ScoreCalculator(UserFilters mainFilter, SimilarityMetric[] metrics, double[] weights) {
    this.mainFilter = mainFilter;
    this.metrics = metrics;
    this.weights = weights;
  }

  public double artistScore(UserFilters o1) {
    return metrics[0].similarityScore(mainFilter.getArtistBF(), o1.getArtistBF());
  }

  public double songScore(UserFilters o1) {
    return metrics[1].similarityScore(mainFilter.getSongBF(), o1.getSongBF());
  }

  public double genreScore(UserFilters o1) {
    return metrics[2].similarityScore(mainFilter.getGenreBF(), o1.getGenreBF());
  }

  public double userScore(double[] normalizedScores) {
    double sum = 0;
    for (int i = 0; i < 3; i++) {
      sum += normalizedScores[i] * weights[i];
    }
    return sum;
  }
}
