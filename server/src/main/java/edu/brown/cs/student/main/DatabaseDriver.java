package edu.brown.cs.student.main;

import se.michaelthelin.spotify.model_objects.specification.User;

import java.sql.*;
import java.time.Instant;
import java.util.*;

public class DatabaseDriver {
    private final Connection connection;

    /**
     * Creates a connection to the database and initializes tables if there wasn't already database.
     * @param filename path to the file to be created or used
     * @throws ClassNotFoundException when we don't have sql installed
     * @throws SQLException coding error
     */
    DatabaseDriver(String filename) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + filename;
        connection = DriverManager.getConnection(urlToDB);

        connection.prepareStatement("create table if not exists credentials ( id text primary key, accessToken text, refreshToken text, expires timestamp, foreign key(id) references users(id) );").executeUpdate();
        connection.prepareStatement("create table if not exists users ( id text primary key, displayName text, imageUrl text, followerCount text );").executeUpdate();
        connection.prepareStatement("create table if not exists sessionTokens ( sessionToken text primary key, id text, foreign key(id) references users(id) );").executeUpdate();
        connection.prepareStatement("create table if not exists recommendation ( id text, dataName text, itemName text, similarity boolean, weight integer, foreign key(id) references users(id));").executeUpdate();
        connection.prepareStatement("create table if not exists suggestions ( id text, songId text, suggestion text, foreign key(id) references users(id));").executeUpdate();
    }

    /**
     * Empties the sql tables for testing purposes.
     * @throws SQLException when the update fails
     */
    void clearTables() throws SQLException {
        connection.prepareStatement("delete from credentials;").executeUpdate();
        connection.prepareStatement("delete from users;").executeUpdate();
        connection.prepareStatement("delete from sessionTokens;").executeUpdate();
        connection.prepareStatement("delete from recommendation;").executeUpdate();
        connection.prepareStatement("delete from suggestions;").executeUpdate();
    }

    /**
     * Add suggestions for a particular song.
     * @param userId user whos making the suggestion
     * @param forSongId the song thats being listened to
     * @param suggestion what were suggestion
     * @param max how many suggestions can be made for one song
     * @throws SQLException sql failed
     * @throws IllegalArgumentException when we make too many suggestions
     */
    void insertSuggestion(
            String userId,
            String forSongId,
            String suggestion,
            int max) throws SQLException, IllegalArgumentException
    {
        // check if valid
        PreparedStatement find = connection.prepareStatement("select count(*) from suggestions where id = ? and songId = ?");
        find.setString(1, userId);
        find.setString(2, forSongId);
        ResultSet results = find.executeQuery();
        results.next();
        int result = results.getInt(1);
        if (result >= max) {
            throw new IllegalArgumentException("too many inserted: " + result);
        }
        PreparedStatement addNew = connection.prepareStatement("insert into suggestions values ( ?, ?, ? );");
        addNew.setString(1, userId);
        addNew.setString(2, forSongId);
        addNew.setString(3, suggestion);
        addNew.executeUpdate();
    }

    /**
     * Remove a particular suggestion for a song.
     * @param userId the user whos making the suggestions
     * @param forSongId being listened to
     * @param suggestion what to delete
     * @throws SQLException when sql fails
     */
    void deleteSuggestion(String userId, String forSongId, String suggestion) throws SQLException {
        PreparedStatement delete = connection.prepareStatement("delete from suggestions where id = ? and songId = ? and suggestion = ?;");
        delete.setString(1, userId);
        delete.setString(2, forSongId);
        delete.setString(3, suggestion);
        delete.executeUpdate();
    }

    /**
     * Gets all the suggestions user made.
     * @param userId the user whos making the suggestions
     * @param forSongId being listened to
     * @throws SQLException when sql fails
     */
    List<String> getUsersSuggestionsFor(String userId, String forSongId) throws SQLException {
        PreparedStatement find = connection.prepareStatement("select suggestion from suggestions where id = ? and songId = ?");
        find.setString(1, userId);
        find.setString(2, forSongId);
        ResultSet results = find.executeQuery();
        List<String> ret = new ArrayList<>();
        while (results.next()) {
            ret.add(results.getString(1));
        }
        return ret;
    }

    /**
     * Gets all the most suggested to songs from.
     * @param songId being listened to
     * @param toAccept how many suggestions should be returned
     * @throws SQLException when sql fails
     */
    List<String> topSuggestionsFor(String songId, int toAccept) throws SQLException {
        PreparedStatement getMaxs = connection.prepareStatement("select suggestion from suggestions where songId = ? group by suggestion order by count(suggestion) limit 3;");
        getMaxs.setString(1, songId);
        ResultSet results = getMaxs.executeQuery();
        List<String> ret = new ArrayList<>();
        while (results.next()) {
            ret.add(results.getString(1));
        }
        return ret;
    }

    /**
     * Stores a users recommendation preferences.
     * @param userId current user
     * @param data are their preferences
     * @throws SQLException when sql fails
     */
    void initializeRecommendations(
            String userId, Map<String, Server.MatchData> data) throws SQLException {
        // let's delete everything in the recommendations table that matches userId
        PreparedStatement statement = connection.prepareStatement("delete from recommendation where id = ?;");
        statement.setString(1, userId);
        statement.executeUpdate();

        for (Map.Entry<String, Server.MatchData> datas : data.entrySet()) {
            Server.MatchData value = datas.getValue();
            for (String item : value.items()) {
                PreparedStatement addItem = connection.prepareStatement("insert into recommendation values ( ?, ?, ?, ?, ?)");
                addItem.setString(1, userId);
                addItem.setString(2, datas.getKey());
                addItem.setString(3, item);
                addItem.setBoolean(4, value.matchSame());
                addItem.setInt(5, value.weight());
                addItem.executeUpdate();
            }
        }
    }

    /**
     * Gets the recommendations in the table.
     * @throws SQLException when sql fails
     */
    Map<String, Map<String, Server.MatchData>> getRecommendations() throws SQLException {
        Map<String, Map<String, Server.MatchData>> ret = new HashMap<>();

        PreparedStatement statement = connection.prepareStatement("select id, dataName, itemName, similarity, weight from recommendation;");
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            String userId = result.getString(1);
            String dataName = result.getString(2);
            String itemName = result.getString(3);
            boolean matchSame = result.getBoolean(4);
            int weight = result.getInt(5);
            if (!ret.containsKey(userId)) {
                ret.put(userId, new HashMap<>());
            }
            Map<String, Server.MatchData> user = ret.get(userId);

            if (!user.containsKey(dataName)) {
                user.put(dataName, new Server.MatchData(matchSame, new ArrayList<>(), weight));
            }
            Server.MatchData data = user.get(dataName);

            data.items().add(itemName);
        }

        return ret;
    }

    /**
     * Put basic user data in the database.
     * @param sessionToken what we generated
     * @param tokens  what spotify gives us
     * @param user user class representing spotify user
     * @throws SQLException when sql fails
     */
    void initializeUser(String sessionToken, Server.Tokens tokens, User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("replace into credentials values ( ?, ?, ?, ? );");
        statement.setString(1, user.getId());
        statement.setString(2, tokens.accessToken());
        statement.setString(3, tokens.refreshToken());
        statement.setTimestamp(4, Timestamp.from(tokens.expires()));
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

    /**
     * Gets the tokens associated with a particular user.
     * @param id user id from spotify
     * @throws SQLException when sql fails
     */
    Optional<Server.Tokens> getTokens(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select accessToken, refreshToken, expires from credentials where id = ?;");
        statement.setString(1, id);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            return Optional.empty();
        }
        Instant expires = result.getTimestamp(3).toInstant();
        return Optional.of(
                new Server.Tokens(result.getString(1), result.getString(2), expires));
    }

    /**
     * Gets the user id associated with a particular session token.
     * @param sessionToken token from spotify
     * @throws SQLException when sql fails
     */
    Optional<String> userIdFromSessionToken(String sessionToken) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select id from sessionTokens where sessionToken = ?;");
        statement.setString(1, sessionToken);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            return Optional.empty();
        }
        return Optional.of(result.getString(1));
    }
}
