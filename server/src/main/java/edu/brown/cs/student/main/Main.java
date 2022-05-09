package edu.brown.cs.student.main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {
    private static final short OUR_PORT = 8888;
    private static final String STATIC_SITE_PATH = "site/build";
    private static final String OUR_URL = "http://localhost:8888/";
    private static final String STATIC_SITE_URL = "http://localhost:3000/";
    /**
     *
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

        KnownUsers users;
        try {
            users = new KnownUsers("secret/known.sqlite3");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not open user database" + e.getMessage());
            e.printStackTrace();
            return;
        }

        Server server = new Server(
                users,
                STATIC_SITE_URL,
                OUR_URL,
                OUR_PORT,
                clientId,
                clientSecret,
                STATIC_SITE_PATH);
        server.start();
    }
}
