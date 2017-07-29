package com.hwuiwon.alma.Directories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwuiwon.alma.R;

@SuppressLint("ViewConstructor")
public class DirectoryView extends RelativeLayout {

    TextView textView1;
    TextView textView2;

    public DirectoryView(Context context, Directory directory) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.directory, this, true);

        textView1 = findViewById(R.id.directoryTV1);
        textView2 = findViewById(R.id.directoryTV2);

        textView1.setText(directory.getName());
        textView2.setText(directory.getEmail());
    }

    public void setData(Directory directory) {
        textView1.setText(directory.getName());
        textView2.setText(directory.getEmail());
    }
}