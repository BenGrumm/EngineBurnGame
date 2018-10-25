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
import com.bgrummitt.engineburn.activities.leaderboard.LeaderboardActivity;

import java.util.Locale;

public class GameOverActivity extends Activity {

    final static public String SCORE_EXTRA = "SCORE_EXTRA";

    final static private String TAG = GameOverActivity.class.getSimpleName();

    final static public int Leaderboard_Result = 58;

    private Boolean hasSavedLocally = false;
    private Boolean hasSavedGlobally = false;
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
        Button mButtonLeaderboard = findViewById(R.id.leaderboardButton);

        // If the restart game button is click set result to 3 and finish the activity
        mButtonRestartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(3);
                finish();
            }
        });

        // If the home button is click set result to 2 and finish the activity
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });

        // If the leaderboard button is clicked create a new intent with the score from the game, and is the
        // scores have been saved as extras and start the activity looking for Leaderboard_Result (58)
        mButtonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverActivity.this, LeaderboardActivity.class);
                intent.putExtra(SCORE_EXTRA, mFinalGameScore);
                intent.putExtra(LeaderboardActivity.Local_Save_Extra, !hasSavedLocally);
                intent.putExtra(LeaderboardActivity.Global_Save_Extra, !hasSavedGlobally);
                startActivityForResult(intent, GameOverActivity.Leaderboard_Result);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, Integer.toString(resultCode));

        switch (requestCode){
            case Leaderboard_Result:
                hasSavedLocally = !data.getBooleanExtra(LeaderboardActivity.Local_Save_Extra, false);
                hasSavedGlobally = !data.getBooleanExtra(LeaderboardActivity.Global_Save_Extra, false);
                break;
        }
    }
}
