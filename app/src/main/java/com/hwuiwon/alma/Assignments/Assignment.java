package com.hwuiwon.alma.Assignments;

import java.util.List;

public class Assignment {
    private String dueDate;
    private String assignmentName;
    private String type;
    private String assignmentDetail;
    private String status;
    private List<String> file;

    public Assignment(String dueDate, String assignmentName, String type, String assignmentDetail, String status, List<String> file) {
        this.dueDate = dueDate;
        this.assignmentName = assignmentName;
        this.type = type;
        this.assignmentDetail = assignmentDetail;
        this.status = status;
        this.file = file;
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

    public String getStatus() {
        return status;
    }

    public List<String> getFile() {
        return file;
    }
}