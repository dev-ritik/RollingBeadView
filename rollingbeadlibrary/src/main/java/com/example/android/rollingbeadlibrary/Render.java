package com.example.android.rollingbeadlibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Render {
    private Activity context;
    ImageView imageView;
    private boolean generateCycle = false;
    Bitmap immutableBitmap;
    Bitmap changedBitmap;
    Bitmap changedReturnedBitmap;
    RollingBead bead1;
    RollingBeadImageView image;

    public Render(Activity context) {
        this.context = context;
    }

    public Render(Activity context, Bitmap immutableBitmap, Bitmap changedBitmap, RollingBead bead1, ImageView imageView) {
        this.context = context;
        this.immutableBitmap = immutableBitmap;
        this.changedBitmap = changedBitmap;
        this.bead1 = bead1;
        this.imageView = imageView;
    }
    public Render(Activity context, Bitmap immutableBitmap, Bitmap changedBitmap, RollingBead bead1,RollingBeadImageView image) {
        this.context = context;
        this.immutableBitmap = immutableBitmap;
        this.changedBitmap = changedBitmap;
        this.bead1 = bead1;
        this.image=image;
    }

    public void timer() {
        Log.i("point ma247", "timer started");

        Timer updateWordTimer = new Timer();
        updateWordTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("point ma255", "run started");
//                        animateInHorizontal(icon);
//                        if (generateCycle) {
//                            changedBitmap = bead1.generateBump(changedBitmap, immutableBitmap, bead1.getUpdatedcenterCircle_X());
//                            generateCycle = false;
//                        } else {
//                            changedBitmap = bead1.dissolveBitmap(changedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X());
//                            generateCycle = true;
//                        }
//                        imageView.setImageBitmap(changedBitmap);
                        ExecuteAsync task = new ExecuteAsync(bead1);
                        task.execute(new String[]{null});
                    }
                });
            }
        }, 5, 160);
    }

    private class ExecuteAsync extends AsyncTask<String, String, String> {
        Bitmap firstBitmap;
        Bitmap secondBitmap;
        long millisUntilFinished;
        RollingBead bead;

        public ExecuteAsync(RollingBead bead) {
            this.bead = bead;
        }

        public ExecuteAsync() {
        }

        public ExecuteAsync(Bitmap firstBitmap) {
            this.firstBitmap = firstBitmap;
        }

        public ExecuteAsync(Bitmap firstBitmap, Bitmap secondBitmap) {
            this.firstBitmap = firstBitmap;
            this.secondBitmap = secondBitmap;
        }

        @Override
        protected String doInBackground(String... urls) {
            if (generateCycle) {
                secondBitmap = bead.generateBump(changedBitmap, immutableBitmap, bead.getUpdatedcenterCircle_X());
                generateCycle = false;
            } else {
                secondBitmap = bead.dissolveBitmap(changedBitmap, immutableBitmap, bead.getPreviouscenterCircle_X());
                generateCycle = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
//            imageView.setImageBitmap(secondBitmap);
            image.invalidate();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            imageView.setImageBitmap(secondBitmap);
//            RollingBeadImageView.invalidate();
            image.invalidate();

        }
    }


}
