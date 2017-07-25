package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Directories.Directory;

public class DirectoryTask extends AsyncTask<String, Void, Directory[]> {

    @Override
    protected Directory[] doInBackground(String... strings) {
        Directory[] directories = null;
        String keyword = strings[0];
        String cookie = strings[1];
        String url = "https://spps.getalma.com/";

        return directories;
    }
}
