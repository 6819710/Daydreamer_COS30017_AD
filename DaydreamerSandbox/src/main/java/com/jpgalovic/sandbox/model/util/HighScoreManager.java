package com.jpgalovic.sandbox.model.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Manager to handle high scores.
 */
public class HighScoreManager {
    private static final String TAG = "High Score Manager";

    private Context context;
    private String fileName;

    private Score[] highScores;

    public HighScoreManager(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
        highScores = new Score[12];
        loadHighScores();
    }

    /**
     * Loads high scores from file.
     * @return true if scores successfully loaded.
     */
    private boolean loadHighScores() {
        try {
            highScores = FileManager.getScores(context, fileName);
            sort();
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            // TODO: Handle error loading high scores
            return false;
        }
    }

    /**
     * Saves high scores to file.
     * @return true if scores successfully saved.
     */
    public boolean saveHighScores() {
        try {
            FileManager.saveScores(context, fileName, highScores);
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            // TODO: Handle error saving high scores;
            return false;
        }
    }

    /**
     * Checks if given score qualifies as high score.
     * @param score players score.
     * @return true if score qualifies as high score.
     */
    public boolean isHighScore(int score) {
        return score > highScores[0].getScore();
    }

    /**
     * Sets new highscore.
     * @param name players name.
     * @param score players score.
     * @return true if score set.
     */
    public boolean setNewHighScore(String name, int score) {
        highScores[0] = new Score(name, score);
        sort();
        return saveHighScores();
    }

    public Score getScore(int index) {
        if(index < 0 || index > 11) {
            throw new RuntimeException("Index out of bounds.");
        }
        return highScores[index];
    }

    /**
     * Utility function to swap values at indexes a & b;
     * @param a index of first value.
     * @param b index of second value.
     */
    private void swap(int a, int b) {
        Score temp = highScores[a];
        highScores[a] = highScores[b];
        highScores[b] = temp;
    }

    /**
     * Starts the sorting process.
     */
    private void sort() {
        sort (0, 11);
    }

    /**
     * Sorts scores using quick sort method.
     * @param low starting index.
     * @param high ending index.
     */
    private void sort(int low, int high) {
        if(low < high) {
            int pi = partition(low, high);
            sort(low, pi - 1);
            sort(pi + 1, high);
        }
    }

    /**
     * Quick sort aux. method. places the pivot element in its correct position.
     * @param low starting index.
     * @param high ending index.
     * @return index of pivot element.
     */
    private int partition(int low, int high) {
        int pivot = highScores[high].getScore();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if(highScores[j].getScore() < pivot) {
                i++;
                swap(i,j);
            }
        }

        swap(i + 1, high);

        return i + 1;
    }
}
