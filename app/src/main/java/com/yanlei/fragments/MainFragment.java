package com.yanlei.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.yanlei.Utils.HttpUtil;
import com.yanlei.adapter.MovieAdapter;
import com.yanlei.holder.LocalImageHolderView;
import com.yanlei.models.Movie;
import com.yanlei.mooclike.MainActivity;
import com.yanlei.mooclike.NewsActivity;
import com.yanlei.mooclike.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.yanlei.Utils.HttpUtil.BASE_URL;


public class MainFragment extends Fragment implements OnItemClickListener {
    private List<Movie> mData = null;
    private Context mContext;
    private MovieAdapter mAdapter = null;
    private ListView list_movie;
    private TextView progressBarText;
    private TextView load_fail1;
    private TextView load_fail2;
    private ConvenientBanner convenientBanner;
    View view = null;

    private List<String> realNameList ;

    private List<String> newsUrlList;

    private LoadVideoListTask mAuthTask = null;

    private ArrayList<Integer> localImages = null;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            init();
            mAuthTask = new LoadVideoListTask();
            mAuthTask.execute((Void) null);

            list_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent mainIntent = new Intent(view.getContext(), MainActivity.class);
                    if (realNameList.size() > 0) {
                        mainIntent.putExtra(HttpUtil.REAL_NAME, realNameList.get(i));
                        view.getContext().startActivity(mainIntent);
                    } else {
                        Toast.makeText(view.getContext(), "出现错误！请退出后重新登录！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            localImages = new ArrayList<Integer>();
            localImages.add(R.mipmap.show1);
            localImages.add(R.mipmap.show2);
            localImages.add(R.mipmap.show3);
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
            convenientBanner.setCanLoop(true);
            convenientBanner.setOnItemClickListener(this);
        }
        return view;
    }

    public void init() {
        mContext = view.getContext();
        load_fail1 = (TextView) view.findViewById(R.id.loadFail1);
        load_fail2 = (TextView) view.findViewById(R.id.loadFail2);
        progressBarText = (TextView) view.findViewById(R.id.progressBarText);
        list_movie = (ListView) view.findViewById(R.id.listView);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
    }

    @Override
    public void onItemClick(int position) {

        Intent mainIntent = new Intent(view.getContext(), NewsActivity.class);
        if (newsUrlList.size() > 0) {
            mainIntent.putExtra(HttpUtil.NEWS_URL, newsUrlList.get(position));
            view.getContext().startActivity(mainIntent);
        } else {
            Toast.makeText(view.getContext(), "出现错误！请退出后重新登录！", Toast.LENGTH_SHORT).show();
        }
        Log.i("===========","ssssssssssssssss");
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
                    realNameList = new ArrayList<String>();
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
                    mData = new LinkedList<Movie>();
                    newsUrlList = new ArrayList<String>();
                    newsUrlList.add("http://baike.baidu.com/link?url=YrsH8U8yZjxZmiOjqId1x2pwnbRRnVvn1OaQf23yk0q6GYcc6abxvpFC5Y3VSxuWTX9U6ZPG9DFOU6VBxQbH0nsBDSb-YvWSdCk1yZ-VF_yS_m-xBLX6hOTe7e9Z0-8HTsgFubL7O0qOwXcQTKti5Jg3ySXBHlavIBTVUTsXdJcsJgNi--zc8qN0zSDqwGR7uPSBmmT-ygc8SYjuDt9cpujwGxlPD5MDyz1Yti4deq3");
                    newsUrlList.add("http://baike.baidu.com/link?url=YrsH8U8yZjxZmiOjqId1x2pwnbRRnVvn1OaQf23yk0q6GYcc6abxvpFC5Y3VSxuWTX9U6ZPG9DFOU6VBxQbH0nsBDSb-YvWSdCk1yZ-VF_yS_m-xBLX6hOTe7e9Z0-8HTsgFubL7O0qOwXcQTKti5Jg3ySXBHlavIBTVUTsXdJcsJgNi--zc8qN0zSDqwGR7uPSBmmT-ygc8SYjuDt9cpujwGxlPD5MDyz1Yti4deq3");
                    newsUrlList.add("https://www.baidu.com/");

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
            progressBarText.setVisibility(View.GONE);
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
                list_movie.setVisibility(View.VISIBLE);
                convenientBanner.setVisibility(View.VISIBLE);
            } else {
                load_fail1.setVisibility(View.VISIBLE);
                load_fail2.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
