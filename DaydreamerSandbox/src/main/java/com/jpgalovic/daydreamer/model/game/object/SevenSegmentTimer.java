package com.jpgalovic.daydreamer.model.game.object;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jpgalovic.daydreamer.model.TexturedMesh;
import com.jpgalovic.daydreamer.model.TexturedMeshObject;

import java.util.ArrayList;

public class SevenSegmentTimer {
    private TexturedMeshObject unitsCounter;
    private TexturedMeshObject tensCounter;
    private TexturedMeshObject hundredsCounter;

    private int countStart;
    private int currentCount;

    /**
     * Sets up units, tens and hundreds counter, and starting count
     * @param countStart starting count of timer.
     *                   TODO: Refine Offsets, subtle curve?
     */
    public SevenSegmentTimer(Context context, int objectPositionParam, int objectUvParam, float x, float y, float z, int countStart) {
        float offset = 1.9f;

        String[] sevenSegmentTextures = {
                "obj/seven_segment_disp_off.png",
                "obj/seven_segment_disp_1.png",
                "obj/seven_segment_disp_2.png",
                "obj/seven_segment_disp_3.png",
                "obj/seven_segment_disp_4.png",
                "obj/seven_segment_disp_5.png",
                "obj/seven_segment_disp_6.png",
                "obj/seven_segment_disp_7.png",
                "obj/seven_segment_disp_8.png",
                "obj/seven_segment_disp_9.png",
                "obj/seven_segment_disp_0.png"};

        unitsCounter = new TexturedMeshObject(context, "Units 7 Segment", "obj/seven_segment_disp.obj", sevenSegmentTextures, objectPositionParam, objectUvParam, x + offset, y, z);
        tensCounter = new TexturedMeshObject(context, "Tens 7 Segment", "obj/seven_segment_disp.obj", sevenSegmentTextures, objectPositionParam, objectUvParam, x, y, z);
        hundredsCounter = new TexturedMeshObject(context, "Hundreds 7 Segment", "obj/seven_segment_disp.obj", sevenSegmentTextures, objectPositionParam, objectUvParam, x - offset, y, z);

        this.countStart = countStart;
        currentCount = countStart;
    }

    /**
     * Draws 7-Segment displays to display current count.
     * @param perspective
     * @param view
     * @param objectProgram
     * @param objectModelViewProjectionParam
     */
    public void draw(float[] perspective, float[] view, int objectProgram, int objectModelViewProjectionParam) {
        // Create array with current count.
        int number = currentCount;
        ArrayList<Integer> counts = new ArrayList<>();
        while (number > 0) {
            counts.add(number % 10);
            number = number / 10;
        }

        // Render when counter > 1000;
        if (counts.size() > 3) {
            hundredsCounter.draw(perspective, view, 9, objectProgram,objectModelViewProjectionParam);
            tensCounter.draw(perspective, view, 9, objectProgram,objectModelViewProjectionParam);
            unitsCounter.draw(perspective, view, 9, objectProgram,objectModelViewProjectionParam);
        }
        else {
            if(counts.size() == 3) {
                // 100s
                hundredsCounter.draw(perspective, view, counts.get(2), objectProgram,objectModelViewProjectionParam);

                // 10s
                if(counts.get(1) == 0) {
                    tensCounter.draw(perspective, view, 10, objectProgram,objectModelViewProjectionParam);
                } else {
                    tensCounter.draw(perspective, view, counts.get(1), objectProgram,objectModelViewProjectionParam);
                }

                // 1s
                if(counts.get(0) == 0) {
                    unitsCounter.draw(perspective, view, 10, objectProgram,objectModelViewProjectionParam);
                } else {
                    unitsCounter.draw(perspective, view, counts.get(0), objectProgram,objectModelViewProjectionParam);
                }
            } else if(counts.size() == 2) {
                // Count < 100, 100s counter is "off"
                hundredsCounter.draw(perspective, view,0, objectProgram,objectModelViewProjectionParam);

                // 10s
                tensCounter.draw(perspective, view, counts.get(1), objectProgram,objectModelViewProjectionParam);

                // 1s
                if(counts.get(0) == 0) {
                    unitsCounter.draw(perspective, view, 10, objectProgram,objectModelViewProjectionParam);
                } else {
                    unitsCounter.draw(perspective, view, counts.get(0), objectProgram,objectModelViewProjectionParam);
                }
            } else if(counts.size() == 1) {
                hundredsCounter.draw(perspective, view, 0, objectProgram,objectModelViewProjectionParam);
                tensCounter.draw(perspective, view, 0, objectProgram,objectModelViewProjectionParam);
                unitsCounter.draw(perspective, view, counts.get(0), objectProgram,objectModelViewProjectionParam);
            } else {
                hundredsCounter.draw(perspective, view, 0, objectProgram,objectModelViewProjectionParam);
                tensCounter.draw(perspective, view, 0, objectProgram,objectModelViewProjectionParam);
                unitsCounter.draw(perspective, view, 10, objectProgram,objectModelViewProjectionParam);
            }
        }
    }

    public void start() {
        new Timer().execute(countStart);
    }

    class Timer extends AsyncTask<Integer, Integer, Integer> {
        private  String TAG = "Timer";
        @Override
        protected Integer doInBackground(Integer... values) {
            try{
                for(int i = values[0]; i > 0; i--) {
                    Thread.sleep(500);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            currentCount = values[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            currentCount = integer;
        }
    }
}
