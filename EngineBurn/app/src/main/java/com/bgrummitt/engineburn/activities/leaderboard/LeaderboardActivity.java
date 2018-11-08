package com.bgrummitt.engineburn.activities.leaderboard;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bgrummitt.engineburn.R;
import com.bgrummitt.engineburn.activities.gameover.GameOverActivity;
import com.bgrummitt.engineburn.controller.database.DataBaseLocalLeaderboardAdapter;
import com.bgrummitt.engineburn.controller.leaderboard.UserScore;
import com.bgrummitt.engineburn.controller.other.LowScoreException;
import com.bgrummitt.engineburn.controller.server.connection.Client;

import java.io.IOException;

public class LeaderboardActivity extends Activity {

    final static private String TAG = LeaderboardActivity.class.getSimpleName();
    final static public String Local_Save_Extra = "Can_Local_Save_Extra";
    final static public String Global_Save_Extra = "Can_Global_Save_Extra";

    private String leaderBoardType = "Local";
    private Boolean canLocalSave;
    private Boolean canGlobalSave;
    private Boolean isButtonActive = false;

    private Intent returnIntent;

    private Button leaderboardLocalButton;
    private Button leaderboardGlobalButton;
    private RecyclerView leaderboardRecyclerView;
    private TextView leaderboardLabelTextView;
    private ConstraintLayout leaderboardConstraintLayout;
    private Button saveScoreButton;
    private Drawable clicked;
    private Drawable notClicked;

    private UserScore[] localScoresArray;
    private UserScore[] globalScoresArray;
    private int userScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Get the intent and get the score, if the score does not exist set the score to -1
        Intent intent = getIntent();
        userScore = intent.getIntExtra(GameOverActivity.SCORE_EXTRA, -1);
        canLocalSave = intent.getBooleanExtra(Local_Save_Extra, false);
        canGlobalSave = intent.getBooleanExtra(Global_Save_Extra, false);

        // Initialise the return intent
        returnIntent = new Intent();
        returnIntent.putExtra(Local_Save_Extra, canLocalSave);
        returnIntent.putExtra(Global_Save_Extra, canGlobalSave);

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
        clicked = getResources().getDrawable(R.drawable.button_clicked);
        notClicked = getResources().getDrawable(R.drawable.button_not_clicked);

        // If the userScore is 0 add the button to save else set can save locally and globally to false
        if(userScore > 0 && canLocalSave || canGlobalSave){
            addScoreSaveFunctionality();
            if(!canLocalSave){
                RemoveSaveButton();
            }
        }else{
            canLocalSave = false;
            canGlobalSave = false;
        }

        localScoresArray = GetLocalScores();
        GetGlobalScores();

