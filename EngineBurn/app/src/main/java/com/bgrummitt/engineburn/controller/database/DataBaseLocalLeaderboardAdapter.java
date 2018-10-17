package com.bgrummitt.engineburn.controller.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bgrummitt.engineburn.controller.leaderboard.UserScore;
import com.bgrummitt.engineburn.controller.other.LowScoreException;

import java.sql.SQLException;

public class DataBaseLocalLeaderboardAdapter extends DataBaseAdapter {

    protected static final String TAG = DataBaseLocalLeaderboardAdapter.class.getSimpleName();

    final static public String TABLE_NAME = "LOCAL_LEADERBOARD";
    final static public String COLUMN_NAME = "USER_NAME";
    final static public String COLUMN_SCORE = "USER_SCORE";
    final static public String COLUMN_POSITION = "USER_POSITION";

    public DataBaseLocalLeaderboardAdapter(Context context){
        super(context, TABLE_NAME);
    }

    final static public String SELECT_ALL = "SELECT %s FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " ASC;";

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

    public void submitScore(UserScore score) throws LowScoreException {
        int[] scores = getIntArray(COLUMN_SCORE);
        int userScore = Integer.parseInt(score.getScore());
        int newPosition;
        if(userScore <= scores[9]){
            throw new LowScoreException("Score Is Less Or Equal To The Lowest On Leaderboard");
        }
        for(int i = 0; i < 10; i++){
            if(userScore > scores[i]){
                newPosition = i;
            }
        }
    }

}
