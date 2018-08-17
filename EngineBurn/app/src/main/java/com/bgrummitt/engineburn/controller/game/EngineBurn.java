package com.bgrummitt.engineburn.controller.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.bgrummitt.engineburn.R;

import java.util.ArrayList;
import java.util.List;

public class EngineBurn {

    final static private String TAG = EngineBurn.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private static Bitmap BitmapMoonFloor;
    private static Bitmap BitmapUFO;
    private static int FloorHeight;

    private Boolean isGameOver;
    private UFO  mUFO;
    private List<Obstacle> mObstacles = new ArrayList<>();
    private int mNumberOfScreenPresses;
    private int mScore;
    private Paint mScoreNumberPaint;
    private int mScoreTextSize;

    /**
     * Default Constructor to initiate all classes with beginning settings
     * @param resources the resources from the android directory
     */
    public EngineBurn(Resources resources){

        getBitmaps(resources);

        //Spawn the position in the middle of the screen. The bitmap is painted with the top left at the co-ordinates so the bitmap is moved
        //left half of its width and up half of its height
        mUFO = new UFO(BitmapUFO, (screenWidth / 2) - (BitmapUFO.getWidth() / 2), (screenHeight / 2) - (BitmapUFO.getHeight() / 2), screenHeight / 10);
        //Initialise the starting obstacle
        mObstacles.add(new Obstacle(this, screenWidth + 100 , screenHeight / 2, screenWidth / 20, 100, 500, 0, 400));

        isGameOver = false;
        mNumberOfScreenPresses = 0;
    }

    /**
     * Constructor with settings to game in saved positions
     * @param resources the resources from the android directory
     * @param score the score when the game was stopped
     * @param ufoX x position of the UFO
     * @param ufoY y position of the UFO
     */
    public EngineBurn(Resources resources, int score, int ufoX, int ufoY){

        getBitmaps(resources);

        //Spawn the position in the middle of the screen. The bitmap is painted with the top left at the co-ordinates so the bitmap is moved
        //left half of its width and up half of its height
        mUFO = new UFO(BitmapUFO, ufoX, ufoY, screenHeight / 10);

        isGameOver = false;
        mNumberOfScreenPresses = 0;
        mScore = score;
        setScorePaint();
    }

    /**
     * Function to get the bitmaps if they are currently null
     * @param resources the android resources file
     */
    public void getBitmaps(Resources resources){
        if(BitmapUFO == null) {
            //Get the spaceship bitmaps from the Resources Folder
            BitmapUFO = BitmapFactory.decodeResource(resources, R.mipmap.spaceship_mid_fire);

            //Resize the bitmap to 5% of the height and 11% of the width
            BitmapUFO = Bitmap.createScaledBitmap(BitmapUFO, screenWidth / 9, screenHeight / 20, true);
        }
        if(BitmapMoonFloor == null) {
            FloorHeight = screenHeight - (screenHeight / 10);
            //Get the floor png from the drawable folder
            BitmapMoonFloor = BitmapFactory.decodeResource(resources, R.drawable.moon_floor_simple);
            //Resize the bitmap
            BitmapMoonFloor = Bitmap.createScaledBitmap(BitmapMoonFloor, screenWidth + 100, screenHeight / 10, true);
        }
        mScore = 0;
        setScorePaint();
    }

    /**
     * Function called when the Surface View is clicked
     */
    public void ScreenClicked(){
        if(mNumberOfScreenPresses++ == 0){
            mObstacles.get(0).startMovement();
            mUFO.startMovement();
            mScore = 0;
        }else {
            mUFO.Fire();
        }
    }

    /**
     * Update is called every game loop
     */
    public void Update(){
        if(!isGameOver) {
            mUFO.Update();
            for(Obstacle obstacle : mObstacles)
                obstacle.Update();
        }
        if(!isGameOver && isCollision()){
            isGameOver = true;
        }
    }

    /**
     * Function that checks if the UFO has any collision
     * @return true if UFO collides with floor or pipes
     */
    public Boolean isCollision(){
        if(mUFO.hitsFloor(FloorHeight) || mUFO.collidesWithObstacle(mObstacles)){
            return true;
        }
        return false;
    }

    /**
     * Draw function called every game loop
     * @param canvas the canvas to draw on
     */
    public void Draw(Canvas canvas){
        mUFO.Draw(canvas);
        for(Obstacle obstacle : mObstacles) {
            obstacle.Draw(canvas);
        }
        canvas.drawText(Integer.toString(mScore), screenWidth / 2, screenHeight / 10, mScoreNumberPaint);
        canvas.drawBitmap(BitmapMoonFloor, 0, FloorHeight, null);
    }

    public void setScorePaint(){
        mScoreNumberPaint = new Paint();
        mScoreNumberPaint.setColor(Color.BLACK);
        mScoreNumberPaint.setStyle(Paint.Style.FILL);
        mScoreTextSize = screenWidth / 10;
        mScoreNumberPaint.setTextSize(mScoreTextSize);
        mScoreNumberPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Get all the settings of the game
     * @return Integer array of settings
     */
    public int[] getSettings(){
        return new int[] {mScore, mUFO.getX(), mUFO.getY()};
    }

    /**
     * Start Or Initiate the next obstacle
     */
    public void startNextObstacle(int numberCalled, int gapYPos){
        //As the obstacle has passed the halfway point and the game is still going the obstacle must
        //have been cleared so increase the score by 1
        mScore++;
        //If the obstacle is the last in the list reset the 1st obstacles position
        if(numberCalled == 2){
            mObstacles.get(0).resetPosition(gapYPos);
        }
        //If the list is not full add a new obstacle else reset the next obstacle
        else if(mObstacles.size() != 3) {
            mObstacles.add(new Obstacle(this, screenWidth, (screenHeight / 2),
                    screenWidth / 20, 100, 500, numberCalled + 1, 400));
            mObstacles.get(numberCalled + 1).startMovement();
        }else{
            mObstacles.get(numberCalled + 1).resetPosition(gapYPos);
        }

    }

    /**
     * Reset the timings
     */
    public void resetTiming(){
        mUFO.resetTiming();
        for(Obstacle o : mObstacles) {
            o.resetTiming();
        }
    }

    /**
     * Function to check if game is over
     * @return boolean true if game is over
     */
    public Boolean isGameOver(){
        return isGameOver;
    }

    /**
     * Getter for the games score
     * @return score of the game
     */
    public int getScore(){
        return mScore;
    }
}
