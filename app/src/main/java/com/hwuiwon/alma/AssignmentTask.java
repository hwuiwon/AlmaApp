package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Assignments.Assignment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignmentTask extends AsyncTask<String, Void, Assignment[]> {

    @Override
    protected Assignment[] doInBackground(String... strings) {

        HashMap<String, String> temp = new HashMap<>();
        Assignment[] assignments;
        String username = strings[0];
        String password = strings[1];
        String classID = strings[2];
        String cookie = strings[3];
        String url = "https://spps.getalma.com/";
        int tmp = 0;

        try {
            Document document = Jsoup.connect(url + "home/assignments?classId=" + classID)
                    .timeout(0).header("Cookie", cookie).post();

            Elements e = document.select("tbody > tr");

            int size = e.size();
            assignments = new Assignment[size];
            for(int i=0; i<size; i+=2){
                List<String> files = new ArrayList<>();
                Elements els = e.get(i+1).select("ul").get(0).children();
                for(Element el: els){
                    files.add(el.select("a").get(0).attr("href")+";"+el.select("a").text().trim());
                }
                assignments[i] = new Assignment(
                        e.get(i).child(0).text().trim(),
                        e.get(i).child(1).text().trim(),
                        e.get(i).child(2).text().trim(),
                        e.get(i+1).select("p").text().trim(),
                        e.get(i).child(3).text().trim(),
                        files
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Assignment[0];
    }
}
