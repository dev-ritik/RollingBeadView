package com.example.android.rollingbeadview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    Bitmap immutableBitmap;
    Bitmap changedBitmap;
    RollingBead bead1;
    RollingBeadImageView mimage;

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
        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ma57", "onclick");

            }
        });
    }

    public void test2(View v) {

        mimage.setAlpha(0.5f);
    }

    public void test3(View v) {
        mimage.setImageAlpha(20);
    }

    public void test4(View v) {

//        Render render = new Render(this, immutableBitmap, changedBitmap, bead1, imageView);
//        render.timer();
        mimage.setLeft(50);
    }

}
