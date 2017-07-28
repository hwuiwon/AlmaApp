package com.hwuiwon.alma;

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

    private EditText directoryET;
    private ListView directoryLV;

    private String cookie;
    private Directory[] directories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        cookie = getIntent().getStringExtra("cookie");
        Button directoryBT = (Button) findViewById(R.id.directoryBT);
        directoryET = (EditText) findViewById(R.id.directoryET);
        directoryLV = (ListView) findViewById(R.id.directoryLV);

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
            directories = new DirectoryTask().execute(keyword, cookie).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Directory directory : directories) {
            adapter.addDirectory(directory);
        }

        return adapter;
    }
}
