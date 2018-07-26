package com.bgrummitt.engineburn.controller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class UFO {

    final static private String TAG = UFO.class.getSimpleName();

    private int mX;
    private int mY;
    private Bitmap ufoBitmap;
    private Boolean isFiring;
    private int mUFOFireDistance;

    /**
     * UFO constructor
     * @param ufoBitmap the bitmap picture of the UFO
     * @param startingX the X position the ufo will spawn at
     * @param startingY the Y position the ufo will spawn at
     * @param UFOFireDistance the distance that the ufo will go upwards when the screen is pressed
     */
    public UFO(Bitmap ufoBitmap, int startingX, int startingY, int UFOFireDistance){
        this.ufoBitmap = ufoBitmap;

        //Set the co-ordinates
        mX = startingX;
        mY = startingY;

        //Set the distance the UFO will go up when the screen is clicked
        this.mUFOFireDistance = UFOFireDistance;

        //Initialise the isFiring var
        isFiring = false;

    }

    /**
     * Reset the timing of the fall or boost
     */
    public void resetTiming(){
        //Get the current time and remove the distance moved
        long tempTime = System.currentTimeMillis();
        long tempRemove = (long)(mPercentageMoved * 250L);
        mStartTime = (tempTime - tempRemove);
    }

    /**
     * Function called when the screen is touched
     */
    public void Fire(){
        //Start the firing
        isFiring = true;
        //Reset Variables
        mPercentageMoved = 0;
        mPercentagePassed = 0;
        //Get a new start time
        mStartTime = System.currentTimeMillis();
    }

    private float mPercentageMoved;
    private float mPercentagePassed;
    private long mStartTime;

    /**
     * Update called every game loop
     */
    public void Update(){
        if(isFiring){
            //Get the percentage of time (0.25 seconds) that has passed
            mPercentagePassed = (System.currentTimeMillis() - mStartTime) / 250.0f;
            //Get the percentage change since the last movement then move the UFO that percentage of the distance is should travel
            mY -= ((mPercentagePassed - mPercentageMoved) * mUFOFireDistance);
            //Set the percentage that the UFO has moved on its upward journey
            mPercentageMoved = mPercentagePassed;
            //If it has moved 100% of its journey stop the firing
            if(mPercentageMoved >= 1) {
                isFiring = false;
                mStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }else if(mStartTime !=0){
            //Get the percentage of time (0.25 seconds) that has passed
            mPercentagePassed = (System.currentTimeMillis() - mStartTime) / 250.0f;
            //Get the percentage change since the last movement then move the UFO that percentage of the distance is should travel
            mY += ((mPercentagePassed - mPercentageMoved) * mUFOFireDistance);
            //Set the percentage that the UFO has moved on its upward journey
            mPercentageMoved = mPercentagePassed;
            //If it has moved 100% of its journey stop the firing
            if(mPercentageMoved >= 1){
                mStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }
    }

    /**
     * Draw function called every game loop
     * @param canvas drawn on
     */
    public void Draw(Canvas canvas){
        canvas.drawBitmap(ufoBitmap, mX, mY, null);
    }

    /**
     * Get X Position
     * @return X
     */
    public int getX() {
        return mX;
    }

    /**
     * Get Y Position
     * @return Y
     */
    public int getY() {
        return mY;
    }
}
