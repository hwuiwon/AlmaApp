package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Grades.Grade;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;

public class GradeTask extends AsyncTask<String, Void, Grade[]> {

    @Override
    protected Grade[] doInBackground(String... strings) {
        Grade[] grades = null;
        String username = strings[0];
        String password = strings[1];
        String classID = strings[2];
        String url = "https://spps.getalma.com/";

        try {
            Connection.Response response = Jsoup.connect(url + "login")
                    .data("username", username).data("password", password)
                    .method(Connection.Method.POST).execute();

            Map<String, String> loginCookies = response.cookies();

            Document document = Jsoup.connect(url+"home/grades?classId="+classID)
                    .timeout(0).cookies(loginCookies).post();

            Elements elements = document.select("tbody > tr");
            grades = new Grade[elements.size()];

            for (int i=0; i<grades.length; i++) {
                grades[i] = new Grade(
                        elements.get(i).child(1).text().trim(),
                        elements.get(i).child(2).text().trim(),
                        elements.get(i).child(3).ownText().trim(),
                        elements.get(i).child(3).select("small").text().trim(),
                        elements.get(i).child(5).text().trim()
                );
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return grades;
    }
}
