package com.hwuiwon.alma;

public class Assignment {
    private String dueDate;
    private String assignmentName;
    private String type;
    private String assignmentDetail;

    public Assignment(String dueDate, String assignmentName, String type, String assignmentDetail) {
        this.dueDate = dueDate;
        this.assignmentName = assignmentName;
        this.type = type;
        this.assignmentDetail = assignmentDetail;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getType() {
        return type;
    }

    public String getAssignmentDetail() {
        return assignmentDetail;
    }
}