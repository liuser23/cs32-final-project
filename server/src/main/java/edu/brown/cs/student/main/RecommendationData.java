package edu.brown.cs.student.main;


import java.util.List;

public record RecommendationData(
        List<String> artists,
        List<String> songs,
        List<String> genres
) {}
