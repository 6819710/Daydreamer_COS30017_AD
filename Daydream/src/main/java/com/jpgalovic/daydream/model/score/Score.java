package com.jpgalovic.daydream.model.score;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Score)) {
            return false;
        }

        if(obj == this) {
            return true;
        }

        Score other = (Score) obj;
        return this.name == other.name && this.score == other.score;
    }
}
