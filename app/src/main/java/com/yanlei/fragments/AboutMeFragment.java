package com.yanlei.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yanlei.mooclike.AboutMeActivity;
import com.yanlei.mooclike.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AboutMeFragment extends Fragment {
    private ListView aboutMe;

    public AboutMeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme, container, false);
        aboutMe = (ListView) view.findViewById(R.id.aboutMe);

        ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();

        map1.put("about", "关于");
        map1.put("value", R.mipmap.ic_launcher);
        array.add(map1);
        map1.put("user", "更换背景");
        map1.put("value", R.mipmap.ic_launcher);
        array.add(map1);

        MyAdapter adapter = new MyAdapter(getActivity(), array, R.layout.setlist_listview,
                new String[]{"user", "value"},
                new int[]{R.id.user, R.id.value});
        aboutMe.setAdapter(adapter);
        aboutMe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent mainIntent = new Intent(getActivity(), AboutMeActivity.class);
                    getActivity().startActivity(mainIntent);
                }
            }
        });
        return view;
    }

    class MyAdapter extends SimpleAdapter {
        private LayoutInflater layout;

        public MyAdapter(Context context, List<? extends Map<String, ?>> data,
                         int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View result = super.getView(position, convertView, parent);
            //获取LayoutInflater的实例对象
            layout = getActivity().getLayoutInflater();
            if (result == null) {
                //载入指定布局
                layout.inflate(R.layout.setlist_listview, null);
            }
            return result;
        }

    }
}
