package com.bgrummitt.engineburn.controller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class UFO {

    private int mX;
    private int mY;
    private Bitmap ufoBitmap;
    private Boolean isFiring;
    private int mUFOFireDistance;

    public UFO(Bitmap ufoBitmap, int startingX, int startingY, int UFOFireDistance){
        this.ufoBitmap = ufoBitmap;

        //Set the co-ordinates
        mX = startingX;
        mY = startingY;

        //Set the distance the UFO will go up when the screen is clicked
        this.mUFOFireDistance = UFOFireDistance;

        //Initialise the isFiring var
        isFiring = false;

        //Initialise the start time
        mFallStartTime = System.currentTimeMillis();

    }

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
    private long mFallStartTime;

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
                mFallStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }else if(mFallStartTime !=0){
            //Get the percentage of time (0.25 seconds) that has passed
            mPercentagePassed = (System.currentTimeMillis() - mFallStartTime) / 250.0f;
            //Get the percentage change since the last movement then move the UFO that percentage of the distance is should travel
            mY += ((mPercentagePassed - mPercentageMoved) * mUFOFireDistance);
            //Set the percentage that the UFO has moved on its upward journey
            mPercentageMoved = mPercentagePassed;
            //If it has moved 100% of its journey stop the firing
            if(mPercentageMoved >= 1){
                mFallStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(ufoBitmap, mX, mY, null);
    }

}
