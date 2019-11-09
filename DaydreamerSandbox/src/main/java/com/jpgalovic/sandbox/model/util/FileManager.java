package com.jpgalovic.sandbox.model.util;

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
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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
            out.write(buffer, 0, read);
        }
    }

    /**
     * Reads scores from the given scores.csv file.
     * TODO: Change return to high score manager class.
     * @param context Context reference.
     * @param fileName File name of scores.csv file.
     * @return Array of scores.
     * @throws IOException if I/O exception occurs.
     */
    public static Score[] getScores(Context context, String fileName) throws IOException {
        try {
            Score[] result = new Score[12];
            InputStream in = new FileInputStream(new File(context.getFilesDir().toString()+'/'+fileName));
            CSVReader reader = new CSVReader(new InputStreamReader(in));
            String[] nextLine;

            //read each line from file
            for(int i = 0; i < 12; i++) {
                if((nextLine = reader.readNext()) != null) {
                    result[i] = new Score(nextLine[0], Integer.parseInt(nextLine[1]));
                }
            }

            return result;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
    }

    /**
     * Overrides file with provided scores.
     * @param context Context reference.
     * @param fileName File name of scores.csv file.
     * @param scores Array containing scores.
     * @throws IOException if I/O exception occurs.
     */
    public static void saveScores(Context context, String fileName, Score[] scores) throws IOException {
        try {
            OutputStream out = new FileOutputStream(new File(context.getFilesDir().toString()+'/'+fileName), false);
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(out));

            //write each score to list.
            List<String[]> lines = new ArrayList<>();
            for(Score score : scores) {
                lines.add(new String[]{score.getName(), Integer.toString(score.getScore())});
            }
            writer.writeAll(lines);
            writer.close();
            out.close();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            throw e;
        }
    }
}
