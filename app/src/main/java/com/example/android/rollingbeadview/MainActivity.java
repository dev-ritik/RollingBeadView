package com.example.android.rollingbeadview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.rollingbeadlibrary.Render;
import com.example.android.rollingbeadlibrary.RollingBead;
import com.example.android.rollingbeadlibrary.RollingBeadImageView;

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


    }

    public void test1(View v) {
//        Log.i("point ma127", "test1");
//        changedReturnedBitmap = bead1.generateBump(changedBitmap, immutableBitmap, bead1.getUpdatedcenterCircle_X());
//        Log.i("point ma127", "test1");
//        imageView.setImageBitmap(changedReturnedBitmap);
//        Log.i("point ma127", "test1");
//        imageView.setImageBitmap(bead1.dissolveBitmap(changedReturnedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X()));
//        Log.i("point ma127", "test1");

        mimage.stopRender();
    }

    public void test2(View v) {

//        imageView.setImageBitmap(bead1.generateBump(changedBitmap, immutableBitmap, bead1.getUpdatedcenterCircle_X()));
        mimage.resumeRender();
//        imageView.setImageBitmap(icon);
    }

    public void test3(View v) {
        bead1 = new RollingBead(changedBitmap, immutableBitmap, 350, 350, 40, 50, 1,true,true);
    }

    public void test4(View v) {

        Render render = new Render(this, immutableBitmap, changedBitmap, bead1, imageView);
        render.timer();

    }

}
