package com.hwuiwon.alma;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GradeView extends RelativeLayout {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;

    public GradeView(Context context, Grade grade) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.grade, this, true);

        textView1 = findViewById(R.id.gradeTV1);
        textView2 = findViewById(R.id.gradeTV2);
        textView3 = findViewById(R.id.gradeTV3);
        textView4 = findViewById(R.id.gradeTV4);
        textView5 = findViewById(R.id.gradeTV5);

        textView1.setText(grade.getAssignmentName());
        textView2.setText(grade.getAlphabetGrade());
        textView3.setText(grade.getPercentageGrade());
        textView4.setText(grade.getUpdatedDate());
        textView5.setText(grade.getWeighted());
    }

    public void setData(Grade grade) {
        textView1.setText(grade.getAssignmentName());
        textView2.setText(grade.getAlphabetGrade());
        textView3.setText(grade.getPercentageGrade());
        textView4.setText(grade.getUpdatedDate());
        textView5.setText(grade.getWeighted());
    }
}