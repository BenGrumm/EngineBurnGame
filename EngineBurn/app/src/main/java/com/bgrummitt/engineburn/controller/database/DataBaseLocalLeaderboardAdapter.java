package com.bgrummitt.engineburn.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.bgrummitt.engineburn.controller.leaderboard.UserScore;
import com.bgrummitt.engineburn.controller.other.LowScoreException;

public class DataBaseLocalLeaderboardAdapter extends DataBaseAdapter {

    protected static final String TAG = DataBaseLocalLeaderboardAdapter.class.getSimpleName();

    final static public String TABLE_NAME = "LOCAL_LEADERBOARD";
    final static public String COLUMN_NAME = "USER_NAME";
    final static public String COLUMN_SCORE = "USER_SCORE";
    final static public String COLUMN_POSITION = "USER_POSITION";

    public DataBaseLocalLeaderboardAdapter(Context context){
        super(context, TABLE_NAME);
    }

    final static public String SELECT_ALL = "SELECT %s FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " DESC;";

    /**
     * Function to get the top scores from the database
     * @return array of UserScores
     */
    public UserScore[] getTopScores(){
        // Create new list
        UserScore[] userScores = new UserScore[10];
        // Get the names
        String[] names = getStringArray(COLUMN_NAME);
        // Get the scores
        int[] scores = getIntArray(COLUMN_SCORE);
        for(int i = 0; i < 10; i++){
            // As the lists are sorted the positions in the array are the same as the actual leaderboard positions
            userScores[i] = new UserScore(names[i], Integer.toString(scores[i]), Integer.toString(i + 1));
        }
        return userScores;
    }

    public String[] getStringArray(String column){
        String[] stringArr = new String[10];
        // Query database
        Cursor mCur = getDb().rawQuery(String.format(SELECT_ALL, column), null);
        // Move the cursor to the first item
        if(mCur.moveToFirst()) {
            // Get the index of the names column
            int index = mCur.getColumnIndex(column);
            int position = 0;
            // While there are items left in the list
            while (!mCur.isAfterLast()) {
                // Log the name and then move to the next item
                stringArr[position] = mCur.getString(index);
                position++;
                mCur.moveToNext();
            }
        }
        mCur.close();
        return stringArr;
    }

    public int[] getIntArray(String column){
        int[] integerArr = new int[10];
        // Query database
        Cursor mCur = getDb().rawQuery(String.format(SELECT_ALL, column), null);
        // Move the cursor to the first item
        if(mCur.moveToFirst()) {
            // Get the index of the names column
            int index = mCur.getColumnIndex(column);
            int position = 0;
            // While there are items left in the list
            while (!mCur.isAfterLast()) {
                // Log the name and then move to the next item
                integerArr[position] = mCur.getInt(index);
                position++;
                mCur.moveToNext();
            }
        }
        mCur.close();
        return integerArr;
    }

    /**
     * Add the new score to the database if it is big enough
     * @param score UserScore class containing name and score
     * @throws LowScoreException if score is too low
     */
    public void submitScore(UserScore score) throws LowScoreException {
        int[] scores = getIntArray(COLUMN_SCORE);
        int userScore = Integer.parseInt(score.getScore());
        // If the score is not higher than the lowest score throw an exception
        if(userScore <= scores[9]){
            throw new LowScoreException("Score Is Less Or Equal To The Lowest On Leaderboard");
        }

        // Get the lowest scores id
        int[] userScoreIDs = getIntArray(BaseColumns._ID);
        int replaceID = userScoreIDs[9];

        // Update the name
        ContentValues cvName = new ContentValues();
        cvName.put(COLUMN_NAME, score.getName());
        getDb().update(TABLE_NAME, cvName, BaseColumns._ID + " = ?", new String[]{Integer.toString(replaceID)});

        // Update the Score
        ContentValues cvScore = new ContentValues();
        cvScore.put(COLUMN_SCORE, score.getScore());
        getDb().update(TABLE_NAME, cvScore, BaseColumns._ID + " = ?", new String[]{Integer.toString(replaceID)});

    }

}
