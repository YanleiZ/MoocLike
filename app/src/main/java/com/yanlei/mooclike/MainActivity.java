package com.yanlei.mooclike;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.yanlei.Utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.yanlei.Utils.HttpUtil.BASE_URL;
import static com.yanlei.Utils.HttpUtil.REAL_NAME;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView webView;

    private LoadDocTask mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String realname = getIntent().getStringExtra(REAL_NAME);
        //拼装Video的URL
        String videoUrl = BASE_URL + "upload_file/" + realname;
        Log.i("+++++++++视频地址：", videoUrl);
        Uri uri = Uri.parse(videoUrl);

        videoView = (VideoView) this.findViewById(R.id.videoView);

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();

        webView = (TextView) findViewById(R.id.webView);

        mAuthTask = new LoadDocTask();
        mAuthTask.execute((Void) null);

    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(MainActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }

    public class LoadDocTask extends AsyncTask<Void, Void, Boolean> {
        String result = "";
        String subResult = "";

        LoadDocTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String urll = BASE_URL + "WordsServlet?method=content&id=1";
            try {
                result = HttpUtil.getRequest(urll);
                Log.i("=======返回结果：",result);
                int start = result.indexOf("<p>");
                int end = result.lastIndexOf("</p>");
                subResult = result.substring(start,end);
                Log.i("======截取的文字部分：",subResult);
                return true;
            } catch (Exception e) {
                Log.i("==========显示文档失败", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                webView.setText(Html.fromHtml(subResult));

            } else {
                webView.setText("文档加载失败！");
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}