package com.bgrummitt.engineburn.activities.leaderboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.gameover.GameOverActivity;
import com.bgrummitt.engineburn.controller.database.DataBaseLocalLeaderboardAdapter;
import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

public class LeaderboardActivity extends Activity {

    final static private String TAG = LeaderboardActivity.class.getSimpleName();

    private Button leaderboardLocalButton;
    private Button leaderboardGlobalButton;
    private RecyclerView leaderboardRecyclerView;
    private TextView leaderboardLabelTextView;
    private ConstraintLayout leaderboardConstraintLayout;

    private int userScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Get the intent and get the score, if the score does not exist set the score to -1
        Intent intent = getIntent();
        userScore = intent.getIntExtra(GameOverActivity.SCORE_EXTRA, -1);

        // Retrieve all the id's
        leaderboardConstraintLayout = findViewById(R.id.leaderboardLayout);
        leaderboardLocalButton = findViewById(R.id.localButton);
        leaderboardGlobalButton = findViewById(R.id.globalButton);
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardLabelTextView = findViewById(R.id.recyclerViewLabel);

        // Get the size of the window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set the size of this activity
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));

        // Center the activity
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Save the positioning
        getWindow().setAttributes(params);

        // Get the button layouts for when there label is the selected leaderboard and not
        final Drawable clicked = getResources().getDrawable(R.drawable.button_clicked);
        final Drawable notClicked = getResources().getDrawable(R.drawable.button_not_clicked);

        // On Click Listener for the local leaderboard button
        leaderboardLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rename label and flip button backgrounds
                leaderboardLabelTextView.setText("Local Leaderboard");
                leaderboardLocalButton.setBackground(clicked);
                leaderboardGlobalButton.setBackground(notClicked);
            }
        });

        // On Click Listener for the global button
        leaderboardGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rename label and flip button backgrounds
                leaderboardLabelTextView.setText("Global Leaderboard");
                leaderboardGlobalButton.setBackground(clicked);
                leaderboardLocalButton.setBackground(notClicked);
            }
        });

        UserScore[] userScoreArray = GetLocalScores();
        leaderboardRecyclerView.setAdapter(new LeaderboardAdapter(this, userScoreArray));

        if(userScore > 0){
            addScoreSaveFunctionality();
        }

        //Set the layout manager and tell the recycler view that the number of users scores will not change
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        leaderboardRecyclerView.setLayoutManager(layoutManager);
        leaderboardRecyclerView.setHasFixedSize(true);
    }

    /**
     * Function to add a button to the activity so the score can be saved.
     */
    private void addScoreSaveFunctionality(){
        ConstraintSet set = new ConstraintSet();

        // Create a new button
        Button saveScoreButton = new Button(this);
        saveScoreButton.setText("Save Score");
        saveScoreButton.setId(View.generateViewId());
        // Add the button to the view
        leaderboardConstraintLayout.addView(saveScoreButton);

        // Clone the layout
        set.clone(leaderboardConstraintLayout);

        // Remove the recycler views bottom constraint
        set.clear(leaderboardRecyclerView.getId(), ConstraintSet.BOTTOM);

        // Constrain th button to the bottom middle of the activity
        set.connect(leaderboardRecyclerView.getId(), ConstraintSet.BOTTOM, saveScoreButton.getId(), ConstraintSet.TOP, 0);
        set.connect(saveScoreButton.getId(), ConstraintSet.BOTTOM, leaderboardConstraintLayout.getId(),  ConstraintSet.BOTTOM, 0);
        set.connect(saveScoreButton.getId(), ConstraintSet.LEFT, leaderboardConstraintLayout.getId(),  ConstraintSet.LEFT, 0);
        set.connect(saveScoreButton.getId(), ConstraintSet.RIGHT, leaderboardConstraintLayout.getId(),  ConstraintSet.RIGHT, 0);

        // Apply this new layout
        set.applyTo(leaderboardConstraintLayout);
    }

    /**
     * Function to get the top scores from the database
     * @return top user scores
     */
    private UserScore[] GetLocalScores(){
        // Open and create database
        DataBaseLocalLeaderboardAdapter mDb = new DataBaseLocalLeaderboardAdapter(this);
        mDb.createDatabase();
        mDb.open();
        // Retrieve scores and close db and return
        UserScore[] userScores = mDb.getTopScores();
        mDb.close();
        return userScores;
    }

}
