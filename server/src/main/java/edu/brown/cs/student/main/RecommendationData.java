package edu.brown.cs.student.main;


import java.util.List;

public record RecommendationData(
        List<Artist> artists,
        List<Song> songs,
        List<Genre> genres
) {
    public record Song(String songId) {}
    public record Artist(String artistId) {}
    public record Genre(String name) {}
}
