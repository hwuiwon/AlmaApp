package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hwuiwon.alma.Directories.Directory;
import com.hwuiwon.alma.Directories.DirectoryAdapter;
import com.hwuiwon.alma.Tasks.ProfileImageTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DirectoryActivity extends AppCompatActivity {

    private EditText directoryET;
    private ListView directoryLV;

    private String cookie;
    private Directory[] directories;
    private View progressView;

    private HashMap<String, Directory> cumulativeDirectory;

    private ArrayList<String[]> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        cookie = getIntent().getStringExtra("cookie");
        Button directoryBT = (Button) findViewById(R.id.directoryBT);
        directoryET = (EditText) findViewById(R.id.directoryET);
        directoryLV = (ListView) findViewById(R.id.directoryLV);

        progressView = findViewById(R.id.directory_progress);

        cumulativeDirectory = new HashMap<>();

        directoryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directoryLV.setAdapter(makeDirectoryAdapter());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
            }
        });

        directoryLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] emailTo = {directories[i].getEmail()};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, emailTo);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    public DirectoryAdapter makeDirectoryAdapter() {
        final DirectoryAdapter adapter = new DirectoryAdapter(this);
        try {
            String keyword = String.valueOf(directoryET.getText());
//            showProgress(true);
            directories = new DirectoryTask().execute(keyword, cookie).get();
//            showProgress(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Directory directory : directories) {
            cumulativeDirectory.put(directory.getName(), directory);
            adapter.addDirectory(directory);
        }

        return adapter;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        directoryLV.setVisibility(show ? View.GONE : View.VISIBLE);
        directoryLV.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                directoryLV.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    private class DirectoryTask extends AsyncTask<String, Void, Directory[]> {

        @Override
        protected Directory[] doInBackground(String... strings) {

            Directory[] directories = null;
            String keyword = strings[0];
            final String cookie = strings[1];
            final String url = "https://spps.getalma.com";

            char[] chars = keyword.toCharArray();

            keyword="";
            for(char c:chars){
                keyword+="%"+Integer.toHexString((int)c);
            }

            final String searchQuery = keyword;

            try {
                Document document = Jsoup.connect(url + "/directory/search?u=0&q=" + searchQuery)
                        .timeout(0).header("Cookie", cookie).post();

                final Elements e = document.select("div.directory-results > ul > li");
                final int size = e.size();
                Log.d("Tag", size+"");
                directories = new Directory[size];

                for (int i=0; i<size; i++) {
                    String name = e.get(i).select(".fn").text().trim();
//                    if (cumulativeDirectory.get(name)==null) {
                        Log.d("Tag", name + " was not in cumulativeDirectory");
                        String email = e.get(i).select("a").text().trim();
                        String str = e.get(i).select(".profile-pic").attr("data-src");
                        if (TextUtils.isEmpty(str))
                            str = e.get(i).select(".profile-pic").attr("src");
                        Bitmap arr = new ProfileImageTask(DirectoryActivity.this).execute(cookie, str, e.get(i).select(".category").text().toLowerCase());

                    Log.d("Tag", "\n"+name+"\n"+email+"\n"+str);
                        directories[i] = new Directory(name, email, arr);
//                    } else {
//                        directories[i] = cumulativeDirectory.get(name);
//                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return directories;
        }
    }
}
