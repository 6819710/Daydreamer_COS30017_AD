package com.jpgalovic.daydream;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

    // Data Flags
    public static boolean flag_textures_loaded;
    public static boolean flag_meshes_loaded;

    // Async References.
    private static LoadMeshes loadMeshes;
    public static int loadIndex;

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
        flag_textures_loaded = false;
        flag_meshes_loaded = false;
        String[] filePaths;
        loadIndex = 0;

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

    public static void loadAssets(Context context, int positionAttribute, int uvAttribute) {
        // Load Textures.
        loadTextures(context);

        loadMeshes = new LoadMeshes(context, positionAttribute, uvAttribute);
        loadMeshes.execute();
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
            flag_textures_loaded = true;
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
            // Total Meshes = 44, threshold = 4
            if(values[0] <= 4) {
                loadIndex = 0;
            } else if(values[0] <= 8) {
                loadIndex = 1;
            } else if(values[0] <= 12) {
                loadIndex = 2;
            } else if(values[0] <= 16) {
                loadIndex = 3;
            } else if(values[0] <= 20) {
                loadIndex = 4;
            } else if(values[0] <= 24) {
                loadIndex = 5;
            } else if(values[0] <= 28) {
                loadIndex = 6;
            } else if(values[0] <= 32) {
                loadIndex = 7;
            } else if(values[0] <= 36) {
                loadIndex = 8;
            } else if(values[0] <= 40) {
                loadIndex = 9;
            } else{
                loadIndex = 10;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Mesh> arrayLists) {
            super.onPostExecute(arrayLists);

            flag_meshes_loaded = true;
            Log.i(TAG, "Meshes Loaded.");

            meshes = arrayLists;
        }
    }
}
