package com.hwuiwon.alma;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class GradeAdapter extends BaseAdapter {
    Context context;
    ArrayList<Grade> datas = new ArrayList<>();

    public GradeAdapter(Context context) {
        this.context = context;
    }

    public void addGrade(Grade grade) {
        datas.add(grade);
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
        GradeView gradeView = null;
        if (view == null) {
            gradeView = new GradeView(context, datas.get(i));
        } else {
            gradeView = (GradeView) view;
            gradeView.setData(datas.get(i));
        }
        return gradeView;
    }
}