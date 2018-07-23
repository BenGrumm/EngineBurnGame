package com.bgrummitt.engineburn.controller.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.bgrummitt.engineburn.activities.game.GameView;

public class GameThread extends Thread {

    private final String TAG = GameThread.class.getSimpleName();

    private final SurfaceHolder surfaceHolder;
    private GameView gameView;
    private Boolean running;
    private static Canvas canvas;
    private int targetFPS = 60;
    private double averageFPS;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        //super the thread class
        super();

        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    //Main game loop
    @Override
    public void run() {

        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / targetFPS;

        while(running){

            startTime = System.nanoTime();

            canvas = null;

            try{
                //Surface is locked for rendering and a fresh canvas instance is returned that we can use.
                canvas = this.surfaceHolder.lockCanvas();

                synchronized (surfaceHolder){
                    //Update element positions
                    this.gameView.update();
                    //Draw the new images
                    this.gameView.draw(canvas);
                }
            }catch (Exception e){ } finally {
                //Finally will make sure this code executes even if there is an exception
                if(canvas != null){
                    try{
                        //This canvas must be one receive from .lockCanvas()
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            } catch (Exception e) {}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS){
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                Log.v(TAG, Double.toString(averageFPS));
            }

        }
        super.run();
    }

    public void setRunning(boolean isRunning){
        this.running = isRunning;
    }

}