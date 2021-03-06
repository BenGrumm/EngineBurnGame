package com.bgrummitt.engineburn.activities.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bgrummitt.engineburn.activities.gameover.GameOverActivity;
import com.bgrummitt.engineburn.controller.characters.GameCharacter;
import com.bgrummitt.engineburn.controller.characters.GameCharacters;
import com.bgrummitt.engineburn.controller.database.DataBaseAdapter;
import com.bgrummitt.engineburn.controller.database.DataBaseSettingsAdapter;
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
        GameCharacter CurrentGameCharacter = getCurrentCharacter();
        if(gameSettings == null) {
            game = new EngineBurn(getResources(), CurrentGameCharacter);
        }else {
            int[] tempArr = new int[gameSettings.length - 3];
            for(int i = 3; i < gameSettings.length; i++){
                tempArr[i - 3] = gameSettings[i];
            }
            game = new EngineBurn(getResources(), gameSettings[0], gameSettings[1], gameSettings[2], tempArr, CurrentGameCharacter);
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
        game = new EngineBurn(getResources(), getCurrentCharacter());
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
            Log.v(TAG, "Game Over");
            //Create a new Intent with the score as an extra
            Intent intent = new Intent(mContext, GameOverActivity.class);
            intent.putExtra(SCORE_EXTRA, game.getScore());
            ((Activity) mContext).startActivityForResult(intent, 3);
            gameOverCount++;
        }
    }

    /**
     * Get the game character that has been saved in the user settings
     * @return the saved game character
     */
    public GameCharacter getCurrentCharacter(){
        //Create new database adapter passing context
        DataBaseAdapter mDbHelper = new DataBaseAdapter(mContext, DataBaseSettingsAdapter.DB_TABLE_NAME_SETTINGS);
        //Create and open database
        mDbHelper.createDatabase();
        mDbHelper.open();
        //Get the setting from the database passing the Final variable for the character in the DatabaseAdapter
        // getDBEntry(String columnToReturn, String columnComparingNameTo, String entryNameToFind)
        String characterName = mDbHelper.getDBEntry(DataBaseSettingsAdapter.SETTING_SETTING_COLUMN, DataBaseSettingsAdapter.SETTING_NAME_COLUMN, DataBaseSettingsAdapter.CHARACTER_SKIN_SETTING);
        mDbHelper.close();

        //Using the name return the character from the game characters getCharacter static variable
        return GameCharacters.getCharacter(characterName);
    }

}
