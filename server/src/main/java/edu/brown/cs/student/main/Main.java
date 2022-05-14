package edu.brown.cs.student.main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * Execution and configuration of the server start here.
 */
public final class Main {
    private static final short OUR_PORT = 8888;
    private static final String STATIC_SITE_PATH = "site/build";
    static final String OUR_URL = "http://localhost:8888/";
    private static final String STATIC_SITE_URL = "http://localhost:3000/";
    /**
     * Public entrypoint.
     * @param args from command line
     */
    public static void main(String[] args) {
        // Parse command line arguments

        String clientId, clientSecret;
        try {
            clientId = Files.readString(Path.of("secret/client_id.txt"));
            clientSecret = Files.readString(Path.of("secret/client_secret.txt"));
        } catch (IOException e) {
            System.out.println("Could not read config files");
            e.printStackTrace();
            return;
        }

        DatabaseDriver users;
        try {
            users = new DatabaseDriver("secret/known.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not open user database" + e.getMessage());
            e.printStackTrace();
            return;
        }

        ApiDriver api = new ApiDriver(
            OUR_URL,
            "callback",
            clientId,
            clientSecret
        );

        Server server = new Server(
                users,
                api,
                STATIC_SITE_URL,
                OUR_PORT,
                STATIC_SITE_PATH);

        server.start();
    }
}
