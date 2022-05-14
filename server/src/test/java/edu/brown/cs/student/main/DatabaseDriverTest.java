package edu.brown.cs.student.main;

import edu.brown.cs.student.main.DatabaseDriver;
import edu.brown.cs.student.main.Server;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class DatabaseDriverTest {
    @Test
    public void clearTest() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
        } finally {
            driver.close();
        }
    }

    @Test
    public void emptyRecommendations() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
            Map<String, Map<String, Server.MatchData>> data = driver.getRecommendations();
            assert data.isEmpty();
        } finally {
            driver.close();
        }
    }

    @Test
    public void noSuggestions() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
            List<String> suggestions = driver.getUsersSuggestionsFor("bob", "best song");
            assert suggestions.isEmpty();
        } finally {
            driver.close();
        }
    }

    @Test
    public void singleSuggestion() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");

        try {
            driver.clearTables();
            driver.insertSuggestion("bob", "best song", "second best song", 3);
            List<String> suggestions = driver.getUsersSuggestionsFor("bob", "best song");
            assert suggestions.get(0).equals("second best song");
        } finally {
            driver.close();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void suggestionsOverflow() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
            driver.insertSuggestion("bob", "best song", "second best song", 4);
            driver.insertSuggestion("bob", "best song", "third best song", 4);
            driver.insertSuggestion("bob", "best song", "fourth best song", 4);
            driver.insertSuggestion("bob", "best song", "fifth best song", 4);
            driver.insertSuggestion("bob", "best song", "sixth best song", 4);
            driver.insertSuggestion("bob", "best song", "seventh best song", 4);
        } finally {
            driver.close();
        }
    }

    @Test
    public void coupleSuggestions() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
            driver.insertSuggestion("bob", "best song", "second best song", 4);
            driver.insertSuggestion("bob", "best song", "third best song", 4);
            driver.insertSuggestion("bob", "best song", "fourth best song", 4);
            driver.insertSuggestion("bob", "best song", "fifth best song", 4);

            driver.insertSuggestion("charlie", "best song", "second best song", 4);
            driver.insertSuggestion("charlie", "best song", "third best song", 4);

            driver.insertSuggestion("charlie", "cool rock", "second best song", 4);
            driver.insertSuggestion("charlie", "cool bop song", "third best song", 4);
            driver.insertSuggestion("charlie", "cool trock song", "third best song", 4);

            List<String> suggestions = driver.getUsersSuggestionsFor("charlie", "best song");
            assertEquals(Set.of("second best song", "third best song"), Set.copyOf(suggestions));
        } finally {
            driver.close();
        }
    }

    @Test
    public void deleteSuggestions() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
            driver.insertSuggestion("bob", "best song", "second best song", 4);
            driver.insertSuggestion("bob", "best song", "third best song", 4);
            driver.insertSuggestion("bob", "best song", "fourth best song", 4);
            driver.insertSuggestion("bob", "best song", "fifth best song", 4);

            driver.deleteSuggestion("bob", "best song", "fifth best song");
            driver.deleteSuggestion("bob", "best song", "third best song");

            driver.insertSuggestion("bob", "best song", "sixth best song", 4);
            driver.insertSuggestion("bob", "best song", "seventh best song", 4);
        } finally {
            driver.close();
        }
    }

    @Test
    public void topSuggestions() throws SQLException, ClassNotFoundException {
        DatabaseDriver driver = new DatabaseDriver("../secret/test.sqlite3");
        try {
            driver.clearTables();
            driver.insertSuggestion("bob", "best song", "second best song", 4);
            driver.insertSuggestion("bob", "best song", "third best song", 4);
            driver.insertSuggestion("bob", "best song", "fourth best song", 4);
            driver.insertSuggestion("bob", "best song", "fifth best song", 4);

            driver.insertSuggestion("charlie", "best song", "second best song", 4);
            driver.insertSuggestion("charlie", "best song", "third best song", 4);

            driver.insertSuggestion("charlie", "cool rock", "second best song", 4);
            driver.insertSuggestion("charlie", "cool bop song", "third best song", 4);
            driver.insertSuggestion("charlie", "cool trock song", "third best song", 4);

            List<String> suggestions = driver.topSuggestionsFor("best song", 2);
            assertEquals(Set.of("second best song", "third best song"), Set.copyOf(suggestions));
        } finally {
            driver.close();
        }
    }
}
