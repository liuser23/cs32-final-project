package edu.brown.cs.student.main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Filter;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import freemarker.template.Configuration;

import org.json.JSONObject;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {
    private static final short DEFAULT_PORT = 8888;

    private static final String STATIC_SITE_PATH = "site/build";

    private static final String WEBSITE_URL = "http://localhost:" + DEFAULT_PORT;

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

        ServWrapper server = new ServWrapper(users, WEBSITE_URL, DEFAULT_PORT, clientId, clientSecret, STATIC_SITE_PATH);
        server.start();
    }
}
