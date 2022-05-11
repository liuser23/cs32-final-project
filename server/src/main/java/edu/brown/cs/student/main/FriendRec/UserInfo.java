package edu.brown.cs.student.main.FriendRec;

import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarXNOR;
import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarXOR;
import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

import java.util.List;
import java.util.stream.IntStream;

public class UserInfo  {
  private final List<String> artistData;
  private final List<String> songData;
  private final List<String> genreData;
  private SimilarityMetric[] metrics;
  private double[] weights;

  public UserInfo(List<String> artistData, List<String> songData, List<String> genreData) {
    this(artistData, songData, genreData, null, null);
  }

  public UserInfo(List<String> artistData, List<String> songData, List<String> genreData,
                  boolean[] similarity, int[] bfWeights) {
    this.artistData = artistData;
    this.songData = songData;
    this.genreData = genreData;
    if (similarity != null) {
      this.metrics = new SimilarityMetric[3];
      for (int i = 0; i < 3; i++) {
        if (similarity[i]) {
          this.metrics[i] = new SimilarXNOR();
        } else {
          this.metrics[i] = new SimilarXOR();
        }
      }
    }
    if (bfWeights != null) {
      this.weights = new double[3];
      int sum = IntStream.of(bfWeights).sum();
      for (int i = 0; i < 3; i++) {
        this.weights[i] = ((1.0 * bfWeights[i]) / sum);
      }
    }
  }

  public List<String> getArtistData()
  {
    return artistData;
  }

  public List<String> getSongData() {
    return songData;
  }

  public List<String> getGenreData() {
    return genreData;
  }

  public SimilarityMetric[] getMetrics() {
    return metrics;
  }

  public double[] getWeights() {
    return weights;
  }

}
