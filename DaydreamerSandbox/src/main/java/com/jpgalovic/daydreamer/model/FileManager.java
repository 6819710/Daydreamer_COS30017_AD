package com.jpgalovic.daydreamer.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Manages Files for Score Keeping
 */
public class FileManager {
    private static final String TAG = "File Manager";

    /**
     * Copies asset files to local storage if file does not already exist.
     * @param context Context reference.
     */
    public static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;

        try {
            files = assetManager.list("files");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        for(String fileName : files) {
            try {
                File file = new File(context.getFilesDir().toString() + "/" + fileName);
                if (file.exists()) {
                    Log.i(TAG, "File: " + fileName + " already exists");
                } else {
                    Log.i(TAG, "Copying File: " + fileName);
                    InputStream in;
                    OutputStream out;

                    in = assetManager.open("files/" + fileName);
                    out = new FileOutputStream(context.getFilesDir().toString() + '/' + fileName);
                    copyFiles(in, out);
                    in.close();
                    out.flush();
                    out.close();
                }
            }
            catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Auxiliary File Transfer Function.
     * @param   in          Input stream.
     * @param   out         Output stream.
     * @throws  IOException if I/O exception occurs.
     */
    private static void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != 1) {
            out.write(buffer);
        }
    }

    /**
     * Reads scores from the given scores.csv file.
     * TODO: Change return to high score manager class.
     * @param context Context reference.
     * @param fileName File name of scores.csv file.
     * @return ArrayList of integers containing highscores.
     * @throws IOException if I/O exception occurs.
     */
    public ArrayList<Integer> getScores(Context context, String fileName) throws IOException {
        try {
            ArrayList<Integer> result = new ArrayList<>();
            InputStream in = new FileInputStream(new File(context.getFilesDir().toString()+'/'+fileName));
            CSVReader reader = new CSVReader(new InputStreamReader(in));
            String[] nextLine;

            //read each line from file
            while ((nextLine = reader.readNext()) != null) {
                for(String value : nextLine) {
                    result.add(Integer.parseInt(value));
                }
            }

            return result;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
    }
}
