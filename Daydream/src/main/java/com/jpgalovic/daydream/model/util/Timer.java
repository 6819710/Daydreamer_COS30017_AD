package com.jpgalovic.daydream.model.util;

import android.os.AsyncTask;
import android.util.Log;

public class Timer {
    private static final String TAG = "TIMER";

    private int count;

    private int startCount;
    private int delay;

    private boolean started;

    AsyncTimer timer;

    public Timer() {
        this(0, 0);
    }

    /**
     * Initialises Timer
     * @param   count   number of seconds to set timer initial count.
     */
    public Timer(int count) {
        this(count, 1000);
    }

    /**
     * Initialises Timer with a given delay.
     * @param   count   number of units to set timer initial count.
     * @param   delay   delay in milliseconds
     */
    public Timer(int count, int delay) {
        this.count = 0;
        this.startCount = count;
        this.delay = delay;
        started = false;
        timer = new AsyncTimer();
    }

    /**
     * Starts timer.
     */
    public void start() {
        count = startCount;
        timer.execute(startCount, delay);
        started = true;
    }

    /**
     * gets the current count.
     * @return          current count.
     */
    public int getCount() {
        return count;
    }

    public boolean zero() {
        return count == 0 && started;
    }

    private class AsyncTimer extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... values) {
            try {
                for(int i = values[0]; i > 0; i--) {
                    Thread.sleep(values[1]);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            count = values[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
            count = integer;
        }
    }
}


