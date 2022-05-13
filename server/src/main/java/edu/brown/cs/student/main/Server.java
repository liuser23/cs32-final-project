package edu.brown.cs.student.main;

import com.google.gson.Gson;
import edu.brown.cs.student.main.BloomFilter.SimilarityMetrics.SimilarityMetric;
import edu.brown.cs.student.main.FriendRec.RecommendationSystem;
import edu.brown.cs.student.main.FriendRec.UserInfo;
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
import spark.Request;
import spark.Spark;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is an attempt to implement the Authorization Code Flow using
 * the Spotify Web API Java Wrapper: https://github.com/spotify-web-api-java/spotify-web-api-java
 *
 * Should be renamed Server if Server.java is deleted.
 */

public class Server {
    private static final String CALLBACK_LOCATION = "callback"; // must change in spotify dashboard
    private static final String LOGGED_IN_LOCATION = "newSession";
    private static final String REQUIRED_SCOPES = "user-read-email,user-top-read,user-follow-read,user-library-read,streaming,user-read-private,user-read-playback-state,user-modify-playback-state,user-library-modify";

    private final String staticSiteUrl;
    private final String ourUrl;
    private final short ourPort;
    private final String clientId;
    private final String clientSecret;
    private final String staticFiles;
    private final KnownUsers users;
    private final SpotifyApi spotifyApi;

    /**
     * Constructor, sets local variables for site URL, port, ID and Secret codes, and static files.
     * Builds redirect URI and Spotify API
     *
     * @param ourUrl
     * @param ourPort
     * @param clientId
     * @param clientSecret
     * @param staticFiles
     */
    Server(KnownUsers users, String staticSiteUrl, String ourUrl, short ourPort, String clientId, String clientSecret, String staticFiles) {
        this.staticSiteUrl = staticSiteUrl;
        this.ourUrl = ourUrl;
        this.ourPort = ourPort;
        this.clientId =  clientId;
        this.clientSecret = clientSecret;
        this.staticFiles = staticFiles;
        this.users = users;

        URI redirectUri = SpotifyHttpManager.makeUri(this.ourUrl + CALLBACK_LOCATION);
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .setRedirectUri(redirectUri)
                .build();
    }

    /**
     * Get the Authorization Code URI from Spotify.
     * @return the Spotify Authorization Code URI
     */
    public URI getRedirectUri(String sessionToken) {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
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
    public Tokens getAccessTokens(String authorizationCode) {
        AuthorizationCodeCredentials credentials = spotifyApi.authorizationCode(authorizationCode)
                .build()
                .executeAsync()
                .join();
        return new Tokens(credentials.getAccessToken(), credentials.getRefreshToken());
    }

    /**
     * Get the current user's profile from Spotify.
     */
    public User getCurrentUserProfile(Tokens tokens) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = this.spotifyApi.getCurrentUsersProfile().build();
        final CompletableFuture<User> userFuture = getCurrentUsersProfileRequest.executeAsync();
        return userFuture.join();
    }

