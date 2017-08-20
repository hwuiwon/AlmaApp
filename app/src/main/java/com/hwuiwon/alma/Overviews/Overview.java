package com.hwuiwon.alma.Overviews;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Overview implements Parcelable {
//    private String time;
    private String period;
    private String className;
    private String originalClassName;
    private String alphabetGrade;
    private String roomNum;

    public Overview(/*String time, */String period, String className, String alphabetGrade, String roomNum/*, String teacher*/) {
//        this.time = time;
        originalClassName = className;
        String[] notNeeded = {"S1", "S2", "Ⓐ", "Ⓑ", /*"&",*/ "§1", "§2", "^\\s+"};
        for (String words : notNeeded) {
            className = className.replaceAll(words, "");
        }

        this.period = period;
        this.className = className.trim();
        this.alphabetGrade = alphabetGrade;
        this.roomNum = roomNum;
//        this.teacher = teacher;
    }

    public Overview(String objectString) {
        Log.d("Tag", objectString);
        String[] arr = objectString.split("\n");
        originalClassName = arr[1];
        String[] notNeeded = {"S1", "S2", "Ⓐ", "Ⓑ", /*"&",*/ "§1", "§2", "^\\s+"};
        for (String words : notNeeded) {
            arr[1] = arr[1].replaceAll(words, "");
        }

        this.period = arr[0];
        this.className = arr[1];
        this.alphabetGrade = arr[2];
        this.roomNum = arr[3];
    }

    private Overview(Parcel in) {
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

    String getPeriod() {
        return period;
    }

    public String getClassName() {
        return className;
    }

    public String getAlphabetGrade() {
        return alphabetGrade;
    }

    String getRoomNum() {
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

    @Override
    public String toString() {
        Log.d("Tag", period+"\n"+originalClassName+"\n"+alphabetGrade+"\n"+roomNum+"\n");
        return period+"\n"+originalClassName+"\n"+alphabetGrade+"\n"+roomNum+"\n";
    }
}