package edu.brown.cs.student.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import spark.Spark;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * This class is an attempt to implement the Authorization Code Flow using
 * the Spotify Web API Java Wrapper: https://github.com/spotify-web-api-java/spotify-web-api-java
 *
 * Should be renamed Server if Server.java is deleted.
 */

public class ServWrapper {

    private final String siteUrl;
    private final short port;
    private final String clientId;
    private final String clientSecret;
    private final String staticFiles;
    private final KnownUsers users;

    private static final String CALLBACK_LOCATION = "/callback"; // must change in spotify dashboard
    private static final String LOGGED_IN_LOCATION = "/newSession";

    // TODO: put these in a config file
    private static final short SITE_SERVER_PORT = 3000;
    private static final String SITE_SERVER_URI = "http://localhost:" + SITE_SERVER_PORT;
    private static final String SESSION_TOKEN_COOKIE_NAME = "sessionToken";

    private /*static*/ final SpotifyApi spotifyApi;
    private /*static*/ final URI redirectUri;

    private URI authorizationCodeUri;

    private AuthorizationCodeRequest authorizationCodeRequest;


    /**
     * Constructor, sets local variables for site URL, port, ID and Secret codes, and static files.
     * Builds redirect URI and Spotify API
     *
     * @param siteUrl
     * @param port
     * @param clientId
     * @param clientSecret
     * @param staticFiles
     */
    ServWrapper(KnownUsers users, String siteUrl, short port, String clientId, String clientSecret, String staticFiles) {
        this.siteUrl = siteUrl;
        this.port = port;
        this.clientId =  clientId;
        this.clientSecret = clientSecret;
        this.staticFiles = staticFiles;
        this.users = users;
        this.redirectUri = SpotifyHttpManager.makeUri(this.siteUrl + CALLBACK_LOCATION);
        //this.redirectUri = SpotifyHttpManager.makeUri("http://localhost:3000" + CALLBACK_LOCATION);
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .setRedirectUri(this.redirectUri)
                .build();
    }

    /**
     * Get the Authorization Code URI from Spotify.
     * @return the Spotify Authorization Code URI
     */
    public /*static*/ URI authorizationCodeUri_Async(String sessionToken) {
        URI output = null;
        try {
            AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                    .state(sessionToken)
                    //.scope("[SCOPES]")
                    //.show_dialog(true)
                    .build();
            final CompletableFuture<URI> uriFuture = authorizationCodeUriRequest.executeAsync();

            // FROM PACKAGE DEV: Thread free to do other tasks...

            // FROM PACKAGE DEV: Example Only. Never block in production code.
            final URI uri = uriFuture.join();

            System.out.println("URI: " + uri.toString());
            output = uri;
        } catch (CompletionException e) {
            System.out.println("ERROR: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
        return output;
    }

    private record Tokens(String accessToken, String refreshToken) {}

    /**
     * Get the Authorization Code from Spotify.
     */
    public /*static*/ Tokens authorizationCode_Async() throws CompletionException, CancellationException {
        final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture =
                authorizationCodeRequest.executeAsync();

        // FROM PACKAGE DEV: Thread free to do other tasks...

        // FROM PACKAGE DEV: Example Only. Never block in production code.
        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();

        // FROM PACKAGE DEV: Set access and refresh token for further "spotifyApi" object usage
        return new Tokens(authorizationCodeCredentials.getAccessToken(), authorizationCodeCredentials.getRefreshToken());
    }

    /**
     * Get the current user's profile from Spotify.
     */
    public /*static*/ User getCurrentUsersProfile_Async(Tokens tokens) {
        /*static final*/
        this.spotifyApi.setAccessToken(tokens.accessToken);
        this.spotifyApi.setRefreshToken(tokens.refreshToken);
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = this.spotifyApi.getCurrentUsersProfile().build();
        final CompletableFuture<User> userFuture = getCurrentUsersProfileRequest.executeAsync();
        // FROM PACKAGE DEV: Thread free to do other tasks...

        // FROM PACKAGE DEV: Example Only. Never block in production code.
        return userFuture.join();
    }

    private void sparkStartup() {
        Spark.port(port);
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
            String body = new Gson().toJson(Map.of(
                    "error",stacktrace.toString()
            ));
            response.body(body);
        });
    }

    public void start() {

        sparkStartup();

        Spark.get("/login", (request, response) -> {
            String sessionToken = randomString(20);
            authorizationCodeUri = authorizationCodeUri_Async(sessionToken);
            return new Gson().toJson(Map.of(
                    "redirect", authorizationCodeUri,
                    "sessionToken", sessionToken
            ));
        });

        Spark.get(CALLBACK_LOCATION, (request, response) -> {
            String errorCode = request.params("error");
            if (errorCode != null) {
                System.out.println(errorCode);
                response.body("<pre>Error" + errorCode + "</pre>");
                return null;
            }
            String sessionToken = request.queryParams("state");
            if (sessionToken == null) {
                throw new RuntimeException("Spotify did not provide session token");
            }
            String authorizationCode = request.queryParams("code");
            this.authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode)
                    .build();
            Tokens tokens = authorizationCode_Async();
            User user = getCurrentUsersProfile_Async(tokens);
            try {
                users.initializeUser(sessionToken, tokens.refreshToken, tokens.accessToken, user);
            } catch (SQLException e) {
                System.out.println("Storing user details failed: " + e.getMessage());
            }

            String queryParameter = "?sessionToken=" + sessionToken;
            response.redirect(SITE_SERVER_URI + LOGGED_IN_LOCATION + queryParameter);
            return null;
        });

        Spark.get("/userData", (request, response) -> {
            String sessionToken = request.headers("Authentication");
            System.out.println("session token in header: " + sessionToken);
            if (sessionToken == null) {
                System.out.println("Could not find session token, user data request");
                response.body("<pre>No session token please load session token</pre>");
                return "<pre>No session token please turn on cookies</pre>";
            }
            Optional<String> result = users.userIdFromSessionToken(sessionToken);
            if (result.isEmpty()) {
                throw new RuntimeException("Session token `" + sessionToken + "` could not be found in database");
            }
            Optional<String> result2 = users.userDataJson(result.get());
            if (result2.isEmpty()) {
                throw new RuntimeException("User data for " + result.get() + " could not be found in database");
            }
            return result2.get();
        });

//        TODO: re-enable when we merge servers
//        Spark.get("/*", (request, response) -> {
//           response.type("text/html");
//           return Files.readString(Path.of("site/build/index.html"));
//        });
    }

    private static String randomString(int len) {
        char[] hex = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        StringBuilder ret = new StringBuilder(len);
        byte[] randomBytes = new byte[len];
        new SecureRandom().nextBytes(randomBytes);
        for (byte b : randomBytes) {
            ret.append(hex[(b & 0b1111) & 0xf]);
            ret.append(hex[(b >>> 4) & 0xf]);
        }
        return ret.toString();
    }
}
