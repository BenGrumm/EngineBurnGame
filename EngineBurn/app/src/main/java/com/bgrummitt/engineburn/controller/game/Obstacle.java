package com.bgrummitt.engineburn.controller.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class Obstacle implements Serializable{

    final static private String TAG = Obstacle.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    final static private int obstacleHeight = 500;
    final static private int obstacleWidth = 100;
    final static private int moveSpeed = screenWidth / 20;

    static private int gapSize = screenHeight / 5;

    private EngineBurn mContext;
    private int mX;
    private int mGapY;
    private int mObstacleNumber;
    private Boolean isMoving;
    private Boolean resetNextObstacle;
    private Rect topObstacle;
    private Rect bottomObstacle;
    private Random random;
    private int mGapSize;

    /**
     * Constructor function of Obstacle class
     * @param X starting position
     * @param previousGapY the gap position of the obstacle in front of this one
     * */
    public Obstacle(EngineBurn context, int X, int previousGapY, int obstacleNumber){
        mContext = context;
        mX = X;
        mObstacleNumber = obstacleNumber;
        random = new Random();
        generateNewGap(previousGapY);
        mGapSize = gapSize;
        isMoving = false;
        resetNextObstacle = X < (screenWidth / 2);

        topObstacle = new Rect(mX, (mGapY - (mGapSize / 2)) - obstacleHeight, mX + obstacleWidth, mGapY - (mGapSize / 2));
        bottomObstacle = new Rect(mX, mGapY + (mGapSize / 2), mX + obstacleWidth, mGapY + (mGapSize / 2) + obstacleHeight);
    }

    /**
     * Function to generate the position of the gap in the obstacle
     * @param previousGapY the gap position of the obstacle in front of this one
     */
    public void generateNewGap(int previousGapY){
        int max = previousGapY + (gapSize / 2);
        int min = previousGapY - (gapSize / 2);
        mGapY = random.nextInt((max - min) + 1) + min;
        //If the gap is out of bounds generate a new gapY
        while (mGapY <= (mGapSize) || mGapY >= (screenHeight - mGapSize)){
            mGapY = random.nextInt((max - min) + 1) + min;
        }
        if(gapSize <= screenHeight * 0.8){
            gapSize += 10;
        }
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
            float movement = ((mPercentagePassed - mPercentageMoved) * moveSpeed);
            mX -= movement;
            //Set the percentage that the UFO has moved on its upward journey
            mPercentageMoved = mPercentagePassed;
            //If it has moved 100% of its journey stop the firing
            if (mPercentageMoved >= 1) {
                mStartTime = System.currentTimeMillis();
                mPercentageMoved = 0;
            }
        }
        //Temporary in update while the obstacles are just Rectangles
        //TODO remove and update draw to canvas
        topObstacle.set(mX, 0, mX + obstacleWidth, mGapY - (mGapSize / 2));
        bottomObstacle.set(mX, mGapY + (mGapSize / 2), mX + obstacleWidth, screenHeight);
        if((mX + obstacleWidth) < 0){
            isMoving = false;
        }else if(!resetNextObstacle && (mX + obstacleWidth) < screenWidth / 2){
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
        Paint paintTwo = new Paint(paint);
        canvas.drawRect(topObstacle, paint);
        canvas.drawRect(bottomObstacle, paintTwo);
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
     * @param previousGapY the gap position of the obstacle in front
     */
    public void resetPosition(int previousGapY){
        mX = screenWidth + obstacleWidth;
        generateNewGap(previousGapY);
        resetNextObstacle = false;
        startMovement();
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
    
    public int getObstacleWidth(){
        return obstacleWidth;
    }

    public int getGapSize() {
        return mGapSize;
    }

    public int getX() {
        return mX;
    }

    public int getGapY(){
        return mGapY;
    }

}
