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

public class RollingBeadImageView extends ImageView {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private final RectF mDrawableRect = new RectF();
    private RollingBead bead;
    ExecuteAsync task;
    Timer moveBeadTimer;
    private Context context;
    private Bitmap changedBitmap, immutableBitmap;
    private int centerCircle_X = 0;
    private int centerCircle_Y = 0;
    private int movement = 15;
    private int radius = 35;
    private int numberOfTimes = 1;
    private int repetitionTime = 70;
    private boolean mReady, mSetupPending;
    private boolean orientationHorizontal, direction_Positive;
    private MyInterface time = new MyInterface(false);
    private boolean generateCycle = true;

    public RollingBeadImageView(Context context) {
        super(context);
//        Log.i("point rbi50", "RollingBeadImageView  ");
        init();
    }

    public RollingBeadImageView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
//        Log.i("point rbi54", "RollingBeadImageView  ");
    }

    public RollingBeadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        Log.i("point rbi59", "RollingBeadImageView  ");
        this.initBaseXMLAttrs(context, attrs);
        init();
    }

    public int getOriginalCenterCircle_X() {
        return centerCircle_X;
    }

    public void setCenterCircle_X(int centerCircle_X) {
//        this.centerCircle_X = centerCircle_X;
        if (orientationHorizontal)
            changeBead(centerCircle_X, bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(centerCircle_X, bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
    }

    public int getOriginalCenterCircle_Y() {
        return centerCircle_Y;
    }

    public void setCenterCircle_Y(int centerCircle_Y) {
//        this.centerCircle_Y = centerCircle_Y;
        if (orientationHorizontal)
            changeBead(bead.getMovingCoordinate(), centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(bead.getConstantCoordinate(), centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
        if (orientationHorizontal)
            changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        if (orientationHorizontal)
            changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes) {
        if (orientationHorizontal)
            changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        this.numberOfTimes = numberOfTimes;
    }

    public int getRepetitionTime() {
        return repetitionTime;
    }

    public void setRepetitionTime(int repetitionTime) {
        if (repetitionTime < 70)
            throw new IllegalArgumentException(String.format("repetition_Times %s may result in repetitive Garbage Collection.", repetitionTime));
        this.repetitionTime = repetitionTime;
        stopRender();
        resumeRender();
    }

    public boolean isOrientationHorizontal() {
        return orientationHorizontal;
    }

    public void setOrientationHorizontal(boolean orientationHorizontal) {
        if (this.orientationHorizontal)
            changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        this.orientationHorizontal = orientationHorizontal;
    }

    public boolean isDirection_Positive() {
        return direction_Positive;
    }

    public void setDirection_Positive(boolean direction_Positive) {
        this.direction_Positive = direction_Positive;
        if (orientationHorizontal)
            changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        else
            changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
    }

    public int getCurrentCenterCircle_X() {
        if (orientationHorizontal) return bead.getMovingCoordinate();
        else return bead.getConstantCoordinate();
    }

    public int getCurrentCenterCircle_Y() {
        if (orientationHorizontal) return bead.getConstantCoordinate();
        else return bead.getMovingCoordinate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
//        Log.i("point rbi214", " setImageBitmap");
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.i("point rbi147", "onSizeChanged" + w + "w" + h + " h" + oldw + "oldw" + oldh);
//        if (w != oldw || h != oldh) {
//            if (moveBeadTimer != null) {
//                stopRender();
//            }
//        }
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
//                Log.i("point rbi168", "bitmap  ");

            } else {
                immutableBitmap = bitmap;
                changedBitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
//                Log.i("point rbi173", "bitmap  ");
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
//                    Log.i("point rbi186", "bitmap  ");
                    changedBitmap = bitmap;
                } else {
//                    Log.i("point rbi189", "bitmap  ");
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
//        Log.i("point rbi210", "initializeBitmap");
        getBitmapFromDrawable(getDrawable());
        setup();
    }

    final void initBaseXMLAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RollingBeadImageView);
        final int N = a.getIndexCount();
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
            } else if (attr == R.styleable.RollingBeadImageView_number_Of_Times) {
                numberOfTimes = a.getInt(attr, 1);
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
        a.recycle();
    }

    void init() {
//        Log.i("point rbi103", " init");
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    private void setup() {
        Log.i("point rbi319", "setup");

        if (!mReady) {
            mSetupPending = true;
            return;
        }
        if (immutableBitmap == null) {
            invalidate();
            return;
        }
        if (changedBitmap == null) {
            invalidate();
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        mDrawableRect.set(calculateBounds());

        bead = new RollingBead(changedBitmap, immutableBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
//        Log.i("point rbi206", "setup");

        timer();
    }

    private RectF calculateBounds() {
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
        return new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    void timer() {

        if (moveBeadTimer == null) {
//            Log.i("point rbi369", "timer null");
            moveBeadTimer = new Timer();
            moveBeadTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
//                Log.i("point ma255", "run started");
                    task = new ExecuteAsync(bead);
                    task.execute(new String[]{null});
                }
            }, 5, repetitionTime);
        }
//        else
//            Log.i("point rbi379", "timer not null");

    }

    private class ExecuteAsync extends AsyncTask<String, String, String> {
        RollingBead bead;

        public ExecuteAsync(RollingBead bead) {
            this.bead = bead;
        }

        public ExecuteAsync() {
        }

        @Override
        protected String doInBackground(String... urls) {
            if (generateCycle) {
                bead.generateMovingBead(changedBitmap, immutableBitmap);
                generateCycle = false;
            } else {
//                Log.i("point rbi467","here");
                bead.dissolveMovingBead(changedBitmap, immutableBitmap);
                generateCycle = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            time.setAsyncValue(true);
        }

        @Override
        protected void onPostExecute(String result) {
            invalidate();
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
//            Log.i("point rbi189", "ondraw problem");
            return;
        }
        if (immutableBitmap == null) {
//            Log.i("point rbi193", "ondraw problem");
            return;
        }
        canvas.drawBitmap(changedBitmap, null, mDrawableRect, null);
    }

    void changeBead(final int centerCircle_X, final int centerCircle_Y, final int movement, final int radius, final int numberOfTimes, final boolean orientationHorizontal, final boolean direction_Positive) {

//        Log.i("point rbi500", "changeBead method");
        stopRender();
//        Log.i("point rbi502", "inside changeBead method" + " stop " + time.getStopValue() + " async " + time.getAsyncValue());
        if (!time.getStopValue() && !time.getAsyncValue()) {
//            Log.i("point rbi504", "if changeBead method");
            bead = new RollingBead(changedBitmap, immutableBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            resumeRender();

        } else {
            time.setmStopListener(new MyInterface.StopListener() {
                @Override
                public void onStopValueChanged(boolean newValue) {
//                    Log.i("point rbi490", "inside onvalue method");
                    bead = new RollingBead(changedBitmap, immutableBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
                    resumeRender();
                    time.setmStopListener(null);
                }
            });


        }
    }

    public void pauseRenderer() {
        if (moveBeadTimer != null) {
            moveBeadTimer.cancel();
            moveBeadTimer.purge();
            moveBeadTimer = null;
        }
    }

    public void stopRender() {
//        Log.i("point rbi354", "stopRender" + " start");

        pauseRenderer();

        time.setStopValue(true);
//        Log.i("point rbi442", time.getAsyncValue() + "async value");
        if (!time.getAsyncValue()) {
//            Log.i("point rbi444", "if above method");
            bead.dissolveAll(changedBitmap, immutableBitmap);
            invalidate();
            time.setStopValue(false);
        } else {
            time.setmAsyncListener(new MyInterface.AsyncListener() {
                @Override
                public void onAsyncValueChanged(boolean newValue) {
                    if (!newValue) {
//                        Log.i("point rbi453", "if stop method");
                        bead.dissolveAll(changedBitmap, immutableBitmap);
                        invalidate();
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
}