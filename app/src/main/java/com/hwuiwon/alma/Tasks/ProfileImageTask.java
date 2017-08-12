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

public class ProfileImageTask{

    private Context context;
    private Bitmap temp;

    public ProfileImageTask (Context context){
        this.context = context;
    }


    public Bitmap execute(String... strings) {

        final String cookie = strings[0];
        final String url = "https://spps.getalma.com";
        final String imageUrl = url + strings[1];
        final String filepath = strings[2];
        final String filename;
        if (strings[1].contains("id=")) {
            filename = imageUrl.split("id=")[1];
        } else {
            filename = imageUrl.split("images/")[1];
        }

//        Bitmap b = new ImageIO(context).setFilepath(filepath).setFileName(filename).load();

//        if(b==null) {

            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.d("Tag", imageUrl);
                        byte[] r = Jsoup.connect(imageUrl).ignoreContentType(true).header("Cookie", cookie).execute().bodyAsBytes();
                        temp = BitmapFactory.decodeByteArray(r, 0, r.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    new ImageIO(context).setFilepath(filepath).setFileName(filename).save(temp);
                }
            }.start();
//        }
        temp = new ImageIO(context).setFileName(filename).setFilepath(filepath).load();
        return temp;
    }
}