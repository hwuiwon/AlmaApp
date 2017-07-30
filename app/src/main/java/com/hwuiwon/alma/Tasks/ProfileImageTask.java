package com.hwuiwon.alma.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ProfileImageTask extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... strings) {

        String cookie = strings[0];
        String url = "https://spps.getalma.com/";
        Bitmap temp = null;

        try {
            Document document = Jsoup.connect(url + "home").timeout(5000)
                    .header("Cookie", cookie).get();

            String imageUrl = document.select("ul > li.pure-menu-item.pure-menu-has-children.pure-menu-allow-hover.user").select(".profile-pic").attr("src");
            Log.d("ImageUrl", imageUrl);

            temp = BitmapFactory.decodeStream(new java.net.URL(url + imageUrl).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}