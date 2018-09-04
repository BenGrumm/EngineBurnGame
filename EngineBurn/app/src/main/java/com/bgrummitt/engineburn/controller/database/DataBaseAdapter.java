package com.bgrummitt.engineburn.controller.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseAdapter {

    protected static final String TAG = "DataAdapter";

    final static public String SETTING_NAME_COLUMN = "SETTING_NAME";
    final static public String SETTING_SETTING_COLUMN = "SETTING_VALUE";
    final static public String CHARACTER_SKIN_SETTING = "CHARACTER_SKIN";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    //Template sql query
    private final String mQuerySetting ="SELECT " + SETTING_SETTING_COLUMN + " FROM " + DataBaseHelper.DB_TABLE_NAME + " WHERE " + SETTING_NAME_COLUMN + " LIKE '%s'";

    public DataBaseAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    /**
     * Function to create a database
     * @return DatabaseAdapter
     * @throws SQLException if database could not be created
     */
    public DataBaseAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    /**
     * Open the database and then close it to check if it is working and then get a readable database from it
     * @return DataBaseAdapter
     * @throws SQLException if database could not be opened or closed or a readable database could not be created
     */
    public DataBaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    /**
     * Close the database
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Get the settings names from the database
     * @return a string array of settings names
     */
    public String[] getSettings(){
        //Using an array list to easily add more questions
        List<String> stringArr = new ArrayList<String>();
        Cursor mCur;
        try{
            //Try selecting all questions from the database
            String sql = "SELECT " + SETTING_NAME_COLUMN + " FROM " + DataBaseHelper.DB_NAME;

            mCur = mDb.rawQuery(sql, null);

            if(mCur != null){
                mCur.moveToNext();
            }
        }catch (SQLException mSQLException){
            Log.e(TAG, "ERROR GETTING QUESTIONS === " + mSQLException.toString());
            throw mSQLException;
        }

        //For all the questions in the Database
        for(int i = 0; i < getSettingsCount(); i++){
            Log.d(TAG, "Question " + i + " = " + mCur.getString(0));
            //Get the question at column 0 and add it to the list
            stringArr.add(mCur.getString(0));
            //Move to the next row
            mCur.moveToNext();
        }

        Log.d(TAG, stringArr.size() + "");

        //Turn the List into an Array
        String[] tempArr = new String[stringArr.size()];
        tempArr = stringArr.toArray(tempArr);

        return tempArr;
    }

    /**
     * Get the number of entries into the database
     * @return number of entries in the database
     */
    public long getSettingsCount(){
        long count = DatabaseUtils.queryNumEntries(mDb, DataBaseHelper.DB_NAME);
        return count;
    }

    /**
     * Given the question get the Answer from the database
     * @param SettingName name of the setting to return
     * @return String of the setting
     */
    public String getSetting(String SettingName){
        try {
            //Query the database with the formatted sql query
            Cursor mCur = mDb.rawQuery(String.format(mQuerySetting, SettingName), null);
            if (mCur!=null) {
                mCur.moveToNext();
            }
            //Return the string at position 0
            return mCur.getString(0);
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    private final String mEditSettingSql = "UPDATE " + DataBaseHelper.DB_TABLE_NAME + " SET " + SETTING_SETTING_COLUMN + " = '%s' WHERE " + SETTING_NAME_COLUMN + " = '%s'";

    /**
     * Function to edit the settings in the database
     * @param settingName the name of the setting in the column
     * @param newSetting the new data to replace the old
     */
    public void editSetting(String settingName, String newSetting){
        try {
            ContentValues cv = new ContentValues();
            cv.put(SETTING_SETTING_COLUMN, newSetting);
            int NumberOfAffectedRows = mDb.update(DataBaseHelper.DB_TABLE_NAME, cv,SETTING_NAME_COLUMN + " = ?", new String[]{settingName});
            Log.v(TAG, String.format("%d Rows Edited", NumberOfAffectedRows));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

}