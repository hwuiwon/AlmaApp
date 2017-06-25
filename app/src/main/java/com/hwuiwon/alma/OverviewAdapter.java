package com.hwuiwon.alma;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class OverviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Overview> datas = new ArrayList<>();

    public OverviewAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(ArrayList<Overview> datas) {
        this.datas = datas;
    }

    public void addOverview(Overview overview) {
        datas.add(overview);
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
        OverviewView overviewView = null;
        if (view == null) {
            overviewView = new OverviewView(context, datas.get(i));
        } else {
            overviewView = (OverviewView) view;
            overviewView.setData(datas.get(i));
        }
        return overviewView;
    }
}