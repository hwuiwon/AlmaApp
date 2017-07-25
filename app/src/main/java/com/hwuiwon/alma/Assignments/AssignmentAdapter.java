package com.hwuiwon.alma.Assignments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class AssignmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Assignment> datas = new ArrayList<>();

    public AssignmentAdapter(Context context) {
        this.context = context;
    }

    public void addAssignment(Assignment assignment) {
        datas.add(assignment);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AssignmentView assignmentView;
        if (view == null) {
            assignmentView = new AssignmentView(context, datas.get(i));
        } else {
            assignmentView = (AssignmentView) view;
            assignmentView.setData(datas.get(i));
        }
        return assignmentView;
    }
}