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

    public String getAlphabetGrade() {
        return alphabetGrade;
    }
}
