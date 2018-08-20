package com.bgrummitt.engineburn.activities.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bgrummitt.engineburn.activities.gameover.GameOverActivity;
import com.bgrummitt.engineburn.controller.game.EngineBurn;
import com.bgrummitt.engineburn.controller.game.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    final static private String TAG = GameView.class.getSimpleName();
    final static private String SCORE_EXTRA = "SCORE_EXTRA";

    private Context mContext;
    private GameThread thread;
    private EngineBurn game;
    private int[] gameSettings;
    private int gameOverCount;

    public GameView(Context context){
        super(context);

        mContext = context;
        getHolder().addCallback(this);
        gameOverCount = 0;
    }

    /**
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(gameSettings == null) {
            game = new EngineBurn(getResources());
        }else {
            game = new EngineBurn(getResources(), gameSettings[0], gameSettings[1], gameSettings[2]);
        }

        StartThread();
    }

    /**
     * Unused function but needed as function is abstract
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    /**
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        PauseThread();

        gameSettings = game.getSettings();
    }

    /**
     * Override on touch event to intercept any screen interaction that takes place
     * @param event the event that takes place
     * @return the super so we don't get another event on release
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Do something with screen touched
        game.ScreenClicked();

        return super.onTouchEvent(event);
    }

    /**
     * Where any the game's graphics are drawn
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //If the canvas is not empty draw the game
        if(canvas != null){
            game.Draw(canvas);
        }
    }

    /**
     * Start the thread
     */
    public void StartThread(){
        //Create new thread class with the SurfaceHolder and context
        thread = new GameThread(getHolder(), this);
        //Keep the inputs on this thread and not on the new one
        setFocusable(true);
        //Start the games update infinite loop
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Resume a paused thread
     */
    public void ResumeThread(){
        game.resetTiming();
        StartThread();
    }

    /**
     * Function to start a new game
     */
    public void startNewGame(){
        game = new EngineBurn(getResources());
        gameOverCount = 0;
    }

    /**
     * Stop the thread
     */
    public void PauseThread(){
        //To stop the thread it may take a few attempts so we create a while loop
        boolean retry = true;
        while(retry){
            try {
                thread.setRunning(false);
                thread.join();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /**
     * Update function for updating the game every loop
     */
    public void update(){
        game.Update();
        if(game.isGameOver() && gameOverCount == 0){
            Log.d(TAG, "Game Over");
            Intent intent = new Intent(mContext, GameOverActivity.class);
            intent.putExtra(SCORE_EXTRA, game.getScore());
            ((Activity) mContext).startActivityForResult(intent, 3);
            gameOverCount++;
        }
    }

}
