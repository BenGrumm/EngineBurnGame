package com.bgrummitt.engineburn.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.character_select.CharacterSelectionActivity;
import com.bgrummitt.engineburn.activities.character_select.Characters;
import com.bgrummitt.engineburn.activities.game.GameActivity;

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
        intent.putExtra(CHARACTER_ARRAY, getCharacterArray());
        startActivity(intent);
    }

    public Characters[] getCharacterArray(){
        Characters[] characters = new Characters[5];
        characters[0] = new Characters("Default UFO", R.drawable.default_ufo, R.drawable.default_ufo_low_fire, R.drawable.default_ufo_no_fire);
        characters[1] = new Characters("Red UFO", R.drawable.red_ufo, R.drawable.red_ufo_low_fire, R.drawable.red_ufo_no_fire);
        characters[2] = new Characters("Green UFO", R.drawable.green_ufo, R.drawable.green_ufo_low_fire, R.drawable.green_ufo_no_fire);
        characters[3] = new Characters("Blue UFO", R.drawable.blue_ufo, R.drawable.blue_ufo_low_fire, R.drawable.blue_ufo_no_fire);
        characters[4] = new Characters("Yellow UFO", R.drawable.yellow_ufo, R.drawable.yellow_ufo_low_fire, R.drawable.yellow_ufo_no_fire);
        return characters;
    }

}
