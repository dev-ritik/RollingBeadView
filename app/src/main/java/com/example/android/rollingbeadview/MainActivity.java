package com.example.android.rollingbeadview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.rollingbeadlibrary.Render;
import com.example.android.rollingbeadlibrary.RollingBead;
import com.example.android.rollingbeadlibrary.RollingBeadImageView;

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
    RollingBeadImageView mimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
//        text = (TextView) findViewById(R.id.text);

        immutableBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.five);
//        Log.i("point ma39", "immutableBitmap  " + immutableBitmap.isMutable());
        changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mimage=(RollingBeadImageView)findViewById(R.id.mimage);

//        Log.i("point ma41", "changedBitmap  " + changedBitmap.isMutable());
//        Drawable d = new BitmapDrawable(getResources(), changedBitmap);
//
//        Log.i("point ma42", "((BitmapDrawable) drawable).getBitmap()  " + ((BitmapDrawable) d).getBitmap().isMutable());

//
//        ColorDrawable cd = new ColorDrawable(0xFFFF6666);
//        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
//        Log.i("point ma41", "ColorDrawable  " + bitmap.isMutable());
//        Log.i("point ma54", bitmap.getWidth() + "  " + bitmap.getHeight());
//        Log.i("point ma54", ((BitmapDrawable) d).getBitmap().getWidth() + "  " + ((BitmapDrawable) d).getBitmap().getHeight());
//        Log.i("point ma56", changedBitmap.getWidth() + "  " + changedBitmap.getHeight());

        bead1 = new RollingBead(changedBitmap, immutableBitmap, 350, 350, 40, 50, 1);

    }


    //    private Bitmap getBitmapFromDrawable(Drawable drawable) {
//        if (drawable == null) {
//            return null;
//        }
//
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
////        try {
////            Bitmap bitmap;
////
////            if (drawable instanceof ColorDrawable) {
////                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
////            } else {
////                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
////            }
//
////            Canvas canvas = new Canvas(bitmap);
////            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
////            drawable.draw(canvas);
////            return bitmap;
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
//    }


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
//        Log.i("point ma127", "test1");
//        changedReturnedBitmap = bead1.generateBump(changedBitmap, immutableBitmap, bead1.getUpdatedcenterCircle_X());
//        Log.i("point ma127", "test1");
//        imageView.setImageBitmap(changedReturnedBitmap);
//        Log.i("point ma127", "test1");
//        imageView.setImageBitmap(bead1.dissolveBitmap(changedReturnedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X()));
//        Log.i("point ma127", "test1");

        mimage.calculateBounds();
    }

    public void test2(View v) {

        imageView.setImageBitmap(bead1.generateBump(changedBitmap, immutableBitmap, bead1.getUpdatedcenterCircle_X()));

//        imageView.setImageBitmap(icon);
    }

    public void test3(View v) {
        imageView.setImageBitmap(bead1.dissolveBitmap(changedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X()));
    }

    public void test4(View v) {

        Render render = new Render(this, immutableBitmap, changedBitmap, bead1, imageView);
        render.timer();

    }

}
