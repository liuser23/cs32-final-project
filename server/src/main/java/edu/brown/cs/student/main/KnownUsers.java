package edu.brown.cs.student.main;

import com.google.gson.Gson;
import se.michaelthelin.spotify.model_objects.specification.User;

import java.nio.file.Path;
import java.sql.*;
import java.util.Map;
import java.util.Optional;

public class KnownUsers {
    private final Connection connection;

    // create table if not exists users ( id text primary key, accessToken text, refreshToken text, displayName text, imageUrl text, followerCount text );
    // create table if not exists sessionTokens ( primary key sessionToken text, id text, foreign key(id) references users(id) );
    KnownUsers(String filename) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + filename;
        connection = DriverManager.getConnection(urlToDB);

        connection.prepareStatement("create table if not exists credentials ( id text primary key, accessToken text, refreshToken text, foreign key(id) references users(id) ); );").executeUpdate();
        connection.prepareStatement("create table if not exists users ( id text primary key, displayName text, imageUrl text, followerCount text );").executeUpdate();
        connection.prepareStatement("create table if not exists sessionTokens ( sessionToken text primary key, id text, foreign key(id) references users(id) );").executeUpdate();
    }

    void initializeUser(String sessionToken, Tokens tokens, User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("replace into credentials values ( ?, ?, ? );");
        statement.setString(1, user.getId());
        statement.setString(2, tokens.accessToken());
        statement.setString(3, tokens.refreshToken());
        statement.executeUpdate();

        PreparedStatement statement3 = connection.prepareStatement("replace into users values ( ?, ?, ?, ? );");
        statement3.setString(1, user.getId());
        statement3.setString(2, user.getDisplayName());
        String imageUrl = null;
        if (user.getImages().length > 0) {
            imageUrl = user.getImages()[0].getUrl();
        }
        statement3.setString(3, imageUrl);
        statement3.setInt(4, user.getFollowers().getTotal());
        statement3.executeUpdate();

        PreparedStatement statement2 = connection.prepareStatement("insert into sessionTokens values ( ?, ? );");
        statement2.setString(1, sessionToken);
        statement2.setString(2, user.getId());
        statement2.executeUpdate();
    }

    Optional<Tokens> getTokens(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select accessToken, refreshToken from credentials where id = ?;");
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            return Optional.empty();
        }
        return Optional.of(new Tokens(result.getString(1), result.getString(2)));
    }

    Optional<String> userIdFromSessionToken(String sessionToken) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select id from sessionTokens where sessionToken = ?;");
        statement.setString(1, sessionToken);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            return Optional.empty();
        }
        return Optional.of(result.getString(1));
    }

    Optional<String> userDataJson(String id) throws SQLException {
        // TODO: refresh if expired
        PreparedStatement statement = connection.prepareStatement("select displayName, imageUrl, followerCount from users where id = ?;");
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();

        if (!result.next()) {
            return Optional.empty();
        }

        String displayName = Optional.ofNullable(result.getString(1)).orElse("");
        String imageUrl = Optional.ofNullable(result.getString(2)).orElse("");
        int followerCount = result.getInt(3);

        return Optional.of(new Gson().toJson(Map.of(
                "displayName", displayName,
                "imageUrl", imageUrl,
                "followerCount", followerCount)));
    }
}
