package com.example.android.rollingbeadlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class RollingBeadImageView extends ImageView {
    private Context context;
    private Bitmap changedBitmap;
    private Bitmap immutableBitmap;
    private int centerCircle_X = 0;
    private int centerCircle_Y = 350;
    private boolean generateCycle = true;
    private int movementInX = 15;
    private int radius = 35;
    private int numberOfTimes = 1;
    private int repetitionTime = 1;
    private int height;
    private int width;
    RollingBead bead1;
    private boolean mReady;
    private boolean mSetupPending;

    private final RectF mDrawableRect = new RectF();


    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public RollingBeadImageView(Context context) {
        super(context);
//        Log.i("point rbi50", "RollingBeadImageView  ");
    }

    public RollingBeadImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        Log.i("point rbi54", "RollingBeadImageView  ");
        this.context = context;
        this.initBaseXMLAttrs(context, attrs);
        init();

    }

    public RollingBeadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        Log.i("point rbi59", "RollingBeadImageView  ");

        this.initBaseXMLAttrs(context, attrs);
        init();
    }

    final void initBaseXMLAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RollingBeadImageView);
        final int N = a.getIndexCount();
//        Log.i("point rbi66", "attrs  " + N);
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RollingBeadImageView_center_X) {
                centerCircle_X = a.getInt(attr, 0);
            } else if (attr == R.styleable.RollingBeadImageView_center_Y) {
                centerCircle_Y = a.getInt(attr, 350);
            } else if (attr == R.styleable.RollingBeadImageView_movement_In_X) {
                movementInX = a.getInt(attr, 20);
            } else if (attr == R.styleable.RollingBeadImageView_radius) {
                radius = a.getInt(attr, 40);
            } else if (attr == R.styleable.RollingBeadImageView_number_Of_Times) {
                radius = a.getInt(attr, 1);
            } else if (attr == R.styleable.RollingBeadImageView_repetition_Times) {
                repetitionTime = a.getInt(attr, 200);
            }
        }
        a.recycle();
    }

    public void init() {
//        super.setScaleType(SCALE_TYPE);
        mReady = true;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setOutlineProvider(new OutlineProvider());
//        }
//
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

//    @Override
//    public ScaleType getScaleType() {
//        return SCALE_TYPE;
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    private void getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            invalidate();
            return;
        }

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap.isMutable()) {
                changedBitmap = bitmap;
                Log.i("point rbi168", "bitmap  ");

            } else {
                immutableBitmap = bitmap;
                changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
                Log.i("point rbi173", "bitmap  ");
                return;
            }
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                invalidate();
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
                if (bitmap.isMutable()) {
                    Log.i("point rbi186", "bitmap  ");
                    changedBitmap = bitmap;
                } else {
                    Log.i("point rbi189", "bitmap  ");
                    immutableBitmap = bitmap;
                    changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            invalidate();
        }
    }

    private void initializeBitmap() {
        Log.i("point rbi180", "initializeBitmap");

        getBitmapFromDrawable(getDrawable());
        setup();
    }

    private void setup() {
        Log.i("point rbi186", "setup");

        if (!mReady) {
            mSetupPending = true;
            return;
        }
        if (immutableBitmap == null) {
            invalidate();
            return;
        }
        Log.i("point rbi198", "setup");

        if (changedBitmap == null) {
            invalidate();
            return;
        }

        mDrawableRect.set(calculateBounds());

        Log.i("point rbi204", "setup");

        bead1 = new RollingBead(changedBitmap, immutableBitmap, 350, 350, 40, 50, 1);
        Log.i("point rbi206", "setup");

//        Render render = new Render(this, immutableBitmap, changedBitmap, bead1, imageView);
//        render.timer();
        timer();
//        invalidate();

        changedBitmap = bead1.generateBump(changedBitmap, immutableBitmap, bead1.getPreviouscenterCircle_X());
        invalidate();

    }

    public RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

//        Log.i("point rbi224", "getPaddingBottom()" + getPaddingBottom());
//        Log.i("point rbi225", "end" + getPaddingEnd());
//        Log.i("point rbi226", "left" + getPaddingLeft());
//        Log.i("point rbi227", "right" + getPaddingRight());
//        Log.i("point rbi228", "start" + getPaddingStart());
//        Log.i("point rbi229", "top" + getPaddingTop());
        Log.i("point rbi230", "bottom" + getBottom());
        Log.i("point rbi231", "width" + getWidth());
        Log.i("point rbi232", "height" + getHeight());
        Log.i("point rbi233", "getTop" + getTop());
        Log.i("point rbi234", "getLeft" + getLeft());
        Log.i("point rbi235", "getRight" + getRight());
//        return new RectF(1, 2, 300, 300);
//        return new RectF(left, top, left + sideLength, top + sideLength);
        return new RectF(getLeft(), getTop(), getRight(), getBottom());
    }

    public void timer() {
        Log.i("point rbi209", "timer started");

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
            invalidate();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            imageView.setImageBitmap(secondBitmap);
//            RollingBeadImageView.invalidate();
            invalidate();

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("point rbi183", "ondraw");
        if (changedBitmap == null) {
            Log.i("point rbi189", "reached");
            return;
        }
        if (immutableBitmap == null) {
            Log.i("point rbi193", "reached");
            return;
        }
//        Log.i("point rbi367", mDrawableRect.height() + "  height  " + mDrawableRect.width() + "  width");
//        Log.i("point rbi368", immutableBitmap.getWidth() + "  immutableBitmap.width  "+immutableBitmap.getHeight());
//        Log.i("point rbi368", changedBitmap.getWidth() + "  changedBitmap.width  "+changedBitmap.getHeight());

        canvas.drawBitmap(changedBitmap, null, mDrawableRect, null);
//        Log.i("point rbi349", "ondraw completed");

//        if (mCircleBackgroundColor != Color.TRANSPARENT) {
//            Log.i("point cv166", "reached");
//            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mCircleBackgroundPaint);
//        }
//        Log.i("point cv169", "reached");

//        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint);

//        super.draw(canvas);
    }
}