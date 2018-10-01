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

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

public class LeaderboardActivity extends Activity {

    private static final int selectedLeaderboardColour = Color.BLUE;
    private static final int deSelectedLeaderboardColour = Color.RED;
    private Button leaderboardLocalButton;
    private Button leaderboardGlobalButton;
    private RecyclerView leaderboardRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardLocalButton = findViewById(R.id.localButton);
        leaderboardGlobalButton = findViewById(R.id.globalButton);
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        final Drawable clicked = getResources().getDrawable(R.drawable.button_clicked);
        final Drawable notClicked = getResources().getDrawable(R.drawable.button_not_clicked);

        leaderboardLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderboardLocalButton.setBackground(clicked);
                leaderboardGlobalButton.setBackground(notClicked);
            }
        });

        leaderboardGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
