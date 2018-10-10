package com.bgrummitt.engineburn.controller.database;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseAdapter {

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private String mDBName;
    private String mDBFileName;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataBaseAdapter(Context context, String DBName) {
        this.mContext = context;
        mDBName = DBName;
        mDBFileName = String.format("%s.db", mDBName);
        mDbHelper = new DataBaseHelper(mContext, mDBFileName);
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
     * Get the number of entries into the database
     * @return number of entries in the database
     */
    public long getSettingsCount(){
        return DatabaseUtils.queryNumEntries(mDb, mDBName);
    }

    //Template sql query
    private final String mQuerySetting ="SELECT %s FROM %s WHERE %s LIKE '%s'";

    /**
     * Given the setting name get the setting from the database
     * @param entryName name of the setting to return
     * @return String of the setting
     */
    public String getDBEntry(String column, String columnComparingTo, String entryName){
        try {
            //Query the database with the formatted sql query
            Cursor mCur = mDb.rawQuery(String.format(mQuerySetting, column, mDBName, columnComparingTo, entryName), null);
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

    public SQLiteDatabase getDb() {
        return mDb;
    }

}