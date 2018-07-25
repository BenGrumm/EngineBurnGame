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
import com.bgrummitt.engineburn.activities.pause.PauseActivity;

public class GameActivity extends Activity {

    final static private String TAG = GameActivity.class.getSimpleName();
    final static private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final static private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private GameView gameSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar programmatically
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Lock The Screen Orientation In Portrait Mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Create a layout on top of the surface to have button in the game
        FrameLayout game = new FrameLayout(this);
        gameSurface = new GameView(this);
        ConstraintLayout gameWidgets = new ConstraintLayout(this);

        //Create new Image button
        ImageButton pauseGameButton = new ImageButton(this);
        //Retrieve the pause button from the android resources
        pauseGameButton.setImageResource(android.R.drawable.ic_media_pause);
        //Remove the buttons background
        pauseGameButton.setBackgroundResource(0);

        //Add the button to the constraint layout
        gameWidgets.addView(pauseGameButton);

        //Add the layouts to the main Frame layout
        game.addView(gameSurface);
        game.addView(gameWidgets);

        //Set The Content View To A New Game
        setContentView(game);

        pauseGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Button Pressed");
                gameSurface.PauseThread();
                Intent intent = new Intent(GameActivity.this, PauseActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        gameSurface.ResumeThread();
    }
}
