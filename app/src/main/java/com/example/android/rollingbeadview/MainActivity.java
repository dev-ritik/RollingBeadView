package com.example.android.rollingbeadview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        imageView = (ImageView) findViewById(R.id.imageView);
//        text = (TextView) findViewById(R.id.text);

//        immutableBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                R.drawable.hope);
//        changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mimage = (RollingBeadImageView) findViewById(R.id.mimage);

        im = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.colors);
        cb = im.copy(Bitmap.Config.ARGB_8888, true);
        //TODO: removing these
        //TODO: making fractions
        rb = new RollingBead(im);
    }


    public void test1(View v) {
//        imageView.setImageBitmap(rb.generateBead(10, 10, 300, 2.0, true, true));
        mimage.setOrientationHorizontal(true);
    }

    public void test2(View v) {
//        rb.mixCircleBitmap(20, 20, 200, true, true);
        imageView.setImageBitmap(rb.dissolveBead(10, 10, 300, true, true));
//        mimage.stopRender();
    }

    public void test3(View v) {
        imageView.setImageBitmap(im);
    }

    public void test4(View v) {

        mimage.resumeRender();
    }

}
