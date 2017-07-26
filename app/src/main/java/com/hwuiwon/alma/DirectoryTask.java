package com.hwuiwon.alma;

import android.os.AsyncTask;
import android.util.Log;

import com.hwuiwon.alma.Directories.Directory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DirectoryTask extends AsyncTask<String, Void, Directory[]> {

    @Override
    protected Directory[] doInBackground(String... strings) {
        Directory[] directories = null;
        String keyword = strings[0];
        String cookie = strings[1];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url + "directory/search?u=0&q=" + keyword)
                    .timeout(0).header("Cookie", cookie).post();

            Elements e = document.select("div.directory-results > ul > li");
            directories = new Directory[e.size()];

            for (int i=0; i<directories.length; i++) {
                Log.d("DirectoryString", e.get(i).text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return directories;
    }
}
