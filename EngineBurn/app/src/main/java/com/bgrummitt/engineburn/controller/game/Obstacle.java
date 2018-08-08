package com.bgrummitt.engineburn.controller.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Obstacle {

    final static private String TAG = Obstacle.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    static private int gapDistanceMax;
    static private int obstacleMoveDistance;
    static private int obstacleWidthClass;
    static private int obstacleHeightClass;

    private EngineBurn mContext;
    private int mX;
    private int mGapY;
    private int mObstacleNumber;
    private Boolean isMoving;
    private Boolean resetNextObstacle;
    private Rect topObstacle;
    private Bitmap bottomObstacle;

    /**
     * Constructor function of Obstacle class
     * @param X starting position
     * @param previousGapY the gap position of the obstacle in front of this one
     * @param givenObstacleMoveDistance the distance the obstacle will move in 0.25 seconds
     * @param obstacleWidth the width of the obstacle on the screen
     * @param obstacleHeight the height of the obstacle
     */
    public Obstacle(EngineBurn context, int X, int previousGapY, int givenObstacleMoveDistance, int obstacleWidth, int obstacleHeight, int obstacleNumber){
        mContext = context;
        mX = X;
        mObstacleNumber = obstacleNumber;
        generateNewGap(previousGapY);
        obstacleMoveDistance = givenObstacleMoveDistance;
        isMoving = resetNextObstacle = false;

        if(topObstacle == null || bottomObstacle == null){
            obstacleHeightClass = obstacleHeight;
            obstacleWidthClass = obstacleWidth;
            topObstacle = new Rect(X, 0, obstacleWidth, obstacleHeight);
        }
    }

    /**
     * Function to generate the position of the gap in the obstacle
     * @param previousGapY the gap position of the obstacle in front of this one
     */
    public void generateNewGap(int previousGapY){
        mGapY = previousGapY;
    }

    private float mPercentageMoved;
    private float mPercentagePassed;
    private long mStartTime;

    /**
     * Function to update the position of the obstacle
     */
    public void Update(){
        if(isMoving) {
            //Get the percentage of time (0.25 seconds) that has passed
            mPercentagePassed = (System.currentTimeMillis() - mStartTime) / 250.0f;
            //Get the percentage change since the last movement then move the UFO that percentage of the distance is should travel
            mX -= ((mPercentagePassed - mPercentageMoved) * obstacleMoveDistance);
            //Set the percentage that the UFO has moved on its upward journey
            mPercentageMoved = mPercentagePassed;
            //If it has moved 100% of its journey stop the firing
            if (mPercentageMoved >= 1) {
                mStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }
        topObstacle.set(mX, 0, mX + obstacleWidthClass, obstacleHeightClass);
        if((mX + obstacleWidthClass) < 0){
            isMoving = false;
        }else if(!resetNextObstacle && (mX + obstacleWidthClass) < screenWidth / 2){
            mContext.startNextObstacle(mObstacleNumber, mGapY);
            resetNextObstacle = true;
        }
    }

    /**
     * Function to draw the obstacle onto the canvas
     * @param canvas to draw the obstacle onto
     */
    public void Draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        canvas.drawRect(topObstacle, paint);
    }

    /**
     * Start the movement of the obstacle
     */
    public void startMovement(){
        isMoving = true;
        mStartTime = System.currentTimeMillis();
        mPercentageMoved = 0;
        mPercentagePassed = 0;
    }

    /**
     * Reset the position of the obstacle
     * @param previousGapY
     */
    public void resetPosition(int previousGapY){
        mX = screenWidth + obstacleWidthClass;
        generateNewGap(previousGapY);
        resetNextObstacle = false;
        startMovement();
    }

}
