package edu.brown.cs.student.main;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ApiDriver {
    private static final String REQUIRED_SCOPES = "user-read-email,user-top-read,user-follow-read,user-library-read,streaming,user-read-private,user-read-playback-state,user-modify-playback-state,user-library-modify";

    private final String ourUrl;
    private final String callbackLocation;
    private final String clientId;
    private final String clientSecret;
    
    public ApiDriver(String ourUrl, String callbackLocation, String clientId, String clientSecret) {
        this.ourUrl = ourUrl;
        this.callbackLocation = callbackLocation;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
    
    private SpotifyApi spotifyApi() {
        URI redirectUri = SpotifyHttpManager.makeUri(ourUrl + callbackLocation);
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();
    }
    
    /**
     * Get the Authorization Code URI from Spotify.
     * @return the Spotify Authorization Code URI
     */
    public URI getRedirectUri(String sessionToken) {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi().authorizationCodeUri()
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
     */
    public Server.Tokens getAccessTokens(String authorizationCode) {
        AuthorizationCodeCredentials credentials = spotifyApi().authorizationCode(authorizationCode)
                .build()
                .executeAsync()
                .join();
        return new Server.Tokens(credentials.getAccessToken(), credentials.getRefreshToken());
    }

    /**
     * Get the current user's profile from Spotify.
     */
    public User getCurrentUserProfile(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = api.getCurrentUsersProfile().build();
        final CompletableFuture<User> userFuture = getCurrentUsersProfileRequest.executeAsync();
        return userFuture.join();
    }

    /**
     *  Gets a user's top tracks
     */
    public Track[] getTopTracks(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        GetUsersTopTracksRequest getUsersTopTracksRequest = api.getUsersTopTracks().build();
        CompletableFuture<Paging<Track>> pagingFuture = getUsersTopTracksRequest.executeAsync();
        Paging<Track> trackPaging = pagingFuture.join();
        return trackPaging.getItems();
    }

    public Track getTrack(Server.Tokens tokens, String trackId) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        return api.getTrack(trackId).build().executeAsync().join();
    }

    /**
     *  Get's the lyrics to a song
     */
    public Track[] searchTracks(Server.Tokens tokens, String query) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        SearchTracksRequest request = api.searchTracks(query).build();
        CompletableFuture<Paging<Track>> pagingFuture = request.executeAsync();
        Paging<Track> trackPaging = pagingFuture.join();
        return trackPaging.getItems();
    }

    /**
     * Gets a user's top artists.
     */
    public Artist[] getTopArtists(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = api.getUsersTopArtists().build();
        CompletableFuture<Paging<Artist>> pagingFuture = getUsersTopArtistsRequest.executeAsync();
        Paging<Artist> artistPaging = pagingFuture.join();
        return artistPaging.getItems();
    }

    /**
     * Gets a user's top artists.
     */
    public Artist[] getFollowedArtists(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        GetUsersFollowedArtistsRequest request = api.getUsersFollowedArtists(ModelObjectType.ARTIST).build();
        PagingCursorbased<Artist> pagingFuture = request.executeAsync().join();
        return pagingFuture.getItems();
    }

    public Artist getArtist(Server.Tokens tokens, String artistId) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        GetArtistRequest request = api.getArtist(artistId).build();
        return request.executeAsync().join();
    }

    public Server.RecommendationData getRecommendationData(Server.Tokens tokens) {
        SpotifyApi api = spotifyApi();
        api.setAccessToken(tokens.accessToken());
        api.setRefreshToken(tokens.refreshToken());
        Track[] topTracks = getTopTracks(tokens);
        Stream<Artist> followedArtists = Stream.of(getFollowedArtists(tokens));
        Stream<Artist> topArtists = Stream.of(getTopArtists(tokens));
        Stream<Artist> trackArtists = Stream.of(topTracks)
                .map(Track::getArtists)
                .flatMap(Stream::of)
                .map(ArtistSimplified::getId)
                .map(artistId -> getArtist(tokens, artistId));

        List<Artist> allArtists = Stream.concat(Stream.concat(followedArtists, topArtists), trackArtists).toList();

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
