package com.hwuiwon.alma.Directories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwuiwon.alma.ImageIO;
import com.hwuiwon.alma.R;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ViewConstructor")
public class DirectoryView extends RelativeLayout {

    TextView textView1;
    TextView textView2;
    CircleImageView circleImageView;

    public DirectoryView(Context context, Directory directory) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.directory, this, true);

        textView1 = findViewById(R.id.directoryTV1);
        textView2 = findViewById(R.id.directoryTV2);
        circleImageView = findViewById(R.id.directoryCIV);

        textView1.setText(directory.getName());
        textView2.setText(directory.getEmail());
        circleImageView.setImageBitmap(directory.getProfilePic());
    }

    public void setData(Directory directory) {
        textView1.setText(directory.getName());
        textView2.setText(directory.getEmail());
    }
}