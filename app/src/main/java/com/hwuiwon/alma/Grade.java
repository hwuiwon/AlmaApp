package com.hwuiwon.alma;

public class Grade {
    private String assignmentName;
    private String weighted;
    private String alphabetGrade;
    private String percentageGrade;
    private String updatedDate;

    public Grade(String assignmentName, String weighted, String alphabetGrade, String percentageGrade, String updatedDate) {
        this.assignmentName = assignmentName;
        this.weighted = weighted;
        this.alphabetGrade = alphabetGrade;
        this.percentageGrade = percentageGrade;
        this.updatedDate = updatedDate;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getWeighted() {
        return weighted;
    }

    public String getAlphabetGrade() {
        return alphabetGrade;
    }

    public String getPercentageGrade() {
        return percentageGrade;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
}