package edu.brown.cs.student.main.FriendRec;

import edu.brown.cs.student.main.UserFilters.UserFilterRecommender;
import edu.brown.cs.student.main.UserFilters.UserFilters;
import edu.brown.cs.student.main.UserFilters.UserFiltersMap;


import java.util.List;
import java.util.Map;


public class RecommendationSystem {
  /**
   * Contains the set of BloomFilters, used for finding k nearest neighbors for qualitative,
   * String data.
   */
  private final UserFiltersMap data;


  /**
   * From qualitative and quantitative data sets, create a new RecommendationSystem. It is expected
   * that the qualitative and quantitative data sets contain the same number of students, and
   * that each student appears once in both sets.
   */
  public RecommendationSystem(UserFiltersMap data) {
    if (data == null) {
      throw new IllegalArgumentException("Rec system: qualitative data cannot be null");
    }
    this.data = data;
  }

  /**
   * @param id
   * @param num
   * @return
   */
  public List<String> getRecsFor(String id, int num) {
    if (num < 0) {
      throw new IllegalArgumentException("num must be non-negative.");
    }

    UserFilters currUser = data.get(id);

    UserFilterRecommender recommender = new UserFilterRecommender(num, id,
        this.data, currUser.getMetrics(), currUser.getWeights());

    // Extract Unique Recs
    return recommender.getNeighborIDs();
  }


  public static RecommendationSystem fromUserData(Map<String, UserInfo> userData) {
    UserFiltersMap userFiltersMap = new UserFiltersMap();

    // need to keep track of the maximum-sized list of qualitative attributes, as all other
    // bloom filters will be sized acccording to this.
    int[] maxEles = {-1, -1, -1};
    for (Map.Entry<String, UserInfo> entry : userData.entrySet()) {
      if (entry.getValue().getArtistData().size() > maxEles[0]) {
        maxEles[0] = entry.getValue().getArtistData().size();
      }
      if (entry.getValue().getSongData().size() > maxEles[1]) {
        maxEles[1] = entry.getValue().getSongData().size();
      }
      if (entry.getValue().getGenreData().size() > maxEles[2]) {
        maxEles[2] = entry.getValue().getGenreData().size();
      }
    }

    // Add the qualitative data for each student into a new bloom filter in the BloomFilterMap.
    for (Map.Entry<String, UserInfo> entry : userData.entrySet()) {
      userFiltersMap.addUserFilter(entry.getKey(), entry.getValue(), maxEles);
    }

    // Now that we have completed building a BloomFilterMap put it into
    // the RecommendationSystem.
    return new RecommendationSystem(userFiltersMap);
  }
}
