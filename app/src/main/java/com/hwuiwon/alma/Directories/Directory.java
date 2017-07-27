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

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }
}
