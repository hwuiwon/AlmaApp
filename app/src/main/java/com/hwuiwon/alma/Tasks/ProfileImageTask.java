package com.hwuiwon.alma.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLConnection;

public class ProfileImageTask extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... strings) {

        String cookie = strings[0];
        String url = "https://spps.getalma.com/";
        Bitmap temp = null;

        try {
            Document document = Jsoup.connect(url + "home").timeout(0).header("Cookie", cookie).get();
            String imageUrl = document.select("ul > " +
                    "li.pure-menu-item.pure-menu-has-children.pure-menu-allow-hover.user > a > img").attr("abs:data-src");
            Log.d("ImageUrl", imageUrl);
            Log.d("ImageUrlCookie", cookie);

            URLConnection connection = new java.net.URL(imageUrl).openConnection();
            connection.addRequestProperty("Cookie", cookie);
            connection.connect();

            temp = BitmapFactory.decodeStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}