        // On Click Listener for the local leaderboard button
        leaderboardLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!leaderBoardType.equals("Local")) {
                    SwitchViewToLocal();
                }
            }
        });

        // On Click Listener for the global button
        leaderboardGlobalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalScoresArray == null || globalScoresArray[0] == null){
                    Log.d(TAG, "An error occurred retrieving scores");
                    Toast.makeText(LeaderboardActivity.this, "Could Not Connect To Server", Toast.LENGTH_LONG).show();
                }else if(!leaderBoardType.equals("Global")){
                    SwitchViewToGlobal();
                }
            }
        });

        // Populate the recycler view
        setRecyclerView();

        //Set the layout manager and tell the recycler view that the number of users scores will not change
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        leaderboardRecyclerView.setLayoutManager(layoutManager);
        leaderboardRecyclerView.setHasFixedSize(true);

        setResult(GameOverActivity.Leaderboard_Result, returnIntent);
    }

    /**
     * Function to add a button to the activity so the score can be saved.
     */
    private void addScoreSaveFunctionality(){
        ConstraintSet set = new ConstraintSet();

        // Create a new button
        saveScoreButton = new Button(this);
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

        saveScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveScoreDialog dialog = SaveScoreDialog.newInstance(leaderBoardType);
                dialog.show(getFragmentManager(), "save");
            }
        });

        isButtonActive = true;
    }

    /**
     * Function to remove button from constraint layout
     */
    public void RemoveSaveButton(){
        ConstraintSet set = new ConstraintSet();
        // Clone the layout
        set.clone(leaderboardConstraintLayout);
        // Remove button
        set.clear(saveScoreButton.getId());
        // Constrain the recycler view to the bottom of the activity
        set.connect(leaderboardRecyclerView.getId(), ConstraintSet.BOTTOM,
                leaderboardConstraintLayout.getId(), ConstraintSet.BOTTOM);
        // Apply the layout
        set.applyTo(leaderboardConstraintLayout);
        // Remove the button from the layout
        leaderboardConstraintLayout.removeView(saveScoreButton);
        isButtonActive = false;
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

    private void GetGlobalScores() {
        // TODO Implement global leaderboard retrieval
        InternetUse internetConnection = new InternetUse();
        internetConnection.execute("Not Actually needed");
    }

    /**
     * Function to populate the recycler view
     */
    public void setRecyclerView(){
        UserScore[] userScoreArray = leaderBoardType.equals("Local") ? localScoresArray : globalScoresArray;
        leaderboardRecyclerView.setAdapter(new LeaderboardAdapter(this, userScoreArray));
    }

    /**
     * Function called when the dialog is closed
     * @param name the name in the dialog
     */
    public void OnDialogClose(String name){
        // If the leaderboard selected is the local leaderboard else if it is the global
        // save score to respective leaderboard and set if can save to false
        if(leaderBoardType.equals("Local")) {
            saveScoreToLocal(name);
            canLocalSave = false;
            returnIntent.putExtra(Local_Save_Extra, canLocalSave);
        }else if(leaderBoardType.equals("Global")){
            saveScoreToGlobal(name);
            canGlobalSave = false;
            returnIntent.putExtra(Global_Save_Extra, canLocalSave);
        }
        setResult(GameOverActivity.Leaderboard_Result, returnIntent);
        // Refresh the recycler view
        setRecyclerView();
        //Remove the button
        RemoveSaveButton();
    }

    /**
     * Save the score with given name to the global leaderboard
     * @param name the name the user has chosen in the dialog
     */
    private void saveScoreToGlobal(String name) {
        // TODO Save Score To Global
        GetGlobalScores();
    }

    /**
     * Save the score with the given name to the local leaderboard
     * @param name the name the user has chosen in the dialog
     */
    private void saveScoreToLocal(String name) {
        // Open and create database
        DataBaseLocalLeaderboardAdapter mDb = new DataBaseLocalLeaderboardAdapter(LeaderboardActivity.this);
        mDb.createDatabase();
        mDb.open();
        // Try to add score to db is the score is not high enough the exception will be caught and a toast is made
        try {
            mDb.submitScore(new UserScore(name, Integer.toString(userScore), "-1"));
        }catch (LowScoreException e){
            Toast.makeText(LeaderboardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mDb.close();
        localScoresArray = GetLocalScores();
    }

    /**
     * Switch the recycler view to the Global Leaderboard Array
     */
    private void SwitchViewToGlobal(){
        leaderBoardType = "Global";
        //Rename label and flip button backgrounds
        leaderboardLabelTextView.setText("Global Leaderboard");
        leaderboardGlobalButton.setBackground(clicked);
        leaderboardLocalButton.setBackground(notClicked);

        // If the user can save globally and it is not active activate the button
        if(canGlobalSave && !isButtonActive){
            addScoreSaveFunctionality();
        }
        // If the button was active but cant globally save remove the button
        else if(isButtonActive && !canGlobalSave){
            RemoveSaveButton();
        }

        // Refresh recycler view so it will show global leaderboard
        setRecyclerView();
    }

    /**
     * Switch the recycler view to the Local Leaderboard Array
     */
    private void SwitchViewToLocal(){
        leaderBoardType = "Local";
        //Rename label and flip button backgrounds
        leaderboardLabelTextView.setText("Local Leaderboard");
        leaderboardLocalButton.setBackground(clicked);
        leaderboardGlobalButton.setBackground(notClicked);

        // If the user can save locally and it is not active activate button
        if(canLocalSave && !isButtonActive){
            addScoreSaveFunctionality();
        }
        // If the button was active but cant locally save remove the button
        else if(isButtonActive && !canLocalSave){
            RemoveSaveButton();
        }

        // Refresh recycler view so it will show local leaderboard
        setRecyclerView();
    }

    /**
     * Set the global array on return from the AsyncTask
     * @param globalScores the user scores from the global array
     */
    public void onGlobalScoreRetrieved(UserScore[] globalScores){
        globalScoresArray = globalScores;
    }

    // Class Used To Connect To Internet In Background
    private class InternetUse extends AsyncTask<String, UserScore, UserScore[]>{

        @Override
        protected UserScore[] doInBackground(String... strings) {

            UserScore[] scores;

            try {
                Client client = new Client();
                client.Start();
                scores = client.getScores();
                client.close();
            }catch (IOException ioException){
                Log.e(TAG, "Error Connecting To Internet : " + ioException.toString());
                scores = new UserScore[10];
            }

            return scores;
        }

        @Override
        protected void onPostExecute(UserScore[] scoresArray) {
            super.onPostExecute(scoresArray);
            onGlobalScoreRetrieved(scoresArray);
        }
    }

}
