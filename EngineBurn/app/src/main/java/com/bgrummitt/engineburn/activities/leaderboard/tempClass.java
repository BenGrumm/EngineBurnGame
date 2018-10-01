package com.bgrummitt.engineburn.activities.leaderboard;

import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

public class tempClass {

    public static UserScore[] getUserScores(){
        UserScore[] userArr = new UserScore[10];
        for(int i = 1; i < 11; i++){
            userArr[i - 1] = new UserScore("User101" + Integer.toString(i), Integer.toString(i * 100) + "", Integer.toString(i) + "");
        }
        return userArr;
    }

}
