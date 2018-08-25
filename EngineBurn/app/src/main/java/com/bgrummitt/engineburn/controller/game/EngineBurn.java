package com.bgrummitt.engineburn.controller.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.bgrummitt.engineburn.R;

import java.util.ArrayList;
import java.util.List;

public class EngineBurn {

    final static private String TAG = EngineBurn.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private static Bitmap BitmapMoonFloor;
    private static Bitmap BitmapUFOMin;
    private static Bitmap BitmapUFONone;
    private static Bitmap BitmapUFOMax;
    private static Bitmap BitmapBackground;
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
        initialiseObjects((screenWidth / 2) - (BitmapUFOMax.getWidth() / 2), (screenHeight / 2) - (BitmapUFOMax.getHeight() / 2));
        initialiseObstacles(new int[]{(screenWidth + 100), screenHeight / 2}, false);

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
    public EngineBurn(Resources resources, int score, int ufoX, int ufoY, int[] obstacleSettings){

        getBitmaps(resources);
        initialiseObjects(ufoX, ufoY);
        initialiseObstacles(obstacleSettings, true);

        isGameOver = false;
        mNumberOfScreenPresses = 1;
        resetTiming();
        mScore = score;
    }

    public void initialiseObjects(int ufoX, int ufoY){
        //Spawn UFO at given x and y with the distance travelled when pressed 10% of screen height
        mUFO = new UFO(BitmapUFOMax, BitmapUFOMin, BitmapUFONone, ufoX, ufoY, screenHeight / 10);

        //Remove bitmaps
        BitmapUFOMax = null;
        BitmapUFOMin = null;
        BitmapUFONone = null;
        System.gc();
    }

    public void initialiseObstacles(int[] obstacleSettingArr, Boolean startMovement){
        for(int i = 0; i < obstacleSettingArr.length / 2; i++){
            Log.d(TAG, "Number : " + i + ": obstacle settings arr : " + (i * 2) + " X = " + obstacleSettingArr[(i * 2)]);
            mObstacles.add(new Obstacle(this, obstacleSettingArr[(i * 2)], obstacleSettingArr[(i * 2)+1], i, obstacleSettingArr[(i * 2)+1] ));
            if(startMovement) {
                mObstacles.get(i).startMovement();
            }
        }
    }

    /**
     * Function to get the bitmaps if they are currently null
     * @param resources the android resources file
     */
    public void getBitmaps(Resources resources){
        if(BitmapUFOMax == null) {
            //Get the spaceship bitmaps from the Resources Folder
            BitmapUFOMax = BitmapFactory.decodeResource(resources, R.drawable.spaceship_max);
            BitmapUFOMin = BitmapFactory.decodeResource(resources, R.drawable.spaceship_min);
            BitmapUFONone = BitmapFactory.decodeResource(resources, R.drawable.spaceship_no);

            float percentageMinMax = (float) BitmapUFOMin.getHeight() / BitmapUFOMax.getHeight();
            float percentageNoneMax = (float) BitmapUFONone.getHeight() / BitmapUFOMax.getHeight();

            //Resize the bitmap to 5% of the height and 11% of the width
            BitmapUFOMax = Bitmap.createScaledBitmap(BitmapUFOMax, screenWidth / 9, screenHeight / 20, true);
            BitmapUFOMin = Bitmap.createScaledBitmap(BitmapUFOMin, screenWidth / 9, (int)((screenHeight / 20) * percentageMinMax), true);
            BitmapUFONone = Bitmap.createScaledBitmap(BitmapUFONone, screenWidth / 9, (int)((screenHeight / 20) * percentageNoneMax), true);
        }
        if(BitmapMoonFloor == null) {
            FloorHeight = screenHeight - (screenHeight / 10);
            //Get the floor png from the drawable folder
            BitmapMoonFloor = BitmapFactory.decodeResource(resources, R.drawable.moon_floor_simple);
            //Resize the bitmap
            BitmapMoonFloor = Bitmap.createScaledBitmap(BitmapMoonFloor, screenWidth + 100, screenHeight / 10, true);
        }
        if(BitmapBackground == null){
            BitmapBackground = BitmapFactory.decodeResource(resources, R.mipmap.stars_background);
            BitmapBackground = Bitmap.createScaledBitmap(BitmapBackground, screenWidth, screenHeight, true);
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
        //Draw a black background
        canvas.drawColor(Color.BLACK);
        //Draw the bitmap background
        canvas.drawBitmap(BitmapBackground, 0, 0, null);
        mUFO.Draw(canvas);
        for(Obstacle obstacle : mObstacles)
            obstacle.Draw(canvas);
        //Draw Score and floor
        canvas.drawText(Integer.toString(mScore), screenWidth / 2, screenHeight / 10, mScoreNumberPaint);
        canvas.drawBitmap(BitmapMoonFloor, 0, FloorHeight, null);
    }

    public void setScorePaint(){
        mScoreNumberPaint = new Paint();
        mScoreNumberPaint.setColor(Color.WHITE);
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
        int[] settingsArr = new int[3 + (2 * mObstacles.size())];
        settingsArr[0] = mScore;
        settingsArr[1] = mUFO.getX();
        settingsArr[2] = mUFO.getY();
        int count = 0;
        for(int i = 3; i < settingsArr.length; i+=2){
            Log.d(TAG, "Saving Number : " + count);
            settingsArr[i] = mObstacles.get(count).getX();
            settingsArr[i + 1] = mObstacles.get(count).getGapY();
            count++;
        }
        return settingsArr;
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
            mObstacles.add(new Obstacle(this, screenWidth, (screenHeight / 2),numberCalled + 1, 0));
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
