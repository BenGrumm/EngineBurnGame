package com.bgrummitt.engineburn.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;

public class DataBaseSettingsAdapter extends DataBaseAdapter{

    final static private String TAG = DataBaseSettingsAdapter.class.getSimpleName();

    final static public String DB_TABLE_NAME_SETTINGS = "GAME_SETTINGS";
    final static public String SETTING_NAME_COLUMN = "SETTING_NAME";
    final static public String SETTING_SETTING_COLUMN = "SETTING_VALUE";
    final static public String CHARACTER_SKIN_SETTING = "CHARACTER_SKIN";

    public DataBaseSettingsAdapter(Context context){
        super(context, DB_TABLE_NAME_SETTINGS);
    }

    //This is effectively what is sent when the .update function is used in the editSetting function
    private final String mEditSettingSql = "UPDATE %s SET %s = '%s' WHERE %s = '%s'";

    /**
     * Function to edit the settings in the database
     * @param settingName the name of the setting in the column
     * @param newSetting the new data to replace the old
     */
    public void editSetting(String settingName, String newSetting) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(SETTING_SETTING_COLUMN, newSetting);
            // Update the Table in a very similar string to mEditSettingSql string with setting in it
            int NumberOfAffectedRows = getDb().update(DB_TABLE_NAME_SETTINGS, cv,SETTING_NAME_COLUMN + " = ?", new String[]{settingName});
            Log.v(TAG, String.format("%d Rows Edited", NumberOfAffectedRows));
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

}
