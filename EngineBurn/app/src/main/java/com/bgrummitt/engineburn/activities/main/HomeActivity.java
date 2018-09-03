package com.bgrummitt.engineburn.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.character_select.CharacterSelectionActivity;
import com.bgrummitt.engineburn.controller.characters.GameCharacter;
import com.bgrummitt.engineburn.activities.game.GameActivity;
import com.bgrummitt.engineburn.controller.characters.GameCharacters;

public class HomeActivity extends AppCompatActivity {

    final static public String CHARACTER_ARRAY = "CHARACTER_ARRAY";

    private Button mButtonStartGame;
    private Button mButtonSelectCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mButtonStartGame = findViewById(R.id.startGameButton);
        mButtonSelectCharacter = findViewById(R.id.characterSelectButton);

        mButtonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        mButtonSelectCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCharacterSelectionActivity();
            }
        });
    }

    public void startCharacterSelectionActivity(){
        Intent intent = new Intent(HomeActivity.this, CharacterSelectionActivity.class);
        intent.putExtra(CHARACTER_ARRAY, GameCharacters.getGameCharacters());
        startActivity(intent);
    }

}
