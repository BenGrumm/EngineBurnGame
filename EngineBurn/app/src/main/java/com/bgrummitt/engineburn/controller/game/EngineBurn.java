package com.bgrummitt.engineburn.controller.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.bgrummitt.engineburn.R;

public class EngineBurn {

    final static private String TAG = EngineBurn.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private UFO mUFO;

    /**
     * Default Constructor to initiate all classes with beginning settings
     * @param resources the resources from the android directory
     */
    public EngineBurn(Resources resources){

        //Get the spaceship bitmaps from the Resources Folder
        Bitmap ufoBitmap = BitmapFactory.decodeResource(resources, R.mipmap.spaceship_mid_fire);

        //Resize the bitmap to 5% of the height and 11% of the width
        ufoBitmap =  Bitmap.createScaledBitmap(ufoBitmap, screenWidth / 9, screenHeight / 20, true);

        //Spawn the position in the middle of the screen. The bitmap is painted with the top left at the co-ordinates so the bitmap is moved
        //left half of its width and up half of its height
        mUFO = new UFO(ufoBitmap, (screenWidth / 2) - (ufoBitmap.getWidth() / 2), (screenHeight / 2) - (ufoBitmap.getHeight() / 2), screenHeight / 10);

    }

    /**
     * Constructor with settings to game in saved positions
     * @param resources the resources from the android directory
     * @param ufoX x position of the UFO
     * @param ufoY y position of the UFO
     */
    public EngineBurn(Resources resources, int ufoX, int ufoY){

        //Get the spaceship bitmaps from the Resources Folder
        Bitmap ufoBitmap = BitmapFactory.decodeResource(resources, R.mipmap.spaceship_mid_fire);

        //Resize the bitmap to 5% of the height and 11% of the width
        ufoBitmap =  Bitmap.createScaledBitmap(ufoBitmap, screenWidth / 9, screenHeight / 20, true);

        //Spawn the position in the middle of the screen. The bitmap is painted with the top left at the co-ordinates so the bitmap is moved
        //left half of its width and up half of its height
        mUFO = new UFO(ufoBitmap, ufoX, ufoY, screenHeight / 10);

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
        mUFO.Update();
    }

    /**
     * Draw function called every game loop
     * @param canvas the canvas to draw on
     */
    public void Draw(Canvas canvas){
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

}
