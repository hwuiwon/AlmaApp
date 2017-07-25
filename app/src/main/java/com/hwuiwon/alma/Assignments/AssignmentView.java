package com.hwuiwon.alma.Assignments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwuiwon.alma.R;

@SuppressLint("ViewConstructor")
public class AssignmentView extends RelativeLayout {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    public AssignmentView(Context context, Assignment assignment) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.assignment, this, true);

        textView1 = findViewById(R.id.assignmentTV1);
        textView2 = findViewById(R.id.assignmentTV2);
        textView3 = findViewById(R.id.assignmentTV3);
        textView4 = findViewById(R.id.assignmentTV4);

        textView1.setText(assignment.getType());
        textView2.setText(assignment.getDueDate());
        textView3.setText(assignment.getAssignmentName());
        textView4.setText(assignment.getAssignmentDetail());
    }

    public void setData(Assignment assignment) {
        textView1.setText(assignment.getType());
        textView2.setText(assignment.getDueDate());
        textView3.setText(assignment.getAssignmentName());
        textView4.setText(assignment.getAssignmentDetail());
    }
}