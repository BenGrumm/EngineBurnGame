package com.bgrummitt.engineburn.activities.character_select;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.main.HomeActivity;
import com.bgrummitt.engineburn.controller.characters.GameCharacter;
import com.bgrummitt.engineburn.controller.database.DataBaseAdapter;

import java.util.Arrays;

public class CharacterSelectionActivity extends AppCompatActivity {

    private GameCharacter[] mCharacters;
    private RecyclerView mRecyclerView;
    private Button mSaveCharacterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_selection);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Get the given intent
        Intent intent = getIntent();

        //Get the Array stored in the intent and return it to a character array
        Parcelable[] parcelables = intent.getParcelableArrayExtra(HomeActivity.CHARACTER_ARRAY);
        mCharacters = Arrays.copyOf(parcelables, parcelables.length, GameCharacter[].class);

        mRecyclerView = findViewById(R.id.recyclerView);
        mSaveCharacterButton = findViewById(R.id.SaveCharacterSelection);
        ImageButton mHomeButton = findViewById(R.id.ReturnHomeImageButton);


        //Set the recycler views adapter
        final CharacterAdapter characterAdapter = new CharacterAdapter(this, mCharacters);
        mRecyclerView.setAdapter(characterAdapter);

        //Set the layout manager and tell the recycler view that the number of characters will not change
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mSaveCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCharacterChoice(characterAdapter.getSelectedCharacter());
                Toast toast = Toast.makeText(CharacterSelectionActivity.this, "Saved", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void saveCharacterChoice(GameCharacter characterChoice){
        if(characterChoice == null)
            return;
        //Set the database to a new Test adapter, create the database and then open it
        DataBaseAdapter mDbHelper = new DataBaseAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();
        mDbHelper.editSetting(DataBaseAdapter.CHARACTER_SKIN_SETTING, characterChoice.getCharacterName());
        mDbHelper.close();
    }

}
