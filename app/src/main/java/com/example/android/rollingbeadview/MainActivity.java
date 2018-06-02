package com.example.android.rollingbeadview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.rollingbeadlibrary.Render;
import com.example.android.rollingbeadlibrary.RollingBead;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //    TextView text;
    ImageView imageView;
    private boolean generateCycle = false;
    Bitmap immutableBitmap;
    Bitmap changedBitmap;
    Bitmap changedReturnedBitmap;
    RollingBead bead1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
//        text = (TextView) findViewById(R.id.text);

        immutableBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.five);
        changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        bead1 = new RollingBead(changedBitmap, immutableBitmap, 350,350, 40, 50, 1);

    }

//    private void timer() {
//        Log.i("point ma247", "timer started");
//
//        Timer updateWordTimer = new Timer();
//        updateWordTimer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        Log.i("point ma255", "run started");
////                        animateInHorizontal(icon);
//
////                        if (asd) {
////                            convert(icon, iconStored,getUpdatedCenterCirlce_X(), 400, 90);
////                            asd = false;
////                        } else {
////                            mixBitmap(icon, iconStored, getCenterCirlce_X(), 400, 90);
////                            asd = true;
////                        }
////                        imageView.setImageBitmap(icon);
//
//                        ExecuteAsync task = new ExecuteAsync(bead1);
//                        task.execute(new String[]{null});
//                    }
//                });
//            }
//        }, 5, 160);
//    }

//    private class ExecuteAsync extends AsyncTask<String, String, String> {
//        Bitmap firstBitmap;
//        Bitmap secondBitmap;
//        long millisUntilFinished;
//        RollingBead bead;
//
//        public ExecuteAsync(RollingBead bead) {
//            this.bead = bead;
//        }
//
//        public ExecuteAsync() {
//        }
//
//        public ExecuteAsync(Bitmap firstBitmap) {
//            this.firstBitmap = firstBitmap;
//        }
//
//        public ExecuteAsync(Bitmap firstBitmap, Bitmap secondBitmap) {
//            this.firstBitmap = firstBitmap;
//            this.secondBitmap = secondBitmap;
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            if (generateCycle) {
//                secondBitmap = bead.generateBump(changedBitmap, immutableBitmap, bead.getUpdatedcenterCircle_X());
//                generateCycle = false;
//            } else {
//                secondBitmap = bead.dissolveBitmap(changedBitmap, immutableBitmap, bead.getPreviouscenterCircle_X());
//                generateCycle = true;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            imageView.setImageBitmap(secondBitmap);
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            imageView.setImageBitmap(secondBitmap);
//
//        }
//    }


    public void test1(View v) {
        Log.i("point ma127", "test1");
        changedReturnedBitmap = bead1.generateBump(changedBitmap, immutableBitmap, bead1.getUpdatedcenterCircle_X());
        Log.i("point ma127", "test1");
        imageView.setImageBitmap(changedReturnedBitmap);
        Log.i("point ma127", "test1");
        imageView.setImageBitmap(bead1.dissolveBitmap(changedReturnedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X()));
        Log.i("point ma127", "test1");

    }

    public void test2(View v) {

//        convert(changedBitmap, immutableBitmap, 50, 400, 90);
        imageView.setImageBitmap(immutableBitmap);
        Log.i("point ma310", "test2");

//        imageView.setImageBitmap(icon);
    }

    public void test3(View v) {
        imageView.setImageBitmap(bead1.dissolveBitmap(changedReturnedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X()));
    }

    public void test4(View v) {

        Render render=new Render(this,immutableBitmap,changedBitmap,bead1,imageView);
             render.timer();

    }

}
