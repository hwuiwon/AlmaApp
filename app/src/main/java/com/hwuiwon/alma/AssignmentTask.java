package com.hwuiwon.alma;

import android.os.AsyncTask;
import android.util.Log;

import com.hwuiwon.alma.Assignments.Assignment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

class AssignmentTask extends AsyncTask<String, Void, Assignment[]> {

    @Override
    protected Assignment[] doInBackground(String... strings) {

        Assignment[] assignments = new Assignment[0];
        String classID = strings[0];
        String cookie = strings[1];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url + "home/assignments?classId=" + classID)
                    .timeout(0).header("Cookie", cookie).get();

            Elements e = document.select("tbody > tr");

            int size = e.size();
            assignments = new Assignment[size / 2];
            for (int i=0; i<size/2; i++){
                assignments[i] = new Assignment(
                        e.get(i * 2).child(0).ownText().trim(),
                        e.get(i * 2).child(1).text().trim(),
                        e.get(i * 2).child(2).text().trim(),
                        e.get(i * 2 + 1).select("p").get(0).html().replaceAll("<br\\s*>\\s*","\n").trim()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assignments;
    }
}