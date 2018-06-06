package com.example.android.rollingbeadlibrary;

public class TimerInterface {
    /**
     * The current value.
     */
    boolean mStopValue;
    boolean mAsyncValue;

    private TimerInterfaceListener mStopListener;
    private AsyncListener mAsyncListener;

    /**
     * Construct a the boolean store.
     *
     * @param initialValue The initial value.
     */
    public TimerInterface(boolean initialValue) {
        mStopValue = initialValue;

    }

    public TimerInterface(boolean mStopValue, boolean mAsyncValue) {
        this.mStopValue = mStopValue;
        this.mAsyncValue = mAsyncValue;
    }

    /**
     * Sets a listener on the store. The listener will be modified when the
     * value changes.
     */

    public void setmStopListener(TimerInterfaceListener mStopListener) {
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

    public interface TimerInterfaceListener {
        /**
         * Called when the value of the boolean changes.
         *
         * @param newValue The new value.
         */
        void onStopValueChanged(boolean newValue);
    }

    public interface AsyncListener {
        /**
         * Called when the value of the boolean changes.
         *
         * @param newValue The new value.
         */
        void onAsyncValueChanged(boolean newValue);
    }
}