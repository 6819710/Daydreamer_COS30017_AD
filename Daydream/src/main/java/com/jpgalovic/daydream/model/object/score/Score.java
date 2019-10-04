package com.jpgalovic.daydream.model.object.score;

public class Score {
    private static final String TAG = "SCORE";

    private String name;
    private int score;

    /**
     * Constructs score.
     * @param   name    name of score holder.
     * @param   score   score.
     */
    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean lessThan(Score other) {
        return this.score < other.score;
    }
}
