package com.bgrummitt.engineburn.activities.gameover;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bgrummitt.engineburn.R;

import java.util.Locale;

public class GameOverActivity extends Activity {

    final static private String TAG = GameOverActivity.class.getSimpleName();
    final static private String SCORE_EXTRA = "SCORE_EXTRA";

    private int mFinalGameScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set Layout
        setContentView(R.layout.activity_game_over);

        //Get the score that was stored as an extra in the intent
        Intent GameOverStartedIntent = getIntent();
        mFinalGameScore = GameOverStartedIntent.getIntExtra(SCORE_EXTRA, 0);

        setResult(3);

        //Get the Labels and Buttons
        TextView mTextViewScore = findViewById(R.id.ScoreTextView);
        mTextViewScore.setText(String.format(Locale.UK, "%d", mFinalGameScore));

        //Retrieve buttons
        Button mButtonRestartGame = findViewById(R.id.restartGameButtonGameOver);
        Button mButtonHome = findViewById(R.id.HomeButton);

        mButtonRestartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(3);
                finish();
            }
        });

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
    }
}
