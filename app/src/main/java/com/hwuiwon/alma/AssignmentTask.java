package com.hwuiwon.alma;

import android.os.AsyncTask;
import android.util.Log;

import com.hwuiwon.alma.Assignments.Assignment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

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

            Log.d("tag", document.select("tbody").html());
            Elements e = document.select("tbody > tr");

            int size = e.size();
            assignments = new Assignment[size/2];
            for(int i=0; i<size/2; i++){
//                List<String> files = new ArrayList<>();
//                Elements els = e.get(i+1).select("ul").get(0).children();
//                for(Element el: els){
//                    files.add(el.select("a").get(0).attr("href")+";"+el.select("a").text().trim());
//                }
                assignments[i] = new Assignment(
                        e.get(i*2).child(0).ownText().trim(),
                        e.get(i*2).child(1).text().trim(),
                        e.get(i*2).child(2).text().trim(),
                        e.get(i*2+1).select("p").get(0).html().replaceAll("<br\\s*>\\s*","\n").trim()/*,
                        e.get(i).child(3).text().trim(),
                        files*/
                );

            }

            for(Assignment a : assignments){
                Log.d("Debug", "Due Date : "+a.getDueDate()+"\n"+
                        "Assignment Title : "+a.getAssignmentName()+"\n"+
                        "Assignment Type : "+a.getType()+"\n"+
                        "Assignment Detail : "+a.getAssignmentDetail());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assignments;
    }
}
