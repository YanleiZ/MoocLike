package com.yanlei.mooclike;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private List<Movie> mData = null;
    private Context mContext;
    private MovieAdapter mAdapter = null;
    private ListView list_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        list_movie = (ListView) findViewById(R.id.listView);
        mData = new LinkedList<Movie>();
        Resources res= getResources();
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.splash1);

        mData.add(new Movie(bm1,"第一课 概论", "叫兽：侯成"));
        mData.add(new Movie(bm1,"第二课 电商的历史", "讲师：侯成"));
        mData.add(new Movie(bm1,"第三课 电商的发展", "侯成"));
        mData.add(new Movie(bm1,"第四课 电商模式", "侯成"));
        mData.add(new Movie(bm1,"第五课 未来电商", "侯成"));
        mData.add(new Movie(bm1,"第六课 电商创业", "侯成"));
        mData.add(new Movie(bm1,"第七课 期中复习", "侯成?"));
        mData.add(new Movie(bm1,"第八课 电子商务", "侯成?"));
        mData.add(new Movie(bm1,"第九课 电商", "侯成"));
        mData.add(new Movie(bm1,"第十课 淘宝的经营模式", "侯成?"));
        mData.add(new Movie(bm1,"第十一课 京东模式", "侯成?"));
        mData.add(new Movie(bm1,"第十二课 当当、亚马逊", "侯成?"));
        mData.add(new Movie(bm1,"第十三课 期末复习", "侯成?"));
        mData.add(new Movie(bm1,"第十四课 期末复习二", "侯成?"));
        mData.add(new Movie(bm1,"第十五课 期末复习三", "侯成?"));
        mAdapter = new MovieAdapter((LinkedList<Movie>) mData, mContext);
        list_movie.setAdapter(mAdapter);
        list_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mainIntent = new Intent(HomeActivity.this, MainActivity.class);
                HomeActivity.this.startActivity(mainIntent);
                Toast.makeText(HomeActivity.this,"你点击的是第"+i+"个Item。",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
