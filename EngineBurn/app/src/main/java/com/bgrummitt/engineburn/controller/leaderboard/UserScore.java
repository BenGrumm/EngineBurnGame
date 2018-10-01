package com.bgrummitt.engineburn.controller.leaderboard;

import android.os.Parcel;
import android.os.Parcelable;

public class UserScore implements Parcelable {

    private String mUserName;
    private String mUserScore;
    private String mUserPosition;

    public UserScore(String userName, String userScore, String userPosition){
        mUserName = userName;
        mUserScore = userScore;
        mUserPosition = userPosition;
    }

    public String getName() {
        return mUserName;
    }

    public String getScore() {
        return mUserScore;
    }

    public String getPosition() {
        return mUserPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserName);
        dest.writeString(mUserScore);
        dest.writeString(mUserPosition);
    }

    private UserScore(Parcel in){
        mUserName = in.readString();
        mUserScore = in.readString();
        mUserPosition = in.readString();
    }

    public static final Creator<UserScore> CREATOR = new Creator<UserScore>() {
        @Override
        public UserScore createFromParcel(Parcel source) {
            return new UserScore(source);
        }

        @Override
        public UserScore[] newArray(int size) {
            return new UserScore[size];
        }
    };

}
