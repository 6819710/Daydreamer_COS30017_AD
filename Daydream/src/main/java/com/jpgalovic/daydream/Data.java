package com.jpgalovic.daydream;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.vr.sdk.audio.GvrAudioEngine;
import com.jpgalovic.daydream.model.object.Mesh;
import com.jpgalovic.daydream.model.object.Texture;

import java.io.IOException;
import java.util.ArrayList;

public class Data {
    public static final String TAG = "DATA";

    // Data elements
    public static ArrayList<Texture> textures;
    public static ArrayList<Mesh> meshes;

    public static ArrayList<Texture> loading_textures;
    public static ArrayList<Mesh> loading_meshes;

    public static GvrAudioEngine audio_engine;

    // Data Flags
    public static boolean FLAG_TEXTURES_LOADED;
    public static boolean FLAG_MESHES_LOADED;
    public static boolean FLAG_AUDIO_LOADED;

    // Async References.
    private static LoadMeshes loadMeshes;
    private static LoadAudio loadAudio;

    public static int loadMeshesProgress;
    public static int loadAudioFilesProgress;

    public static Texture[] getTextures(Context context, int id) {
        int[] data = context.getResources().getIntArray(id);
        int size = (data[2] - data[1]) + 1;

        ArrayList<Texture> texResult = new ArrayList<>();

        for(int i = data[1]; i <= data[2]; i++) {
            texResult.add(textures.get(i));
        }

        Texture[] result = new Texture[size];
        
        texResult.toArray(result);

        return result;
    }

    public static Mesh getMesh(Context context, int id) {
        int[] data = context.getResources().getIntArray(id);

        return meshes.get(data[0]);
    }

    /**
     * Runs initialisation process.
     */
    public static void initialise(Context context, int positionAttribute, int uvAttribute) {
        FLAG_TEXTURES_LOADED = false;
        FLAG_MESHES_LOADED = false;
        FLAG_AUDIO_LOADED = false;

        String[] filePaths;

        loadMeshesProgress = 0;
        loadAudioFilesProgress = 0;

        // Pre-Load Textures.
        loading_textures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.OBJ_LOADING_TEXTURE_FILES);
        Log.i(TAG, "Pre-Loading Textures.");

        try {
            for(int i = 0; i < filePaths.length; i++) {
                loading_textures.add(new Texture(context, filePaths[i]));
            }
            Log.i(TAG, "Pre-Loaded Textures Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        // Pre-load Meshes
        loading_meshes = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.OBJ_LOADING_MESH_FILES);
        Log.i(TAG, "Pre-Loading Meshes.");

        try {
            for(String path : filePaths) {
                loading_meshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
            }
            Log.i(TAG, "Pre-Loaded Textures Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void loadAssets(final Context context, int positionAttribute, int uvAttribute) {
        // Load Textures.
        loadTextures(context);

        loadMeshes = new LoadMeshes(context, positionAttribute, uvAttribute);
        loadMeshes.execute();

        loadAudio = new LoadAudio(context);
        loadAudio.execute();
    }

    private static void loadTextures(Context context) {
        String[] filePaths;

        // Load Selector Textures.
        textures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.OBJ_TEXTURE_FILES);
        Log.i(TAG, "Loading Textures.");

        try {
            for(int i = 0; i < filePaths.length; i++) {
                textures.add(new Texture(context, filePaths[i]));
            }
            FLAG_TEXTURES_LOADED = true;
            Log.i(TAG, "Textures Loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static class LoadMeshes extends AsyncTask<Void, Integer, ArrayList<Mesh>> {

        private Context context;
        private int positionAttribute;
        private int uvAttribute;

        LoadMeshes(Context context, int positionAttrib, int uvAttrib) {
            this.context = context;
            this.positionAttribute = positionAttrib;
            this.uvAttribute = uvAttrib;
        }

        @Override
        protected ArrayList<Mesh> doInBackground(Void... voids) {
            ArrayList<Mesh> result = new ArrayList<>();
            String[] filePaths;
            int count = 0;

            // Load Meshes
            filePaths = context.getResources().getStringArray(R.array.OBJ_MESH_FILES);
            Log.i(TAG, "Loading Meshes.");

            for(String path : filePaths) {
                try {
                    Log.i(TAG, "Loading Mesh File: " + path);
                    result.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    count++;
                    publishProgress(new Integer[]{count});
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loadMeshesProgress = values[0];
        }

        @Override
        protected void onPostExecute(ArrayList<Mesh> arrayLists) {
            super.onPostExecute(arrayLists);

            FLAG_MESHES_LOADED = true;
            Log.i(TAG, "Meshes Loaded.");

            meshes = arrayLists;
        }
    }

    private static class LoadAudio extends AsyncTask<Void, Integer, GvrAudioEngine> {

        Context context;

        LoadAudio(Context context) {
            this.context = context;
        }

        @Override
        protected GvrAudioEngine doInBackground(Void... voids) {
            GvrAudioEngine result = new GvrAudioEngine(context, GvrAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
            int count = 0;

            Log.i(TAG, "Loading Audio Files.");

            String[] adoFiles = context.getResources().getStringArray(R.array.ADO_FILES);
            for(String file : adoFiles) {
                Log.i(TAG, "Loading Audio File: " + file);
                result.preloadSoundFile(file);
                count++;
                publishProgress(new Integer[]{count});
            }

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loadAudioFilesProgress = values[0];
        }

        @Override
        protected void onPostExecute(GvrAudioEngine gvrAudioEngine) {
            super.onPostExecute(gvrAudioEngine);

            FLAG_AUDIO_LOADED = true;
            Log.i(TAG, "Audio Files Loaded.");

            audio_engine = gvrAudioEngine;
        }
    }
}
