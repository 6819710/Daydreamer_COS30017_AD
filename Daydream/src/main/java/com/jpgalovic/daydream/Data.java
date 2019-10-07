package com.jpgalovic.daydream;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jpgalovic.daydream.model.object.Texture;

import java.io.IOException;
import java.util.ArrayList;

public class Data {
    public static final String TAG = "DATA";

    // Data elements
    public static ArrayList<Texture> alphaNumericTextures;

    // Data Flags
    public static boolean flag_textures_loaded;

    // Async References.
    public static LoadTextures loadTextures;

    /**
     * Runs initialisation process.
     */
    public static void initialise(Context context) {
        flag_textures_loaded = false;

        loadTextures = new LoadTextures();

        loadTextures.execute(context);
    }

    public static class LoadTextures extends AsyncTask<Context, Integer, ArrayList<ArrayList<Texture>>> {
        @Override
        protected ArrayList<ArrayList<Texture>> doInBackground(Context... contexts) {
            String[] filePaths = contexts[0].getResources().getStringArray(R.array.obj_char_tex);
            ArrayList<ArrayList<Texture>> result = new ArrayList<>();
            int count = 0;

            // Load AlphaNumericTextures.
            ArrayList<Texture> textures = new ArrayList<>();
            String[] texturePaths = contexts[0].getResources().getStringArray(R.array.obj_char_tex);

            for(String path : filePaths) {
                try {
                    textures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(textures);

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<Texture>> arrayLists) {
            super.onPostExecute(arrayLists);

            Log.i(TAG, "Textures Loaded.");
            alphaNumericTextures = arrayLists.get(0);

            flag_textures_loaded = true;
        }
    }
}
