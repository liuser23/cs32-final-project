package edu.brown.cs.student.main.UserFilters;

import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarXNOR;
import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The BloomFilterRecommender stores functionality for getting a list of neighbors and returning
 * the IDs of those neighbors in a string format. This is used for the similar_bf command
 * when generating a list of k nearest neighbors to a student with given id.
 *
 */
public class UserFilterRecommender {
  private final int numNeighbors;
  private final String id;
  private final UserFiltersMap userMap;
  private final ScoreCalculator scoreCalc;
  private SimilarityMetric[] defaultSimilarityMetric =  {new SimilarXNOR(), new SimilarXNOR(), new SimilarXNOR()};
  private double[] defaultWeights = {0.333, 0.333, 0.333};
  /**
   * The BloomFilterRecommender constructor takes in a number of neighbors, id, BloomFilterList,
   * and similarity metric.
   * @param numNeighbors integer, representing number of neighbors to find
   * @param id integer, representing ID of student we are finding neighbors for
   * @param userMap represents database of filters for students
   * @throws IllegalArgumentException numNeighbors is negative or student cannot be found via ID
   */
  public UserFilterRecommender(int numNeighbors, String id, UserFiltersMap userMap,
                                SimilarityMetric[] metrics, double[] weights) throws IllegalArgumentException {
    if (numNeighbors < 0) {
      throw new IllegalArgumentException("number of neighbors to find should be non-negative"
          + "integer");
    }
    this.numNeighbors = numNeighbors;
    this.userMap = userMap;
    this.id = id;
    if (!userMap.containsKey(id)) {
      throw new IllegalArgumentException("ID is invalid, could not find student "
          + "with given ID in database");
    }

    // If a similarity metric is provided, change the default metric for comparator
    if (metrics != null) {
      this.setDefaultMetrics(metrics);
    }
    // If weights are provided, change default weights for comparator
    if (weights != null) {
      this.setDefaultWeights(weights);
    }
    this.scoreCalc = new ScoreCalculator(userMap.get(id), this.defaultSimilarityMetric, this.defaultWeights);
  }

  /**
   * 1. use scorecalc to get score for each in new map from userfiltermap, store in double[]
   * 2. normalize function? create normalizer? => map with double[] to map with normalized double[]
   * 3.
   */
  private Map<String, double[]> createSimilarityScoreMap(Map<String, UserFilters> initMap) {
    Map<String, double[]> resultMap = new HashMap<>();
    initMap.forEach((k, v) -> resultMap.put(k,
        new double[] {scoreCalc.artistScore(v), scoreCalc.songScore(v), scoreCalc.genreScore(v)}));
    return resultMap;
  }

  private Map<String, Double> createUserSimilarityMap(Map<String, double[]> initMap) {
    Map<String, Double> resultMap = new HashMap<>();
    initMap.forEach((k, v) -> resultMap.put(k, scoreCalc.userScore(v)));
    return resultMap;
  }
  /**
  Return modified map, z-score normalized double[]
 */
  private void normalizeMap(Map<String, double[]> initMap) {
    List<Double> artistVal = new ArrayList<>();
    List<Double> songVal = new ArrayList<>();
    List<Double> genreVal = new ArrayList<>();
    for (double[] array : initMap.values()) {
      artistVal.add(array[0]);
      songVal.add(array[1]);
      genreVal.add(array[2]);
    }
    double[][] params = new double[3][2];
    params[0] = getMeanAndSD(artistVal);
    params[1] = getMeanAndSD(songVal);
    params[2] = getMeanAndSD(genreVal);

    initMap.forEach((k, v) -> initMap.replace(k, normalizeArray(v, params)));
  }

  private double normalizeValue(double value, double mean, double SD) {
    if (SD == 0) {
      SD = 1;
    }
    return (value - mean) / SD;
  }

  private double[] normalizeArray(double[] values, double[][] params) {
    double val1 = normalizeValue(values[0], params[0][0], params[0][1]);
    double val2 = normalizeValue(values[1], params[1][0], params[1][1]);
    double val3 = normalizeValue(values[2], params[2][0], params[2][1]);
    return new double[] {val1, val2, val3};
  }

  private double[] getMeanAndSD(List<Double> doubleList) {
    double sum = 0.0;
    double standardDeviation = 0.0;
    int n = doubleList.size();

    for (double v : doubleList) {
      sum = sum + v;
    }

    double mean = sum / (n);

    for (double v : doubleList) {
      standardDeviation += Math.pow((v - mean), 2);
    }

    double sd = Math.sqrt(standardDeviation / n);
    return new double[] {mean, sd};
  }
  /**
   * Gets nearest k neighbors to student with given ID.
   * @return List of IDs representing the nearest neighbors.
   */
  public List<String> getNeighborIDs() {
    Map<String, UserFilters> copy = userMap.copyMap();
    copy.remove(id);
    Map<String, double[]> categoryScoresMap = createSimilarityScoreMap(copy);
    normalizeMap(categoryScoresMap);
    Map<String, Double> userScoresMap = createUserSimilarityMap(categoryScoresMap);
    List<Map.Entry<String, Double>> entries = new ArrayList<>(userScoresMap.entrySet());
    Collections.shuffle(entries);
    entries.sort(Map.Entry.<String, Double>comparingByValue().reversed());
    System.out.println(entries);
    return entries.stream().map(Map.Entry::getKey).limit(numNeighbors).collect(Collectors.toList());
  }

  /**
   * Private method to change default metric if a metric argument is passed to constructor.
   * @param metrics Similarity Metric, defines similarity between two bloom filters
   */
  private void setDefaultMetrics(SimilarityMetric[] metrics) {
    this.defaultSimilarityMetric = metrics;
  }

  private void setDefaultWeights(double[] weights) {
    this.defaultWeights = weights;
  }
}
