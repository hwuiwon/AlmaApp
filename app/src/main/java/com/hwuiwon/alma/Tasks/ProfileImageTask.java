package com.hwuiwon.alma.Tasks;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.hwuiwon.alma.ImageIO;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class ProfileImageTask {

    private Context context;

    public ProfileImageTask (Context context){
        this.context = context;
    }

    public String execute(String... strings) {

        String cookie = strings[0];
        String url = "https://spps.getalma.com/";
        Bitmap temp;
        String filename;
        String filepath = "student_profile";

        try {
            Document document = Jsoup.connect(url + "home").timeout(0).header("Cookie", cookie).get();
            String imageUrl = document.select("ul > " +
                    "li.pure-menu-item.pure-menu-has-children.pure-menu-allow-hover.user > a > img").attr("data-src");
            Log.d("ImageUrl", imageUrl);

            byte[] r = Jsoup.connect(url.substring(0,url.length()-1)+imageUrl).ignoreContentType(true).header("Cookie",cookie).execute().bodyAsBytes();
            temp = BitmapFactory.decodeByteArray(r,0,r.length);

            filename = imageUrl.split("id=")[1];
            new ImageIO(context).setFilepath(filepath).setFileName(filename).save(temp);
            return filepath+":"+filename;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}