package com.hwuiwon.alma;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ClassIdTask extends AsyncTask<String, Void, HashMap<String, String>> {

    @Override
    protected HashMap<String, String> doInBackground(String... strings) {

        HashMap<String, String> temp = new HashMap<>();
        String username = strings[0];
        String password = strings[1];
        String cookie = strings[2];
        String url = "https://spps.getalma.com/";

        try {
            /*
            Connection.Response response = Jsoup.connect(url + "login")
                    .data("username", username).data("password", password)
                    .method(Connection.Method.POST).execute();

            Map<String, String> loginCookies = response.cookies();
            */

            Document document = Jsoup.connect(url + "home/grades")
                    .timeout(0).header("Cookie", cookie).post();

            Elements options = document.getElementsByAttributeValue("name", "classId").get(0).children();
            for (Element option : options) {
                // Log.d("ClassIDs", option.text() + " " + option.val());
                temp.put(option.text(), option.val());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}