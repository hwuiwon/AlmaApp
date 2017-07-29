package com.hwuiwon.alma.Tasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class ClassIdTask extends AsyncTask<String, Void, HashMap<String, String>> {

    @Override
    protected HashMap<String, String> doInBackground(String... strings) {

        HashMap<String, String> temp = new HashMap<>();
        String cookie = strings[0];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url + "home/grades").timeout(0).header("Cookie", cookie).get();
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