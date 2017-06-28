package com.hwuiwon.alma;

import android.os.Parcel;
import android.os.Parcelable;

public class Overview implements Parcelable {
    private String period;
    private String className;
    private String alphabetGrade;

    public Overview(String period, String className, String alphabetGrade) {
        this.period = period;
        this.className = className;
        this.alphabetGrade = alphabetGrade;
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
