package com.bgrummitt.engineburn.activities.leaderboard;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bgrummitt.engineburn.R;

public class LeaderboardActivity extends Activity {

    private Button leaderboardLocalButton;
    private Button leaderboardGlobalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardLocalButton = findViewById(R.id.localButton);
        leaderboardGlobalButton = findViewById(R.id.globalButton);

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

        leaderboardGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderboardGlobalButton.setPressed(true);
                leaderboardLocalButton.setPressed(true);
            }
        });

        leaderboardLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderboardLocalButton.setPressed(true);
                leaderboardGlobalButton.setPressed(false);
            }
        });

    }
}
