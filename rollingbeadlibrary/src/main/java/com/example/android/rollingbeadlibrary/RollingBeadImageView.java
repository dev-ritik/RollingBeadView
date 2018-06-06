package com.example.android.rollingbeadlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class RollingBeadImageView extends ImageView implements TimerInterface.TimerInterfaceListener {
    private Context context;
    private Bitmap changedBitmap;
    private Bitmap immutableBitmap;
    private int centerCircle_X = 0;
    private int centerCircle_Y = 350;
    private boolean generateCycle = true;
    private int movement = 15;
    private int radius = 35;
    private int numberOfTimes = 1;
    private int repetitionTime = 1;
    RollingBead bead1;
    private boolean mReady;
    private boolean mSetupPending;
    private boolean orientationHorizontal;
    private boolean direction_Positive;
    private boolean asyncTaskRunning = false;
    private boolean stopTaskPending = false;

    ExecuteAsync task;

    Timer moveBeadTimer;

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
//        Log.i("point rbi66", "attrs  " + attrs);
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RollingBeadImageView_center_X) {
                centerCircle_X = a.getInt(attr, 0);
            } else if (attr == R.styleable.RollingBeadImageView_center_Y) {
                centerCircle_Y = a.getInt(attr, 0);
            } else if (attr == R.styleable.RollingBeadImageView_movement) {
                movement = a.getInt(attr, 20);
            } else if (attr == R.styleable.RollingBeadImageView_radius) {
                radius = a.getInt(attr, 40);
                if (radius > 150)
                    throw new IllegalArgumentException(String.format("radius %s not supported.", radius));
            } else if (attr == R.styleable.RollingBeadImageView_number_Of_Times) {
                numberOfTimes = a.getInt(attr, 1);
                if (numberOfTimes < 1)
                    throw new IllegalArgumentException(String.format("number_Of_Times %s not supported.", numberOfTimes));
            } else if (attr == R.styleable.RollingBeadImageView_repetition_Times) {
                repetitionTime = a.getInt(attr, 200);
                if (repetitionTime < 70)
                    throw new IllegalArgumentException(String.format("repetition_Times %s may result in repetitive Garbage Collection.", repetitionTime));
            } else if (attr == R.styleable.RollingBeadImageView_orientation) {
                orientationHorizontal = (a.getInt(attr, 1) == 1);
            } else if (attr == R.styleable.RollingBeadImageView_direction) {
                direction_Positive = (a.getInt(attr, 1) == 1);
            }
        }
        Log.i("point rbi94", "centerCircle_Y  " + centerCircle_Y);

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
            Log.i("point rbi109", "init");
            setup();
            mSetupPending = false;
        }
    }

    public int getCenterCircle_X() {
        return centerCircle_X;
    }

    public void setCenterCircle_X(int centerCircle_X) {
        this.centerCircle_X = centerCircle_X;
    }

    public int getCenterCircle_Y() {
        return centerCircle_Y;
    }

    public void setCenterCircle_Y(int centerCircle_Y) {
        this.centerCircle_Y = centerCircle_Y;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public int getRepetitionTime() {
        return repetitionTime;
    }

    public void setRepetitionTime(int repetitionTime) {
        this.repetitionTime = repetitionTime;
    }

    public boolean isOrientationHorizontal() {
        return orientationHorizontal;
    }

    public void setOrientationHorizontal(boolean orientationHorizontal) {
        this.orientationHorizontal = orientationHorizontal;
    }

    public boolean isDirection_Positive() {
        return direction_Positive;
    }

    public void setDirection_Positive(boolean direction_Positive) {
        this.direction_Positive = direction_Positive;
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
        Log.i("point rbi147", "onSizeChanged");
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        Log.i("point rbi154", "setPadding");
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        Log.i("point rbi161", "setPaddingRelative");
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
        Log.i("point rbi210", "initializeBitmap");

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
        if (calculateBounds().width() == 0) {
            invalidate();
            return;
        }
        mDrawableRect.set(calculateBounds());

        bead1 = new RollingBead(changedBitmap, immutableBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        Log.i("point rbi206", "setup");

//        Render render = new Render(this, immutableBitmap, changedBitmap, bead1, imageView);
//        render.timer();
        timer();
//        invalidate();
    }

    public RectF calculateBounds() {
//        Log.i("point rbi224", "getPaddingBottom()" + getPaddingBottom());
//        Log.i("point rbi225", "end" + getPaddingEnd());
//        Log.i("point rbi226", "left" + getPaddingLeft());
//        Log.i("point rbi227", "right  " + getPaddingRight());
//        Log.i("point rbi228", "start" + getPaddingStart());
//        Log.i("point rbi229", "top" + getPaddingTop());
//        Log.i("point rbi230", "bottom" + getBottom());
//        Log.i("point rbi231", "width" + getWidth());
//        Log.i("point rbi232", "height" + getHeight());
//        Log.i("point rbi233", "getTop" + getTop());
//        Log.i("point rbi234", "getLeft" + getLeft());
//        Log.i("point rbi235", "getRight" + getRight());
        return new RectF(getPaddingLeft(), getPaddingTop(), getRight() - getPaddingRight(), getBottom() - getPaddingBottom());
    }

    public void timer() {
        if (moveBeadTimer == null) {
            moveBeadTimer = new Timer();
            moveBeadTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
//                Log.i("point ma255", "run started");
                    task = new ExecuteAsync(bead1);
                    task.execute(new String[]{null});
                }
            }, 5, repetitionTime);
        }
    }

    private class ExecuteAsync extends AsyncTask<String, String, String> {
        //        Bitmap firstBitmap;
//        Bitmap secondBitmap;
        RollingBead bead;

        public ExecuteAsync(RollingBead bead) {
            this.bead = bead;
        }

        public ExecuteAsync() {
        }

        @Override
        protected String doInBackground(String... urls) {
            if (generateCycle) {
                bead.generateBump(changedBitmap, immutableBitmap);
                generateCycle = false;
            } else {
                bead.dissolveBitmap(changedBitmap, immutableBitmap);
                generateCycle = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncTaskRunning = true;
            time.setAsyncValue(true);
        }

        @Override
        protected void onPostExecute(String result) {
            invalidate();
            asyncTaskRunning = false;
            time.setAsyncValue(false);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("point rbi183", "ondraw");
        if (changedBitmap == null) {
            Log.i("point rbi189", "ondraw problem");
            return;
        }
        if (immutableBitmap == null) {
            Log.i("point rbi193", "ondraw problem");
            return;
        }
//        Log.i("point rbi367", mDrawableRect.height() + "  height  " + mDrawableRect.width() + "  width");
//        Log.i("point rbi368", immutableBitmap.getWidth() + "  immutableBitmap.width  "+immutableBitmap.getHeight());
//        Log.i("point rbi368", changedBitmap.getWidth() + "  changedBitmap.width  "+changedBitmap.getHeight());

        canvas.drawBitmap(changedBitmap, null, mDrawableRect, null);
//        Log.i("point rbi349", "ondraw completed");

//        super.draw(canvas);
    }

    public void stopRender() {
        Log.i("point rbi354", "stopRender" + " start");

        if (moveBeadTimer != null) {
            moveBeadTimer.cancel();
            moveBeadTimer.purge();
            moveBeadTimer = null;
        }

        stopTaskPending = true;
        time.setStopValue(true);
        Log.i("point rbi442", time.getAsyncValue() + "async value");
        if (!time.getAsyncValue()) {
            Log.i("point rbi444", "if above method");
            bead1.dissolveAll(changedBitmap, immutableBitmap);
            invalidate();
            stopTaskPending = false;
            time.setStopValue(false);
        } else {
            time.setmAsyncListener(new TimerInterface.AsyncListener() {
                @Override
                public void onAsyncValueChanged(boolean newValue) {
                    if (!newValue) {
                        Log.i("point rbi453", "if stop method");
                        bead1.dissolveAll(changedBitmap, immutableBitmap);
                        invalidate();
                        stopTaskPending = false;
                        time.setStopValue(false);
                        time.setmAsyncListener(null);
                    }
                }
            });
        }
    }

    public void resumeRender() {
        if (moveBeadTimer == null)
            timer();
    }

    @Override
    public void onStopValueChanged(boolean newValue) {
        Log.i("point rbi468", "onValueChanged listener");

    }

    private TimerInterface time = new TimerInterface(false);

    public void changeBead() {

        Log.i("point rbi500", "changeBead method");
        stopRender();
        Log.i("point rbi502", "inside changeBead method" + " stop " + time.getStopValue() + " async " + time.getAsyncValue());
        if (!time.getStopValue() && !time.getAsyncValue()) {
            Log.i("point rbi504", "if changeBead method");
            bead1 = new RollingBead(changedBitmap, immutableBitmap, 50, 50, movement, 100, numberOfTimes, orientationHorizontal, !direction_Positive);
            resumeRender();

        } else {
            time.setmStopListener(new TimerInterface.TimerInterfaceListener() {
                @Override
                public void onStopValueChanged(boolean newValue) {
                    Log.i("point rbi490", "inside onvalue method");
                    bead1 = new RollingBead(changedBitmap, immutableBitmap, 50, 50, movement, 100, numberOfTimes, !orientationHorizontal, !direction_Positive);
                    resumeRender();
                    time.setmStopListener(null);
                }
            });


        }
    }
}