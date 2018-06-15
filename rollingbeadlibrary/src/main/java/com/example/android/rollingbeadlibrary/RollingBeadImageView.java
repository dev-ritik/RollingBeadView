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

import static android.util.TypedValue.TYPE_DIMENSION;

// Custom class that handles overall functions of bead movement

public class RollingBeadImageView extends ImageView {
    private final RectF mDrawableRect = new RectF();
    RollingBead bead;
    ExecuteAsync task;
    Timer moveBeadTimer;
    private Bitmap changedBitmap;
    private int centerCircle_X = 0;
    private int centerCircle_Y = 0;
    private int movement = 15;
    private int radius = 30;
    private int numberOfTimes = 1;
    private int repetitionTime = 50;
    private boolean mReady, mSetupPending;
    private boolean orientationHorizontal = true, direction_Positive = true;

    // interface to monitor value of toggles (when required)
    private MyInterface time = new MyInterface(false);

    // toggles between generating and moving cycles in bead movement
    private boolean generateCycle = true;

    public RollingBeadImageView(Context context) {
        super(context);
//        Log.i("point rbi50", "RollingBeadImageView  ");
        init();
    }

    public RollingBeadImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RollingBeadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        Log.i("point rbi59", "RollingBeadImageView  ");
        this.initBaseXMLAttrs(context, attrs);
        init();
    }

    public int getOriginalCenterCircle_X() {
        return centerCircle_X;
    }

    public void setCenterCircle_X(int centerCircle_XInPx) {
        if (!time.getStopValue()) {
            if (orientationHorizontal)
                changeBead(centerCircle_XInPx, bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(centerCircle_XInPx, bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        }
    }

    public void setCenterCircle_X(float centerCircle_XInDecimal) {
        setCenterCircle_X((int) (centerCircle_XInDecimal * changedBitmap.getWidth()));
    }

    public int getOriginalCenterCircle_Y() {
        return centerCircle_Y;
    }

    public void setCenterCircle_Y(int centerCircle_YInPx) {
        if (!time.getStopValue()) {
            if (orientationHorizontal)
                changeBead(bead.getMovingCoordinate(), centerCircle_YInPx, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(bead.getConstantCoordinate(), centerCircle_YInPx, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        }
    }

    public void setCenterCircle_Y(float centerCircle_YInDecimal) {
        setCenterCircle_Y((int) (centerCircle_YInDecimal * changedBitmap.getHeight()));
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movementInPx) {
        if (!time.getStopValue()) {
            this.movement = movementInPx;
            if (orientationHorizontal)
                changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movementInPx, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movementInPx, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        }
    }

    public void setMovement(float movementInDecimal) {
        if (movementInDecimal < 1.0 && movementInDecimal >= 0)
            setMovement((int) (movementInDecimal * changedBitmap.getHeight()));
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radiusInPx) {
        if (!time.getStopValue()) {
            this.radius = radiusInPx;
            if (orientationHorizontal)
                changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radiusInPx, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radiusInPx, numberOfTimes, orientationHorizontal, direction_Positive);
        }
    }

    public void setRadius(float radiusInDecimal) {
        if (radiusInDecimal < 1.0 && radiusInDecimal >= 0)
            setRadius((int) (radiusInDecimal * changedBitmap.getHeight()));
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes) {
        if (!time.getStopValue()) {
            if (orientationHorizontal)
                changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            this.numberOfTimes = numberOfTimes;
        }
    }

    public int getRepetitionTime() {
        return repetitionTime;
    }

    // stops renderer and restarts it with new time period
    public void setRepetitionTime(int repetitionTime) {
        if (!time.getStopValue()) {
            if (repetitionTime < 50)
                throw new IllegalArgumentException(String.format("repetition_Times %s may result in repetitive Garbage Collection.", repetitionTime));
            this.repetitionTime = repetitionTime;
            stopRender();
            resumeRender();
        }
    }

    public boolean isOrientationHorizontal() {
        return orientationHorizontal;
    }

    public void setOrientationHorizontal(boolean orientationHorizontal) {
        if (!time.getStopValue()) {
            if (this.orientationHorizontal)
                changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            this.orientationHorizontal = orientationHorizontal;
        }
    }

    public boolean isDirection_Positive() {
        return direction_Positive;
    }

    public void setDirection_Positive(boolean direction_Positive) {
        if (!time.getStopValue()) {
            this.direction_Positive = direction_Positive;
            if (orientationHorizontal)
                changeBead(bead.getMovingCoordinate(), bead.getConstantCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            else
                changeBead(bead.getConstantCoordinate(), bead.getMovingCoordinate(), movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
        }
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

    private void initializeBitmap() {
//        Log.i("point rbi210", "initializeBitmap");
        getBitmapFromDrawable(getDrawable());
        setup();
    }

    // get bitmap from drawable given by user
    void getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            invalidate();
            return;
        }

        if (drawable instanceof BitmapDrawable) {
            classifyBitmap(((BitmapDrawable) drawable).getBitmap());
            return;
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                invalidate();
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                classifyBitmap(bitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            invalidate();
        }
    }

    // convert bitmap to mutable
    void classifyBitmap(Bitmap inputBitmap) {
        if (inputBitmap.isMutable()) {
            changedBitmap = inputBitmap;
        } else {
            changedBitmap = inputBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
    }

    // getting attributes from users
    final void initBaseXMLAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RollingBeadImageView);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RollingBeadImageView_center_X) {
                if (a.peekValue(attr).type == TYPE_DIMENSION)
                    centerCircle_X = a.getDimensionPixelSize(attr, 0);
                else
                    centerCircle_X = (int) a.getFraction(attr, changedBitmap.getWidth(), changedBitmap.getWidth(), 0);
            } else if (attr == R.styleable.RollingBeadImageView_center_Y) {
                if (a.peekValue(attr).type == TYPE_DIMENSION)
                    centerCircle_Y = a.getDimensionPixelSize(attr, 0);
                else
                    centerCircle_Y = (int) a.getFraction(attr, changedBitmap.getHeight(), changedBitmap.getHeight(), 0);
            } else if (attr == R.styleable.RollingBeadImageView_movement) {
                if (a.peekValue(attr).type == TYPE_DIMENSION)
                    movement = a.getDimensionPixelSize(attr, 15);
                else
                    movement = (int) a.getFraction(attr, changedBitmap.getHeight(), changedBitmap.getHeight(), 15);
            } else if (attr == R.styleable.RollingBeadImageView_radius) {
                if (a.peekValue(attr).type == TYPE_DIMENSION)
                    radius = a.getDimensionPixelSize(attr, 30);
                else
                    radius = (int) a.getFraction(attr, changedBitmap.getHeight(), changedBitmap.getHeight(), 30);
            } else if (attr == R.styleable.RollingBeadImageView_number_Of_Times) {
                numberOfTimes = a.getInt(attr, 1);
            } else if (attr == R.styleable.RollingBeadImageView_repetition_Times) {
                repetitionTime = a.getInt(attr, 50);
                // can't do such fast calculations
                if (repetitionTime < 50)
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
//        Log.i("point rbi319", "setup");

        if (!mReady) {
            mSetupPending = true;
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

        bead = new RollingBead(changedBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
//        Log.i("point rbi206", "setup");

        timer();
    }

    //returns view bounds
    private RectF calculateBounds() {
        return new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    // periodically calls async method to run the bead
    void timer() {
        if (moveBeadTimer == null) {
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
    }

    // asynctask to reduce load on main thread
    private class ExecuteAsync extends AsyncTask<String, String, String> {
        RollingBead bead;

        public ExecuteAsync(RollingBead bead) {
            this.bead = bead;
        }

        @Override
        protected String doInBackground(String... urls) {
            //alternative generate and dissolve cycles
            if (generateCycle)
                bead.generateMovingBead();
            else bead.dissolveMovingBead();

            generateCycle = !generateCycle;
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
            return;
        }
        // draw the changed bitmap on the canvas
        canvas.drawBitmap(changedBitmap, null, mDrawableRect, null);
    }

    // method called before changing bead
    // stops new bead at the right time based on interface callbacks
    void changeBead(final int centerCircle_X, final int centerCircle_Y, final int movement, final int radius, final int numberOfTimes, final boolean orientationHorizontal, final boolean direction_Positive) {

//        Log.i("point rbi500", "changeBead method");
        stopRender();
//        Log.i("point rbi502", "inside changeBead method" + " stop " + time.getStopValue() + " async " + time.getAsyncValue());

        if (!time.getStopValue() && !time.getAsyncValue()) {
//            Log.i("point rbi504", "if changeBead method");
            bead = new RollingBead(changedBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
            resumeRender();

        } else {
            time.setmStopListener(new MyInterface.StopListener() {
                @Override
                public void onStopValueChanged(boolean newValue) {
//                    Log.i("point rbi490", "inside onvalue method");
                    bead = new RollingBead(changedBitmap, centerCircle_X, centerCircle_Y, movement, radius, numberOfTimes, orientationHorizontal, direction_Positive);
                    resumeRender();
                    time.setmStopListener(null);
                }
            });


        }
    }

    // just stops motion of bead to a stand still
    public void pauseRenderer() {
        if (moveBeadTimer != null) {
            moveBeadTimer.cancel();
            moveBeadTimer.purge();
            moveBeadTimer = null;
        }
    }

    // completely stops renderer making dissolving all recent changes on bitmap
    public void stopRender() {
//        Log.i("point rbi354", "stopRender" + " start");

        pauseRenderer();

        time.setStopValue(true);
        if (!time.getAsyncValue()) {
//            Log.i("point rbi521", "if stop renderer method" + time.getAsyncValue());
            bead.dissolveAll();
            invalidate();
            time.setStopValue(false);
        } else {
//            Log.i("point rbi526", "if stop renderer method" + time.getAsyncValue());
            time.setmAsyncListener(new MyInterface.AsyncListener() {
                @Override
                public void onAsyncValueChanged(boolean newValue) {
                    if (!newValue) {
//                        Log.i("point rbi531", "if stop renderer method" + newValue);
                        bead.dissolveAll();
                        invalidate();
                        time.setStopValue(false);
                        time.setmAsyncListener(null);
                    }
                }
            });
        }
    }

    // generates new bead from the same spot as left
    public void resumeRender() {
        if (moveBeadTimer == null)
            timer();
    }
}