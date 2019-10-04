package com.jpgalovic.daydream.model.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager {
    private static final String TAG = "FILE_MANAGER";

    /**
     * Copies assets files to local storage if file does not already exist.
     * @param   context   Application Context.
     */
    public static void copyAssets(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;

        // Get list of files from asset manager.
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
     * @param   in              Input stream.
     * @param   out             Output stream.
     * @throws  IOException     if I/O exception occurs.
     */
    private static void copyFiles(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != 1) {
            out.write(buffer, 0, read);
        }
    }
}
