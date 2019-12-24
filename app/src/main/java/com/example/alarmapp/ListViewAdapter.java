package com.example.alarmapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> arrayList = new ArrayList<>();

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final ViewGroup viewGroup = parent;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView);

        ListViewItem listViewItem = arrayList.get(position);
        textView.setText(listViewItem.getTime()[0] + " : " + listViewItem.getTime()[1]);
        return convertView;
    }

    public void addItem(int[] time){
        ListViewItem item = new ListViewItem();
        item.setTime(time);
        arrayList.add(item);
    }

    public void removeData(int position){
        arrayList.remove(position);
        notifyDataSetChanged();
    }

    public int getRawTime(int position){
        ListViewItem item = new ListViewItem();
        item = arrayList.get(position);
        int[] time = item.getTime();
        return time[0] * 100 + time[1];
    }
}
