package com.bgrummitt.engineburn.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.game.GameActivity;

public class HomeActivity extends AppCompatActivity {

    private Button mButtonStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mButtonStartGame = findViewById(R.id.startGameButton);

        mButtonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