    /**
     *  Gets a user's top tracks
     */
    public Track[] getTopTracks(Tokens tokens) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks().build();
        CompletableFuture<Paging<Track>> pagingFuture = getUsersTopTracksRequest.executeAsync();
        Paging<Track> trackPaging = pagingFuture.join();
        return trackPaging.getItems();
    }

    public Track getTrack(String trackId) {
        return spotifyApi.getTrack(trackId).build().executeAsync().join();
    }

    /**
     *  Get's the lyrics to a song
     */
    public Track[] searchTracks(Tokens tokens, String query) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());
        SearchTracksRequest request = spotifyApi.searchTracks(query).build();
        CompletableFuture<Paging<Track>> pagingFuture = request.executeAsync();
        Paging<Track> trackPaging = pagingFuture.join();
        return trackPaging.getItems();
    }

    /**
     * Gets a user's top artists.
     */
    public Artist[] getTopArtists(Tokens tokens) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());
        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists().build();
        CompletableFuture<Paging<Artist>> pagingFuture = getUsersTopArtistsRequest.executeAsync();
        Paging<Artist> artistPaging = pagingFuture.join();
        return artistPaging.getItems();
    }

    /**
     * Gets a user's top artists.
     */
    public Artist[] getFollowedArtists(Tokens tokens) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());
        GetUsersFollowedArtistsRequest request = spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST).build();
        PagingCursorbased<Artist> pagingFuture = request.executeAsync().join();
        return pagingFuture.getItems();
    }

    public Artist getArtist(Tokens tokens, String artistId) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());
        GetArtistRequest request = spotifyApi.getArtist(artistId).build();
        return request.executeAsync().join();
    }

    public RecommendationData getRecommendationData(Tokens tokens) {
        this.spotifyApi.setAccessToken(tokens.accessToken());
        this.spotifyApi.setRefreshToken(tokens.refreshToken());

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

        return new RecommendationData(artists, songs, genres);
    }

    public List<String> recommendations(String userId, int numRecs) throws SQLException {
        Map<String, UserInfo> data = users.getRecommendations().entrySet().stream()
                .map(e -> {
                    Map<String, KnownUsers.Data> t = e.getValue();

                    List<String> artists = t.get("artists").items();
                    List<String> songs = t.get("songs").items();
                    List<String> genres = t.get("genres").items();

                    boolean[] metrics = {
                            t.get("artists").matchSame(),
                            t.get("songs").matchSame(),
                            t.get("genres").matchSame()};

                    int[] weights = {
                            t.get("artists").weight(),
                            t.get("songs").weight(),
                            t.get("genres").weight()};

                    UserInfo info = new UserInfo(artists, songs, genres, metrics, weights);

                    return new AbstractMap.SimpleEntry<>(e.getKey(), info);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        RecommendationSystem system = RecommendationSystem.fromUserData(data);
        return system.getRecsFor(userId, numRecs);
    }

    public void start() {
        sparkStartup();

        Spark.get("/login", (request, response) -> {
            String sessionToken = randomString(20);
            response.redirect(getRedirectUri(sessionToken).toString());
            return null;
        });

        Spark.get(CALLBACK_LOCATION, (request, response) -> {
            String errorCode = request.params("error");
            if (errorCode != null) {
                throw new RuntimeException("Spotify returned error: " + errorCode);
            }
            String sessionToken = request.queryParams("state");
            if (sessionToken == null) {
                throw new RuntimeException("Spotify did not provide session token");
            }
            String authorizationCode = request.queryParams("code");
            Tokens tokens = getAccessTokens(authorizationCode);
            User user = getCurrentUserProfile(tokens);
            users.initializeUser(sessionToken, tokens, user);
            String queryParameters =
                    "?sessionToken=" + sessionToken
                    + "&accessToken=" + tokens.accessToken();
            response.redirect(staticSiteUrl + LOGGED_IN_LOCATION + queryParameters);
            return null;
        });

        Spark.get("/userData", (request, response) -> {
            User user = getCurrentUserProfile(tokensFromRequest(request));
            String userJsonString = new Gson().toJson(user);
            System.out.println(userJsonString);
            return userJsonString;
        });

        Spark.get("/topTracks", (request, response) -> {
            Track[] tracks = getTopTracks(tokensFromRequest(request));
            return new Gson().toJson(tracks);
        });

        Spark.get("/topArtists", (request, response) -> {
            Artist[] artists = getTopArtists(tokensFromRequest(request));
            return new Gson().toJson(artists);
        });

        Spark.get("/search", (request, response) -> {
            String query = request.queryParams("query");
            if (query == null) return new Gson().toJson(List.of());
            Track[] tracks = searchTracks(tokensFromRequest(request), query);
            return new Gson().toJson(tracks);
        });

        Spark.get("/findArtist", (request, response) -> {
           String query = request.queryParams("query");
           if (query == null) return new Gson().toJson(List.of());
           Artist artist = getArtist(tokensFromRequest(request), query);
           return new Gson().toJson(artist);
        });

        Spark.post("/storeRecommendationPreferences", (request, response) -> {
            String sessionToken = request.headers("Authentication");
            Optional<String> userId = users.userIdFromSessionToken(sessionToken);
            if (userId.isEmpty()) {
                throw new RuntimeException("user was not found");
            }
            Optional<Tokens> tokens = users.getTokens(userId.get());
            if (tokens.isEmpty()) {
                throw new RuntimeException("tokens were not found");
            }

            System.out.println(request.body());


            dwItemsAssoc data = new Gson().fromJson(request.body(), dwItemsAssoc.class);

            System.out.println(data);
            System.out.println(data.data());

            RecommendationData recommendationData = getRecommendationData(tokens.get());

            users.initializeRecommendations(userId.get(), Map.of(
                    "songs", new KnownUsers.Data(data.data().songs.matchSame, recommendationData.songs(), data.data().songs.matchWeight),
                    "genres", new KnownUsers.Data(data.data().genres.matchSame, recommendationData.genres(), data.data().genres.matchWeight),
                    "artists", new KnownUsers.Data(data.data().artists.matchSame, recommendationData.artists(), data.data().artists.matchWeight)
            ));

            return "200 OK";
        });

        Spark.get("/recommendations", (request, response) -> {
            String sessionToken = request.headers("Authentication");
            Optional<String> userId = users.userIdFromSessionToken(sessionToken);
            if (userId.isEmpty()) {
                throw new RuntimeException("user was not found");
            }

            List<User> ret = new ArrayList<>();

            for (String id : recommendations(userId.get(), 5)) {
                Optional<Tokens> tokens = users.getTokens(id);
                if (tokens.isEmpty()) {
                    throw new RuntimeException("tokens were not found");
                }
                ret.add(getCurrentUserProfile(tokens.get()));
            }
           return new Gson().toJson(ret);
        });

        Spark.post("/insertSuggestion", (request, response) -> {
            String sessionToken = request.headers("Authentication");
            Optional<String> userId = users.userIdFromSessionToken(sessionToken);
            if (userId.isEmpty()) {
                throw new RuntimeException("user was not found");
            }
            InsertSuggestionsQuery toInsert = new Gson().fromJson(request.body(), InsertSuggestionsQuery.class);
            try {
                users.insertSuggestion(userId.get(), toInsert.songId, toInsert.suggestion);
                return new Gson().toJson(Map.of());
            } catch (IllegalArgumentException e) {
                return new Gson().toJson(Map.of("error", e.getMessage()));
            }
        });

        Spark.post("/deleteSuggestion", (request, response) -> {
            String sessionToken = request.headers("Authentication");
            Optional<String> userId = users.userIdFromSessionToken(sessionToken);
            if (userId.isEmpty()) {
                throw new RuntimeException("user was not found");
            }
            InsertSuggestionsQuery toInsert = new Gson().fromJson(request.body(), InsertSuggestionsQuery.class);
            users.deleteSuggestion(userId.get(), toInsert.songId, toInsert.suggestion);
            return "200 OK";
        });

        Spark.get("/usersSuggestionsFor", (request, response) -> {
            String sessionToken = request.headers("Authentication");
            Optional<String> userId = users.userIdFromSessionToken(sessionToken);
            if (userId.isEmpty()) {
                throw new RuntimeException("user was not found");
            }
            String songId = request.queryParams("query");
            List<Track> songs = users.getUsersSuggestionsFor(userId.get(), songId).stream()
                    .map(this::getTrack)
                    .toList();
            return new Gson().toJson(songs);
        });

        Spark.get("/topSuggestionsFor", (request, response) -> {
            String songId = request.queryParams("query");
            List<Track> songs = users.topSuggestionsFor(songId, 3).stream()
                    .map(this::getTrack)
                    .toList();
            return new Gson().toJson(songs);
        });
    }

    private static class InsertSuggestionsQuery {
        String songId;
        String suggestion;
    }

    private static class dwItems {
        boolean matchSame;
        int matchWeight;
    }

    private static class dwItemsAssocData {
        dwItems songs;
        dwItems genres;
        dwItems artists;
    }

    private class dwItemsAssoc {
        dwItemsAssocData data;
        public dwItemsAssocData data() {
            return data;
        }
    }

    private Tokens tokensFromRequest(Request request) throws SQLException {
        String sessionToken = request.headers("Authentication");
        Optional<String> userId = users.userIdFromSessionToken(sessionToken);
        if (userId.isEmpty()) {
            throw new RuntimeException("user was not found");
        }
        Optional<Tokens> tokens = users.getTokens(userId.get());
        if (tokens.isEmpty()) {
            throw new RuntimeException("tokens were not found");
        }
        return tokens.get();
    }

    private void sparkStartup() {
        Spark.port(ourPort);
        Spark.externalStaticFileLocation(staticFiles);

        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            StringWriter stacktrace = new StringWriter();
            try (PrintWriter pw = new PrintWriter(stacktrace)) {
                exception.printStackTrace(pw);
            }
            String body = new Gson().toJson(Map.of("error", stacktrace.toString()));
            response.body(body);
        });
    }

    private static String randomString(int len) {
        char[] hex = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        StringBuilder ret = new StringBuilder(len);
        byte[] randomBytes = new byte[len/2];
        new SecureRandom().nextBytes(randomBytes);
        for (byte b : randomBytes) {
            ret.append(hex[b & 0xf]);
            ret.append(hex[(b >>> 4) & 0xf]);
        }
        return ret.toString();
    }
}
