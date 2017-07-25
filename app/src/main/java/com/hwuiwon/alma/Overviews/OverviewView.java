package com.hwuiwon.alma.Overviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwuiwon.alma.R;

@SuppressLint("ViewConstructor")
public class OverviewView extends RelativeLayout {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    public OverviewView(Context context, Overview overview) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.overview, this, true);

        textView1 = findViewById(R.id.overviewTV1);
        textView2 = findViewById(R.id.overviewTV2);
        textView3 = findViewById(R.id.overviewTV3);
        textView4 = findViewById(R.id.overviewTV4);

        textView1.setText(overview.getPeriod());
        textView2.setText(overview.getClassName());
        textView3.setText(overview.getAlphabetGrade());
        textView4.setText(overview.getRoomNum());
    }

    public void setData(Overview overview) {
        textView1.setText(overview.getPeriod());
        textView2.setText(overview.getClassName());
        textView3.setText(overview.getAlphabetGrade());
        textView4.setText(overview.getRoomNum());
    }
}