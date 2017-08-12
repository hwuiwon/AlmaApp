//package com.hwuiwon.alma.Tasks;
//
//import android.content.Context;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//
//import com.hwuiwon.alma.Directories.Directory;
//import com.hwuiwon.alma.DirectoryActivity;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
//public class DirectoryTask extends AsyncTask<String, Void, Directory[]> {
//
//    private Context context;
//
//    public DirectoryTask(Context context){
//        this.context = context;
//    }
//
//    @Override
//    protected Directory[] doInBackground(String... strings) {
//
//        Directory[] directories = null;
//        String keyword = strings[0];
//        String cookie = strings[1];
//        String url = "https://spps.getalma.com";
//
//        char[] chars = keyword.toCharArray();
//
//        keyword="";
//        for(char c:chars){
//            keyword+="%"+Integer.toHexString((int)c);
//        }
//
//        try {
//            Document document = Jsoup.connect(url + "/directory/search?u=0&q=" + keyword)
//                    .timeout(0).header("Cookie", cookie).post();
//
//            Elements e = document.select("div.directory-results > ul > li");
//            directories = new Directory[e.size()];
//
//            for (int i=0; i<directories.length; i++) {
//                directories[i] = new Directory(
//                        e.get(i).select(".fn").text().trim(),
//                        e.get(i).select("a").text().trim(),
//                        new ProfileImageTask(context).execute(cookie, url+e.get(i).select(".profile-pic").attr("data-src")).get());
//            }
//
//        } catch (IOException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        return directories;
//    }
//}
