package com.bgrummitt.engineburn.controller.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import com.bgrummitt.engineburn.R;

public class EngineBurn {

    final static private String TAG = EngineBurn.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private Boolean isGameOver;
    private UFO  mUFO;
    private static Bitmap BitmapMoonFloor;
    private static Bitmap BitmapUFO;

    private static int FloorHeight;

    /**
     * Default Constructor to initiate all classes with beginning settings
     * @param resources the resources from the android directory
     */
    public EngineBurn(Resources resources){

        getBitmaps(resources);

        //Spawn the position in the middle of the screen. The bitmap is painted with the top left at the co-ordinates so the bitmap is moved
        //left half of its width and up half of its height
        mUFO = new UFO(BitmapUFO, (screenWidth / 2) - (BitmapUFO.getWidth() / 2), (screenHeight / 2) - (BitmapUFO.getHeight() / 2), screenHeight / 10);

        isGameOver = false;
    }

    /**
     * Constructor with settings to game in saved positions
     * @param resources the resources from the android directory
     * @param ufoX x position of the UFO
     * @param ufoY y position of the UFO
     */
    public EngineBurn(Resources resources, int ufoX, int ufoY){

        getBitmaps(resources);

        //Spawn the position in the middle of the screen. The bitmap is painted with the top left at the co-ordinates so the bitmap is moved
        //left half of its width and up half of its height
        mUFO = new UFO(BitmapUFO, ufoX, ufoY, screenHeight / 10);

        isGameOver = false;
    }

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
            BitmapMoonFloor = Bitmap.createScaledBitmap(BitmapMoonFloor, screenWidth, screenHeight / 10, true);
        }
    }

    /**
     * Function called when the Surface View is clicked
     */
    public void ScreenClicked(){
        mUFO.Fire();
    }

    /**
     * Update is called every game loop
     */
    public void Update(){
        if(!isGameOver) {
            mUFO.Update();
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
        if(mUFO.hitsFloor(FloorHeight)){
            return true;
        }
        return false;
    }

    /**
     * Draw function called every game loop
     * @param canvas the canvas to draw on
     */
    public void Draw(Canvas canvas){
        canvas.drawBitmap(BitmapMoonFloor, 0, FloorHeight, null);
        mUFO.Draw(canvas);
    }

    /**
     * Get all the settings of the game
     * @return Integer array of settings
     */
    public int[] getSettings(){
        return new int[] {mUFO.getX(), mUFO.getY()};
    }

    /**
     * Reset the timings
     */
    public void resetTiming(){
        mUFO.resetTiming();
    }

    /**
     * Function to check if game is over
     * @return boolean true if game is over
     */
    public Boolean isGameOver(){
        return isGameOver;
    }

}
