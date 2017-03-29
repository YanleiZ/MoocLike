package com.yanlei.mooclike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Yanlei on 2017/3/20.
 */

public class MovieAdapter extends BaseAdapter {

    private LinkedList<Movie> mData;
    private Context mContext;

    public MovieAdapter(LinkedList<Movie> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_movie,parent,false);
        ImageView img_icon = (ImageView) convertView.findViewById(R.id.movieicon);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.name);
        TextView txt_aSpeak = (TextView) convertView.findViewById(R.id.teacher);
        img_icon.setImageBitmap(mData.get(position).getIcon());
        txt_aName.setText(mData.get(position).getName());
        txt_aSpeak.setText(mData.get(position).getTeacher());
        return convertView;
    }
}