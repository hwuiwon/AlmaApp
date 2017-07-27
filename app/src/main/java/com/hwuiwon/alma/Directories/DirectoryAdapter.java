package com.hwuiwon.alma.Directories;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class DirectoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Directory> datas = new ArrayList<>();

    public DirectoryAdapter(Context context) {
        this.context = context;
    }

    public void addDirectory(Directory directory) {
        datas.add(directory);
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
        DirectoryView directoryView;

        if (view == null) {
            directoryView = new DirectoryView(context, datas.get(i));
        } else {
            directoryView = (DirectoryView) view;
            directoryView.setData(datas.get(i));
        }
        return directoryView;
    }
}