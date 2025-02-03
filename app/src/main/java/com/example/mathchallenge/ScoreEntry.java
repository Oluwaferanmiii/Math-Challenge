package com.example.mathchallenge;

public class ScoreEntry implements Comparable<ScoreEntry> {
    public int score;
    public String name;

    public ScoreEntry(int score, String name) {
        this.score = score;
        this.name = name;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return other.score - this.score; // Sort in descending order of scores
    }
}

