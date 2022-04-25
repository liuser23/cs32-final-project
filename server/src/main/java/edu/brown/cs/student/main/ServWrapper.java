package edu.brown.cs.student.main;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import spark.Spark;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
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

    private static final String CALLBACK_LOCATION = "/callback"; // must change in spotify dashboard
    private static final String LOGGED_IN_LOCATION = "/authorized";

    private /*static*/ final SpotifyApi spotifyApi;
    private /*static*/ final URI redirectUri;
    private /*static*/ final AuthorizationCodeUriRequest authorizationCodeUriRequest;

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
    ServWrapper(String siteUrl, short port, String clientId, String clientSecret, String staticFiles) {
        this.siteUrl = siteUrl;
        this.port = port;
        this.clientId =  clientId;
        this.clientSecret = clientSecret;
        this.staticFiles = staticFiles;
        this.redirectUri = SpotifyHttpManager.makeUri(this.siteUrl + CALLBACK_LOCATION);
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .setRedirectUri(this.redirectUri)
                .build();
        this.authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                //.state("[STATE]")
                //.scope("[SCOPES]")
                //.show_dialog(true)
                .build();
    }

    public /*static*/ URI authorizationCodeUri_Async() {
        URI output = null;
        try {
            final CompletableFuture<URI> uriFuture = this.authorizationCodeUriRequest.executeAsync();

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

    public /*static*/ void authorizationCode_Async() {
        try {
            final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture =
                    authorizationCodeRequest.executeAsync();

            // FROM PACKAGE DEV: Thread free to do other tasks...

            // FROM PACKAGE DEV: Example Only. Never block in production code.
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();

            // FROM PACKAGE DEV: Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
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
                pw.println("<pre>");
                exception.printStackTrace(pw);
                pw.println("</pre>");
            }
            response.body(stacktrace.toString());
        });
    }

    public void start() {
        sparkStartup();

        Spark.get("/login", (request, response) -> {
            authorizationCodeUri = authorizationCodeUri_Async();
            response.redirect(authorizationCodeUri.toString());
            return null;
        });

        Spark.get(CALLBACK_LOCATION, (request, response) -> {
            String errorCode = request.params("error");
            if (errorCode != null) {
                System.out.println(errorCode);
                response.body("<pre>Error" + errorCode + "</pre>");
                return null;
            }

            String authorizationCode = request.queryParams("code");
            this.authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode)
                    .build();
            authorizationCode_Async();
            response.redirect(siteUrl + LOGGED_IN_LOCATION);
            //response.redirect("https://www.google.com/");
            return null;
        });

        Spark.get(LOGGED_IN_LOCATION, (request, response) -> {
            String errorCode = request.params("error");
            if (errorCode != null) {
                System.out.println(errorCode);
                response.body("<pre>Error" + errorCode + "</pre>");
                return null;
            }
            return null;
        });



        //this.authorizationCodeUri = authorizationCodeUri_Async();
    }


}
