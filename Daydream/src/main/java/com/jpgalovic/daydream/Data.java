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
    public static ArrayList<Texture> selectorTextures;
    public static ArrayList<Texture> alphaNumericTextures;

    public static ArrayList<Texture> blockTextures;
    public static ArrayList<Texture> sevenSegmentTimerTextures;

    public static ArrayList<Texture> findTheBlockLabelTextures;
    public static ArrayList<Texture> highScoresLabelTextures;
    public static ArrayList<Texture> tableTextures;
    public static ArrayList<Texture> crtMonitorTextures;



    public static ArrayList<Mesh> selectorMeshes;
    public static ArrayList<Mesh> alphaNumericMeshes;

    public static ArrayList<Mesh> blockMeshes;
    public static ArrayList<Mesh> sevenSegmentTimerMeshes;

    public static ArrayList<Mesh> findTheBlockLabelMeshes;
    public static ArrayList<Mesh> highScoresLabelMeshes;
    public static ArrayList<Mesh> tableMeshes;
    public static ArrayList<Mesh> crtMonitorMeshes;

    // Data Flags
    public static boolean flag_textures_loaded;
    public static boolean flag_meshes_loaded;

    // Async References.
    private static LoadMeshes loadMeshes;

    /**
     * Runs initialisation process.
     */
    public static void initialise(Context context, int positionAttrib, int uvAttrib) {
        flag_textures_loaded = false;
        flag_meshes_loaded = false;

        loadTextures(context);

        loadMeshes = new LoadMeshes(context, positionAttrib, uvAttrib);
        loadMeshes.execute();
    }

    private static void loadTextures(Context context) {
        String[] filePaths;

        // Load Selector Textures.
        selectorTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_selector_tex);
        Log.i(TAG, "Loading AlphaNumeric Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                selectorTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load AlphaNumeric Textures.
        alphaNumericTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_char_tex);
        Log.i(TAG, "Loading AlphaNumeric Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                alphaNumericTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load Block Textures.
        blockTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_block_tex);
        Log.i(TAG, "Loading Block Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                blockTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load Seven Segment Display Textures.
        sevenSegmentTimerTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_seven_segment_tex);
        Log.i(TAG, "Loading Seven Segment Display Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                sevenSegmentTimerTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load Find The Block Label Textures.
        findTheBlockLabelTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_find_the_block_tex);
        Log.i(TAG, "Loading Find The Block Label Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                findTheBlockLabelTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load High Scores Label Textures.
        highScoresLabelTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_high_scores_tex);
        Log.i(TAG, "Loading High Scores Label Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                highScoresLabelTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load Table Textures.
        tableTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_table_tex);
        Log.i(TAG, "Loading Table Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                tableTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        // Load CRT Monitor Textures.
        crtMonitorTextures = new ArrayList<>();
        filePaths = context.getResources().getStringArray(R.array.obj_crt_monitor_tex);
        Log.i(TAG, "Loading CRT Monitor Textures.");

        for(int i = 0; i < filePaths.length; i++) {
            try {
                crtMonitorTextures.add(new Texture(context, filePaths[i]));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        flag_textures_loaded = true;
    }

    //TODO: Work out how to get Async Loading of Textures.
    private static class LoadTextures extends AsyncTask<Void, Integer, ArrayList<ArrayList<Texture>>> {

        private Context context;
        private int positionAttribute;
        private int uvAttribute;

        LoadTextures(Context context, int positionAttrib, int uvAttrib) {
            this.context = context;
            this.positionAttribute = positionAttrib;
            this.uvAttribute = uvAttrib;
        }

        @Override
        protected ArrayList<ArrayList<Texture>> doInBackground(Void... voids) {
            ArrayList<ArrayList<Texture>> result = new ArrayList<>();
            String[] filePaths;
            int count = 0;

            // Load Selector Textures.
            selectorTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_selector_tex);
            Log.i(TAG, "Loading AlphaNumeric Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    selectorTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(selectorTextures);

            // Load AlphaNumeric Textures.
            ArrayList<Texture> alphaNumericTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_char_tex);
            Log.i(TAG, "Loading AlphaNumeric Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    alphaNumericTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(alphaNumericTextures);

            // Load Block Textures.
            ArrayList<Texture> blockTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_block_tex);
            Log.i(TAG, "Loading Block Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    blockTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(blockTextures);

            // Load Seven Segment Display Textures.
            ArrayList<Texture> sevenSegmentDisplayTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_seven_segment_tex);
            Log.i(TAG, "Loading Seven Segment Display Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    sevenSegmentDisplayTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(sevenSegmentDisplayTextures);

            // Load Find The Block Label Textures.
            ArrayList<Texture> findTheblockLabelTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_find_the_block_tex);
            Log.i(TAG, "Loading Find The Block Label Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    findTheblockLabelTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(findTheblockLabelTextures);

            // Load High Scores Label Textures.
            ArrayList<Texture> highScoresLabelTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_high_scores_tex);
            Log.i(TAG, "Loading High Scores Label Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    highScoresLabelTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(highScoresLabelTextures);

            // Load Table Textures.
            ArrayList<Texture> tableTextures = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_table_tex);
            Log.i(TAG, "Loading Table Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    tableTextures.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(tableTextures);

            // Load CRT Monitor Textures.
            ArrayList<Texture> crtMonitorTextrues = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_crt_monitor_tex);
            Log.i(TAG, "Loading CRT Monitor Textures.");

            for(int i = 0; i < filePaths.length; i++) {
                try {
                    crtMonitorTextrues.add(new Texture(context, filePaths[i]));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(crtMonitorTextrues);

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
            selectorTextures = arrayLists.get(0);
            alphaNumericTextures = arrayLists.get(1);
            blockTextures = arrayLists.get(2);
            sevenSegmentTimerTextures = arrayLists.get(3);
            findTheBlockLabelTextures = arrayLists.get(4);
            highScoresLabelTextures = arrayLists.get(5);
            tableTextures = arrayLists.get(6);
            crtMonitorTextures = arrayLists.get(7);

            flag_textures_loaded = true;
        }
    }

    private static class LoadMeshes extends AsyncTask<Void, Integer, ArrayList<ArrayList<Mesh>>> {

        private Context context;
        private int positionAttribute;
        private int uvAttribute;

        LoadMeshes(Context context, int positionAttrib, int uvAttrib) {
            this.context = context;
            this.positionAttribute = positionAttrib;
            this.uvAttribute = uvAttrib;
        }

        @Override
        protected ArrayList<ArrayList<Mesh>> doInBackground(Void... voids) {
            ArrayList<ArrayList<Mesh>> result = new ArrayList<>();
            String[] filePaths;
            int count = 0;

            // Load Selector Meshes.
            ArrayList<Mesh> selectorMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_selector_obj);
            Log.i(TAG, "Loading Selector Meshes.");

            for(String path : filePaths) {
                try {
                    selectorMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(selectorMeshes);

            // Load AlphaNumeric Meshes.
            ArrayList<Mesh> alphaNumericMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_char_obj);
            Log.i(TAG, "Loading AlphaNumeric Meshes.");

            for(String path : filePaths) {
                try {
                    alphaNumericMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(alphaNumericMeshes);

            // Load Block Meshes.
            ArrayList<Mesh> blcokMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_block_obj);
            Log.i(TAG, "Loading Block Meshes.");

            for(String path : filePaths) {
                try {
                    blcokMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(blcokMeshes);

            // Load Seven Segment Display Meshes.
            ArrayList<Mesh> sevenSegmentDisplayMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_seven_segment_obj);
            Log.i(TAG, "Loading Seven Segment Display Meshes.");

            for(String path : filePaths) {
                try {
                    sevenSegmentDisplayMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(sevenSegmentDisplayMeshes);

            // Load Find The Block Label Meshes.
            ArrayList<Mesh> findTheBlockLabelMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_find_the_block_obj);
            Log.i(TAG, "Loading Find The Block Label Meshes.");

            for(String path : filePaths) {
                try {
                    findTheBlockLabelMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(findTheBlockLabelMeshes);

            // Load High Scores Label Meshes.
            ArrayList<Mesh> highScoresLabelMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_high_scores);
            Log.i(TAG, "Loading High Scores Meshes.");

            for(String path : filePaths) {
                try {
                    highScoresLabelMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(highScoresLabelMeshes);

            // Load Table Meshes.
            ArrayList<Mesh> tableMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_table_obj);
            Log.i(TAG, "Loading Table Meshes.");

            for(String path : filePaths) {
                try {
                    tableMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(tableMeshes);

            // Load CRT Monitor Meshes.
            ArrayList<Mesh> crtMonitorMeshes = new ArrayList<>();
            filePaths = context.getResources().getStringArray(R.array.obj_crt_moniter_obj);
            Log.i(TAG, "Loading CRT Monitor Meshes.");

            for(String path : filePaths) {
                try {
                    crtMonitorMeshes.add(new Mesh(context, path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(crtMonitorMeshes);

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<Mesh>> arrayLists) {
            super.onPostExecute(arrayLists);

            Log.i(TAG, "Meshes Loaded.");
            selectorMeshes = arrayLists.get(0);
            alphaNumericMeshes = arrayLists.get(1);
            blockMeshes = arrayLists.get(2);
            sevenSegmentTimerMeshes = arrayLists.get(3);
            findTheBlockLabelMeshes = arrayLists.get(4);
            highScoresLabelMeshes = arrayLists.get(5);
            tableMeshes = arrayLists.get(6);
            crtMonitorMeshes = arrayLists.get(7);

            flag_meshes_loaded = true;
        }
    }
}
