package com.bgrummitt.engineburn.activities.pause;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bgrummitt.engineburn.R;

public class PauseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set Layout
        setContentView(R.layout.activity_pause);

        //Get the buttons
        Button mButtonResumeGame = findViewById(R.id.ResumeGameButton);
        Button mButtonRestartGame = findViewById(R.id.RestartGameButton);
        Button mHomeButton = findViewById(R.id.HomeButton);

        //Set the onclick listener to end activity when pressed.
        mButtonResumeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the result to 0 and finish the activity
                setResult(0);
                finish();
            }
        });
        mButtonRestartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the result to 1 and finish the activity
                setResult(1);
                finish();
            }
        });
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the result to 2 and finish the activity
                setResult(2);
                finish();
            }
        });
    }
}
