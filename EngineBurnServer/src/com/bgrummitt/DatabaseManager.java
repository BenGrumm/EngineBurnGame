package com.bgrummitt;

import java.sql.*;

public class DatabaseManager {

    private static int iTimeout = 30;

    final static public String TABLE_NAME = "GLOBAL_LEADERBOARD";
    final static public String COLUMN_NAME = "USER_NAME";
    final static public String COLUMN_SCORE = "USER_SCORE";
    final static public String COLUMN_POSITION = "USER_POSITION";

    private Connection conn;
    private Statement stmt;

    final static private String SQL_GET_SCORES = "SELECT " + COLUMN_NAME + ", " + COLUMN_SCORE  + ", " + COLUMN_POSITION + ", " + "_id" + " FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " DESC;";

    private String[][] globalScoresArray;
    private Boolean scoresArrayHasUpdated;

    public DatabaseManager() throws Exception{
        openDatabase();

        scoresArrayHasUpdated = false;
        globalScoresArray =  new String[10][4];
    }

    private void openDatabase() throws Exception{
        // Register the driver being used for the sql
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);

        // Create the file locations
        String sTempDb = "C:/sqlite/testDB/" + TABLE_NAME + ".db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + sTempDb;

        // Create the connection to the database
        conn = DriverManager.getConnection(sDbUrl);

        stmt = conn.createStatement();
        stmt.setQueryTimeout(iTimeout);

    }

    public String[][] getGlobalScores() throws Exception{

        if(!stmt.isClosed()){
            ResultSet rs = stmt.executeQuery(SQL_GET_SCORES);

            int i = 0;

            while (rs.next()){
                globalScoresArray[i][0] = rs.getString(COLUMN_NAME);
                globalScoresArray[i][1] = rs.getString(COLUMN_SCORE);
                globalScoresArray[i][2] = rs.getString(COLUMN_POSITION);
                globalScoresArray[i][3] = Integer.toString(rs.getInt("_id"));

                i++;
            }

            for (String[] s : globalScoresArray){
                System.out.println(String.format("Name = %s, Score = %s, Position %s", s[0], s[1], s[2]));
            }

        }

        return globalScoresArray;

    }

    // SET USER_NAME = userName where scoreToReplaceID = _id;
    final static private String SQL_REPLACE_INFORMATION = "UPDATE %s SET %s = '%s' WHERE _id = '%d';";

    public void addNewUser(String userName, String userScore) throws Exception{

        if(globalScoresArray == null || scoresArrayHasUpdated){
            getGlobalScores();
        }

//        for(int i = 9; i >= 0; i--){
//            if(Integer.parseInt(userScore) > Integer.parseInt(globalScoresArray[i][1])){
//                userPosition = Integer.parseInt(globalScoresArray[i][3]);
//            }else{ break; }
//        }

        if(Integer.parseInt(userScore) <= Integer.parseInt(globalScoresArray[9][1])){
            throw new Error("Score Not High Enough");
        }

        int userToReplace = Integer.parseInt(globalScoresArray[9][3]);

        stmt.executeUpdate(String.format(SQL_REPLACE_INFORMATION, TABLE_NAME, COLUMN_NAME, userName, userToReplace));
        stmt.executeUpdate(String.format(SQL_REPLACE_INFORMATION, TABLE_NAME, COLUMN_SCORE, userScore, userToReplace));

        scoresArrayHasUpdated = true;

    }

    public void CloseOutstanding() throws Exception{
        if(!stmt.isClosed()){
            stmt.close();
        }
        if(!conn.isClosed()){
            conn.close();
        }
    }

}
