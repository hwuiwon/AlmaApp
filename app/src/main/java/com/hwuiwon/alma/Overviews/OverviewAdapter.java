package com.hwuiwon.alma.Overviews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class OverviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Overview> data = new ArrayList<>();

    public OverviewAdapter(Context context) {
        this.context = context;
    }

    public void addOverview(Overview overview) {
        data.add(overview);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        OverviewView overviewView;
        if (view == null) {
            overviewView = new OverviewView(context, data.get(i));
        } else {
            overviewView = (OverviewView) view;
            overviewView.setData(data.get(i));
        }
        return overviewView;
    }
}