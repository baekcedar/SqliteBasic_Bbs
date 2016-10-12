package com.baekcedar.android.sqlitebasic_bbs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HM on 2016-10-10.
 */
class ListAdapter extends BaseAdapter {
    ArrayList<Data> datas;
    Context context;
    LayoutInflater inflater;
    Data data;
    TextView no, title, name, ndate;

    public void listRefresh(ArrayList<Data> datas){
        this.datas = datas;
        notifyDataSetChanged();
        Log.i("TEST adapterlistRefresh", "notifyDataSetChanged");
    }
    public void listAdd(ArrayList<Data> addData){
        for(int i =0 ;i <  addData.size(); i ++){
            datas.add(addData.get(i));
        }
        notifyDataSetChanged();
    }
    // 생성자
    public ListAdapter(Context context, ArrayList<Data> datas){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.datas =  datas;
    }

    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        data = datas.get(position);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);
            no      = (TextView) convertView.findViewById(R.id.textNoItem);
            title   = (TextView) convertView.findViewById(R.id.textTitleItem);
            name    = (TextView) convertView.findViewById(R.id.textNameItem);
            ndate   = (TextView) convertView.findViewById(R.id.textDateItem);

         // title 20 자 이상이면 잘라서 출력
        if(data.title.length() > 20){
            title.setText(data.title.substring(0,20));
        }else{
            title.setText(data.title);
        }

        no.setText(Integer.toString(data.no));
        name.setText(data.name);
        ndate.setText(data.ndate);

        return convertView;
    }
}