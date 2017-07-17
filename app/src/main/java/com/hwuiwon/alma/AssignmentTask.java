package com.hwuiwon.alma;

import android.os.AsyncTask;

import com.hwuiwon.alma.Assignments.Assignment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssignmentTask extends AsyncTask<String, Void, Assignment[]> {

    @Override
    protected Assignment[] doInBackground(String... strings) {

        HashMap<String, String> temp = new HashMap<>();
        String username = strings[0];
        String password = strings[1];
        String classID = strings[2];
        String url = "https://spps.getalma.com/";
        int tmp = 0;

        try {
            Connection.Response response = Jsoup.connect(url + "login")
                    .data("username", username).data("password", password)
                    .method(Connection.Method.POST).execute();

            Map<String, String> loginCookies = response.cookies();

            Document document = Jsoup.connect(url + "home/assignments?classId=" + classID)
                    .timeout(0).cookies(loginCookies).post();

            Elements elements = document.select("tbody > tr > td");

            for (Element e : elements) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Assignment[0];
    }
}
