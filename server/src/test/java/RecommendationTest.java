import edu.brown.cs.student.main.FriendRec.RecommendationSystem;
import edu.brown.cs.student.main.FriendRec.UserInfo;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;



public class RecommendationTest   {

  private List<String> toList(String commaSeparatedString) {
    return Stream.of(commaSeparatedString.split(",", -1)).toList();
  }
  @Test
  public void testValidRecommendation() {
    HashMap<String, UserInfo> userData = new HashMap<>();
    String artistData1 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData1 = "SongA,SongB,SongC,SongD,Never Say Never, Random Song";
    String genreData1 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo1 = new UserInfo(toList(artistData1), toList(songData1), toList(genreData1));

    String artistData2 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData2 = "SongA,SongB,SongC,SongD,Never Say Never, Random Song";
    String genreData2 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo2 = new UserInfo(toList(artistData2), toList(songData2), toList(genreData2));

    userData.put("user1", userInfo1);
    userData.put("user2", userInfo2);

    RecommendationSystem recommender = RecommendationSystem.fromUserData(userData);
    List<String> result = recommender.getRecsFor("user1", 1);
    assert(result.get(0).equals("user2"));
  }

  @Test
  public void testMostSimilarOrderingEvenWeights() {
    HashMap<String, UserInfo> userData = new HashMap<>();
    String artistData1 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData1 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData1 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo1 = new UserInfo(toList(artistData1), toList(songData1), toList(genreData1));

    String artistData2 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData2 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData2 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo2 = new UserInfo(toList(artistData2), toList(songData2), toList(genreData2));

    // Missing Artists
    String artistData3 = "Random Artist";
    String songData3 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData3 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo3 = new UserInfo(toList(artistData3), toList(songData3), toList(genreData3));

    // Missing Artists and Songs
    String artistData4 = "Random Artist";
    String songData4 = "SongA,SongB,SongC";
    String genreData4 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo4 = new UserInfo(toList(artistData4), toList(songData4), toList(genreData4));

    userData.put("user1", userInfo1);
    userData.put("user2", userInfo2);
    userData.put("user3", userInfo3);
    userData.put("user4", userInfo4);

    RecommendationSystem recommender = RecommendationSystem.fromUserData(userData);
    List<String> result = recommender.getRecsFor("user1", 3);
    assert(result.get(0).equals("user2"));
    assert(result.get(1).equals("user3"));
    assert(result.get(2).equals("user4"));
  }


  @Test
  public void testLeastSimilarOrderingEvenWeights() {
    HashMap<String, UserInfo> userData = new HashMap<>();
    String artistData1 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData1 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData1 = "Hip Hop,Rock,Canadian Pop,Pop";

    // Dissimilar Metric + Even Weights
    UserInfo userInfo1 = new UserInfo(toList(artistData1), toList(songData1), toList(genreData1),
        new boolean[] {false, false, false}, new int[] {1, 1, 1});

    String artistData2 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData2 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData2 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo2 = new UserInfo(toList(artistData2), toList(songData2), toList(genreData2));

    // Missing Artists
    String artistData3 = "Random Artist";
    String songData3 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData3 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo3 = new UserInfo(toList(artistData3), toList(songData3), toList(genreData3));

    // Missing Artists and Songs
    String artistData4 = "Random Artist";
    String songData4 = "SongA,SongB,SongC";
    String genreData4 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo4 = new UserInfo(toList(artistData4), toList(songData4), toList(genreData4));

    userData.put("user1", userInfo1);
    userData.put("user2", userInfo2);
    userData.put("user3", userInfo3);
    userData.put("user4", userInfo4);

    RecommendationSystem recommender = RecommendationSystem.fromUserData(userData);
    List<String> result = recommender.getRecsFor("user1", 3);
    // Reverse Order from Prev Test!
    assert(result.get(2).equals("user2"));
    assert(result.get(1).equals("user3"));
    assert(result.get(0).equals("user4"));
  }


  @Test
  public void testSimilarityWeighted() {
    HashMap<String, UserInfo> userData = new HashMap<>();
    String artistData1 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData1 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData1 = "Hip Hop,Rock,Canadian Pop,Pop";

    // Only Comparing on Song! (similarity since true)
    UserInfo userInfo1 = new UserInfo(toList(artistData1), toList(songData1), toList(genreData1),
        new boolean[] {true, true, true}, new int[] {0, 1, 0});

    // Somewhat similar song, but other fields very different
    String artistData2 = "Unique Artist, Wow, Cool";
    String songData2 = "SongA,SongD,Random Song";
    String genreData2 = "What the heck, nice one";

    UserInfo userInfo2 = new UserInfo(toList(artistData2), toList(songData2), toList(genreData2));

    // Very similar other data but no similar songs
    String artistData3 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData3 = "SongX, SongY, SongZ";
    String genreData3 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo3 = new UserInfo(toList(artistData3), toList(songData3), toList(genreData3));

    userData.put("user1", userInfo1);
    userData.put("user2", userInfo2);
    userData.put("user3", userInfo3);

    RecommendationSystem recommender = RecommendationSystem.fromUserData(userData);
    List<String> result = recommender.getRecsFor("user1", 2);
    assert(result.get(0).equals("user2"));
    assert(result.get(1).equals("user3"));
  }
}
