package com.hwuiwon.alma.Tasks;

import android.util.Log;

import com.hwuiwon.alma.Overviews.Overview;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OverviewTask {

    private ArrayList<Overview> overviews = new ArrayList<>();

    public ArrayList<Overview> execute(String... strings) {

        String cookie = strings[0];
        String url = "https://spps.getalma.com/";

        String date;
        String responseString = "Student is not enrolled in any classes";
        Connection.Response response;

        try {
            Document document = Jsoup.connect(url+"home").timeout(0)
                    .header("Cookie", cookie).get();

            Element scriptElement = document.select("script").last();
            Pattern p = Pattern.compile("user:\\s\\{\\s+id:\\s\"(.+?)\"");
            Matcher m = p.matcher(scriptElement.html());
            String studentID = null;
            if(m.find()) studentID = m.group(1);

            String strDateMin = document.select(".date-picker > input").get(0).attr("min");
            String strDateMax = document.select(".date-picker > input").get(0).attr("max");
            String strCurrentDate = document.select(".date-picker > input").get(0).attr("value");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateMin = sdf.parse(strDateMin);
            Date dateMax = sdf.parse(strDateMax);
            Date currentDate = sdf.parse(strCurrentDate);
            long mid = dateMin.getTime()+Math.abs(dateMin.getTime()-dateMax.getTime())/2;
            Date dateMid = new Date(mid);
            if (dateMid.after(currentDate)) {
                dateMax = dateMid;
            } else {
                dateMin = dateMid;
            }

            int NAVIGATE_FRONT = 0;
            int NAVIGATE_BACK = 1;
            int navigateMode =
                    Math.abs(dateMin.getTime()-currentDate.getTime())>Math.abs(dateMax.getTime()-currentDate.getTime())?
                            NAVIGATE_FRONT : NAVIGATE_BACK;

            date = sdf.format(currentDate);

            while (responseString.contains("Student is not enrolled in any classes")||responseString.contains("No classes scheduled today.")) {
                response =
                        Jsoup.connect(url+"home/get-student-schedule?studentId="+studentID+"&date="+date)
                                .header("Cookie", cookie).ignoreContentType(true)
                                .method(Connection.Method.GET).execute();

                responseString = escapeUnicode(response.body().split("\"html\":\"")[1].split("\"\\}")[0]);
                date = Jsoup.parse(responseString).select(".date-picker > a").get(navigateMode).attr("data-date");
                responseString = Jsoup.parse(responseString).select("table").get(0).toString();
            }

            setOverviews(Jsoup.parse(responseString).select("tbody").get(0).select("tr"));

            String responseString2 = responseString;
            while(responseString.equals(responseString2)||responseString.contains("No classes scheduled today.")) {
                response =
                        Jsoup.connect(url + "home/get-student-schedule?studentId=" + studentID + "&date=" + date)
                                .header("Cookie", cookie).ignoreContentType(true)
                                .method(Connection.Method.GET).execute();
                responseString2 = escapeUnicode(response.body().split("\"html\":\"")[1].split("\"\\}")[0]);
                date = Jsoup.parse(responseString2).select(".date-picker > a").get(navigateMode).attr("data-date");
                responseString2 = Jsoup.parse(responseString2).select("table").get(0).toString();
            }

            setOverviews(Jsoup.parse(responseString2).select("tbody").get(0).select("tr"));

        } catch (IOException | ParseException e) {
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
            String s = e.select("td.period").text().trim();
            if(!s.equals("P0")) {
                overviews.add(new Overview(
//                        e.select("td.time").text().trim(),
                        s,
                        e.select("td.class").text().trim(),
                        e.select("td.grade").text(),
                        e.select("td.location").text().trim()/*,
                        e.select("td.teacher").text().trim()*/
                ));
            }
        }
    }
}