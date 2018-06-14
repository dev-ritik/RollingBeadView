package com.example.android.rollingbeadview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.android.rollingbeadlibrary.RollingBead;
import com.example.android.rollingbeadlibrary.RollingBeadImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    RollingBeadImageView mimage;
    Bitmap im, cb;
    RollingBead rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        mimage = (RollingBeadImageView) findViewById(R.id.mimage);
        imageView = (ImageView) findViewById(R.id.imageView);
        rb = new RollingBead(imageView);
//
//        im = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                R.drawable.colors);
//        cb = im.copy(Bitmap.Config.ARGB_8888, true);
    }



    public void test1(View v) {
        imageView.setImageBitmap(rb.generateFixedBead(0.93f, 0.46f, 0.2f, 2.0, true, true));
//        mimage.setCenterCircle_X(0);
//        mimage.setOrientationHorizontal(false);
//         mimage.bead.dissolveMovingBead();
    }

    public void test2(View v) {
//        rb.dissolveFixedBead(10, 10, 100, true, true);
        imageView.setImageBitmap(rb.dissolveFixedBead(0.93f, 0.46f, 0.2f, true, true));
//        imageView.setImageBitmap(rb.dissolveBead(10, 10, 300, true, true));
//        mimage.setMovement(200);
    }

    public void test3(View v) {
        imageView.setImageBitmap(rb.generateFixedBead(100, 100, 100, 3.0, true, true));
//        mimage.setNumberOfTimes(3);
    }

    public void test4(View v) {
        imageView.setImageBitmap(rb.dissolveFixedBead(150, 100, 100, true, true));

//        mimage.setCenterCircle_Y(0.8f);

    }

}
