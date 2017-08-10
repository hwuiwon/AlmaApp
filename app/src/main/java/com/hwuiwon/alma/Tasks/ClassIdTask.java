package com.hwuiwon.alma.Tasks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassIdTask {

    public HashMap<String, String> execute(String... strings) {

        HashMap<String, String> temp = new HashMap<>();
        String cookie = strings[0];
        String url = "https://spps.getalma.com/";

        try {
            Document document = Jsoup.connect(url + "home/grades")
                    .timeout(0).header("Cookie", cookie).get();

            Elements options = document.getElementsByAttributeValue("name", "classId").get(0).children();
            for (Element option : options) {
                temp.put(option.text(), option.val());
            }

            Element scriptElement = document.select("script").last();
            Pattern p = Pattern.compile("user:\\s\\{\\s+id:\\s\"(.+?)\",\\s+name:\\s\"(.+?)\",\\s+role:\\s\"(.+?)\"");
            Matcher m = p.matcher(scriptElement.html());
            if(m.find()) {
                temp.put("name", m.group(2));
                temp.put("role", m.group(3));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}