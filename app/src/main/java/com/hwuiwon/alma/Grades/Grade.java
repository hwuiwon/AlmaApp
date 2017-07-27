package com.hwuiwon.alma.Grades;

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
}