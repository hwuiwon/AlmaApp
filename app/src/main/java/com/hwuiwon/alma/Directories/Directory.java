package com.hwuiwon.alma.Directories;

public class Directory {
    private String name;
    private String email;

    public Directory(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}