package com.example.android.rollingbeadlibrary;

// this class handles callbacks for stop and async methods (required when stopping renderer and/or changing bead)
public class MyInterface {
    /**
     * The current value.
     */
    private boolean mStopValue;
    private boolean mAsyncValue;

    private StopListener mStopListener;
    private AsyncListener mAsyncListener;

    /**
     * Construct a the boolean store.
     *
     * @param initialValue The initial value.
     */
    public MyInterface(boolean initialValue) {
        mStopValue = initialValue;

    }

    public MyInterface(boolean mStopValue, boolean mAsyncValue) {
        this.mStopValue = mStopValue;
        this.mAsyncValue = mAsyncValue;
    }

    /**
     * Sets a listener on the store. The listener will be modified when the
     * value changes.
     */

    public void setmStopListener(StopListener mStopListener) {
        this.mStopListener = mStopListener;
    }

    public void setmAsyncListener(AsyncListener mAsyncListener) {
        this.mAsyncListener = mAsyncListener;
    }

    /**
     * Set a new boolean value.
     *
     * @param newValue The new value.
     */
    public void setStopValue(boolean newValue) {
        mStopValue = newValue;
        if (mStopListener != null) {
            mStopListener.onStopValueChanged(mStopValue);
        }
    }

    public void setAsyncValue(boolean newValue) {
        mAsyncValue = newValue;
        if (mAsyncListener != null) {
            mAsyncListener.onAsyncValueChanged(mAsyncValue);
        }
    }

    /**
     * Get the current value.
     *
     * @return The current boolean value.
     */
    public boolean getStopValue() {
        return mStopValue;
    }

    public boolean getAsyncValue() {
        return mAsyncValue;
    }

    public interface StopListener {
        /**
         * Called when the value of the mStopValue changes.
         *
         * @param newValue The new value.
         */
        void onStopValueChanged(boolean newValue);
    }

    public interface AsyncListener {
        /**
         * Called when the value of the mAsyncValue changes.
         *
         * @param newValue The new value.
         */
        void onAsyncValueChanged(boolean newValue);
    }
}