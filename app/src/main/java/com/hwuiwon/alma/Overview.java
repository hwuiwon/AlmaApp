package com.hwuiwon.alma;

public class Overview {
    private String className;
    private String alphabetGrade;

    public Overview(String className, String alphabetGrade) {
        this.className = className;
        this.alphabetGrade = alphabetGrade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAlphabetGrade() {
        return alphabetGrade;
    }

    public void setAlphabetGrade(String alphabetGrade) {
        this.alphabetGrade = alphabetGrade;
    }
}
