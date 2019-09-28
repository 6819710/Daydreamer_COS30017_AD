package com.jpgalovic.daydreamer.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
            Log.i(TAG, "files=>"+fileName);
            try {
                File file = new File(context.getFilesDir().toString()+"/"+fileName);
                if(!file.exists()) {
                    InputStream in;
                    OutputStream out;

                    in = assetManager.open("files/"+fileName);
                    out = new FileOutputStream(context.getFilesDir().toString()+"/"+fileName);
                    copyFiles(in,out);
                    in.close();
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
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
}
