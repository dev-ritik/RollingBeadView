package com.example.android.rollingbeadview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.android.rollingbeadlibrary.RollingBeadImageView;

public class MainActivity extends AppCompatActivity {
    //    TextView text;
    ImageView imageView;
    Bitmap immutableBitmap, changedBitmap;
    RollingBeadImageView mimage;
    Bitmap im, cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        imageView = (ImageView) findViewById(R.id.imageView);
//        text = (TextView) findViewById(R.id.text);

        immutableBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.five);
//        Log.i("point ma39", "immutableBitmap  " + immutableBitmap.isMutable());
        changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mimage = (RollingBeadImageView) findViewById(R.id.mimage);


        im = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.three);
        cb = im.copy(Bitmap.Config.ARGB_8888, true);
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


    }


    public void test1(View v) {
//        RollingBead rb = new RollingBead();
//
//        imageView.setImageBitmap(rb.generateBead(cb, im, 0, 0, 200, 2.0, true, true));
        mimage.setOrientationHorizontal(true);
        //TODO:  test all feature
    }

    public void test2(View v) {
//        RollingBead rb = new RollingBead();
//
//        imageView.setImageBitmap(rb.generateBead(cb, im, 0, 0, 200, 2.0, true, false));
        mimage.stopRender();
    }

    public void test3(View v) {
        Log.i("point ma71", mimage.getOriginalCenterCircle_Y() + "");
    }

    public void test4(View v) {

        Log.i("point ma79", mimage.getCurrentCenterCircle_Y() + "");
    }

}
