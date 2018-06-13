package com.example.android.rollingbeadview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.android.rollingbeadlibrary.RollingBead;
import com.example.android.rollingbeadlibrary.RollingBeadImageView;

public class MainActivity extends AppCompatActivity {
    //    TextView text;
    ImageView imageView;
    Bitmap immutableBitmap, changedBitmap;
    RollingBeadImageView mimage;
    Bitmap im, cb;
    RollingBead rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        text = (TextView) findViewById(R.id.text);
//
        mimage = (RollingBeadImageView) findViewById(R.id.mimage);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        rb = new RollingBead(imageView,150,20);

//        im = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                R.drawable.colors);
//        cb = im.copy(Bitmap.Config.ARGB_8888, true);
    }


    public void test1(View v) {
        Log.i("point ma39", "start");
//        imageView.setImageBitmap(rb.generateFixedBead(200, 200, 100, 1.0, true, true));
//        imageView.setImageBitmap(rb.generateFixedBead(20, 20, 1.4, true, true));
//        Log.i("point ma42", "end");
//        imageView.setImageBitmap(rb.generateFixedBead(0.02f, 0.02f, 0.5f, 2.0, true, true));
//        rb.generateFixedBead(540.02f, 0.02f, 0.4f, 2.0, true, true);
//        mimage.setCenterCircle_X(0.05f);
//        mimage.setOrientationHorizontal(true);
    }

    public void test2(View v) {
        Log.i("point ma50", "start");
        rb.dissolveFixedBead(20, 20, true, true);
        Log.i("point ma52", "end");
//        rb.dissolveFixedBead(0.02f, 0.02f, 0.4f, true, true);
//        imageView.setImageBitmap(rb.dissolveFixedBead(0.02f, 0.02f, 0.2f, true, true));
//        imageView.setImageBitmap(rb.dissolveBead(10, 10, 300, true, true));
//        mimage.setCenterCircle_X(0.8f);
    }

    public void test3(View v) {
        Log.i("point ma60","test3 start");
        mimage.setCenterCircle_Y(0.1f);
        Log.i("point ma62","test3 end");
    }

    public void test4(View v) {
        Log.i("point ma65","test4 start");

        mimage.setCenterCircle_Y(0.8f);
        Log.i("point ma68","test4 end");

    }

}
