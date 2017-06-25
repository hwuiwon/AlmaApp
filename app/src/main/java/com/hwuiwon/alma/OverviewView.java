package com.hwuiwon.alma;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OverviewView extends RelativeLayout {
    TextView textView1;
    TextView textView2;

    public OverviewView(Context context, Overview overview) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.overview, this, true);

        textView1 = findViewById(R.id.overviewTV1);
        textView2 = findViewById(R.id.overviewTV2);
        textView1.setText(overview.getClassName());
        textView2.setText(overview.getAlphabetGrade());
    }

    public void setData(Overview overview) {
        textView1.setText(overview.getClassName());
        textView2.setText(overview.getAlphabetGrade());
    }
}