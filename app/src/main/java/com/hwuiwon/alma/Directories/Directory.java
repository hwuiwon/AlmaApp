package com.hwuiwon.alma.Directories;

import android.graphics.Bitmap;

public class Directory {
    private String name;
    private String email;
    private Bitmap profilePic;

    public Directory(String name, String email, Bitmap profilePic) {
        this.name = name;
        this.email = email;
        this.profilePic = profilePic;
    }

    public Directory(String objectString) {
        String[] arr = objectString.split("\n");
        this.name = arr[0];
        this.email = arr[1];
        this.profilePic = null;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    Bitmap getProfilePic() {
        return profilePic;
    }

    @Override
    public String toString() {
        return name+"\n"+email+"\n";
    }
}
