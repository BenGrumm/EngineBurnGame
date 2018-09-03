package com.bgrummitt.engineburn.controller.characters;

import android.os.Parcel;
import android.os.Parcelable;

public class GameCharacter implements Parcelable{

    public int mIDFullFire;
    public int mIDLowFire;
    public int mIDNoFire;
    private String mCharacterName;

    public GameCharacter(String name, int IDFullFire, int IDLowFire, int IDNoFire){
        mCharacterName = name;
        mIDFullFire = IDFullFire;
        mIDLowFire = IDLowFire;
        mIDNoFire = IDNoFire;
    }

    public int getIDFullFire() {
        return mIDFullFire;
    }

    public void setIDFullFire(int IDFullFire) {
        mIDFullFire = IDFullFire;
    }

    public int getIDLowFire() {
        return mIDLowFire;
    }

    public void setIDLowFire(int IDLowFire) {
        mIDLowFire = IDLowFire;
    }

    public int getIDNoFire() {
        return mIDNoFire;
    }

    public void setIDNoFire(int IDNoFire) {
        mIDNoFire = IDNoFire;
    }

    public String getCharacterName() {
        return mCharacterName;
    }

    public void setCharacterName(String characterName) {
        mCharacterName = characterName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIDFullFire);
        dest.writeInt(mIDLowFire);
        dest.writeInt(mIDNoFire);
        dest.writeString(mCharacterName);
    }

    private GameCharacter(Parcel in){
        mIDFullFire = in.readInt();
        mIDLowFire = in.readInt();
        mIDNoFire = in.readInt();
        mCharacterName = in.readString();
    }

    public static final Creator<GameCharacter> CREATOR = new Creator<GameCharacter>() {
        @Override
        public GameCharacter createFromParcel(Parcel source) {
            return new GameCharacter(source);
        }

        @Override
        public GameCharacter[] newArray(int size) {
            return new GameCharacter[size];
        }
    };

}
