package com.hwuiwon.alma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private String keyword = "";

    private Directory[] directories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        cookie = getIntent().getStringExtra("cookie");
        directoryBT = (Button) findViewById(R.id.directoryBT);
        directoryET = (EditText) findViewById(R.id.directoryET);
        directoryLV = (ListView) findViewById(R.id.directoryLV);

        directoryLV.setAdapter(makeDirectoryAdapter());
    }

    public DirectoryAdapter makeDirectoryAdapter() {
        final DirectoryAdapter adapter = new DirectoryAdapter(this);
        try {
            keyword = String.valueOf(directoryET.getText());
            directories = new DirectoryTask().execute(keyword, cookie).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adapter;
    }
}
