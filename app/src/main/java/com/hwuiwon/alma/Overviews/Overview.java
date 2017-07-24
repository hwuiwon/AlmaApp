package com.hwuiwon.alma.Overviews;

import android.os.Parcel;
import android.os.Parcelable;

public class Overview implements Parcelable {
//    private String time;
    private String period;
    private String className;
    private String originalClassName;
    private String alphabetGrade;
    private String roomNum;
    private String teacher;
    private String[] notNeeded = {"S1", "S2", "Ⓐ", "Ⓑ", "&", "§1", "§2", "^\\s+"};

    public Overview(/*String time, */String period, String className, String alphabetGrade, String roomNum/*, String teacher*/) {
//        this.time = time;
        originalClassName = className;
        for (String words : notNeeded) {
            className = className.replaceAll(words, "");
        }

        this.period = period;
        this.className = className;
        this.alphabetGrade = alphabetGrade;
        this.roomNum = roomNum;
//        this.teacher = teacher;
    }

    protected Overview(Parcel in) {
        period = in.readString();
        className = in.readString();
        alphabetGrade = in.readString();
    }

    public static final Creator<Overview> CREATOR = new Creator<Overview>() {
        @Override
        public Overview createFromParcel(Parcel in) {
            return new Overview(in);
        }

        @Override
        public Overview[] newArray(int size) {
            return new Overview[size];
        }
    };

    public String getPeriod() {
        return period;
    }

    public String getClassName() {
        return className;
    }

    public String getAlphabetGrade() {
        return alphabetGrade;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public String getOriginalClassName() {
        return originalClassName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(period);
        parcel.writeString(className);
        parcel.writeString(alphabetGrade);
    }
}