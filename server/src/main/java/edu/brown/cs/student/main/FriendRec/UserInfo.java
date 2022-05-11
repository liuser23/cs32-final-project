package edu.brown.cs.student.main.FriendRec;

import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

import java.util.List;

public class UserInfo  {
  private final List<String> artistData;
  private final List<String> songData;
  private final List<String> genreData;
  private final SimilarityMetric[] metrics;
  private final double[] weights;

  public UserInfo(List<String> artistData, List<String> songData, List<String> genreData,
                  SimilarityMetric[] metrics, double[] bfWeights) {
    this.artistData = artistData;
    this.songData = songData;
    this.genreData = genreData;
    this.metrics = metrics;
    this.weights = bfWeights;
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
