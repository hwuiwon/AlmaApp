package com.hwuiwon.alma;

import android.os.AsyncTask;
import android.util.Log;

import com.hwuiwon.alma.Overviews.Overview;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

class OverviewTask extends AsyncTask<String, Void, Overview[]> {

    @Override
    protected Overview[] doInBackground(String... strings) {

        Overview[] overviews = null;
        String cookie = strings[0];
        String url = "https://spps.getalma.com/";
        int tmp = 0;

        try {
            Document document = Jsoup.connect(url)
                    .header("Cookie", cookie).post();
            Log.d("tag", document.outerHtml());
            Elements elements = document.select("tbody > tr > td:not(:has(*))");

            overviews = new Overview[elements.size()];

            for (Element e : elements) {
                if (!e.select("td.period").text().trim().isEmpty()) {
                    overviews[tmp] = new Overview(
                            e.select("td.time").text().trim(),
                            e.select("td.period").text().trim(),
                            e.select("td.class").text().trim(),
                            e.select("td.grade").text(),
                            e.select("td.location").text().trim(),
                            e.select("td.teacher").text().trim()
                    );
                } else {
                    overviews[tmp] = new Overview(
                            "",
                            "",
                            e.select("td.class").text().trim(),
                            e.select("td.grade").text().trim(),
                            "",
                            e.select("td.teacher").text().trim()
                    );
                }
                tmp++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return overviews;
    }
}