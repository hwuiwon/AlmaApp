package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Grades.Grade;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

class GradeTask extends AsyncTask<String, Void, Grade[]> {

    @Override
    protected Grade[] doInBackground(String... strings) {
        Grade[] grades = null;
        String classID = strings[0];
        String cookie = strings[1];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url+"home/grades?classId="+classID)
                    .timeout(0).header("Cookie", cookie).post();

            Elements elements = document.select("tbody > tr");
            grades = new Grade[elements.size()];
            for (int i=0; i<grades.length; i++){
                grades[i] = new Grade(
                        elements.get(i).child(1).text().trim(),
                        elements.get(i).child(2).text().trim(),
                        elements.get(i).child(3).ownText().trim(),
                        elements.get(i).child(3).select("small").text().trim(),
                        elements.get(i).child(5).text().trim()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return grades;
    }
}