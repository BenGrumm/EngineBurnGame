package com.bgrummitt.engineburn.activities.character_select;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.main.HomeActivity;

import java.util.Arrays;
import java.util.List;

public class CharacterSelectionActivity extends AppCompatActivity {

    private Characters[] mCharacters;
    private RecyclerView mRecyclerView;

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
        mCharacters = Arrays.copyOf(parcelables, parcelables.length, Characters[].class);

        mRecyclerView = findViewById(R.id.recyclerView);

        //Set the recycler views adapter
        CharacterAdapter characterAdapter = new CharacterAdapter(this, mCharacters);
        mRecyclerView.setAdapter(characterAdapter);

        //Set the layout manager and tell the recycler view that the number of characters will not change
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

}
