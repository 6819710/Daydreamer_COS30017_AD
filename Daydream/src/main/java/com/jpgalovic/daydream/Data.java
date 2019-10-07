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
    public static ArrayList<Texture> alphaNumericTextures;

    public static ArrayList<Texture> blockTextures;
    public static ArrayList<Texture> sevenSegmentTimerTextures;

    public static ArrayList<Texture> findTheBlockLabelTextures;
    public static ArrayList<Texture> highScoresLabelTextures;
    public static ArrayList<Texture> tableTextures;
    public static ArrayList<Texture> crtMonitorTextures;



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
    private static LoadTextures loadTextures;
    private static LoadMeshes loadMeshes;

    private static int positionAttribute;
    private static int uvAttribute;

    /**
     * Runs initialisation process.
     */
    public static void initialise(Context context, int positionAttrib, int uvAttrib) {
        flag_textures_loaded = false;
        flag_meshes_loaded = false;

        positionAttribute = positionAttrib;
        uvAttribute = uvAttrib;

        loadTextures = new LoadTextures();
        loadMeshes = new LoadMeshes();

        loadTextures.execute(context);
        loadMeshes.execute(context);
    }

    private static class LoadTextures extends AsyncTask<Context, Integer, ArrayList<ArrayList<Texture>>> {
        @Override
        protected ArrayList<ArrayList<Texture>> doInBackground(Context... contexts) {
            ArrayList<ArrayList<Texture>> result = new ArrayList<>();
            String[] filePaths;
            int count = 0;

            // Load AlphaNumeric Textures.
            ArrayList<Texture> alphaNumericTextures = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_char_tex);
            Log.i(TAG, "Loading AlphaNumeric Textures.");

            for(String path : filePaths) {
                try {
                    alphaNumericTextures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(alphaNumericTextures);

            // Load Block Textures.
            ArrayList<Texture> blockTextures = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_char_tex);
            Log.i(TAG, "Loading Block Textures.");

            for(String path : filePaths) {
                try {
                    blockTextures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(blockTextures);

            // Load Seven Segment Display Textures.
            ArrayList<Texture> sevenSegmentDisplayTextures = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_seven_segment_tex);
            Log.i(TAG, "Loading Seven Segment Display Textures.");

            for(String path : filePaths) {
                try {
                    sevenSegmentDisplayTextures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(sevenSegmentDisplayTextures);

            // Load Find The Block Label Textures.
            ArrayList<Texture> findTheblockLabelTextures = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_find_the_block_tex);
            Log.i(TAG, "Loading Find The Block Label Textures.");

            for(String path : filePaths) {
                try {
                    findTheblockLabelTextures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(findTheblockLabelTextures);

            // Load High Scores Label Textures.
            ArrayList<Texture> highScoresLabelTextures = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_high_scores_tex);
            Log.i(TAG, "Loading High Scores Label Textures.");

            for(String path : filePaths) {
                try {
                    highScoresLabelTextures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(highScoresLabelTextures);

            // Load Table Textures.
            ArrayList<Texture> tableTextures = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_table_tex);
            Log.i(TAG, "Loading Table Textures.");

            for(String path : filePaths) {
                try {
                    tableTextures.add(new Texture(contexts[0], path));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(tableTextures);

            // Load CRT Monitor Textures.
            ArrayList<Texture> crtMonitorTextrues = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_crt_monitor_tex);
            Log.i(TAG, "Loading CRT Monitor Textures.");

            for(String path : filePaths) {
                try {
                    crtMonitorTextrues.add(new Texture(contexts[0], path));
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
            alphaNumericTextures = arrayLists.get(0);
            blockTextures = arrayLists.get(1);
            sevenSegmentTimerTextures = arrayLists.get(2);
            findTheBlockLabelTextures = arrayLists.get(3);
            highScoresLabelTextures = arrayLists.get(4);
            tableTextures = arrayLists.get(5);
            crtMonitorTextures = arrayLists.get(6);

            flag_textures_loaded = true;
        }
    }

    private static class LoadMeshes extends AsyncTask<Context, Integer, ArrayList<ArrayList<Mesh>>> {
        @Override
        protected ArrayList<ArrayList<Mesh>> doInBackground(Context... contexts) {
            ArrayList<ArrayList<Mesh>> result = new ArrayList<>();
            String[] filePaths;
            int count = 0;

            // Load AlphaNumeric Meshes.
            ArrayList<Mesh> alphaNumericMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_char_obj);
            Log.i(TAG, "Loading AlphaNumeric Meshes.");

            for(String path : filePaths) {
                try {
                    alphaNumericMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(alphaNumericMeshes);

            // Load Block Meshes.
            ArrayList<Mesh> blcokMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_block_obj);
            Log.i(TAG, "Loading Block Meshes.");

            for(String path : filePaths) {
                try {
                    blcokMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(blcokMeshes);

            // Load Seven Segment Display Meshes.
            ArrayList<Mesh> sevenSegmentDisplayMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_seven_segment_obj);
            Log.i(TAG, "Loading Seven Segment Display Meshes.");

            for(String path : filePaths) {
                try {
                    sevenSegmentDisplayMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(sevenSegmentDisplayMeshes);

            // Load Find The Block Label Meshes.
            ArrayList<Mesh> findTheBlockLabelMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_find_the_block_obj);
            Log.i(TAG, "Loading Find The Block Label Meshes.");

            for(String path : filePaths) {
                try {
                    findTheBlockLabelMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(findTheBlockLabelMeshes);

            // Load High Scores Label Meshes.
            ArrayList<Mesh> highScoresLabelMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_high_scores);
            Log.i(TAG, "Loading High Scores Meshes.");

            for(String path : filePaths) {
                try {
                    highScoresLabelMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(highScoresLabelMeshes);

            // Load Table Meshes.
            ArrayList<Mesh> tableMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_table_obj);
            Log.i(TAG, "Loading Table Meshes.");

            for(String path : filePaths) {
                try {
                    tableMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
                    publishProgress(count++);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            result.add(tableMeshes);

            // Load CRT Monitor Meshes.
            ArrayList<Mesh> crtMonitorMeshes = new ArrayList<>();
            filePaths = contexts[0].getResources().getStringArray(R.array.obj_crt_moniter_obj);
            Log.i(TAG, "Loading CRT Monitor Meshes.");

            for(String path : filePaths) {
                try {
                    crtMonitorMeshes.add(new Mesh(contexts[0], path, positionAttribute, uvAttribute));
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

            Log.i(TAG, "Textures Loaded.");
            alphaNumericMeshes = arrayLists.get(0);

            alphaNumericMeshes = arrayLists.get(0);
            blockMeshes = arrayLists.get(1);
            sevenSegmentTimerMeshes = arrayLists.get(2);
            findTheBlockLabelMeshes = arrayLists.get(3);
            highScoresLabelMeshes = arrayLists.get(4);
            tableMeshes = arrayLists.get(5);
            crtMonitorMeshes = arrayLists.get(6);

            flag_meshes_loaded = true;
        }
    }
}
