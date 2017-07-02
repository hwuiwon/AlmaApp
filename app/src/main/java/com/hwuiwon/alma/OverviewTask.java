package com.hwuiwon.alma;

import android.os.AsyncTask;
import android.util.Log;

import com.hwuiwon.alma.Overviews.Overview;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class OverviewTask extends AsyncTask<String, Void, Overview[]> {

    @Override
    protected Overview[] doInBackground(String... strings) {

        Overview[] overviews = null;
        String username = strings[0];
        String password = strings[1];
        String url = "https://spps.getalma.com/";
        int tmp = 0;

        try {
            Document document = Jsoup.connect(url + "login")
                    .data("username", username).data("password", password).post();

            Elements elements = document.select("tbody > tr > td:not(:has(*))");

            for (Element e : elements) {
                if (!e.select("td.period").text().trim().equals(null)) {
                    // Log.d("OverviewList1", e.select("td.period").text() + " " + e.select("td.class").text() +
                    //         " " + e.select("td.location").text() + " " + e.select("td.grade").text());
                    overviews[tmp] = new Overview(e.select("td.period").text(), e.select("td.class").text(),
                                                  e.select("td.location").text(), e.select("td.grade").text());
                } else {
                    // Log.d("OverviewList2", e.select("td.class").text() + " " + e.select("td.grade").text());
                    overviews[tmp] = new Overview("", e.select("td.class").text(),
                                                  "", e.select("td.grade").text());
                }
                tmp++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return overviews;
    }
}