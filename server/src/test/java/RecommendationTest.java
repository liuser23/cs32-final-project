import edu.brown.cs.student.main.FriendRec.RecommendationSystem;
import edu.brown.cs.student.main.FriendRec.UserInfo;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;



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
  public void testMostSimilarOrdering() {
    HashMap<String, UserInfo> userData = new HashMap<>();
    String artistData1 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData1 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData1 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo1 = new UserInfo(toList(artistData1), toList(songData1), toList(genreData1));
    System.out.println(toList(artistData1));

    String artistData2 = "Justin Bieber,Selena Gomez,Random Artist";
    String songData2 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData2 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo2 = new UserInfo(toList(artistData2), toList(songData2), toList(genreData2));

    // Not Same Artists
    String artistData3 = "Random Artist";
    String songData3 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
    String genreData3 = "Hip Hop,Rock,Canadian Pop,Pop";

    UserInfo userInfo3 = new UserInfo(toList(artistData3), toList(songData3), toList(genreData3));

    userData.put("user1", userInfo1);
    userData.put("user2", userInfo2);
    userData.put("user3", userInfo3);

    RecommendationSystem recommender = RecommendationSystem.fromUserData(userData);
    List<String> result = recommender.getRecsFor("user1", 2);
    System.out.println("result");
    assert(result.get(0).equals("user2"));
    assert(result.get(1).equals("user3"));

  }

//  @Test
//  public void testMostSimilarOrdering2() {
//    HashMap<String, UserInfo> userData = new HashMap<>();
//    String artistData1 = "Justin Bieber,Selena Gomez,Random Artist";
//    String songData1 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
//    String genreData1 = "Hip Hop,Rock,Canadian Pop,Pop";
//
//    UserInfo userInfo1 = new UserInfo(toList(artistData1), toList(songData1), toList(genreData1));
//
//    String artistData2 = "Justin Bieber,Selena Gomez,Random Artist";
//    String songData2 = "SongA,SongB,SongC,SongD,Never Say Never,Random Song";
//    String genreData2 = "Hip Hop,Rock,Canadian Pop,Pop";
//
//    UserInfo userInfo2 = new UserInfo(toList(artistData2), toList(songData2), toList(genreData2));
//
//    // Not Same Artists
//    String artistData3 = "Random Artist";
//    String songData3 = "SongA,SongB,SongC,SongD";
//    String genreData3 = "Hip Hop,Rock,Canadian Pop,Pop";
//
//    UserInfo userInfo3 = new UserInfo(toList(artistData3), toList(songData3), toList(genreData3));
//
//    userData.put("user1", userInfo1);
//    userData.put("user2", userInfo2);
//    userData.put("user3", userInfo3);
//
//    RecommendationSystem recommender = RecommendationSystem.fromUserData(userData);
//    List<String> result = recommender.getRecsFor("user1", 2);
//    assert(result.get(0).equals("user2"));
//    assert(result.get(1).equals("user3"));
//
//  }
}
