package com.hwuiwon.alma.Tasks;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ProfileImageTask extends AsyncTask<Object, Void, String> {

    @Override
    protected String doInBackground(Object... objects) {

        String cookie = (String) objects[0];
        Context context = (Context) objects[1];
        String url = "https://spps.getalma.com/";
        Bitmap temp = null;
        String filename = "";

        try {
            Document document = Jsoup.connect(url + "home").timeout(0).header("Cookie", cookie).get();
            String imageUrl = document.select("ul > " +
                    "li.pure-menu-item.pure-menu-has-children.pure-menu-allow-hover.user > a > img").attr("data-src");
            Log.d("ImageUrl", imageUrl);

            byte[] r = Jsoup.connect(url.substring(0,url.length()-1)+imageUrl).header("Cookie",cookie).execute().bodyAsBytes();
            temp = BitmapFactory.decodeByteArray(r,0,r.length);
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            filename = imageUrl.split("id=")[1];
            File path = new File(directory, filename);

            FileOutputStream fos = null;
            fos = new FileOutputStream(path);
                // Use the compress method on the BitMap object to write image to the OutputStream
            temp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return directory.getAbsolutePath()+":"+filename;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}