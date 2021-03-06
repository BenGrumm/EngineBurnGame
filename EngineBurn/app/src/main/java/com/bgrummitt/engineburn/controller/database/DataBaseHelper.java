package com.bgrummitt.engineburn.controller.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    final static private String TAG = DataBaseHelper.class.getSimpleName();
    static private String DB_PATH = "";
    final private Context mContext;
    private SQLiteDatabase mDataBase;

    public DataBaseHelper(Context context, String db_name) {
        // Pass the context name and version to the super class
        super(context, db_name, null, 1);
        //Get the DB_PATH
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;
    }

    public void createDataBase() throws Error {
        //If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                //Copy the database from assets
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //On Create and On upgrade are required
    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    //Check that the database exists here: /data/data/your package/databases/ database name
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + this.getDatabaseName());
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    /**
     * Copy the database from assets
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        //Open the database in the assets/databases folder
        InputStream mInput = mContext.getAssets().open("databases/" + this.getDatabaseName());
        String outFileName = DB_PATH + this.getDatabaseName();
        OutputStream mOutput = new FileOutputStream(outFileName);
        //Using a buffer copy over the database to the database app folder
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        //Flush and close everything
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    /**
     * Open the database so it can be queried
     * @return true if opened else false
     * @throws SQLException
     */
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + this.getDatabaseName();
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        //Return true if succesfull
        return mDataBase != null;
    }

    /**
     * Function to close the database synchronized so it isn't
     */
    @Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

}