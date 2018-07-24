package com.bgrummitt.engineburn.activities.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bgrummitt.engineburn.controller.game.EngineBurn;
import com.bgrummitt.engineburn.controller.game.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameView.class.getSimpleName();

    private GameThread thread;
    private EngineBurn game;

    public GameView(Context context){
        super(context);

        getHolder().addCallback(this);
    }

    /**
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Create new thread class with the SurfaceHolder and context
        thread = new GameThread(getHolder(), this);
        //Keep the inputs on this thread and not on the new one
        setFocusable(true);

        game = new EngineBurn(getResources());

        //Start the games update infinite loop
        thread.setRunning(true);
        thread.start();
    }

    /**
     *
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    /**
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
            canvas.drawColor(Color.WHITE);
            game.Draw(canvas);
        }
    }

    /**
     * Update function for updating the game every loop
     */
    public void update(){
        game.Update();
    }

}
