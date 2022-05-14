package edu.brown.cs.student.main;

import org.junit.Test;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiDriverTest {
    public ApiDriver getApiDriver() throws IOException {
        String clientId = Files.readString(Path.of("../secret/client_id.txt"));
        String clientSecret = Files.readString(Path.of("../secret/client_secret.txt"));
        return new ApiDriver(Main.OUR_URL, "callback", clientId, clientSecret);
    }

    public Server.Tokens getAccessToken() throws IOException {
        String accessToken = Files.readString(Path.of("../secret/access_token.txt"));
        return new Server.Tokens(accessToken, null, null);
    }

    @Test
    public void basicUserData() throws IOException {
        User user = getApiDriver().getCurrentUserProfile(getAccessToken());
        assert user.getDisplayName() != null;
        assert user.getId().length() > 5;
    }

    @Test
    public void variousMaybeEmpty() throws IOException {
        Track[] tracks = getApiDriver().getTopTracks(getAccessToken());
        Artist[] artists = getApiDriver().getFollowedArtists(getAccessToken());
        Artist[] tops = getApiDriver().getTopArtists(getAccessToken());
    }

    @Test
    public void searchTracks() throws IOException {
        Track[] result = getApiDriver().searchTracks(getAccessToken(), "a");
        assert result.length>0;
    }

    @Test
    public void search() throws IOException {
        Artist result = getApiDriver().getArtist(getAccessToken(), "3RnTXThtZ7Oe4RZYE6mRyp");
        assertEquals("Gasoline", result.getName());
    }
}
