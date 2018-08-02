package com.bgrummitt.engineburn.activities.gameover;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bgrummitt.engineburn.R;

public class GameOverActivity extends Activity {

    final static private String TAG = GameOverActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set Layout
        setContentView(R.layout.activity_game_over);

        Button mButtonRestartGame = findViewById(R.id.restartGameButtonGameOver);
        mButtonRestartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(3);
                finish();
            }
        });
    }
}
