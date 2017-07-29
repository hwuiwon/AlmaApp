package com.hwuiwon.alma;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hwuiwon.alma.Directories.Directory;
import com.hwuiwon.alma.Directories.DirectoryAdapter;

public class DirectoryActivity extends AppCompatActivity {

    private Button directoryBT;
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
        directoryBT = (Button) findViewById(R.id.directoryBT);
        directoryET = (EditText) findViewById(R.id.directoryET);
        directoryLV = (ListView) findViewById(R.id.directoryLV);

        progressView = findViewById(R.id.directory_progress);

        directoryBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directoryLV.setAdapter(makeDirectoryAdapter());
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
}
