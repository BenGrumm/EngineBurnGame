package com.bgrummitt.engineburn.activities.leaderboard;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

public class LeaderboardActivity extends Activity {

    private Button leaderboardLocalButton;
    private Button leaderboardGlobalButton;
    private RecyclerView leaderboardRecyclerView;
    private TextView leaderboardLabelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

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

        UserScore[] userScoreArray = tempClass.getUserScores();
        leaderboardRecyclerView.setAdapter(new LeaderboardAdapter(this, userScoreArray));

        //Set the layout manager and tell the recycler view that the number of users scores will not change
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        leaderboardRecyclerView.setLayoutManager(layoutManager);
        leaderboardRecyclerView.setHasFixedSize(true);
    }
}
