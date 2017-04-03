package com.yanlei.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yanlei.mooclike.R;


public class AboutMeFragment extends Fragment {
private ListView aboutMe;
    public AboutMeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme,container,false);
        aboutMe = (ListView) view.findViewById(R.id.aboutMe);
        return view;
    }
}
