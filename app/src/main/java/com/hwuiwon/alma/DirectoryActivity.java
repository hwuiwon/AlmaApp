package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hwuiwon.alma.Directories.Directory;
import com.hwuiwon.alma.Directories.DirectoryAdapter;
import com.hwuiwon.alma.Tasks.DirectoryTask;

public class DirectoryActivity extends AppCompatActivity {

    private EditText directoryET;
    private ListView directoryLV;

    private String cookie;
    private Directory[] directories;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        cookie = getIntent().getStringExtra("cookie");
        Button directoryBT = (Button) findViewById(R.id.directoryBT);
        directoryET = (EditText) findViewById(R.id.directoryET);
        directoryLV = (ListView) findViewById(R.id.directoryLV);

        progressView = findViewById(R.id.directory_progress);

        directoryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDirectoryAdapter();
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

    public void setDirectoryAdapter() {
        try {
            String keyword = String.valueOf(directoryET.getText());
            showProgress(true);
            new SearchingTask().execute(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private class SearchingTask extends AsyncTask<String, Void, DirectoryAdapter> {
        @Override
        protected DirectoryAdapter doInBackground(String... strings) {
            String keyword = strings[0];
            directories = new DirectoryTask().execute(keyword, cookie);
            DirectoryAdapter adapter = new DirectoryAdapter(DirectoryActivity.this);
            for (Directory directory : directories) {
                adapter.addDirectory(directory);
            }

            return adapter;
        }

        @Override
        protected void onPostExecute(DirectoryAdapter adapter) {
            showProgress(false);
            directoryLV.setAdapter(adapter);
        }
    }
}
