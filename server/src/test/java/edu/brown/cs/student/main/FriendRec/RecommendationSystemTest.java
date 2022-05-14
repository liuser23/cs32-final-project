package edu.brown.cs.student.main.FriendRec;

import junit.framework.TestCase;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationSystemTest extends TestCase {

    public void testBasicRecs() {
        Map<String, UserInfo> userData = new HashMap<String, UserInfo>();

        String userId1 = "1";
        List<String> songs1 = new ArrayList<>();
        songs1.add("Born this Way");
        songs1.add("Barracuda");
        songs1.add("Hotel California");
        songs1.add("California Love");
        List<String> artists1 = new ArrayList<>();
        artists1.add("Kendrick Lamar");
        artists1.add("Lady Gaga");
        artists1.add("Freddie Gibbs");
        artists1.add("U2");
        List<String> genres1 = new ArrayList<>();
        genres1.add("rock");
        genres1.add("hip hop");
        genres1.add("experimental rap");
        genres1.add("soul");
        boolean[] metrics1 = {false, false, false};
        int[] weights1 = {50, 50, 50};
        UserInfo userInfo1 = new UserInfo(artists1, songs1, genres1, metrics1, weights1);
        userData.put(userId1, userInfo1);

        String userId2 = "2";
        List<String> songs2 = new ArrayList<>();
        songs2.add("Born this Way");
        songs2.add("Barracuda");
        songs2.add("Hotel California");
        songs2.add("California Love");
        List<String> artists2 = new ArrayList<>();
        artists2.add("Kendrick Lamar");
        artists2.add("Lady Gaga");
        artists2.add("Freddie Gibbs");
        artists2.add("U2");
        List<String> genres2 = new ArrayList<>();
        genres2.add("rock");
        genres2.add("hip hop");
        genres2.add("experimental rap");
        genres2.add("soul");
        boolean[] metrics2 = {false, false, false};
        int[] weights2 = {50, 50, 50};
        UserInfo userInfo2 = new UserInfo(artists2, songs2, genres2, metrics2, weights2);
        userData.put(userId2, userInfo2);

        String userId3 = "3";
        List<String> songs3 = new ArrayList<>();
        songs3.add("Born this Way");
        songs3.add("Sunday Bloody Sunday");
        songs3.add("Hotel California");
        songs3.add("California Love");
        List<String> artists3 = new ArrayList<>();
        artists3.add("Kendrick Lamar");
        artists3.add("Juice Newton");
        artists3.add("Freddie Gibbs");
        artists3.add("U2");
        List<String> genres3 = new ArrayList<>();
        genres3.add("rock");
        genres3.add("christian rock");
        genres3.add("jazz");
        genres3.add("soul");
        boolean[] metrics3 = {false, false, false};
        int[] weights3 = {50, 50, 50};
        UserInfo userInfo3 = new UserInfo(artists3, songs3, genres3, metrics3, weights3);
        userData.put(userId3, userInfo3);

        String userId4 = "4";
        List<String> songs4 = new ArrayList<>();
        songs4.add("Stay Away from Me");
        songs4.add("Lovely Day");
        songs4.add("Try a Little Tenderness");
        songs4.add("Otis");
        List<String> artists4 = new ArrayList<>();
        artists4.add("Otis Redding");
        artists4.add("Eric Clapton");
        artists4.add("Ka");
        artists4.add("Grip");
        List<String> genres4 = new ArrayList<>();
        genres4.add("rock and roll");
        genres4.add("rhythm and blues");
        genres4.add("house");
        genres4.add("hyperpop");
        boolean[] metrics4 = {false, false, false};
        int[] weights4 = {50, 50, 50};
        UserInfo userInfo4 = new UserInfo(artists4, songs4, genres4, metrics4, weights4);
        userData.put("4", userInfo4);

        RecommendationSystem system = RecommendationSystem.fromUserData(userData);
        List<String> output = system.getRecsFor(userId1, 2);
        assertTrue(output.size() == 2);

        String outputId1 = output.get(0);
        String outputId2 = output.get(1);

        System.out.println(outputId1);
        System.out.println(outputId2);

        assertTrue((outputId1.equals(userId2)) || (outputId2.equals(userId2)));
        assertTrue((outputId1.equals(userId3)) || (outputId2.equals(userId3)));
        assertFalse(outputId1.equals(userId4));
        assertFalse(outputId2.equals(userId4));

    }

}
