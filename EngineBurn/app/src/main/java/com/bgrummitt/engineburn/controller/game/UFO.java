package com.bgrummitt.engineburn.controller.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.bgrummitt.engineburn.R;

import java.util.List;

public class UFO {

    final static private String TAG = UFO.class.getSimpleName();
    final static private int ANIMATION_FLIP_TIME = 125;

    private int mX;
    private int mY;
    private Bitmap ufoBitmapMax;
    private Bitmap ufoBitmapMin;
    private Bitmap ufoBitmapNone;
    private Bitmap bitmapToDraw;
    private Boolean isFiring;
    private int mUFOFireDistance;
    private int noneWidth;
    private int noneHeight;
    private long startAnimationTime;
    private Boolean isFloating = false;

    /**
     * UFO constructor
     * @param ufoBitmapMax the bitmap picture of the UFO with max fire
     * @param ufoBitmapMin the bitmap picture of the UFO with mid fire
     * @param ufoBitmapNone the bitmap picture of the UFO with no fire
     * @param startingX the X position the ufo will spawn at
     * @param startingY the Y position the ufo will spawn at
     * @param UFOFireDistance the distance that the ufo will go upwards when the screen is pressed
     */
    public UFO(Bitmap ufoBitmapMax, Bitmap ufoBitmapMin, Bitmap ufoBitmapNone, int startingX, int startingY, int UFOFireDistance){
        this.ufoBitmapMax = ufoBitmapMax;
        this.ufoBitmapMin = ufoBitmapMin;
        this.ufoBitmapNone = ufoBitmapNone;
        bitmapToDraw = ufoBitmapMax;

        //Set the co-ordinates
        mX = startingX;
        mY = startingY;

        noneWidth = ufoBitmapNone.getWidth();
        noneHeight = ufoBitmapNone.getHeight();

        //Set the distance the UFO will go up when the screen is clicked
        this.mUFOFireDistance = UFOFireDistance;

        //Initialise the isFiring var
        isFiring = false;

        startAnimationTime = System.currentTimeMillis();
    }

    /**
     * Reset the timing of the fall or boost
     */
    public void resetTiming(){
        //Get the current time and remove the distance moved
        long tempTime = System.currentTimeMillis();
        long tempRemove = (long)(mPercentageMoved * 250L);
        mStartTime = (tempTime - tempRemove);
        startAnimationTime = System.currentTimeMillis();
    }

    /**
     * Function called when the screen is touched
     */
    public void Fire(){
        //Start the firing and set floating to false so if it was it will stop
        isFiring = true;
        isFloating = false;
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
        if(isFloating) {
            //If the UFO has been floating for more that 0.075 seconds stop the floating and reset start time
            if(System.currentTimeMillis() - mStartTime > 2){
                isFloating = false;
                mStartTime = System.currentTimeMillis();
            }
        }else if(isFiring){
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
                isFloating = true;
            }
        }else if(mStartTime != 0){
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
        flipDrawingBitmap();
    }

    /**
     * Function to switch the drawable that is being drawn
     */
    public void flipDrawingBitmap(){
        //If the UFO is firing switch between the low and full firing every ANIMATION_FLIP_TIME seconds
        if(isFiring || mStartTime == 0) {
            if(System.currentTimeMillis() - startAnimationTime > ANIMATION_FLIP_TIME) {
                if (bitmapToDraw == ufoBitmapMax) {
                    bitmapToDraw = ufoBitmapMin;
                } else {
                    bitmapToDraw = ufoBitmapMax;
                }
                startAnimationTime = System.currentTimeMillis();
            }
        }else{
            bitmapToDraw = ufoBitmapNone;
        }
    }

    /**
     * Draw function called every game loop
     * @param canvas drawn on
     */
    public void Draw(Canvas canvas){
        canvas.drawBitmap(bitmapToDraw, mX, mY, null);
    }

    /**
     * Function to check if UFO hits floor
     * @param floorHeight the height at which the floor is
     * @return true if UFO hits floor
     */
    public boolean hitsFloor(int floorHeight){
        if((mY + noneHeight) >= floorHeight){
            return true;
        }
        return false;
    }

    private int obstacleWidth;
    private int gapWidth;
    private int obstacleX;
    private int obstacleGapY;

    /**
     * Function to check if the UFO collides with any of the obstacles
     * @param obstacleList all the obstacles in play
     * @return true if collision detected else false
     */
    public boolean collidesWithObstacle(List<Obstacle> obstacleList){
        obstacleWidth = obstacleList.get(0).getObstacleWidth();
        gapWidth = obstacleList.get(0).getGapSize();
        for(Obstacle obstacle : obstacleList){
            obstacleX = obstacle.getX();
            obstacleGapY = obstacle.getGapY();
            //If the ufo is between the either side of the obstacle and is either higher than the bottom of the top obstacle or lower than the top of bottom obstacle return true
            if((mX + noneWidth) >= obstacleX && (mX <= (obstacleX + obstacleWidth)) && (mY <= obstacleGapY - (gapWidth / 2)
                    || (mY + noneHeight) >= obstacleGapY + (gapWidth / 2)) ){
                return true;
            }
        }
        return false;
    }

    public void startMovement(){
        mStartTime = System.currentTimeMillis();
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
