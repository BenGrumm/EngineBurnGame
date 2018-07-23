package com.bgrummitt.engineburn.controller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class UFO {

    private int mX;
    private int mY;
    private Bitmap ufoBitmap;
    private Boolean isFiring;
    private int mUFOFireDistance;

    public UFO(Bitmap ufoBitmap, int startingX, int startingY, int mUFOFireDistance){
        this.ufoBitmap = ufoBitmap;

        mX = startingX;
        mY = startingY;

        this.mUFOFireDistance = mUFOFireDistance;

        isFiring = false;

        fallStartTime = System.currentTimeMillis();
    }

    public void Fire(){
        isFiring = true;
        mPercentagePassed = 0;
        mPercentageMoved = 0;
        mStartTime = System.currentTimeMillis();
    }

    private float mPercentageMoved;
    private float mPercentagePassed;
    private long mStartTime;
    private long fallStartTime;

    public void Update(){
        if(isFiring){
            //Get the percentage of time that has passed in a form of 0.0
            mPercentagePassed = (System.currentTimeMillis() - mStartTime) / 250.0f;
            //Get the percentage of time that has passed since the last measurement and move the bird that percentage of the firing distance
            mY -= ((mPercentagePassed - mPercentageMoved) * mUFOFireDistance);
            //Set the percentage that the UFO has moved on its upward journey
            mPercentageMoved = mPercentagePassed;
            //If it has moved 100% of its journey stop the firing
            if(mPercentageMoved >= 1) {
                isFiring = false;
                mStartTime = System.currentTimeMillis();
                fallStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }else if(fallStartTime !=0){
            mPercentagePassed = (System.currentTimeMillis() - fallStartTime) / 250.0f;
            mY += ((mPercentagePassed - mPercentageMoved) * mUFOFireDistance);
            mPercentageMoved = mPercentagePassed;
            if(mPercentageMoved >= 1){
                fallStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }
    }

    public void Draw(Canvas canvas){
        canvas.drawBitmap(ufoBitmap, mX, mY, null);
    }

}
