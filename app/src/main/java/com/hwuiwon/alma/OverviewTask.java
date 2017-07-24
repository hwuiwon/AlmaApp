package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Overviews.Overview;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class OverviewTask extends AsyncTask<String, Void, Overview[]> {

    private int tmp = 0;
    private Overview[] overviews = null;

    @Override
    protected Overview[] doInBackground(String... strings) {

        String cookie = strings[0];
        String url = "https://spps.getalma.com/";

        String date;

        try {
            Document document = Jsoup.connect(url+"home")
                    .header("Cookie", cookie).get();

            Element scriptElement = document.select("script").last();
            Pattern p = Pattern.compile("user:\\s\\{\\s+id:\\s\\\"(.+?)\\\"");
            Matcher m = p.matcher(scriptElement.html());
            //noinspection ResultOfMethodCallIgnored
            m.find();
            String studentID = m.group(1);

            date = document.select(".date-picker > a").get(0).attr("data-date");
            Connection.Response response =
                    Jsoup.connect(url+"home/get-student-schedule?studentId="+studentID+"&date="+date)
                            .header("Cookie", cookie).ignoreContentType(true)
                            .method(Connection.Method.GET).execute();

            String responseString = escapeUnicode(response.body().split("\"html\":\"")[1].split("\"\\}")[0]);

            overviews = new Overview[Jsoup.parse(responseString).select("tr").size()];

            setOverviews(Jsoup.parse(responseString).select("tbody").get(0).select("tr"));

            date = Jsoup.parse(responseString).select(".date-picker > a").get(0).attr("data-date");

            String responseString2 = responseString;
            while(responseString.equals(responseString2)) {
                response =
                        Jsoup.connect(url + "home/get-student-schedule?studentId=" + studentID + "&date=" + date)
                                .header("Cookie", cookie).ignoreContentType(true)
                                .method(Connection.Method.GET).execute();
                responseString2 = escapeUnicode(response.body().split("\"html\":\"")[1].split("\"\\}")[0]);
                date = Jsoup.parse(responseString2).select(".date-picker > a").get(0).attr("data-date");
            }

            setOverviews(Jsoup.parse(responseString2).select("tbody").get(0).select("tr"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return overviews;
    }

    private String escapeUnicode(String string) {
        String str = string;
        Pattern p = Pattern.compile("\\\\u(\\w{4})");
        Matcher m = p.matcher(str);
        int i=0;
        while(m.find()){
            int ch = Integer.valueOf(m.group(1), 16);
            str = str.substring(0, m.start()-5*i)
                    + String.valueOf((char) ch)
                    + str.substring(m.end()-5*i,str.length());
            i++;
        }
        str = str.replaceAll("\\\\n", "\n").replaceAll("\\\\/", "/");
        return str;
    }

    private void setOverviews(Elements elements) {
        for(Element e : elements){
            overviews[tmp] = new Overview(
//                        e.select("td.time").text().trim(),
                    e.select("td.period").text().trim(),
                    e.select("td.class").text().trim(),
                    e.select("td.grade").text(),
                    e.select("td.location").text().trim()/*,
                        e.select("td.teacher").text().trim()*/
            );
            tmp++;
        }
    }
}