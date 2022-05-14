package edu.brown.cs.student.main;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

/**
 * Mediates the connection to spotify.
 */
public class ApiDriver {
    private static final String REQUIRED_SCOPES = "user-read-email,user-top-read,user-follow-read,user-library-read,streaming,user-read-private,user-read-playback-state,user-modify-playback-state,user-library-modify";

    private final String ourUrl;
    private final String callbackLocation;
    private final String clientId;
    private final String clientSecret;

    /**
     * Creates new class to manage the spotify api.
     * @param ourUrl this servers url
     * @param callbackLocation where to send after authentication
     * @param clientId from spotify dashboard
     * @param clientSecret from spotify dashboard
     */
    public ApiDriver(String ourUrl, String callbackLocation, String clientId, String clientSecret) {
        this.ourUrl = ourUrl;
        this.callbackLocation = callbackLocation;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * Gets api handle.
     * @param tokens from spotify to authenticate
     * @return the handle to spotify
     */
    private SpotifyApi spotifyApi(Server.Tokens tokens) {
        URI redirectUri = SpotifyHttpManager.makeUri(ourUrl + callbackLocation);
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAccessToken(tokens.accessToken())
                .setRefreshToken(tokens.refreshToken())
                .setRedirectUri(redirectUri)
                .build();
    }

    /**
     * Gets an API handle that doesnt provide tokens to the endpoint
     * @return api handle
     */
    private SpotifyApi noTokens() {
        URI redirectUri = SpotifyHttpManager.makeUri(ourUrl + callbackLocation);
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

    }

    public Server.Tokens updateTokens(AuthorizationCodeCredentials credentials, Server.Tokens tokens) {
        int expiresInSeconds = credentials.getExpiresIn();
        Instant expires = Instant.now().plusSeconds(expiresInSeconds);
        return new Server.Tokens(credentials.getAccessToken(), tokens.refreshToken(), expires);
    }

    private Server.Tokens credentialsToTokens(AuthorizationCodeCredentials credentials) {
        int expiresInSeconds = credentials.getExpiresIn();
        Instant expires = Instant.now().plusSeconds(expiresInSeconds);
        return new Server.Tokens(
                credentials.getAccessToken(),
                credentials.getRefreshToken(),
                expires);
    }

    public Server.Tokens refreshTokens(Server.Tokens tokens) throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeCredentials newCredentials = spotifyApi(tokens)
                .authorizationCodeRefresh()
                .build()
                .execute();
        return updateTokens(newCredentials, tokens);
    }

    /**
     * Get the Authorization Code URI from Spotify.
     * @return the Spotify Authorization Code URI
     */
    public URI getRedirectUri(String sessionToken) {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = noTokens().authorizationCodeUri()
                .state(sessionToken)
                .scope(REQUIRED_SCOPES)
                .build();
        CompletableFuture<URI> uriFuture = authorizationCodeUriRequest.executeAsync();
        URI uri = uriFuture.join();
        System.out.println("URI: " + uri.toString());
        return uri;
    }


    /**
     * Get the Authorization Code from Spotify.
     * @param authorizationCode spotify gives us to fetch tokens
     * @return tokens to authenticate
     */
    public Server.Tokens getAccessTokens(String authorizationCode) {
        AuthorizationCodeCredentials credentials = noTokens().authorizationCode(authorizationCode)
                .build()
                .executeAsync()
                .join();
        return credentialsToTokens(credentials);
    }


    public Server.Tokens authorizationCodeRefresh(Server.Tokens tokens) {
            SpotifyApi api = spotifyApi(tokens);
            AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = api.authorizationCodeRefresh()
                    .build();
            final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRefreshRequest.executeAsync();
            // Thread free to do other tasks...
            // Example Only. Never block in production code.
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();
            // Set access token for further "spotifyApi" object usage
            //spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
            int expiresInSeconds = authorizationCodeCredentials.getExpiresIn();
            Instant expires = Instant.now().plusSeconds(expiresInSeconds);
            return new Server.Tokens(
                    authorizationCodeCredentials.getAccessToken(),
                    authorizationCodeCredentials.getRefreshToken(),
                    expires);

    }




