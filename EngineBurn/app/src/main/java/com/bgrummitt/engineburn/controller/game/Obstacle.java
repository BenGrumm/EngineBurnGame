package com.bgrummitt.engineburn.controller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Obstacle {

    private static int gapDistanceMax;
    private static int obstacleMoveDistance;
    private static Bitmap topObstacle;
    private static Bitmap bottomObstacle;

    private int mX;
    private int mGapY;

    public Obstacle(int X, int previousGapY, int givenObstacleMoveDistance){
        mX = X;
        generateNewGap(previousGapY);
        obstacleMoveDistance = givenObstacleMoveDistance;
    }

    public void generateNewGap(int previousGapY){
        mGapY = previousGapY;
    }

    private float mPercentageMoved;
    private float mPercentagePassed;
    private long mStartTime;

    public void update(){
        //Get the percentage of time (0.25 seconds) that has passed
        mPercentagePassed = (System.currentTimeMillis() - mStartTime) / 250.0f;
        //Get the percentage change since the last movement then move the UFO that percentage of the distance is should travel
        mX += ((mPercentagePassed - mPercentageMoved) * obstacleMoveDistance);
        //Set the percentage that the UFO has moved on its upward journey
        mPercentageMoved = mPercentagePassed;
        //If it has moved 100% of its journey stop the firing
        if(mPercentageMoved >= 1){
            mStartTime = System.currentTimeMillis();
            mPercentageMoved = 0;
        }
    }

    public void draw(Canvas canvas){

    }

}
