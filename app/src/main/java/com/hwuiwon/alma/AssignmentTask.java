package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Assignments.Assignment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

public class AssignmentTask extends AsyncTask<String, Void, Assignment[]> {

    @Override
    protected Assignment[] doInBackground(String... strings) {

        Assignment[] assignments = {
                new Assignment("","","",""/*,"",null*/)
        };
        String classID = strings[0];
        String cookie = strings[1];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url + "home/assignments?classId=" + classID)
                    .timeout(0).header("Cookie", cookie).get();

            Elements e = document.select("tbody > tr");

            int size = e.size();
            assignments = new Assignment[size];
            for(int i=0; i<size; i+=2){
//                List<String> files = new ArrayList<>();
//                Elements els = e.get(i+1).select("ul").get(0).children();
//                for(Element el: els){
//                    files.add(el.select("a").get(0).attr("href")+";"+el.select("a").text().trim());
//                }
                assignments[i] = new Assignment(
                        e.get(i).child(0).ownText().trim(),
                        e.get(i).child(1).text().trim(),
                        e.get(i).child(2).text().trim(),
                        e.get(i+1).select("p").text().trim()/*,
                        e.get(i).child(3).text().trim(),
                        files*/
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return assignments;
    }
}
