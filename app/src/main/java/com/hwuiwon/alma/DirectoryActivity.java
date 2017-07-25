package com.hwuiwon.alma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DirectoryActivity extends AppCompatActivity {

    Button directoryBT;
    EditText directoryET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
    }
}
