package com.bgrummitt.engineburn.activities.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.gameover.GameOverActivity;
import com.bgrummitt.engineburn.activities.pause.PauseActivity;

public class GameActivity extends Activity {

    final static private String TAG = GameActivity.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private GameView mGameSurface;
    private ImageButton mImageButtonPauseGame;
    private FrameLayout mGame;
    private ConstraintLayout mGameWidgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar programmatically
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Lock The Screen Orientation In Portrait Mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Create the game
        createNewGame();
        setContentView(mGame);
    }

    public void createNewGame(){
        //Create a layout on top of the surface to have button in the game
        mGame = new FrameLayout(this);
        mGameSurface = new GameView(this);
        mGameWidgets = new ConstraintLayout(this);

        //Create new Image button
        mImageButtonPauseGame = new ImageButton(this);
        //Retrieve the pause button from the android resources
        mImageButtonPauseGame.setImageResource(android.R.drawable.ic_media_pause);
        //Remove the buttons background
        mImageButtonPauseGame.setBackgroundResource(0);

        //Add the button to the constraint layout
        mGameWidgets.addView(mImageButtonPauseGame);

        //Add the layouts to the main Frame layout
        mGame.addView(mGameSurface);
        mGame.addView(mGameWidgets);

        mImageButtonPauseGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Button Pressed");
                mGameSurface.PauseThread();
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        setContentView(mGame);
    }

    public void gameOver(){
        startEndGame();
        mGameSurface.PauseThread();
    }

    public void startEndGame(){
        Log.d(TAG, "Starting Intent");
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the returned code is 0 resume the game if it is 1 restart the game
        if(resultCode == 0) {
            mGameSurface.ResumeThread();
        }else if(resultCode == 1){
            createNewGame();
        }else if(resultCode == 2){
            finish();
        }else if(resultCode == 3){
            mGameSurface.startNewGame();
        }
    }
}
