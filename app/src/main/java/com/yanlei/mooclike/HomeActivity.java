package com.yanlei.mooclike;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.yanlei.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.yanlei.Utils.HttpUtil.BASE_URL;
import static com.yanlei.Utils.HttpUtil.REAL_NAME;

public class HomeActivity extends AppCompatActivity implements OnItemClickListener {


    private List<Movie> mData = null;
    private Context mContext;
    private MovieAdapter mAdapter = null;
    private ListView list_movie;
    private LinearLayout progressBar;
    private LinearLayout load_fail;
    private LinearLayout listview_linerlayout;
    private ConvenientBanner convenientBanner;

    private List<String> realNameList = new ArrayList<String>();

    private LoadVideoListTask mAuthTask = null;

    private ArrayList<Integer> localImages = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        progressBar = (LinearLayout) findViewById(R.id.progressBar);
        load_fail = (LinearLayout) findViewById(R.id.loadFail);
        listview_linerlayout = (LinearLayout) findViewById(R.id.listviewlinerlayout);
        list_movie = (ListView) findViewById(R.id.listView);
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        mData = new LinkedList<Movie>();

        mAuthTask = new LoadVideoListTask();
        mAuthTask.execute((Void) null);

        list_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mainIntent = new Intent(HomeActivity.this, MainActivity.class);
                if (realNameList.size() > 0) {
                    mainIntent.putExtra(REAL_NAME, realNameList.get(i));
                    HomeActivity.this.startActivity(mainIntent);
                } else {
                    Toast.makeText(HomeActivity.this, "出现错误！请退出后重新登录！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_page_indicator_focused);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_page_indicator);

        localImages.add(R.drawable.splash1);
        localImages.add(R.drawable.dianzishangwugailun);
        localImages.add(R.drawable.dianzishangwugailun2);
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setManualPageable(true);
        convenientBanner.startTurning(2500);
        //convenientBanner.setcurrentitem(2000);
        convenientBanner.setCanLoop(true);
        convenientBanner.setOnItemClickListener(this);
        //.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        //convenientBanner.setOnPageChangeListener(this);//监听翻页事件


    }

    public class LoadVideoListTask extends AsyncTask<Void, Void, Boolean> {
        JSONArray videoJsonArray = null;
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();

        LoadVideoListTask() {

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
                        realNameList.add(videoObject.getString("docts").toString());

                    }
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
            progressBar.setVisibility(View.GONE);
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
                list_movie.setAdapter(mAdapter);
                listview_linerlayout.setVisibility(View.VISIBLE);
            } else {
                load_fail.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    /**
     * 轮播组件的点击事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Toast.makeText(HomeActivity.this, "你点击了第"+ position, Toast.LENGTH_SHORT).show();
    }
}
