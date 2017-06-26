package com.hwuiwon.alma;

public class Overview {
    private String period;
    private String className;
    private String alphabetGrade;

    public Overview(String period, String className, String alphabetGrade) {
        this.period = period;
        this.className = className;
        this.alphabetGrade = alphabetGrade;
    }

    public String getPeriod() {
        return period;
    }

    public String getClassName() {
        return className;
    }

    public String getAlphabetGrade() {
        return alphabetGrade;
    }
}
