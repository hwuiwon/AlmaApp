package com.hwuiwon.alma.Grades;

import android.util.Log;

public class Grade {
    private String assignmentName;
    private String weighted;
    private String alphabetGrade;
    private String percentageGrade;
    private String updatedDate;
//    private String feedback;

    public Grade(String assignmentName, String weighted, String alphabetGrade, String percentageGrade, String updatedDate/*, String feedback*/) {
        this.assignmentName = assignmentName;
        this.weighted = weighted;
        this.alphabetGrade = alphabetGrade;
        this.percentageGrade = percentageGrade;
        this.updatedDate = updatedDate;
//        this.feedback = feedback;
    }

    public Grade(String objectString) {
        Log.d("Tag", "object string : "+objectString);
        String[] arr = objectString.split("\n");
        this.assignmentName = arr[0];
        this.weighted = arr[1];
        this.alphabetGrade = arr[2];
        this.percentageGrade = arr[3];
        this.updatedDate = arr[4];
    }

    String getAssignmentName() {
        return assignmentName;
    }

    String getWeighted() {
        return weighted;
    }

    String getAlphabetGrade() {
        return alphabetGrade;
    }

    String getPercentageGrade() {
        return percentageGrade;
    }

    String getUpdatedDate() {
        return updatedDate;
    }

/*
    public String getFeedback() {
        return feedback;
    }
*/

    @Override
    public String toString() {
        return assignmentName+"\n"+weighted+"\n"+alphabetGrade+"\n"+percentageGrade+"\n"+updatedDate+"\n";
    }
}