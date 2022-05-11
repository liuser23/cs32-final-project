package edu.brown.cs.student.main.UserFilters;

import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

import java.util.Comparator;

public class UserFilterComparator implements Comparator <UserFilters>{
  private final UserFilters mainFilters;
  private final SimilarityMetric[] metrics;
  private final double[] weights;

  public UserFilterComparator(UserFilters mainFilters, SimilarityMetric[] metrics, double[] weights) {
    this.mainFilters = mainFilters;
    this.metrics = metrics;
    this.weights = weights;
  }
  // need fields defining each similarity metric and weights(default similar xnor)
  // get similarity score between each filter

  //Todo: normalize scores? might need to store all results somewhere and not use comparator directly
  @Override
  public int compare(UserFilters o1, UserFilters o2) {
    double score11 = metrics[0].similarityScore(mainFilters.getArtistBF(), o1.getArtistBF());
    double score12 = metrics[1].similarityScore(mainFilters.getSongBF(), o1.getSongBF());
    double score13 = metrics[2].similarityScore(mainFilters.getGenreBF(), o1.getGenreBF());

    double score21 = metrics[0].similarityScore(mainFilters.getArtistBF(), o2.getArtistBF()) ;
    double score22 = metrics[1].similarityScore(mainFilters.getSongBF(), o2.getSongBF());
    double score23 = metrics[2].similarityScore(mainFilters.getGenreBF(), o2.getGenreBF());

    double result = ((score11 - score21) * weights[0]) + ((score12 - score22) * weights[1])
        + ((score13 - score23) * weights[2]);

    if (result > 0) {
      return 1;
    } else if (result < 0) {
      return -1;
    } else {
      return 0;
    }
  }
}
