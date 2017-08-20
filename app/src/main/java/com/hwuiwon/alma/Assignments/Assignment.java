package com.hwuiwon.alma.Assignments;

//import java.util.List;

public class Assignment {
    private String dueDate;
    private String assignmentName;
    private String type;
    private String assignmentDetail;
//    private String status;
//    private List<String> file;

    public Assignment(String dueDate, String assignmentName, String type, String assignmentDetail/*, String status, List<String> file*/) {
        this.dueDate = dueDate;
        this.assignmentName = assignmentName;
        this.type = type;
        this.assignmentDetail = assignmentDetail;
//        this.status = status;
//        this.file = file;
    }

    public Assignment (String objectString) {
        String[] arr = objectString.split("\n");
        this.dueDate = arr[0];
        this.assignmentName = arr[1];
        this.type = arr[2];
        this.assignmentDetail = arr[3];
//        this.status = arr[4];
//        this.file = arr[5];
    }

    String getDueDate() {
        return dueDate;
    }

    String getAssignmentName() {
        return assignmentName;
    }

    String getType() {
        return type;
    }

    String getAssignmentDetail() {
        return assignmentDetail;
    }

    /*public String getStatus() {
        return status;
    }

    public List<String> getFile() {
        return file;
    }*/

    @Override
    public String toString() {
        return dueDate+"\n"+assignmentName+"\n"+type+"\n"+assignmentDetail+"\n";
    }
}