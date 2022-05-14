package edu.brown.cs.student.main;

import com.google.gson.Gson;
import edu.brown.cs.student.main.FriendRec.RecommendationSystem;
import edu.brown.cs.student.main.FriendRec.UserInfo;
import se.michaelthelin.spotify.model_objects.specification.*;
import spark.Request;
import spark.Spark;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private static final String CALLBACK_LOCATION = "callback"; // must change in spotify dashboard
    private static final String LOGGED_IN_LOCATION = "newSession";

    private final String staticSiteUrl;
    private final short ourPort;
    private final String staticFiles;
    private final DatabaseDriver users;
    private final ApiDriver api;

    /**
     * Constructor, sets local variables for site URL, port, ID and Secret codes, and static files.
     * Builds redirect URI and Spotify API
     *
     * @param ourPort
     * @param staticFiles
     */
    Server(DatabaseDriver users, ApiDriver api, String staticSiteUrl, short ourPort, String staticFiles) {
        this.api = api;
        this.staticSiteUrl = staticSiteUrl;
        this.ourPort = ourPort;
        this.staticFiles = staticFiles;
        this.users = users;
    }

    public void start() {
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

        Spark.get("/login", (request, response) -> {
            String sessionToken = randomString(20);
            response.redirect(api.getRedirectUri(sessionToken).toString());
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
            Tokens tokens = api.getAccessTokens(authorizationCode);
            User user = api.getCurrentUserProfile(tokens);
            users.initializeUser(sessionToken, tokens, user);
            String queryParameters =
                    "?sessionToken=" + sessionToken
                    + "&accessToken=" + tokens.accessToken();
            response.redirect(staticSiteUrl + LOGGED_IN_LOCATION + queryParameters);
            return null;
        });

        Spark.get("/userData", (request, response) -> {
            User user = api.getCurrentUserProfile(tokensFromRequest(request));
            String userJsonString = new Gson().toJson(user);
            System.out.println(userJsonString);
            return userJsonString;
        });

        Spark.get("/topTracks", (request, response) -> {
            Track[] tracks = api.getTopTracks(tokensFromRequest(request));
            return new Gson().toJson(tracks);
        });

        Spark.get("/topArtists", (request, response) -> {
            Artist[] artists = api.getTopArtists(tokensFromRequest(request));
            return new Gson().toJson(artists);
        });

        Spark.get("/search", (request, response) -> {
            String query = request.queryParams("query");
            if (query == null) return new Gson().toJson(List.of());
            Track[] tracks = api.searchTracks(tokensFromRequest(request), query);
            return new Gson().toJson(tracks);
        });

        Spark.get("/findArtist", (request, response) -> {
           String query = request.queryParams("query");
           if (query == null) return new Gson().toJson(List.of());
           Artist artist = api.getArtist(tokensFromRequest(request), query);
           return new Gson().toJson(artist);
        });

        Spark.post("/storeRecommendationPreferences", (request, response) -> {
            String userId = userIdFromRequest(request);
            Tokens tokens = tokensFromUserId(userId);

            AllPreferences data = new Gson().fromJson(request.body(), AllPreferences.class);
            RecommendationData recommendationData = api.getRecommendationData(tokens);

            users.initializeRecommendations(userId, Map.of(
                    "songs", new DatabaseDriver.Data(data.songs.matchSame, recommendationData.songs(), data.songs.matchWeight),
                    "genres", new DatabaseDriver.Data(data.genres.matchSame, recommendationData.genres(), data.genres.matchWeight),
                    "artists", new DatabaseDriver.Data(data.artists.matchSame, recommendationData.artists(), data.artists.matchWeight)
            ));

            return "200 OK";
        });

        Spark.get("/recommendations", (request, response) -> {
            String userId = userIdFromRequest(request);

            List<User> ret = new ArrayList<>();

            for (String id : recommendations(userId, 5)) {
                Optional<Tokens> tokens = users.getTokens(id);
                if (tokens.isEmpty()) {
                    throw new RuntimeException("tokens were not found");
                }
                ret.add(api.getCurrentUserProfile(tokens.get()));
            }
           return new Gson().toJson(ret);
        });

        Spark.post("/insertSuggestion", (request, response) -> {
            String userId = userIdFromRequest(request);
            InsertSuggestionsQuery toInsert = new Gson().fromJson(request.body(), InsertSuggestionsQuery.class);
            try {
                users.insertSuggestion(userId, toInsert.songId, toInsert.suggestion);
                return new Gson().toJson(Map.of());
            } catch (IllegalArgumentException e) {
                return new Gson().toJson(Map.of("error", e.getMessage()));
            }
        });

        Spark.post("/deleteSuggestion", (request, response) -> {
            String userId = userIdFromRequest(request);
            InsertSuggestionsQuery toInsert = new Gson().fromJson(request.body(), InsertSuggestionsQuery.class);
            users.deleteSuggestion(userId, toInsert.songId, toInsert.suggestion);
            return "200 OK";
        });

        Spark.get("/usersSuggestionsFor", (request, response) -> {
            String userId = userIdFromRequest(request);
            Tokens tokens = tokensFromUserId(userId);
            String songId = request.queryParams("query");
            List<Track> songs = users.getUsersSuggestionsFor(userId, songId).stream()
                    .map(t -> api.getTrack(tokens, t))
                    .toList();
            return new Gson().toJson(songs);
        });

        Spark.get("/topSuggestionsFor", (request, response) -> {
            String userId = userIdFromRequest(request);
            Tokens tokens = tokensFromUserId(userId);
            String songId = request.queryParams("query");
            List<Track> songs = users.topSuggestionsFor(songId, 3).stream()
                    .map(t -> api.getTrack(tokens, t))
                    .toList();
            return new Gson().toJson(songs);
        });
    }

    public record Tokens(String accessToken, String refreshToken) { }

    private static class InsertSuggestionsQuery {
        String songId;
        String suggestion;
    }

    private static class MatchPreference {
        boolean matchSame;
        int matchWeight;
    }

    private static class AllPreferences {
        MatchPreference songs;
        MatchPreference genres;
        MatchPreference artists;
    }

    public record RecommendationData(
            List<String> artists,
            List<String> songs,
            List<String> genres
    ) {}


    private String userIdFromRequest(Request request) throws RuntimeException, SQLException {
        String sessionToken = request.headers("Authentication");
        Optional<String> userId = users.userIdFromSessionToken(sessionToken);
        if (userId.isEmpty()) {
            throw new RuntimeException("user was not found");
        }
        return userId.get();
    }

    private Tokens tokensFromUserId(String userId) throws RuntimeException, SQLException {
        Optional<Tokens> tokens = users.getTokens(userId);
        if (tokens.isEmpty()) {
            throw new RuntimeException("tokens were not found");
        }
        return tokens.get();
    }

    public List<String> recommendations(String userId, int numRecs) throws SQLException {
        Map<String, UserInfo> data = users.getRecommendations().entrySet().stream()
                .map(e -> {
                    Map<String, DatabaseDriver.Data> t = e.getValue();

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
