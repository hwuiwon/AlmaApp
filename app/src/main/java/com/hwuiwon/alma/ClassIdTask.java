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

class ClassIdTask extends AsyncTask<String, Void, HashMap<String, String>> {

    @Override
    protected HashMap<String, String> doInBackground(String... strings) {

        HashMap<String, String> temp = new HashMap<>();
        String username = strings[0];
        String password = strings[1];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url + "home/grades")
                    .timeout(0).cookies(loginCookies).post();

            Elements options = document.getElementsByAttributeValue("name", "classId").get(0).children();
            for (Element option : options) {
                temp.put(option.text(), option.val());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}