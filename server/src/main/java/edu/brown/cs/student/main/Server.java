package edu.brown.cs.student.main;

import com.google.gson.Gson;
import spark.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class Server {
    private final String siteUrl;
    private final short port;
    private final String clientId;
    private final String clientSecret;
    private final String staticFiles;

    private static final String REQUESTED_SCOPES = "user-read-private user-read-email";
    private static final String SPOTIFY_AUTHENTICATION_URL = "https://accounts.spotify.com/authorize?";
    private static final URI SPOTIFY_GET_API_TOKEN = URI.create("https://accounts.spotify.com/api/token");
    private static final URI SPOTIFY_GET_PROFILE = URI.create("https://api.spotify.com/v1/me");
    private static final String CALLBACK_LOCATION = "/callback"; // must change in spotify dashboard
    private static final String LOGGED_IN_LOCATION = "/authorized";


    Server(String siteUrl, short port, String clientId, String clientSecret, String staticFiles) {
        this.siteUrl = siteUrl;
        this.port = port;
        this.clientId =  clientId;
        this.clientSecret = clientSecret;
        this.staticFiles = staticFiles;
    }

    public void start() {
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

        Spark.get("/login", (request, response) -> {
            Map<String, String> params = Map.of(
                    "response_type", "code",
                    "client_id", clientId,
                    "scope", REQUESTED_SCOPES,
                    "state", randomString(20),
                    "redirect_uri", siteUrl + CALLBACK_LOCATION);

            String uri = SPOTIFY_AUTHENTICATION_URL + encodeQueryParameters(params);
            response.redirect(uri);
            return null;
        });

        Spark.get(CALLBACK_LOCATION, (request, response) -> {
            String errorCode = request.params("error");
            if (errorCode != null) {
                System.out.println(errorCode);
                response.body("<pre>Error" + errorCode + "</pre>");
                return null;
            }
            //String state = request.params("state");
            String authorizationCode = request.params("code");

            String authorization = Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret)
                            .getBytes(StandardCharsets.UTF_8));

            String body = encodeQueryParameters(Map.of(
                    "grant_type", "authorization_code",
                    "code", authorizationCode,
                    "redirect_uri", siteUrl + CALLBACK_LOCATION));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(SPOTIFY_GET_API_TOKEN)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Authorization", "Basic " + authorization)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            client
                    .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(b -> new Gson().fromJson(b, RequestAccessTokenResponse.class))
                    .thenAccept(b -> {
                        HttpRequest getProfile = HttpRequest.newBuilder()
                                .GET()
                                .uri(SPOTIFY_GET_PROFILE)
                                .header("Authorization", "Bearer " + b.access_token)
                                .header("Content-Type", "application/json")
                                .build();

                        System.out.println("here");

                        client.sendAsync(getProfile, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenApply(a -> new Gson().fromJson(a, RequestProfileResponse.class))
                                .thenAccept(a -> {
                                    System.out.println(a.id + " " + a.display_name + " " + b.access_token + " " + b.refresh_token);
                                    response.redirect(siteUrl + LOGGED_IN_LOCATION);
                                });
                    });

            return null;
        });

    }

    private record RequestAccessTokenResponse(String access_token, String token_type, String scope, int expires_in, String refresh_token) {
    }
    private record RequestProfileResponse(String country, String display_name, String email, String id, String uri) {
    }

    private static String randomString(int len) {
        final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder ret = new StringBuilder(len);
        for (int i=0; i<len; i++) {
            int chosen = (int)(Math.random()*CHAR_SET.length());
            ret.append(CHAR_SET.charAt(chosen));
        }
        return ret.toString();
    }



    String encodeQueryParameters(Map<String, String> parameters) {
        String encoded = parameters.keySet().stream()
                .map(key -> {
                    String keyEncoded = URLEncoder.encode(key, StandardCharsets.UTF_8);
                    String valueEncoded = URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8);
                    return keyEncoded + "=" + valueEncoded;
                })
                .collect(Collectors.joining("&"));

        System.out.println(encoded);

        return encoded;
    }
}
