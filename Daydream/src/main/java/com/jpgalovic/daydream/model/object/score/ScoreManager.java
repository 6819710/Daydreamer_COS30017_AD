package com.jpgalovic.daydream.model.object.score;

import android.content.Context;
import android.util.Log;

import com.jpgalovic.daydream.model.util.FileManager;

import java.io.IOException;

public class ScoreManager {
    private static final String TAG = "SCORE_MANAGER";

    private Context context;
    private String fileName;

    private Score[] scores;

    /**
     * Initialises score manager.
     * @param   context         Application Context.
     * @param   fileName        File Name.
     */
    public ScoreManager(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        scores = new Score[12];
        loadScores();
    }

    /**
     * Loads scores from file
     * @return                  True if successfully loaded.
     */
    private boolean loadScores() {
        try {
            scores = FileManager.getScores(context, fileName);
            sort();
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            // TODO: Handle failure to load scores.
            return false;
        }
    }

    /**
     * Checks if given score qualifies for the score list (note: only 12 scores can be kept)
     * @param   score           Score to check.
     * @return                  True if score qualifies for the score list.
     */
    public boolean isValidScore(int score) {
        return score > scores[0].getScore();
    }

    public boolean setNewScore(String name, int score) {
        if(isValidScore(score)) {
            scores[0] = new Score(name, score);
            sort();
            return saveScore();
        } else {
            return false;
        }
    }

    /**
     * Overrides scores in files.
     * @return                  True if successfully saved.
     */
    private  boolean saveScore() {
        try {
            FileManager.saveScores(context, fileName, scores);
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            // TODO: Handle failure to save scores.
            return false;
        }
    }

    public Score getScore(int index) throws RuntimeException {
        if(index < 0 || index >= 11) {
            throw new RuntimeException("Index out of bounds.");
        } else {
            return scores[index];
        }
    }

    /**
     * Starts quick sort process.
     */
    private void sort() {
        sort(0, 11);
    }

    /**
     * Sorts list using quick sort method.
     * @param   low         low index.
     * @param   high        high index.
     */
    private void sort(int low, int high) {
        if (low < high) {
            int p = partition(low, high);
            sort(low, p - 1);
            sort(p + 1, high);
        }
    }

    /**
     * creates pivot point by setting value at high into correct position.
     * @param   low         index of low.
     * @param   high        index of high.
     * @return              index of pivot.
     */
    private int partition(int low, int high) {
        int pivot = scores[high].getScore();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if(scores[j].getScore() < pivot) {
                i++;
                swap(i, j);
            }
        }

        swap(i + 1, high);
        return i + 1;
    }

    /**
     * Swaps elements at indexes a and b.
     * @param   a           index a
     * @param   b           index b
     */
    private void swap(int a, int b) {
        Score temp = scores[a];
        scores[a] = scores[b];
        scores[b] = temp;
    }
}
