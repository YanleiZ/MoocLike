package com.yanlei.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanlei.Utils.HttpUtil;
import com.yanlei.adapter.MovieAdapter;
import com.yanlei.models.Movie;
import com.yanlei.mooclike.MainActivity;
import com.yanlei.mooclike.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.yanlei.Utils.HttpUtil.BASE_URL;


public class AllCourseFragment extends Fragment {
    private ListView allcourselistView;
    private List<String> allRealNameList = new ArrayList<String>();
    private List<Movie> mData = null;
    private TextView loading;
    private MovieAdapter mAdapter = null;
    private Context mContext;
    private TextView load_fail;
    private LoadAllVideoListTask mAuthTask = null;



    public AllCourseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allcourse,container,false);
        mContext = view.getContext();
        allcourselistView = (ListView) view.findViewById(R.id.allcourselistView);
        loading = (TextView) view.findViewById(R.id.loading);
        load_fail = (TextView) view.findViewById(R.id.loadFail);
        mAuthTask = new LoadAllVideoListTask();
        mAuthTask.execute((Void) null);

        allcourselistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mainIntent = new Intent(view.getContext(), MainActivity.class);
                if (allRealNameList.size() > 0) {
                    mainIntent.putExtra(HttpUtil.REAL_NAME, allRealNameList.get(i));
                    view.getContext().startActivity(mainIntent);
                } else {
                    Toast.makeText(view.getContext(), "出现错误！请退出后重新登录！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    public class LoadAllVideoListTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray videoJsonArray = null;
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();

        LoadAllVideoListTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // network access.
                String videosJson = "";
                String loginFlag = "";

                // 定义发送请求的URL
                String url = BASE_URL + "AppGetVideoServlet?method=get";
                Log.d("url", url);
                try {
                    // 发送请求
                    videosJson = HttpUtil.getRequest(url);  //get方式

                    Log.d("服务器返回值", videosJson);
                    if (videosJson == null) {
                        return false;
                    }
                    videoJsonArray = new JSONArray(videosJson);
                    for (int i = 0; i < videoJsonArray.length(); i++) {
                        JSONObject videoObject = videoJsonArray.getJSONObject(i);
                        Log.i("=====", videoObject.toString());
                        try {
                            String urlPathContent = BASE_URL + "upload_image/" + videoObject.getString("img").toString();
                            Log.i("+++++图片地址：", urlPathContent);
                            byte[] data = HttpUtil.getImage(urlPathContent);
                            bitmaps.add(BitmapFactory.decodeByteArray(data, 0, data.length));  //生成位图
                        } catch (Exception e) {
                            Log.i("+++++++获取图片错误：", e.toString());
                        }
                        //将视频的Url保存到一个列表里
                        allRealNameList.add(videoObject.getString("docts").toString());

                    }
                    mData = new LinkedList<Movie>();
                    return true;

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return false;


            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            JSONObject videoInfo = null;
            loading.setVisibility(View.GONE);
            if (success) {
                //Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.splash1);
                for (int i = 0; i < videoJsonArray.length(); i++) {
                    try {
                        videoInfo = videoJsonArray.getJSONObject(i);
                        if (bitmaps.size() > 0) {
                            mData.add(new Movie(bitmaps.get(i), videoInfo.getString("name").toString(), videoInfo.getString("descp").toString()));
                        }
                    } catch (Exception e) {
                        Log.i("===========获取json对象错误！", e.toString());
                    }

                }
                mAdapter = new MovieAdapter((LinkedList<Movie>) mData, mContext);
                allcourselistView.setAdapter(mAdapter);
                allcourselistView.setVisibility(View.VISIBLE);
            } else {
                load_fail.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