    /**
     * Get the current user's profile from Spotify.
     * @param tokens to authenticate
     */
    public User getCurrentUserProfile(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi(tokens);
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = api.getCurrentUsersProfile()
                .build();
        final CompletableFuture<User> userFuture = getCurrentUsersProfileRequest.executeAsync();
        return userFuture.join();

    }

    /**
     *  Gets a user's top tracks.
     *  @param tokens to authenticate
     */
    public Track[] getTopTracks(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi(tokens);
        GetUsersTopTracksRequest getUsersTopTracksRequest = api.getUsersTopTracks().build();
        CompletableFuture<Paging<Track>> pagingFuture = getUsersTopTracksRequest.executeAsync();
        Paging<Track> trackPaging = pagingFuture.join();
        return trackPaging.getItems();
    }

    public Track getTrack(Server.Tokens tokens, String trackId) {
        SpotifyApi api = spotifyApi(tokens);
        return api.getTrack(trackId).build().executeAsync().join();
    }

    /**
     *  Gets the lyrics to a song.
     *  @param tokens to authenticate
     * @param query to search
     */
    public Track[] searchTracks(Server.Tokens tokens, String query) {
        SpotifyApi api = spotifyApi(tokens);
        SearchTracksRequest request = api.searchTracks(query).build();
        CompletableFuture<Paging<Track>> pagingFuture = request.executeAsync();
        Paging<Track> trackPaging = pagingFuture.join();
        return trackPaging.getItems();
    }

    /**
     * Gets a user's top artists.
     * @param tokens to authenticate
     * @return artists found
     */
    public Artist[] getTopArtists(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi(tokens);
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = api.getUsersTopArtists().build();
        CompletableFuture<Paging<Artist>> pagingFuture = getUsersTopArtistsRequest.executeAsync();
        Paging<Artist> artistPaging = pagingFuture.join();
        return artistPaging.getItems();
    }

    /**
     * Gets a user's top artists.
     * @param tokens to authenticate
     * @return artist found
     */
    public Artist[] getFollowedArtists(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi(tokens);
        GetUsersFollowedArtistsRequest request = api.getUsersFollowedArtists(ModelObjectType.ARTIST)
                .build();
        PagingCursorbased<Artist> pagingFuture = request.executeAsync().join();
        return pagingFuture.getItems();
    }

    /**
     * Gets info about an artist.
     * @param tokens to authenticate
     * @param artistId from spotify
     * @return artist found
     */
    public Artist getArtist(Server.Tokens tokens, String artistId) {
        SpotifyApi api = spotifyApi(tokens);
        GetArtistRequest request = api.getArtist(artistId).build();
        return request.executeAsync().join();
    }

    /**
     * Gets data about the provided user's music preferences.
     * @param tokens to authenticate
     * @return music data
     */
    public Server.RecommendationData getRecommendationData(Server.Tokens tokens) {
        Track[] topTracks = getTopTracks(tokens);
        Stream<Artist> followedArtists = Stream.of(getFollowedArtists(tokens));
        Stream<Artist> topArtists = Stream.of(getTopArtists(tokens));
        Stream<Artist> trackArtists = Stream.of(topTracks)
                .map(Track::getArtists)
                .flatMap(Stream::of)
                .map(ArtistSimplified::getId)
                .map(artistId -> getArtist(tokens, artistId));

        List<Artist> allArtists = Stream.concat(
                Stream.concat(followedArtists, topArtists),
                trackArtists)
                .toList();

        List<String> artists = allArtists.stream()
                .map(Artist::getId)
                .distinct()
                .toList();

        List<String> songs = Stream.of(topTracks)
                .map(Track::getId)
                .distinct()
                .toList();

        List<String> genres = allArtists.stream()
                .map(Artist::getGenres)
                .flatMap(Stream::of)
                .distinct()
                .toList();

        return new Server.RecommendationData(artists, songs, genres);
    }
}
